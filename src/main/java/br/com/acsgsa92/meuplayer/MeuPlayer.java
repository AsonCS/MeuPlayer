package br.com.acsgsa92.meuplayer;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by acsgs on 28/03/2017.
 */

public class MeuPlayer extends Application {
    public static final String TAG = "Meuplayer";
    public static void log(String s){
        Log.d(TAG, s);}
    public static void toast(Context context, String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
