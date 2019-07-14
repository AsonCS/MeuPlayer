package br.com.acsgsa92.meuplayer.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PlayerReceiver extends BroadcastReceiver {

    public static final String MUDA = "MUDA";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (MUDA.equals(intent.getAction())) {
            if (!PlayerService.CONECTADO) {
                context.startService(new Intent(context, PlayerService.class));
            } else {
                context.sendBroadcast(new Intent("PARA"));
            }
        }
    }
}
