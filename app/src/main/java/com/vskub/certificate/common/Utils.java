package com.vskub.certificate.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

public class Utils {

    Context _context;
    SharedPreferences sharedPreferences;

    public Utils(Context context) {
        this._context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public void savePreferences(String string, String id) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(string, id);
        editor.commit();
    }

    public String loadId() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("id", "");
        return data;
    }

    public String loadonetime() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("one", "");
        return data;
    }

    public String loadcontact() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("contact", "");
        return data;
    }

    public String loadfast() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("fast", "");
        return data;
    }

    public String loadpro() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("pro", "");
        return data;
    }

    public String loadprime() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("prime", "");
        return data;
    }

    public String loadbonus() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("bonus", "");
        return data;
    }

    public String loadauto() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("auto", "");
        return data;
    }

    public String loadAccount() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("account", "");
        return data;
    }

    public String loadImage() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("image", "");
        return data;
    }

    public String loadWithdraw() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("withdraw", "");
        return data;
    }

    public String loadName() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("name", "");
        return data;
    }

    public String loadSubusers() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("subuser", "");
        return data;
    }
    public String loadPurchase() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("purchase", "");
        return data;
    }
    public String loadSales() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("sales", "");
        return data;
    }

    public String loadDoj() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("doj", "");
        return data;
    }

    public String loadDesignation() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("designation", "");
        return data;
    }

    public String loadIbv() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("ibv", "");
        return data;
    }
    public String loadgbv() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("gbv", "");
        return data;
    }
    public String loadCommition() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("commition", "");
        return data;
    }
    public String loadFname() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("fname", "");
        return data;
    }
    public String loadLname() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String data = sharedPreferences.getString("lname", "");
        return data;
    }

    public String getIMEI(Context context) {
        TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        return telephonyManager.getDeviceId();
    }


}
