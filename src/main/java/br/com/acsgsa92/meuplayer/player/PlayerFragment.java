package br.com.acsgsa92.meuplayer.player;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import br.com.acsgsa92.meuplayer.R;
import br.com.acsgsa92.meuplayer.fragments.BaseFragment;

import static br.com.acsgsa92.meuplayer.player.PlayerService.CORRENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends BaseFragment {

    public static TextView text;
    public static SeekBar seekBar;
    public static boolean visible;
    private static ImageButton btnAnte, btnProx, btnOff;
    public static ImageButton btnPlay;
    private View view;

    public PlayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player, container, false);
        visible = true;
        text = (TextView) view.findViewById(R.id.fp_text);
        btnAnte = (ImageButton) view.findViewById(R.id.fp_btn_ant);
        btnPlay = (ImageButton) view.findViewById(R.id.fp_btn_play);
        btnProx = (ImageButton) view.findViewById(R.id.fp_btn_pro);
        btnOff = (ImageButton) view.findViewById(R.id.fp_btn_off);
        seekBar = (SeekBar) view.findViewById(R.id.fp_seekbar);
        setViews(false);
        btnAnte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAnterior();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPlay();
            }
        });
        btnProx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickProxima();
            }
        });
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickStop();
            }
        });
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        return view;
    }

    @Override
    public void onDestroyView() {
        visible = false;
        super.onDestroyView();
    }

    public static void setViews(boolean b) {
        if (!b) {
            btnProx.setVisibility(View.INVISIBLE);
            btnPlay.setVisibility(View.INVISIBLE);
            btnAnte.setVisibility(View.INVISIBLE);
            seekBar.setVisibility(View.INVISIBLE);
            text.setText("Ligue");
        } else {
            btnProx.setVisibility(View.VISIBLE);
            btnPlay.setVisibility(View.VISIBLE);
            btnAnte.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.VISIBLE);
            //text.setText(nome);
        }
    }


    // Interacao Service
    private void onClickPlay() {
        if (PlayerService.play) {
            getContext().sendBroadcast(new Intent("PLAY"));
            btnPlay.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            getContext().sendBroadcast(new Intent("PLAY"));
            btnPlay.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    private void onClickStop() {
        getContext().sendBroadcast(new Intent(PlayerReceiver.MUDA));
    }

    private void onClickProxima() {
        btnProx.setEnabled(false);
        getContext().sendBroadcast(new Intent("PROX"));
        btnProx.setEnabled(true);
    }

    private void onClickAnterior() {
        btnAnte.setEnabled(false);
        getContext().sendBroadcast(new Intent("ANTE"));
        btnAnte.setEnabled(true);
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser)
                getContext().sendBroadcast(new Intent(CORRENT).putExtra(CORRENT, progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
