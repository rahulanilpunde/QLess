package com.parse.starter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.adapter.ProductAdapter;
import com.parse.utils.Constant;



public class MyCart extends AppCompatActivity {

    private ListView listMyCart;
    private TextView txtMyCart;
    private Button btnConfirm, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_my_cart);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("My Cart");

        listMyCart = (ListView) findViewById(R.id.listMyCart);
        txtMyCart = (TextView) findViewById(R.id.txtMyCart);

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnClear = (Button) findViewById(R.id.btnClear);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.sSelectedItemsAl.size() == 0) {
                    Toast.makeText(MyCart.this, "The Cart is empty. Nothing to order!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent mIntent = new Intent(MyCart.this, ReviewBeforeCheckOut.class);
                    startActivity(mIntent);
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(MyCart.this)
                        .setTitle("Delete cart!")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();
                                Constant.sSelectedItemsAl.clear();
                                finish();


                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


        if (Constant.sSelectedItemsAl.size() == 0) {
            listMyCart.setVisibility(View.GONE);
            txtMyCart.setVisibility(View.VISIBLE);
            txtMyCart.setText("Sorry, You don't have any items in your cart. ");
        } else {
            listMyCart.setVisibility(View.VISIBLE);
            txtMyCart.setVisibility(View.GONE);
            listMyCart.setAdapter(new ProductAdapter(MyCart.this, Constant.sSelectedItemsAl));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
