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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.utils.Utils;

import java.util.List;

public class Login extends AppCompatActivity {

    private Button loginbutton, signup;
    private String usernametxt = "", passwordtxt = "";
    private EditText password, username;
    private ProgressDialog mProgressDialog;

    private static final int SHOW_PROGRESS = 0x01;
    private static final int STOP_PROGRESS = 0x02;
    private static final int RESULT_SUCCESS = 0x03;
    private static final int RESULT_FAILED = 0x04;
    private static final int NOT_A_USER = 0x05;


    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String USER_NAME = "USER_NAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String USER_STATUS = "USER_STATUS";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loginsignup);
        setUpInitialUi();
        // Login Button Click Listener
        loginbutton.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {


                usernametxt = username.getText().
                        toString();
                passwordtxt = password.getText().
                        toString();


                if (usernametxt.isEmpty() || passwordtxt.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter valid username and password!", Toast.LENGTH_SHORT).show();
                } else {
                    new LoginAsync().execute();
                }
            }
        });


        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));

            }
        });


    }


    class LoginAsync extends AsyncTask<Void, Void, Void> {

        List<ParseObject> objectList;

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

            Log.d("doInBackground", "doInBackground " + usernametxt + " | " + password);

            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "Customer").whereEqualTo("username", usernametxt).whereEqualTo("password", passwordtxt);

                objectList = query.find();
                Log.d("objectList", "objectList " + objectList.size());


                for (ParseObject data : objectList) {
                    Log.d("data", "data " + data.getString("username"));
                    Log.d("data", "data " + data.getString("password"));

                    SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString(USER_NAME, data.getString("username"));
                    editor.putString(PASSWORD, data.getString("password"));
                    editor.putString(USER_STATUS, data.getString("status"));
                    editor.commit();
                }

                if (objectList.size() == 0) {
                    mHandler.sendEmptyMessage(NOT_A_USER);
                } else {
                    mHandler.sendEmptyMessage(RESULT_SUCCESS);
                }


            } catch (Exception ex) {
                mHandler.sendEmptyMessage(RESULT_FAILED);
                Log.d("exceptions", "exceptions " + ex.getMessage());
            }

            return null;
        }
    }


    private void setUpInitialUi() {

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(this.getResources().getString(R.string.LoginBtn));

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        // Locate Buttons in main.xml
        loginbutton = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);

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
                                .createProgressDialog(Login.this);
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

                case RESULT_FAILED:
                    Toast.makeText(Login.this, "Network Error Please try again later.", Toast.LENGTH_SHORT).show();
                    mHandler.removeMessages(RESULT_FAILED);
                    break;

                case NOT_A_USER:
                    Toast.makeText(Login.this, "You are not a registered user! Please register.", Toast.LENGTH_SHORT).show();
                    mHandler.removeMessages(NOT_A_USER);
                    break;


                case RESULT_SUCCESS:

                    Toast.makeText(Login.this, "Successfully logged in.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, WelcomeActivity.class));
                    finish();
                    mHandler.removeMessages(RESULT_SUCCESS);
                    break;
            }
        }
    };
}
