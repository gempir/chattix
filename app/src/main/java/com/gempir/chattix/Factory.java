package com.gempir.chattix;

import android.arch.persistence.room.Room;
import android.content.Context;


public class Factory {

    private static AppDatabase appDatabase;

    public static AppDatabase getAppDatabase(Context ctx) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(ctx, AppDatabase.class, "userDB").allowMainThreadQueries().build();
        }
        return appDatabase;
    }
}
