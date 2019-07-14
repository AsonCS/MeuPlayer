package br.com.acsgsa92.meuplayer.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.acsgsa92.meuplayer.R;
import br.com.acsgsa92.meuplayer.activitys.MainActivity;
import br.com.acsgsa92.meuplayer.player.PlayerService;

import static br.com.acsgsa92.meuplayer.fragments.ListaFragment.LISTA;
import static br.com.acsgsa92.meuplayer.player.PlayerService.PEDE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContainerFragment extends BaseFragment {

    private Fragment fragment = new ListaFragment();
    private Bundle bundle;
    private Parcelable[] par = null;

    public ContainerFragment() {
        bundle = new Bundle();
        bundle.putParcelableArray(LISTA, par);
        fragment = new ListaFragment();
        fragment.setArguments(bundle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        getContext().registerReceiver(receiver, new IntentFilter(LISTA));
        if (savedInstanceState == null)
            getChildFragmentManager().beginTransaction().add(R.id.fc_container, fragment).commit();
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onResume() {
        if (PlayerService.CONECTADO)
            getContext().sendBroadcast(new Intent(PEDE));
        else {
            bundle = new Bundle();
            bundle.putParcelableArray(LISTA, par = null);
            fragment = new ListaFragment();
            fragment.setArguments(bundle);
            getChildFragmentManager().beginTransaction().replace(R.id.fc_container, fragment).commit();
        }
        super.onResume();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            par = intent.getParcelableArrayExtra(LISTA);
            bundle = new Bundle();
            bundle.putParcelableArray(LISTA, par);
            fragment = new ListaFragment();
            fragment.setArguments(bundle);
            if (MainActivity.activity){
                getChildFragmentManager().beginTransaction().replace(R.id.fc_container, fragment).commit();
            }
        }
    };
}
