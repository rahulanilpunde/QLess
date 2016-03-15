package com.parse.starter;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.adapter.ProductAdapter;
import com.parse.data.DataProduts;
import com.parse.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Menu extends AppCompatActivity {


    private static final int SHOW_PROGRESS = 0x01;
    private static final int STOP_PROGRESS = 0x02;
    private static final int RETREIVE_DATA_SUCCESS = 0x03;
    private ProgressDialog mProgressDialog;
    private ArrayList<DataProduts> mAvailableItemsAl = new ArrayList<>();
    private ListView listProducts;


    private int locationId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        locationId = getIntent().getIntExtra("locationId",0);


        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Menu");

        listProducts = (ListView) findViewById(R.id.listProducts);

        listProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent mIntent = new Intent(Menu.this, ProductDetails.class);
                mIntent.putExtra("customerName", mAvailableItemsAl.get(position).getCustomerName());
                mIntent.putExtra("ItemDesc", mAvailableItemsAl.get(position).getItemDesc());
                mIntent.putExtra("arrivaltime", mAvailableItemsAl.get(position).getArrivaltime());
                mIntent.putExtra("orderedTime", mAvailableItemsAl.get(position).getOrderedTime());
                mIntent.putExtra("ItemName", mAvailableItemsAl.get(position).getItemName());
                mIntent.putExtra("preparationTime", mAvailableItemsAl.get(position).getPreparationTime());
                mIntent.putExtra("price", mAvailableItemsAl.get(position).getPrice());
                mIntent.putExtra("imageUrl", mAvailableItemsAl.get(position).getImageUrl());
                mIntent.putExtra("locationName", mAvailableItemsAl.get(position).getLocationName());

                startActivity(mIntent);

            }
        });


        mAvailableItemsAl.clear();
//        for (int i = 0; i < 30; i++) {
//            addProducts(i);
//        }
      new GetAllProducts().execute();
    }


    class GetAllProducts extends AsyncTask<Void, Void, Void> {

        List<ParseObject> objectList;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "availableItems").whereEqualTo("locationId",locationId);
                objectList = query.find();
                for (ParseObject order : objectList) {
                    DataProduts mData = new DataProduts();
                    mData.setCustomerName((String) order.get("customerName"));
                    mData.setItemDesc((String) order.get("ItemDesc"));
                    mData.setArrivaltime((String) order.get("arrivaltime"));
                    mData.setOrderedTime((String) order.get("orderedTime"));
                    mData.setItemName((String) order.get("ItemName"));
                    mData.setLocationId((Integer) order.get("locationId"));
                    mData.setPreparationTime((Integer) order.get("preparationTime"));
                    mData.setPrice((String) order.get("price"));
                    mData.setImageUrl((String) order.get("imageUrl"));
                    mData.setLocationName((String) order.get("locationName")== null? "" : (String) order.get("locationName"));

                    mAvailableItemsAl.add(mData);

                    Utils.debug("Items Array List size : " + mAvailableItemsAl.size());

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


    private void addProducts(int i) {

        // ParseUser.getCurrentUser().getUsername()

        ParseObject o = new ParseObject("availableItems");
        o.put("ItemName", "Greens and Burgers");
        o.put("ItemDesc", "Burgers, DessertsFast Food Italian Mexican Sandwiches Seafood Snacks Wrapsks");
        o.put("orderedTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        o.put("customerId", "");
        o.put("customerName", "");
        o.put("locationId", i);
        o.put("arrivaltime", "");
        o.saveInBackground();

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
                                .createProgressDialog(Menu.this);

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

                    listProducts.setAdapter(new ProductAdapter(Menu.this, mAvailableItemsAl));
                    mHandler.removeMessages(RETREIVE_DATA_SUCCESS);
                    break;
            }
        }
    };
}