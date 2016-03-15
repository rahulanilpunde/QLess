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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.adapter.ProductAdapter;
import com.parse.data.DataProduts;
import com.parse.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class Search extends AppCompatActivity {

    private static final int SHOW_PROGRESS = 0x01;
    private static final int STOP_PROGRESS = 0x02;
    private static final int RETREIVE_DATA_SUCCESS = 0x03;
    private ProgressDialog mProgressDialog;
    private EditText edtSearch;
    private Button btnSearch;
    private ArrayList<DataProduts> mAvailableItemsAl = new ArrayList<>();
    private ListView listProducts;
    private TextView txtNoOffers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Search");


        edtSearch = (EditText) findViewById(R.id.edtSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        listProducts = (ListView) findViewById(R.id.listProducts);
        txtNoOffers = (TextView) findViewById(R.id.txtNoOffers);

        listProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent mIntent = new Intent(Search.this, ProductDetails.class);
                mIntent.putExtra("customerName", mAvailableItemsAl.get(position).getCustomerName());
                mIntent.putExtra("ItemDesc", mAvailableItemsAl.get(position).getItemDesc());
                mIntent.putExtra("arrivaltime", mAvailableItemsAl.get(position).getArrivaltime());
                mIntent.putExtra("orderedTime", mAvailableItemsAl.get(position).getOrderedTime());
                mIntent.putExtra("ItemName", mAvailableItemsAl.get(position).getItemName());
                mIntent.putExtra("preparationTime", mAvailableItemsAl.get(position).getPreparationTime());
                mIntent.putExtra("price", mAvailableItemsAl.get(position).getPrice());
                mIntent.putExtra("imageUrl", mAvailableItemsAl.get(position).getImageUrl());
                mIntent.putExtra("location", mAvailableItemsAl.get(position).getLocationId());
                mIntent.putExtra("locationName", mAvailableItemsAl.get(position).getLocationName());

                startActivity(mIntent);

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAvailableItemsAl.clear();
                if (Utils.hasText(edtSearch))
                    new GetSearchDetails().execute(edtSearch.getText().toString());
                else
                    Toast.makeText(Search.this, "Please enter search keyword", Toast.LENGTH_SHORT).show();
            }
        });
    }


    class GetSearchDetails extends AsyncTask<String, Void, Void> {

        List<ParseObject> objectList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            edtSearch.setText("");
            mHandler.sendEmptyMessage(STOP_PROGRESS);
        }

        @Override
        protected Void doInBackground(String... params) {


            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "availableItems").whereContains("ItemName", params[0]);
                objectList = query.find();
                Log.d("TAG", "ObjectList" + objectList.size());
                for (ParseObject order : objectList) {
                    DataProduts mData = new DataProduts();
                    mData.setCustomerName((String) order.get("customerName"));
                    mData.setItemDesc((String) order.get("ItemDesc"));
                    mData.setArrivaltime((String) order.get("arrivaltime"));
                    mData.setOrderedTime((String) order.get("orderedTime"));
                    mData.setItemName((String) order.get("ItemName"));
                    mData.setLocationId((Integer) order.get("locationId"));
                    mData.setLocationName((String) order.get("locationName"));

                    mData.setPreparationTime((Integer) order.get("preparationTime"));
                    mData.setPrice((String) order.get("price"));
                    mData.setImageUrl((String) order.get("imageUrl"));
                    mAvailableItemsAl.add(mData);

                    Utils.debug("Items Array List size : " + mAvailableItemsAl.size());
                    Log.d("TAG", "ObjectList" + mAvailableItemsAl.size());
                }
                mHandler.sendEmptyMessage(RETREIVE_DATA_SUCCESS);

            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return null;
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
                                .createProgressDialog(Search.this);

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
                    Log.d("TAG", "RETREIVE_DATA_SUCCESS" + mAvailableItemsAl.size());
                    if (mAvailableItemsAl.size() > 0) {
                        Log.d("TAG", "if" + mAvailableItemsAl.size());
                        listProducts.setVisibility(View.VISIBLE);
                        txtNoOffers.setVisibility(View.GONE);
                        listProducts.setAdapter(new ProductAdapter(Search.this, mAvailableItemsAl));
                    } else {
                        Log.d("TAG", "else" + mAvailableItemsAl.size());
                        listProducts.setVisibility(View.GONE);
                        txtNoOffers.setVisibility(View.VISIBLE);
                    }
                    mHandler.removeMessages(RETREIVE_DATA_SUCCESS);
                    break;
            }
        }
    };

}
