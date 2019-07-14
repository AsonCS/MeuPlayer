package br.com.acsgsa92.meuplayer.activitys;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import br.com.acsgsa92.meuplayer.R;
import br.com.acsgsa92.meuplayer.modelo.Prefer;
import br.com.acsgsa92.meuplayer.modelo.ServiceMusica;

import static br.com.acsgsa92.meuplayer.activitys.MainActivity.FRAGLIS;
import static br.com.acsgsa92.meuplayer.activitys.MainActivity.PAGER;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.ALBUM;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.ARTIST;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.DATA;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.GRUPOS;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.SELECAO;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.TITLE;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.TODAS;
import static br.com.acsgsa92.meuplayer.player.PlayerService.SETLISTA;

/**
 * Created by acsgsa92 on 03/2017.
 */

public class BaseActivity extends AppCompatActivity {

    //public static void log(String s){Log.d("Meuplayer"," BaseActivity:\n"+ s);}
    private DrawerLayout drawerLayout;
    private static boolean conect = false;

    public void toast (String s){
        Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
    }

    // Configura o Drawer Layout
    protected void configDrawer(boolean main){
        final NavigationView navigation = (NavigationView) findViewById(R.id.am_navigation);
        drawerLayout = (DrawerLayout) findViewById(R.id.am_drawer_layout);
        if (drawerLayout != null && navigation != null){
            // Configura o texto
            TextView textView = (TextView) navigation.getHeaderView(0).findViewById(R.id.nh_text);
            textView.setText(R.string.app_name);
            // Trata o click
            navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // deixa o item selecionado
                    item.setChecked(true);
                    // trata os cliques
                    onClickItemNavigation(item);
                    // fecha o menu
                    closeNavigation(item);
                    return true;
                }
            });
        }
    }

    private void closeNavigation(MenuItem item){
        if (drawerLayout != null){
            drawerLayout.closeDrawers();
            item.setChecked(false);
        }
    }

    private void onClickItemNavigation(MenuItem item){
        switch (item.getItemId()){
            case R.id.m_todas:
                ServiceMusica serviceMusica = new ServiceMusica(this);
                Parcelable[] a = null;
                try {
                    a = serviceMusica.getMusicas(TODAS, TITLE).get(0);
                } catch (Exception e) {toast("Erro: 001");}
                Prefer.setInt(this, Prefer.AUTOID, 0);
                Prefer.setInt(this, Prefer.AUTOIDX, 0);
                Prefer.setString(this,Prefer.AUTOCONS, TITLE);
                Prefer.setString(this,Prefer.AUTOSEL, TODAS);
                sendBroadcast(new Intent(SETLISTA).putExtra(SETLISTA, a));
                sendBroadcast(new Intent(PAGER).putExtra(PAGER, FRAGLIS));
                break;
            case R.id.m_pastas:
                sendBroadcast(new Intent(GRUPOS).putExtra(GRUPOS, DATA).putExtra(SELECAO, GRUPOS));
                break;
            case R.id.m_albuns:
                sendBroadcast(new Intent(GRUPOS).putExtra(GRUPOS, ALBUM).putExtra(SELECAO, GRUPOS));
                break;
            case R.id.m_artistas:
                sendBroadcast(new Intent(GRUPOS).putExtra(GRUPOS, ARTIST).putExtra(SELECAO, GRUPOS));
                break;
        }
    }
}
