package com.gempir.chattix;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface DaggerComponent {
    void inject(Login activity);
}
