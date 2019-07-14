package br.com.acsgsa92.meuplayer.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

import br.com.acsgsa92.meuplayer.R;
import br.com.acsgsa92.meuplayer.adapter.GroupAdapter;
import br.com.acsgsa92.meuplayer.modelo.Musica;
import br.com.acsgsa92.meuplayer.modelo.Prefer;
import br.com.acsgsa92.meuplayer.modelo.ServiceMusica;

import static br.com.acsgsa92.meuplayer.activitys.MainActivity.FRAGLIS;
import static br.com.acsgsa92.meuplayer.activitys.MainActivity.PAGER;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.ALBUM;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.ARTIST;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.DATA;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.GRUPOS;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.SELECAO;
import static br.com.acsgsa92.meuplayer.player.PlayerService.SETLISTA;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends BaseFragment {

    private String[] groups;
    private Parcelable[] musicas;
    private List<Parcelable[]> listaGruposMusicas;
    private ServiceMusica serviceMusica;
    private String constante;
    private String selecao;
    private View view;

    public GroupFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group, container, false);
        serviceMusica = new ServiceMusica(getContext());
        GridView gridView = (GridView) view.findViewById(R.id.fg_gridview);
        porGrupo();
        if (groups != null){
            gridView.setAdapter(new GroupAdapter(getContext(), groups, onClickGroup()));
        }
        return view;
    }

    private GroupAdapter.GridViewItemClick onClickGroup() {
        return new GroupAdapter.GridViewItemClick() {
            @Override
            public void OnClickGroup(int idx, String tipo) {
                switch (tipo){
                    case "image":
                        String a = getString(R.string.musicas_nesse_grupo);
                        toast(getContext(), "\""+groups[idx]+"\"  "+listaGruposMusicas.get(idx).length+" "+a);
                        break;
                    case "card":
                        musicas = listaGruposMusicas.get(idx);
                        Prefer.setInt(getContext(), Prefer.AUTOID, 0);
                        Prefer.setInt(getContext(), Prefer.AUTOIDX, idx);
                        Prefer.setString(getContext(),Prefer.AUTOCONS, constante);
                        Prefer.setString(getContext(),Prefer.AUTOSEL, selecao);
                        getContext().sendBroadcast(new Intent(SETLISTA).putExtra(SETLISTA, musicas));
                        getContext().sendBroadcast(new Intent(PAGER).putExtra(PAGER, FRAGLIS));
                        break;
                }

            }
        };
    }


    private void porGrupo(){
        constante = getArguments().getString(GRUPOS, DATA);
        selecao = getArguments().getString(SELECAO, GRUPOS);
        try {
            listaGruposMusicas = serviceMusica.getMusicas(selecao, constante);
        } catch (Exception e) {
            toast(getContext(), "Erro: 002");
        }
        if (listaGruposMusicas == null)
            return;
        groups = new String[listaGruposMusicas.size()];
        int idx = 0;
        switch (constante){
            case ALBUM:
                for (Parcelable[] a : listaGruposMusicas){
                    Musica b = (Musica) a[0];
                    groups[idx] = b.albun;
                    idx++;
                }
                break;
            case ARTIST:
                for (Parcelable[] a : listaGruposMusicas){
                    Musica b = (Musica) a[0];
                    groups[idx] = b.artista;
                    idx++;
                }
                break;
            case DATA:
                for (Parcelable[] a : listaGruposMusicas){
                    Musica b = (Musica) a[0];
                    groups[idx] = b.pasta;
                    idx++;
                }
                break;
        }

    }


}
