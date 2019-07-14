package br.com.acsgsa92.meuplayer.modelo;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import br.com.acsgsa92.meuplayer.Utils.Permissions;

public class ServiceMusica extends AsyncTaskLoader {

    //private static void log(String s){Log.d("Meuplayer","ServiceMusica:\n "+ s);}
    private Context context;
    private Musica musica;
    private Parcelable[] listaMusicas;
    private List<Parcelable> musicas;
    private List<Parcelable[]> listaGrupoMusicas;
    private String funcao;

    // Constantes
    private static final Uri URIMUSICASEX = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private static final Uri URIMUSICASIN = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;

    public static final String TITLE = MediaStore.Audio.Media.TITLE;
    public static final String ARTIST = MediaStore.Audio.Media.ARTIST;
    public static final String DATA = MediaStore.Audio.Media.DATA;
    public static final String ALBUM = MediaStore.Audio.Media.ALBUM;

    public static final String GRUPOS = "GRUPOS";
    public static final String TODAS = "TODAS";
    public static final String SELECAO = "SELECAO";


    public ServiceMusica(Context context) {
        super(context);
        this.context = context;
    }

    public List<Parcelable[]> getMusicas (String selecao, String constante) throws Exception{
        if (selecao == null || constante == null){
            return null;
        }
        switch (selecao){
            case TODAS:
                funcao = TITLE;
                musicas = loadInBackground();
                listaMusicas = new Parcelable[musicas.size()];
                for (int i=0;i<musicas.size();i++)
                    listaMusicas[i] = musicas.get(i);
                listaGrupoMusicas = new ArrayList<>();
                listaGrupoMusicas.add(listaMusicas);
                break;
            case GRUPOS:
                funcao = constante;
                musicas = loadInBackground();
                listaGrupoMusicas = preencherGrupos(musicas);
        }
        return listaGrupoMusicas;
    }


    // AsyncTaskLoader
    @Override
    public List<Parcelable> loadInBackground() {
        Cursor cOut = null, cIn = null;
        musicas = new ArrayList<>();
        cOut = context.getContentResolver().query(URIMUSICASEX, null, null, null, funcao);
        preencher(cOut);
        cIn = context.getContentResolver().query(URIMUSICASIN, null, null, null, funcao);
        preencher(cIn);
        if (cIn != null)
            cIn.close();
        if (cOut != null)
            cOut.close();
        return musicas;
    }

    private void preencher(Cursor cursor){
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String s = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));
                if (s.equals("audio/mpeg")) {
                    musica = new Musica();
                    musica.nome = cursor.getString(cursor.getColumnIndex(TITLE));
                    musica.caminho = cursor.getString(cursor.getColumnIndex(DATA));
                    musica.albun = cursor.getString(cursor.getColumnIndex(ALBUM));
                    musica.artista = cursor.getString(cursor.getColumnIndex(ARTIST));
                    musica.pasta = pegPasta(musica.caminho);
                    musicas.add(musica);
                }
            }
            cursor.close();
        }
    }

    private List<Parcelable[]> preencherGrupos(List<Parcelable> listPar){
        List<List<Parcelable>> listGrupo = new ArrayList<>();
        int idx = 0;
        boolean cont = true;
        switch (funcao){
            case ALBUM:
                idx = 0;
                cont = true;
                do {
                    List<Parcelable> grupo = new ArrayList<>();
                    Musica b, c;
                    do {
                        grupo.add(listPar.get(idx));
                        idx++;
                        if (idx == listPar.size()){
                            idx = 0;
                            cont = false;
                        }
                        b = (Musica) grupo.get(0);
                        c = (Musica) listPar.get(idx);
                    }while (b.albun.equals(c.albun));
                    listGrupo.add(grupo);
                } while (cont);
                break;
            case ARTIST:
                idx = 0;
                cont = true;
                do {
                    List<Parcelable> grupo = new ArrayList<>();
                    Musica b, c;
                    do {
                        grupo.add(listPar.get(idx));
                        idx++;
                        if (idx == musicas.size()){
                            idx = 0;
                            cont = false;
                        }
                        b = (Musica) grupo.get(0);
                        c = (Musica) listPar.get(idx);
                    }while (b.artista.equals(c.artista));
                    listGrupo.add(grupo);
                } while (cont);
                break;
            case DATA:
                idx = 0;
                cont = true;
                do {
                    List<Parcelable> grupo = new ArrayList<>();
                    Musica b, c;
                    do {
                        grupo.add(listPar.get(idx));
                        idx++;
                        if (idx == musicas.size()){
                            idx = 0;
                            cont = false;
                        }
                        b = (Musica) grupo.get(0);
                        c = (Musica) listPar.get(idx);
                    }while (b.pasta.equals(c.pasta));
                    listGrupo.add(grupo);
                } while (cont);
                break;
        }
        List<Parcelable[]> list = new ArrayList<>();
        Parcelable[] c;
        for (List<Parcelable> a : listGrupo){
            c = new Parcelable[a.size()];
            for (int i=0;i<a.size();i++){
                c[i] = a.get(i);
            }
            list.add(c);
        }
        return list;
    }

    private String pegPasta(String a){
        int d = 0;
        boolean c = false, e = true;
        String b = "";
        for (int i=a.length()-1;i>0;i--){
            if (a.charAt(i) == '/' || a.charAt(i) == '\\'){
                if (c){
                    e = false;
                }
                c = c == true ? false : true;
            }
            if (c){
                if (e){
                    if (a.charAt(i) != '/' || a.charAt(i) != '\\'){
                        b = b + String.valueOf(a.charAt(i));
                    }
                }
            }
        }
        a = "";
        for (int i=b.length()-1;i>0;i--){
            a = a + String.valueOf(b.charAt(i));
        }
        return a;
    }
}
