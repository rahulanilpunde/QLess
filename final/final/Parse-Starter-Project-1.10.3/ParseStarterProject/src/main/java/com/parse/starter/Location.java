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
import com.parse.adapter.LocationAdapter;
import com.parse.data.DataLocations;
import com.parse.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class Location extends AppCompatActivity {


    private ListView listLocations;
    private static final int SHOW_PROGRESS = 0x01;
    private static final int STOP_PROGRESS = 0x02;
    private static final int RETREIVE_DATA_SUCCESS = 0x03;

    private ProgressDialog mProgressDialog;


    private ArrayList<DataLocations> locationsAl = new ArrayList<DataLocations>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_location);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(this.getResources().getString(R.string.location));

        listLocations = (ListView) findViewById(R.id.listLocations);
        String[] locations = getResources().getStringArray(R.array.locations);

        listLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent mIntent = new Intent(Location.this, Menu.class);
                mIntent.putExtra("locationId",locationsAl.get(position).getLocationId());
                startActivity(mIntent);

            }
        });

        new GetAllLocation().execute();

    }


    private void addLocation(int i, String name) {

        ParseObject o = new ParseObject("locations");
        o.put("locationId", i);
        o.put("locationName", name);
        o.saveInBackground();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    class GetAllLocation extends AsyncTask<Void, Void, Void> {

        List<ParseObject> objectList;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "locations");
                query.orderByAscending("locationName");
                objectList = query.find();
                for (ParseObject order : objectList) {
                    DataLocations mData = new DataLocations();
                    mData.setLocationName((String) order.get("locationName"));
                    mData.setLocationId((Integer) order.get("locationId"));
                    locationsAl.add(mData);
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


    Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case SHOW_PROGRESS:
                    if (mProgressDialog == null) {
                        mProgressDialog = Utils
                                .createProgressDialog(Location.this);

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

                    listLocations.setAdapter(new LocationAdapter(Location.this, locationsAl));
                    mHandler.removeMessages(RETREIVE_DATA_SUCCESS);
                    break;
            }
        }
    };

}
