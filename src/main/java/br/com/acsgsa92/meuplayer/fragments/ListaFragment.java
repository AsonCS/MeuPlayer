package br.com.acsgsa92.meuplayer.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.acsgsa92.meuplayer.R;
import br.com.acsgsa92.meuplayer.adapter.MusicaAdapter;
import br.com.acsgsa92.meuplayer.modelo.Musica;

import static br.com.acsgsa92.meuplayer.player.PlayerService.SETMUSICA;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaFragment extends BaseFragment {

    private List<Musica> musicas = new ArrayList<>();
    private RecyclerView recyclerView;
    private View view;

    // Configura o Fragment pelo grupo
    public ListaFragment() {

    }

    public static final String LISTA = "LISTA";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lista, container, false);
        iniciar();
        recyclerView = (RecyclerView) view.findViewById(R.id.fl_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        if (musicas != null) {
            recyclerView.setAdapter(new MusicaAdapter(getContext(), musicas, onClickMusica()));
        }
        return view;
    }

    private void iniciar() {
        Parcelable[] par = getArguments().getParcelableArray(LISTA);
        if (par != null) {
            for (Parcelable a : par) {
                Musica b = (Musica) a;
                musicas.add(b);
            }
        }
    }

    private MusicaAdapter.MusicaOnClickListener onClickMusica() {
        return new MusicaAdapter.MusicaOnClickListener() {
            @Override
            public void OnClickMusica(View view, int idx) {
                Intent intent = new Intent(SETMUSICA);
                intent.putExtra(SETMUSICA, idx);
                getContext().sendBroadcast(intent);
            }

            @Override
            public void OnClickMenuMusica(View view, int idx) {
                Musica musica = musicas.get(idx);
                String a = getString(R.string.nome_musica);
                String b = getString(R.string.artista_musica);
                String c = getString(R.string.albun_musica);
                toast(getContext(), a+" "+musica.nome+b+" "+musica.artista+c+" "+musica.albun);
            }
        };

    }
}
