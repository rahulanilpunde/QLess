package com.parse.starter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class WelcomeActivity extends AppCompatActivity {

    // Declare Variable
    private Button logout;
    private Button myCart, myOrders, btnLocation, btnSearch;
    private TextView txtwelcome, txtTimer;
    private long mSeconds;

    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_STATUS = "USER_STATUS";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome);

        if (getIntent().hasExtra("seconds")) {
            Log.d("TAG", "if");
            mSeconds = getIntent().getLongExtra("seconds", 0);
            showTimer(mSeconds);
            Log.d("TAG", "Seconds" + mSeconds);
        }

        initUi();

        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String userName = sharedpreferences.getString(USER_NAME, "");
        String userStatus = sharedpreferences.getString(USER_STATUS, "");


        txtwelcome.setText("Welcome \n You are logged in as \n " + userName + "!" + "\n" + "Status: " + userStatus);


        logout.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Logout current user

                WelcomeActivity.this.getSharedPreferences(MY_PREFERENCES, 0).edit().clear().commit();
                Intent mIntent = new Intent(WelcomeActivity.this, Login.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();
            }
        });

        myCart.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(
                        WelcomeActivity.this,
                        MyCart.class);
                startActivity(intent);
            }
        });

        myOrders.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(
                        WelcomeActivity.this,
                        MyOrders.class);
                startActivity(intent);
            }
        });


        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        WelcomeActivity.this,
                        Search.class);
                startActivity(intent);
            }
        });

        btnLocation.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(
                        WelcomeActivity.this,
                        Location.class);
                startActivity(intent);
            }
        });
    }

    private void showTimer(final long timer) {

        new CountDownTimer(timer, 1000) {

            public void onTick(long millisUntilFinished) {

                Log.d("TAG", "timer : " + timer * 1000);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                Log.d("TAG", "seconds : " + seconds % 60);
                txtTimer.setText(convertSecondsToHMmSs(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)));
            }

            public void onFinish() {

                txtTimer.setText("Done!");
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(WelcomeActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Get your order!")
                        .setContentText("Thanks for your patience. Your order is ready. You can check in and get your order!!")
                        .setAutoCancel(true);
                Intent intent = new Intent(WelcomeActivity.this, MyOrders.class);
                PendingIntent pi = PendingIntent.getActivity(WelcomeActivity.this, 0, intent, 0);
                mBuilder.setContentIntent(pi);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());

                txtTimer.setVisibility(View.GONE);

            }
        }.start();
    }

    void initUi() {
        // Locate Button in welcome.xml

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(this.getResources().getString(R.string.welcome));

        logout = (Button) findViewById(R.id.logout);
        txtwelcome = (TextView) findViewById(R.id.txtwelcome);
        myCart = (Button) findViewById(R.id.myCart);
        myOrders = (Button) findViewById(R.id.myOrders);
        btnLocation = (Button) findViewById(R.id.btnLocation);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        txtTimer = (TextView) findViewById(R.id.txtTimer);


    }

    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%d:%02d:%02d", h, m, s);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}