package br.com.acsgsa92.meuplayer.player;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;

import java.io.IOException;
import java.util.List;

import br.com.acsgsa92.meuplayer.MeuPlayer;
import br.com.acsgsa92.meuplayer.activitys.MainActivity;
import br.com.acsgsa92.meuplayer.modelo.Musica;
import br.com.acsgsa92.meuplayer.modelo.Prefer;
import br.com.acsgsa92.meuplayer.modelo.ServiceMusica;

import static br.com.acsgsa92.meuplayer.fragments.ListaFragment.LISTA;
import static br.com.acsgsa92.meuplayer.modelo.Prefer.AUTOCONS;
import static br.com.acsgsa92.meuplayer.modelo.Prefer.AUTOID;
import static br.com.acsgsa92.meuplayer.modelo.Prefer.AUTOIDX;
import static br.com.acsgsa92.meuplayer.modelo.Prefer.AUTOSEL;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.TITLE;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.TODAS;
import static br.com.acsgsa92.meuplayer.player.PlayerFragment.btnPlay;
import static br.com.acsgsa92.meuplayer.player.PlayerFragment.seekBar;

public class PlayerService extends Service {


    private MediaPlayer mediaPlayer;
    private Parcelable[] musicas;
    private ServiceMusica serviceMusica;
    private Musica musica;
    private int id = 0;
    private Handler handler;

    public static final String PLAY = "PLAY";
    public static final String PROX = "PROX";
    public static final String ANTE = "ANTE";
    public static final String PARA = "PARA";
    public static final String NOME = "NOME";
    public static final String STATU = "STATUS";
    public static final String PEDE = "PEDE";
    public static final String SETMUSICA = "SETMUSICA";
    public static final String SETLISTA = "SETLISTA";
    public static final String CONSTANTESM = "CONSTANTESM";
    public static final String PLAYLIST = "PLAYLIST";
    public static final String DURATION = "DURATION";
    public static final String CORRENT = "CORRENT";
    public static final String ID = "ID";
    public static boolean CONECTADO;
    public static boolean play = false;

    public static final int TOCANDO = 1;
    public static final int PAUSADA = 2;
    public static final int PARADA = 0;
    public static int STATUS = PARADA;
    private String nome = "";


