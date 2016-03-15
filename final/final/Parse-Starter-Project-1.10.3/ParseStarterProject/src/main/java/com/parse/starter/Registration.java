package com.parse.starter;

import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.utils.Utils;


public class Registration extends AppCompatActivity {

    private static final int SHOW_PROGRESS = 0x01;
    private static final int STOP_PROGRESS = 0x02;
    private static final int RESULT_SUCCESS = 0x03;

    private EditText password, username;
    private ProgressDialog mProgressDialog;
    private Button btnSignUp, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_registration);


        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(this.getResources().getString(R.string.register));

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.hasText(username) && Utils.hasText(password)) {
                    new RegisterUser().execute();
                } else {
                    Toast.makeText(Registration.this, "Please enter valid username and password", Toast.LENGTH_SHORT).show();
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


    class RegisterUser extends AsyncTask<Void, Void, Void> {

        String userName = username.getText().toString();
        String passwordTxt = password.getText().toString();

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

        @Override
        protected Void doInBackground(Void... params) {

            try {
                ParseObject o = new ParseObject("Customer");
                ParseACL parseAcl = o.getACL();
                parseAcl.setPublicReadAccess(true);
                parseAcl.setPublicWriteAccess(true);

                o.put("username", userName);
                o.put("password", passwordTxt);
                o.put("status", "active");
                o.saveInBackground();
                mHandler.sendEmptyMessage(RESULT_SUCCESS);

            } catch (Exception ex) {
                Log.d("", "exceptions " + ex.getMessage());
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
                                .createProgressDialog(Registration.this);
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

                case RESULT_SUCCESS:
                    Toast.makeText(Registration.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };
}
