package br.com.acsgsa92.meuplayer.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import br.com.acsgsa92.meuplayer.R;
import br.com.acsgsa92.meuplayer.Utils.Permissions;
import br.com.acsgsa92.meuplayer.fragments.ContainerFragment;
import br.com.acsgsa92.meuplayer.fragments.ContainerGrFragment;
import br.com.acsgsa92.meuplayer.modelo.Prefer;
import br.com.acsgsa92.meuplayer.player.PlayerFragment;

import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.DATA;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.GRUPOS;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.SELECAO;
import static br.com.acsgsa92.meuplayer.modelo.ServiceMusica.TITLE;

public class MainActivity extends BaseActivity{

    private ViewPager viewPager;
    private PlayerFragment playerFragment;
    private ContainerFragment containerFragment;
    private ContainerGrFragment containerGrFragment;
    private Bundle bundle, savedInstance;
    private String sel, con;
    public static boolean activity;
    public static final String PAGER = "PAGER";
    public static final String VIEW = "VIEW";
    public static final int FRAGGRU = 0;
    public static final int FRAGLIS = 1;
    private Permissions permissions = new Permissions();

    public MainActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (permissions.permissions(this)) {
            registerReceiver(receiver, new IntentFilter(PAGER));
            configDrawer(false);
            if (savedInstanceState == null)
                configFragments();
            pager();
        } else {
            onCreate(savedInstanceState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        activity = false;
    }

    private void configFragments() {
        playerFragment = new PlayerFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.am_containerplayer, playerFragment).commit();

        bundle = new Bundle();
        sel = GRUPOS;
        con = DATA;
        String s = Prefer.getString(this, Prefer.AUTOCONS);
        if (s != null){
            con = con == TITLE ? DATA : con;
        }
        bundle.putString(GRUPOS, con);
        bundle.putString(SELECAO, sel);
        containerGrFragment = new ContainerGrFragment();
        containerGrFragment.setArguments(bundle);
    }


    private void pager(){
        viewPager = (ViewPager) findViewById(R.id.am_viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                switch (position){
                    case 0:
                        fragment = containerGrFragment;
                        break;
                    case 1:
                        fragment = new ContainerFragment();
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case PAGER:
                    if (intent.getIntExtra(PAGER, -1) == 0 || intent.getIntExtra(PAGER, -1) == 1){
                        viewPager.setCurrentItem(intent.getIntExtra(PAGER, 0));
                    }
                    break;
                case VIEW:
                    MainActivity activity = new MainActivity();
                    activity.onResume();
            }
        }
    };
}
