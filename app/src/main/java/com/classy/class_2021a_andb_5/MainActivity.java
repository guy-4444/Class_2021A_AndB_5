package com.classy.class_2021a_andb_5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.classy.timelogger.MyTimeLogger;
import com.classy.timelogger.TLog;
import com.google.android.material.button.MaterialButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MaterialButton main_BTN_apiCall;
    private MaterialButton main_BTN_showAllLogs;
    private MaterialButton main_BTN_average;
    private TextView main_LBL_data;

    private long startTimeStamp = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_BTN_apiCall = findViewById(R.id.main_BTN_apiCall);
        main_BTN_showAllLogs = findViewById(R.id.main_BTN_showAllLogs);
        main_BTN_average = findViewById(R.id.main_BTN_average);
        main_LBL_data = findViewById(R.id.main_LBL_data);
        main_LBL_data.setMovementMethod(LinkMovementMethod.getInstance());

        main_BTN_apiCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCall();
            }
        });

        main_BTN_showAllLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readLogs();
            }
        });

        main_BTN_average.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAverage();
            }
        });
    }

    private void showAverage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyTimeLogger.getInstance().getAverageSession("activity_main_time", new MyTimeLogger.CallBack_Time() {
                    @Override
                    public void dataReady(long time) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                main_LBL_data.setText("Average: " + time);
                            }
                        });
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimeStamp = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        long duration = System.currentTimeMillis() - startTimeStamp;
        MyTimeLogger.getInstance().addTlogTime("activity_main_time", (int) duration);
    }

    private void readLogs() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyTimeLogger.getInstance().getAllLogs(new MyTimeLogger.CallBack_Logs() {
                    @Override
                    public void dataReady(List<TLog> tLogs) {
                        int sum = 0;
                        int count = 0;
                        StringBuilder sb = new StringBuilder("");
                        for (TLog tLog : tLogs) {
                            sb.append(tLog.id + " " +
                                    new SimpleDateFormat("dd/MM HH:mm:ss").format(tLog.time)+ " " +
                                    tLog.tag + " " +
                                    tLog.duration + "\n");
                            Log.d("pttt",
                                    tLog.id + " " +
                                            new SimpleDateFormat("dd/MM HH:mm:ss").format(tLog.time)+ " " +
                                            tLog.tag + " " +
                                            tLog.duration);

                            count++;
                            sum += tLog.duration;
                        }


                        try {
                            Log.d("pttt", "Sum: " + sum + "   avg: " + (sum / count));
                            sb.append("Sum: " + sum + "   avg: " + (sum / count));
                        } catch (Exception e) {
                            Log.d("pttt", "Invalid Data");
                            sb.append("Invalid Data");
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                main_LBL_data.setText(sb.toString());
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void apiCall() {
        final String LINK =  "https://pastebin.com/raw/WtvLGNXJ";

        new Thread(new Runnable(){

            public void run(){
                String data = "";

                try {
                    long startTimeStamp = System.currentTimeMillis();
                    URL url = new URL(LINK); //My text file location
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(60000);
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String str;
                    while ((str = in.readLine()) != null) {
                        data += str;
                    }
                    in.close();
                    long duration = System.currentTimeMillis() - startTimeStamp;
                    Log.d("pttt", "Api success in " + duration + " ms");

                    MyTimeLogger.getInstance().addTlogTime("download_json_time", (int) duration);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}