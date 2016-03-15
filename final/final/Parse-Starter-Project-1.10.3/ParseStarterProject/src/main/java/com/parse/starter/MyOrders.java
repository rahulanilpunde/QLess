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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.adapter.ProductAdapter;
import com.parse.data.DataProduts;
import com.parse.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class MyOrders extends AppCompatActivity {


    private static final int SHOW_PROGRESS = 0x01;
    private static final int STOP_PROGRESS = 0x02;
    private static final int RETREIVE_DATA_SUCCESS = 0x03;

    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String USER_NAME = "USER_NAME";

    private ProgressDialog mProgressDialog;
    private TextView txtNoOrdersAvailable;

    private ListView listMyOrders;
    private ArrayList<DataProduts> mOrderAl = new ArrayList<DataProduts>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_my_orders);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("My Order");

        listMyOrders = (ListView) findViewById(R.id.listMyOrders);
        txtNoOrdersAvailable = (TextView) findViewById(R.id.txtNoOrdersAvailable);


        listMyOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent mIntent = new Intent(MyOrders.this, MyOrderDetails.class);
                mIntent.putExtra("customerName", mOrderAl.get(position).getCustomerName());
                mIntent.putExtra("ItemDesc", mOrderAl.get(position).getItemDesc());
                mIntent.putExtra("arrivaltime", mOrderAl.get(position).getArrivaltime());
                mIntent.putExtra("orderedTime", mOrderAl.get(position).getOrderedTime());
                mIntent.putExtra("ItemName", mOrderAl.get(position).getItemName());
                mIntent.putExtra("preparationTime", mOrderAl.get(position).getPreparationTime());
                mIntent.putExtra("price", mOrderAl.get(position).getPrice());
                mIntent.putExtra("imageUrl", mOrderAl.get(position).getImageUrl());
                mIntent.putExtra("locationName", mOrderAl.get(position).getLocationName() == null ? "" : mOrderAl.get(position).getLocationName());

                startActivity(mIntent);

            }
        });


        new GetAllOrders().execute();

    }


    class GetAllOrders extends AsyncTask<Void, Void, Void> {

        List<ParseObject> objectList;

        @Override
        protected Void doInBackground(Void... params) {

            try {

                SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                String userName = sharedpreferences.getString(USER_NAME, "");

                Log.d("Current User ", " user :  " + userName);

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "placedOrder").whereEqualTo("customerName", userName);

                //  query.whereEqualTo("customerName", struser);
                objectList = query.find();


                Log.e("The objectList", "objectList are : " + objectList.size());

                try {

                    for (ParseObject order : objectList) {

                        DataProduts mData = new DataProduts();
                        mData.setItemName((String) order.get("ItemName"));
                        mData.setItemDesc((String) order.get("ItemDesc"));
                        mData.setOrderedTime((String) order.get("orderedTime"));
                        mData.setCustomerId((String) order.get("customerId"));
                        mData.setCustomerName((String) order.get("customerName"));
                        mData.setPrice((String) order.get("price"));
                        mData.setImageUrl((String) order.get("ImageUrl"));
                        mData.setPreparationTime((Integer) order.get("preparationTime"));
                        mData.setLocationName((String) order.get("locationName"));
                        mOrderAl.add(mData);
                    }

                } catch (Exception ex) {
                    Log.e("The objectList", "Exception are : " + ex.getMessage());
                }
                mHandler.sendEmptyMessage(RETREIVE_DATA_SUCCESS);
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
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
                                .createProgressDialog(MyOrders.this);

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

                    if (mOrderAl.size() == 0) {
                        txtNoOrdersAvailable.setVisibility(View.VISIBLE);
                        listMyOrders.setVisibility(View.GONE);
                    } else {
                        txtNoOrdersAvailable.setVisibility(View.GONE);
                        listMyOrders.setVisibility(View.VISIBLE);
                        listMyOrders.setAdapter(new ProductAdapter(MyOrders.this, mOrderAl));
                    }
                    mHandler.removeMessages(RETREIVE_DATA_SUCCESS);
                    break;
            }
        }
    };

}
