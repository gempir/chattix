package com.gempir.chattix;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class Factory {


    private static Bus bus;

    private static AppDatabase appDatabase;


    public static Bus getBus()
    {
        if (bus == null) {
            bus = new Bus(ThreadEnforcer.ANY);
        }
        return bus;
    }

    public static AppDatabase getAppDatabase(Context ctx)
    {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(ctx, AppDatabase.class, "userDB").build();
        }
        return appDatabase;
    }
}
