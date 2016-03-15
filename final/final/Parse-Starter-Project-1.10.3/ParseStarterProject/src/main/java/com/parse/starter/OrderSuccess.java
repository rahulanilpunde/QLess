package com.parse.starter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class OrderSuccess extends AppCompatActivity {

    private TextView txtOrderPlaced, txtTimer, txtOrderReady;
    private Button btnViewOrder;
    private int waitTime = 0;
    private long millsecondstosend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_order_success);


        waitTime = getIntent().getIntExtra("waitTime", 0);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Order Placed!");

        Log.e("The wait time", "Wait time " + waitTime);


        txtOrderPlaced = (TextView) findViewById(R.id.txtOrderPlaced);
        txtTimer = (TextView) findViewById(R.id.txtTimer);
        txtOrderReady = (TextView) findViewById(R.id.txtOrderReady);
        btnViewOrder = (Button) findViewById(R.id.btnViewOrder);


        btnViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(OrderSuccess.this, WelcomeActivity.class);
                mIntent.putExtra("seconds",millsecondstosend);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();

            }
        });

        showTimer(waitTime);
    }

    private void showTimer(final int timer) {

        new CountDownTimer(timer * 60000, 1000) {

            public void onTick(long millisUntilFinished) {

                Log.d("TAG", "timer : " + timer * 1000);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                millsecondstosend = millisUntilFinished;
                Log.d("TAG", "seconds : " + seconds % 60);
                txtTimer.setText(convertSecondsToHMmSs(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)));
            }

            public void onFinish() {


                txtTimer.setText("Done!");
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(OrderSuccess.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Get your order!")
                        .setContentText("Thanks for your patience. Your order is ready. You can check in and get your order!!")
                        .setAutoCancel(true);
                Intent intent = new Intent(OrderSuccess.this, MyOrders.class);
                PendingIntent pi = PendingIntent.getActivity(OrderSuccess.this, 0, intent, 0);
                mBuilder.setContentIntent(pi);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());
            }
        }.start();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent mIntent = new Intent(OrderSuccess.this, WelcomeActivity.class);
            mIntent.putExtra("seconds",millsecondstosend);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%d:%02d:%02d", h, m, s);
    }

}
