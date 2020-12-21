package com.classy.timelogger;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TLog.class}, version = 1)
abstract class AppDatabase extends RoomDatabase {
    public abstract TLogDao tLogDao();
}
