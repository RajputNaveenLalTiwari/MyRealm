package com.realm.myrealm;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Andriod1 on 3/13/2018.
 */

public class MyApplication extends Application {
    private String realmName = "MyRealm.realm";
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .name(realmName)
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
