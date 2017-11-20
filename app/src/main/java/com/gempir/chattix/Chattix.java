package com.gempir.chattix;

import android.util.Log;

import com.gempir.chattix.api.TwitchAPI;
import com.gempir.chattix.persistence.AppDatabase;

/**
 * Source of truth.
 * Contains objects for accessing APIs (currently Twitch, later FFZ/BTTV/etc.) and the database.
 */
public class Chattix {
    private static Chattix instance;

    private TwitchAPI twitchAPI;
    private AppDatabase database;

    private Chattix(TwitchAPI twitchAPI, AppDatabase database) {
        this.twitchAPI = twitchAPI;
        this.database = database;
    }

    public static Chattix instance() {
        if (instance == null) Log.e("Chattix", "Accessing uninitialized instance of Chattix.");
        return instance;
    }

    public static Chattix createInstance(TwitchAPI twitchAPI, AppDatabase database) {
        instance = new Chattix(twitchAPI, database);
        return instance;
    }

    public TwitchAPI getTwitchAPI() {
        return twitchAPI;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
