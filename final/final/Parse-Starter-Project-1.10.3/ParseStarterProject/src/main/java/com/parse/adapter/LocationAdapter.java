package com.parse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.data.DataLocations;
import com.parse.starter.R;

import java.util.ArrayList;




public class LocationAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<DataLocations> mItemsAl = new ArrayList<>();

    public LocationAdapter(Context context,
                           ArrayList<DataLocations> cd) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mItemsAl = cd;

    }

    public class ViewHolder {
        TextView txtLocation;

    }

    @Override
    public int getCount() {
        return mItemsAl.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemsAl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adap_location_item, null);

            holder.txtLocation = (TextView) view.findViewById(R.id.txtLocation);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.txtLocation.setText(mItemsAl.get(position).getLocationName());

        return view;
    }
}


