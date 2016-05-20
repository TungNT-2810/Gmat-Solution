package org.iliat.gmat;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.orm.SugarContext;


import org.iliat.gmat.activity.LoginActivity;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by qhuydtvt on 4/4/2016.
 */
public class GMATApplication extends Application {
    public static final String SHARE_PREFERENCES = "LOGIN_SHAREPREFERENCES";
    public static final String LOGIN_SHARE_PREFERENCES = "LOGIN";
    public static final String EMAIL_SHARE_PREFERENCES = "EMAIL";
    public static final String PASSWORD_SHARE_PREFERENCES = "PASSWORD";
    public static final String KEY_SHARE_PREFERENCES = "KEY_UNLOCK";



    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext())
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
