package br.com.acsgsa92.meuplayer.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.acsgsa92.meuplayer.R;

import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.DATA;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.GRUPOS;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.SELECAO;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContainerGrFragment extends Fragment {

    private Bundle bundle;
    private Fragment fragment;

    public ContainerGrFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_container_gr, container, false);
        bundle = new Bundle();
        bundle.putString(GRUPOS, getArguments().getString(GRUPOS, DATA));
        bundle.putString(SELECAO, getArguments().getString(SELECAO, GRUPOS));
        fragment = new GroupFragment();
        fragment.setArguments(bundle);
        getContext().registerReceiver(receiver, new IntentFilter(GRUPOS));
        if (savedInstanceState == null)
            getChildFragmentManager().beginTransaction().add(R.id.fcg_container, fragment).commit();
        setRetainInstance(true);
        return view;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            bundle = new Bundle();
            bundle.putString(GRUPOS, intent.getStringExtra(GRUPOS));
            bundle.putString(SELECAO, intent.getStringExtra(SELECAO));
            fragment = new GroupFragment();
            fragment.setArguments(bundle);
            getChildFragmentManager().beginTransaction().replace(R.id.fcg_container, fragment).commit();
        }
    };

}
