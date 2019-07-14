package br.com.acsgsa92.meuplayer.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by acsgs on 09/04/2017.
 */

public class Prefer {

    public static final String PREF_ID = "meuplayer";
    //private static void log(String s){Log.d("Meuplayer", "Prefer:\n"+ s);}

    public static final String AUTOCONS = "AUTOCONS";
    public static final String AUTOSEL = "AUTOSEL";
    public static final String AUTOID = "AUTOID";
    public static final String AUTOIDX = "AUTOIDX";

    public static void setInt( @NonNull Context context, @NonNull String chave, @NonNull int valor){
        SharedPreferences preferences = context.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(chave, valor);
        editor.commit();
    }

    public static int getInt(@NonNull Context context, @NonNull String chave){
        SharedPreferences preferences = context.getSharedPreferences(PREF_ID, 0);
        return preferences.getInt(chave, 0);
    }

    public static void setString( @NonNull Context context, @NonNull String chave, @NonNull String valor){
        SharedPreferences preferences = context.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(chave, valor);
        editor.commit();
    }

    public static String getString(@NonNull Context context, @NonNull String chave){
        SharedPreferences preferences = context.getSharedPreferences(PREF_ID, 0);
        return preferences.getString(chave, null);
    }
}
