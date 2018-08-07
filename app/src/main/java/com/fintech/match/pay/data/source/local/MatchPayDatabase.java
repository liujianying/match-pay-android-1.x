package com.fintech.match.pay.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.fintech.match.pay.data.source.TradeLog;

@Database(entities = {TradeLog.class}, version = 1, exportSchema = false)
public abstract class MatchPayDatabase extends RoomDatabase {

    private static MatchPayDatabase INSTANCE;

    public abstract TradeLogDao logDao();

    private static final Object sLock = new Object();

    public static MatchPayDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        MatchPayDatabase.class, "match_pay.db")
//                        .addMigrations()
                        .build();
            }
            return INSTANCE;
        }
    }

}