    // Service
    @Override
    public void onCreate() {
        CONECTADO = true;
        serviceMusica = new ServiceMusica(getApplicationContext());
        registerReceiver(playerReceiver, new IntentFilter(PLAY));
        registerReceiver(playerReceiver, new IntentFilter(PROX));
        registerReceiver(playerReceiver, new IntentFilter(ANTE));
        registerReceiver(playerReceiver, new IntentFilter(PARA));
        registerReceiver(playerReceiver, new IntentFilter(NOME));
        registerReceiver(playerReceiver, new IntentFilter(PEDE));
        registerReceiver(playerReceiver, new IntentFilter(SETMUSICA));
        registerReceiver(playerReceiver, new IntentFilter(SETLISTA));
        registerReceiver(playerReceiver, new IntentFilter(CORRENT));
        registerReceiver(playerReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));
        handler = new Handler();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                proxima();
            }
        });
        preenche();
        intentStatus();
        runnable.run();
    }

    @Override
    public void onDestroy() {
        CONECTADO = false;
        PlayerNotification.notify(getBaseContext(), "", false, 0, 0, play);
        mediaPlayer.release();
        mediaPlayer = null;
        intentStatus();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void play() {
        if (mediaPlayer != null) {
            switch (STATUS) {
                case TOCANDO:
                    mediaPlayer.pause();
                    STATUS = PAUSADA;
                    play = false;
                    break;
                case PAUSADA:
                    mediaPlayer.start();
                    STATUS = TOCANDO;
                    play = true;
                    break;
            }
            if (btnPlay != null) {
                if (play)
                    btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                else
                    btnPlay.setImageResource(android.R.drawable.ic_media_play);
            }
        }
    }

    private void stop() {
        STATUS = PARADA;
        stopSelf();
    }

    private void proxima() {
        if (musicas != null && mediaPlayer != null){
            switch (STATUS){
                case TOCANDO:
                    ///mediaPlayer.stop();
                    if (id >= musicas.length - 1){
                        id = 0;
                    } else {
                        id++;
                    }
                    break;
                case PAUSADA:
                    //mediaPlayer.stop();
                    if (id >= musicas.length - 1){
                        id = 0;
                    } else {
                        id++;
                    }
                    break;
            }
            musica = (Musica) musicas[id];
            setMusica(musica);
        }
    }

    private void anterior() {
        if (musicas != null && mediaPlayer != null){
            switch (STATUS){
                case TOCANDO:
                    //mediaPlayer.stop();
                    if (id <= 0){
                        id = musicas.length - 1;
                    } else {
                        id--;
                    }
                    break;
                case PAUSADA:
                    //mediaPlayer.stop();
                    if (id <= 0){
                        id = musicas.length - 1;
                    } else {
                        id--;
                    }
                    break;
            }
            musica = (Musica) musicas[id];
            setMusica(musica);
        }
    }

    private void setMusica(Musica musica) {
        //log("setMusica > "+musica.nome);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musica.caminho);
            mediaPlayer.prepare();
            nome = musica.nome;
            Prefer.setInt(this, AUTOID, id);
        } catch (IOException e) {
            MeuPlayer.toast(this, "Erro: 003");
        }
        if (STATUS == TOCANDO){
            STATUS = PAUSADA;
            play();
        } else {
            STATUS = PAUSADA;
            play = false;
        }
        intentStatus();
    }


    // Broadcast Receiver
    private BroadcastReceiver playerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            MeuPlayer.log(action);
            switch (action){
                case PLAY:
                    play();
                    break;
                case PROX:
                    proxima();
                    break;
                case ANTE:
                    anterior();
                    break;
                case PARA:
                    stop();
                    break;
                case PEDE:
                    intentStatus();
                    sendBroadcast(new Intent(LISTA).putExtra(LISTA, musicas));
                    break;
                case SETMUSICA:
                    int idx = intent.getIntExtra(SETMUSICA, -1);
                    if (idx != -1 && musicas != null){
                        id = idx;
                        musica = idx < musicas.length ? (Musica) musicas[idx] : null;
                        if (musica != null)
                            setMusica(musica);
                    }
                    break;
                case SETLISTA:
                    Parcelable[] a = intent.getParcelableArrayExtra(SETLISTA);
                    if (a != null) {
                        musicas = a;
                        id = -1;
                        intentStatus();
                        sendBroadcast(new Intent(LISTA).putExtra(LISTA, musicas));
                    }
                    break;
                case CORRENT:
                    int corrent = intent.getIntExtra(CORRENT, -1);
                    if (mediaPlayer != null && corrent >= 0)
                        mediaPlayer.seekTo(corrent);
                    break;
                case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                    if (mediaPlayer != null)
                        sendBroadcast(new Intent("PLAY"));
            }
        }
    };

    private void intentStatus(){
        if (CONECTADO) {
            if (PlayerFragment.visible) {
                PlayerFragment.setViews(CONECTADO);
                PlayerFragment.text.setText(nome);
            }
            if (btnPlay != null) {
                if (play)
                    btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                else
                    btnPlay.setImageResource(android.R.drawable.ic_media_play);
            }
        } else {
            if (PlayerFragment.visible) {
                PlayerFragment.setViews(false);
            }
            if (MainActivity.activity)
                PlayerNotification.cancel(this);
            Parcelable[] a = null;
            sendBroadcast(new Intent(LISTA).putExtra(LISTA, a));
        }
    }

    private void preenche(){
        id = Prefer.getInt(this, AUTOID);
        if (id < 0)
            id = 0;
        String cons = Prefer.getString(this, AUTOCONS);
        if (cons == null)
            cons = TITLE;
        String sel = Prefer.getString(this, AUTOSEL);
        if (sel == null)
            sel = TODAS;
        int idx = Prefer.getInt(this, AUTOIDX);
        if (idx < 0)
            idx = 0;
        try {
            List<Parcelable[]> a = serviceMusica.getMusicas(sel, cons);
            musicas = a.get(idx);
        } catch (Exception e) {
            MeuPlayer.toast(this, "Erro: 004");
        }
        if (musicas != null) {
            sendBroadcast(new Intent(LISTA).putExtra(LISTA, musicas));
            id = id < musicas.length ? id : 0;
            musica = (Musica) musicas[id];
            setMusica(musica);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (CONECTADO) {
                int corrent = mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0;
                int duration = mediaPlayer != null ? mediaPlayer.getDuration() : 0;
                if (seekBar != null) {
                    seekBar.setProgress(corrent);
                    seekBar.setMax(duration);
                }
                PlayerNotification.notify(getBaseContext(), nome, CONECTADO, corrent, duration, play);
                handler.postDelayed(this, 300);
            }
        }
    };
}
