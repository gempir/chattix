package com.gempir.chattix;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        new IrcBot();
    }
}
