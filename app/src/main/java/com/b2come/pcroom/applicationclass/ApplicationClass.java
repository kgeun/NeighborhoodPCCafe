package com.b2come.pcroom.applicationclass;

/**
 * Created by KKLee on 2016. 11. 8..
 */

import android.app.Application;
import android.content.res.Configuration;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.tsengvn.typekit.Typekit;

public class ApplicationClass extends Application {

    public int jumsu = 100;

    /** onCreate()
     * 액티비티, 리시버, 서비스가 생성되기전 어플리케이션이 시작 중일때
     * Application onCreate() 메서드가 만들어 진다고 나와 있습니다.
     * by. Developer 사이트
     */

    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/NanumBarunGothic-Regular.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/NanumBarunGothic-Bold.ttf"));
                /*
                .addNormal(Typekit.createFromAsset(this, "fonts/NotoSansKR-Regular-Hestia.otf"))
                .addBold(Typekit.createFromAsset(this, "fonts/NotoSansKR-Bold-Hestia.otf"));
                */

        super.onCreate();
        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

    }

    /**
     * onConfigurationChanged()
     * 컴포넌트가 실행되는 동안 단말의 화면이 바뀌면 시스템이 실행 한다.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}