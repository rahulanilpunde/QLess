package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.data.DataProduts;
import com.parse.utils.Constant;
import com.squareup.picasso.Picasso;


public class ProductDetails extends AppCompatActivity {


    private String mCustomerName = "", ItemDesc = "", mArrivalTime = "", mOrderedTime = "",locationName="", mItemName = "", price = "", imageUrl = "";
    private TextView txtItemName, txtItemDesc, txtCustomerName, txtOrderedTime, txtArrivalTime,txtLocationName;
    private Button btnAddToCart, btnProceed;

    private int preparationTime = 0,locationId=0;
    private ImageView imgItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_product_details);


        Intent mIntent = getIntent();

        mCustomerName = mIntent.getStringExtra("customerName");
        ItemDesc = mIntent.getStringExtra("ItemDesc");
        mArrivalTime = mIntent.getStringExtra("arrivaltime");
        mOrderedTime = mIntent.getStringExtra("orderedTime");
        mItemName = mIntent.getStringExtra("ItemName");
        imageUrl = mIntent.getStringExtra("imageUrl");
        locationName = mIntent.getStringExtra("locationName");


        price = mIntent.getStringExtra("price");
        locationId = mIntent.getIntExtra("location",0);
        preparationTime = mIntent.getIntExtra("preparationTime", 0);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Details");


        txtItemName = (TextView) findViewById(R.id.txtItemName);
        txtItemDesc = (TextView) findViewById(R.id.txtItemDesc);
        txtCustomerName = (TextView) findViewById(R.id.txtCustomerName);
        txtOrderedTime = (TextView) findViewById(R.id.txtOrderedTime);
        txtArrivalTime = (TextView) findViewById(R.id.txtArrivalTime);
        txtLocationName = (TextView)findViewById(R.id.txtLocationName);
        btnAddToCart = (Button) findViewById(R.id.btnAddToCart);
        btnProceed = (Button) findViewById(R.id.btnProceed);
        imgItems = (ImageView) findViewById(R.id.imgItems);

        if (imageUrl != null && !imageUrl.isEmpty())
            Picasso.with(this).load(imageUrl).into(imgItems);

        txtItemName.setText(mItemName);
        txtItemDesc.setText(ItemDesc);
        txtCustomerName.setText("$" + price);
        txtOrderedTime.setText(mOrderedTime);
        txtLocationName.setText(""+locationName);
        txtArrivalTime.setText("" + preparationTime + " minutes");


        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(ProductDetails.this, "Item has been added to cart", Toast.LENGTH_SHORT).show();
                DataProduts mData = new DataProduts();
                mData.setItemName(mItemName);
                mData.setItemDesc(ItemDesc);
                mData.setCustomerName(mCustomerName);
                mData.setOrderedTime(mOrderedTime);
                mData.setArrivaltime(mArrivalTime);
                mData.setPrice(price);
                mData.setPreparationTime(preparationTime);
                mData.setImageUrl(imageUrl);
                mData.setLocationName(locationName);
                Constant.sSelectedItemsAl.add(mData);

            }
        });


        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(ProductDetails.this, ReviewBeforeCheckOut.class);
                startActivity(mIntent);

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
