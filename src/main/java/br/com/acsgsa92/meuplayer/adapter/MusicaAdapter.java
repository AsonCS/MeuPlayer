package br.com.acsgsa92.meuplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import br.com.acsgsa92.meuplayer.R;
import br.com.acsgsa92.meuplayer.modelo.Musica;

/**
 * Created by acsgs on 28/03/2017.
 */

public class MusicaAdapter extends RecyclerView.Adapter<MusicaAdapter.MusicasViewHolder> {
    protected static String TAG = "App: MeuPlayer";

    private Context context;
    private List<Musica> musicas;
    private MusicaOnClickListener onClickMusica;
    public MusicaAdapter(Context context, List<Musica> musicas, MusicaOnClickListener onClickListener){
        this.context = context;
        this.musicas = musicas;
        this.onClickMusica = onClickListener;
    }

    @Override
    public MusicasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_music_lista , parent, false);
        MusicasViewHolder holder = new MusicasViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MusicasViewHolder holder, final int position) {
        Musica music = musicas.get(position);
        if (music != null){
            holder.nome.setText(music.nome);

            // Click
            if (onClickMusica != null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickMusica.OnClickMusica(holder.itemView, position);
                    }
                });
                holder.menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickMusica.OnClickMenuMusica(holder.menu, position);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.musicas != null ? this.musicas.size() : 0;
    }

    public interface MusicaOnClickListener{
        void OnClickMusica(View view, int idx);
        void OnClickMenuMusica(View view, int idx);
    }

    public class MusicasViewHolder extends RecyclerView.ViewHolder{
        public TextView nome;
        public ImageButton menu;
        public MusicasViewHolder(View view){
            super(view);
            this.nome = (TextView) view.findViewById(R.id.aml_text);
            this.menu = (ImageButton) view.findViewById(R.id.aml_menu);
        }
    }
}
