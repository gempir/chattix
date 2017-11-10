package com.gempir.chattix;

import android.app.Application;

public class App extends Application {

    private DaggerComponent daggerComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        daggerComponent = DaggerComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("http://jsonplaceholder.typicode.com/"))
                .build();
    }

    public DaggerComponent getDaggerComponent() {
        return daggerComponent;
    }
}
