package com.parse.starter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class Splash extends AppCompatActivity {


    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String USER_NAME = "USER_NAME";
    private String userName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(this.getResources().getString(R.string.welcome));


        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
         userName = sharedpreferences.getString(USER_NAME, "");


        mHandler.sendEmptyMessageDelayed(1, 3000);

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (userName.isEmpty()) {
                startActivity(new Intent(Splash.this, Login.class));
            } else {
                startActivity(new Intent(Splash.this, WelcomeActivity.class));
            }
            finish();
        }
    };

}
