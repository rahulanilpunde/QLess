package com.parse.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.data.DataProduts;
import com.parse.starter.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;




public class ProductAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<DataProduts> mItemsAl = new ArrayList<>();

    public ProductAdapter(Context context,
                          ArrayList<DataProduts> cd) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mItemsAl = cd;

        Log.d("", "Arraylist : " + mItemsAl.size());

    }

    public class ViewHolder {
        TextView cust;
        TextView desc;
        TextView ptime;
        TextView atime;
        ImageView imgItems;
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


        try {

            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.o_listview_item, null);

                holder.cust = (TextView) view.findViewById(R.id.cust);
                holder.desc = (TextView) view.findViewById(R.id.order);
                holder.ptime = (TextView) view.findViewById(R.id.ptime);
                holder.atime = (TextView) view.findViewById(R.id.atime);
                holder.imgItems = (ImageView) view.findViewById(R.id.imgItems);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.cust.setText("Name: " + mItemsAl.get(position).getItemName());
            holder.desc.setText("Desc: " + mItemsAl.get(position).getItemDesc());
            holder.ptime.setText("Time need: " + mItemsAl.get(position).getPreparationTime() + "minutes");
            holder.atime.setText("Price:  $" + mItemsAl.get(position).getPrice());

            Picasso.with(context).load(mItemsAl.get(position).getImageUrl()).into( holder.imgItems);

        } catch (Exception ex) {
            Log.d("", "Exception : " + ex.getMessage());
        }
        return view;
    }
}

