package com.classy.timelogger;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyTimeLogger {

    private static MyTimeLogger instance;
    private static AppDatabase appDatabase;

    private MyTimeLogger(Context context) {
        appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "TlogsDB.db")
                // allow queries on the main thread.
                // Don't do this on a real app! See PersistenceBasicSample for an example.
                // .allowMainThreadQueries()
                .build();
    }

    public static MyTimeLogger getInstance() {
        return instance;
    }

    static MyTimeLogger initHelper(Context context) {
        if (instance == null) {
            instance = new MyTimeLogger(context);
        }

        return instance;
    }


    public interface CallBack_Logs {
        void dataReady(List<TLog> tLogs);
    }

    public interface CallBack_Time {
        void dataReady(long time);
    }

    public void getAverageSession(String tag, CallBack_Time callBack_time) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<TLog> tLogs = appDatabase.tLogDao().getAllByTag(tag);
                long sum = 0;
                long count = 0;
                for (TLog tLog : tLogs) {
                    count++;
                    sum += tLog.duration;
                }

                if (callBack_time != null) {
                    callBack_time.dataReady( count > 0 ? sum / count : 0 );
                }
            }
        }).start();
    }

    public void addTlogTime(String tag, int durationSec) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.tLogDao().insertAll(new TLog(tag, System.currentTimeMillis(), durationSec));
            }
        }).start();
    }

    public void getAllLogsByTag(String tag, CallBack_Logs callBack_logs) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<TLog> tLogs = appDatabase.tLogDao().getAllByTag(tag);
                if (callBack_logs != null) {
                    callBack_logs.dataReady(tLogs);
                }
            }
        }).start();
    }

    public void getAllLogs(CallBack_Logs callBack_logs) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<TLog> tLogs = appDatabase.tLogDao().getAll();
                if (callBack_logs != null) {
                    callBack_logs.dataReady(tLogs);
                }
            }
        }).start();
    }
}
