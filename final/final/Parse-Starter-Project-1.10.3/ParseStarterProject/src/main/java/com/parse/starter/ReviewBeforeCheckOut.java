package com.parse.starter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.adapter.ProductAdapter;
import com.parse.utils.Constant;
import com.parse.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewBeforeCheckOut extends AppCompatActivity {


    private ListView listProducts;
    private Button btnConfirm, btnCancel;
    private ProgressDialog mProgressDialog;
    private int waitTime = 0;


    private static final int SHOW_PROGRESS = 0x01;
    private static final int STOP_PROGRESS = 0x02;
    private static final int RETREIVE_DATA_SUCCESS = 0x03;


    private static final String MY_PREFERENCES = "MyPrefs";
    private static final String USER_NAME = "USER_NAME";
    public static final String USER_STATUS = "USER_STATUS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_review_before_checkout);

        waitTime = getIntent().getIntExtra("waitTime", 0);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Confirm");

        listProducts = (ListView) findViewById(R.id.listProducts);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        listProducts.setAdapter(new ProductAdapter(ReviewBeforeCheckOut.this, Constant.sSelectedItemsAl));
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                String userStatus = sharedpreferences.getString(USER_STATUS, "");


                if (userStatus.equals("active")) {
                    if (Constant.sSelectedItemsAl.size() == 0)
                        Toast.makeText(ReviewBeforeCheckOut.this, "Item has been added to cart", Toast.LENGTH_SHORT).show();
                    else
                        new PlaceOrder().execute();
                } else {
                    Toast.makeText(ReviewBeforeCheckOut.this, "Sorry You are blocked by the admin.", Toast.LENGTH_SHORT).show();
                }


            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    class PlaceOrder extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {


            try {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());

                Log.e("userName ", " currentDateandTime :  " + currentDateandTime);

                SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                String userName = sharedpreferences.getString(USER_NAME, "");
                Log.e("userName ", " userName :  " + userName);

                int size = Constant.sSelectedItemsAl.size();

                for (int i = 0; i < size; i++) {



                    ParseObject o = new ParseObject("placedOrder");
                    o.put("ItemName", Constant.sSelectedItemsAl.get(i).getItemName());
                    o.put("ItemDesc", Constant.sSelectedItemsAl.get(i).getItemDesc());
                    o.put("orderedTime", currentDateandTime);
                    o.put("customerId", "dsad");
                    o.put("customerName", userName);
                    o.put("price", Constant.sSelectedItemsAl.get(i).getPrice());
                    o.put("ItemName", Constant.sSelectedItemsAl.get(i).getItemName());
                    o.put("ImageUrl", Constant.sSelectedItemsAl.get(i).getImageUrl() == null ? "" : Constant.sSelectedItemsAl.get(i).getImageUrl());
                    o.put("preparationTime", Constant.sSelectedItemsAl.get(i).getPreparationTime());
                    o.put("locationName", Constant.sSelectedItemsAl.get(i).getLocationName());
                    o.saveInBackground();

                }
                mHandler.sendEmptyMessage(RETREIVE_DATA_SUCCESS);

            } catch (Exception ex) {
                Log.e("Exceptions ", " Xceptions :  " + ex.getMessage());
            }

            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mHandler.sendEmptyMessage(STOP_PROGRESS);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case SHOW_PROGRESS:
                    if (mProgressDialog == null) {
                        mProgressDialog = Utils
                                .createProgressDialog(ReviewBeforeCheckOut.this);

                        mProgressDialog.show();
                    } else
                        mProgressDialog.show();
                    mHandler.removeMessages(SHOW_PROGRESS);
                    break;

                case STOP_PROGRESS:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    mHandler.removeMessages(STOP_PROGRESS);
                    break;

                case RETREIVE_DATA_SUCCESS:


                    int size = Constant.sSelectedItemsAl.size();

                    for (int i = 0; i < size; i++) {
                        waitTime += Constant.sSelectedItemsAl.get(i).getPreparationTime();
                    }

                    Log.e("The wait time", "Wait time  " + waitTime + " size : " + size);

                    Constant.sSelectedItemsAl.clear();
                    Toast.makeText(ReviewBeforeCheckOut.this, "Your Order has been placed", Toast.LENGTH_SHORT).show();
                    Intent mIntent = new Intent(ReviewBeforeCheckOut.this, OrderSuccess.class);
                    mIntent.putExtra("waitTime", waitTime);
                    startActivity(mIntent);

                    mHandler.removeMessages(RETREIVE_DATA_SUCCESS);
                    break;
            }
        }
    };
}
