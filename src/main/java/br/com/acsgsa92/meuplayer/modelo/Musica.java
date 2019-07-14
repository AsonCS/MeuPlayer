package br.com.acsgsa92.meuplayer.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acsgs on 28/03/2017.
 */

public class Musica implements Parcelable {

    public String nome;
    public String caminho;
    public String pasta;
    public String artista;
    public String albun;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nome);
        dest.writeString(this.caminho);
        dest.writeString(this.pasta);
        dest.writeString(this.artista);
        dest.writeString(this.albun);
    }

    private void readToParcel(Parcel source) {
        this.nome = source.readString();
        this.caminho = source.readString();
        this.pasta = source.readString();
        this.artista = source.readString();
        this.albun = source.readString();
    }

    public static final Parcelable.Creator<Musica> CREATOR = new Parcelable.Creator<Musica>(){
        @Override
        public Musica createFromParcel(Parcel source) {
            Musica musica = new Musica();
            musica.readToParcel(source);
            return musica;
        }

        @Override
        public Musica[] newArray(int size) {
            return new Musica[size];
        }
    };
}

