package com.classy.timelogger;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "time_logs")
public class TLog {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "tag")
    public String tag = "";

    @ColumnInfo(name = "time")
    public long time = 0;

    @ColumnInfo(name = "duration")
    public int duration = 0;

    public TLog() { }

    public TLog(String tag, long time, int duration) {
        this.tag = tag;
        this.time = time;
        this.duration = duration;
    }
}
