package br.com.acsgsa92.meuplayer.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by acsgs on 28/03/2017.
 */

public class BaseFragment extends Fragment {

    public static void toast(Context context, String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
