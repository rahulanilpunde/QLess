package com.parse.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;

import com.parse.starter.R;

/**
 * Created by rethinavel on 24/11/15.
 */


public class Utils {

    public static ProgressDialog createProgressDialog(Context mContext) {

        ProgressDialog dialog = new ProgressDialog(mContext,
                R.style.MyProgressDialogStyle);
        try {
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(mContext.getResources().getColor(android.R.color.transparent)));
            dialog.show();
            dialog.setContentView(R.layout.custom_progress_dialog);
        } catch (WindowManager.BadTokenException e) {
            debug(e.getMessage());
        }
        return dialog;
    }

    public static void debug(String s) {
        Log.e("CC//", s);
    }


    public static final boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    public static final int getWidth(Activity mContext) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public static boolean checkNullValues(String valueToCheck) {
        if (!(valueToCheck == null)) {
            String valueCheck = valueToCheck.trim();
            if (valueCheck.equals("") || valueCheck.equals("0")) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static final int getHeight(Activity mContext) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        return height;
    }

    public final static boolean isEmailValid(EditText edtEmail) {

        String email = edtEmail.getText().toString();
        if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public final static String toUpperCaseFirstChar(final String target) {

        if ((target == null) || (target.length() == 0)) {
            return target;
        }
        return Character.toUpperCase(target.charAt(0))
                + (target.length() > 1 ? target.substring(1) : "");
    }


    public static final boolean hasText(EditText edt) {
        String hasSomeText = edt.getText().toString();
        int mLength = hasSomeText.length();
        if (mLength > 0) {
            return true;
        }
        return false;
    }


}
