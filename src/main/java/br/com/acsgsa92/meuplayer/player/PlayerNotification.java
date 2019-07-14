package br.com.acsgsa92.meuplayer.player;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.RemoteViews;

import br.com.acsgsa92.meuplayer.R;
import br.com.acsgsa92.meuplayer.activitys.MainActivity;

import static br.com.acsgsa92.meuplayer.player.PlayerReceiver.MUDA;
import static br.com.acsgsa92.meuplayer.player.PlayerService.ANTE;
import static br.com.acsgsa92.meuplayer.player.PlayerService.PLAY;
import static br.com.acsgsa92.meuplayer.player.PlayerService.PROX;


public class PlayerNotification {


    private static final String NOTIFICATION_TAG = "Player";
    private static final int id = 0;


    private static PendingIntent getPending(Context context){
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        if (!MainActivity.activity)
            stackBuilder.addNextIntent(new Intent(context, MainActivity.class));
        else
            stackBuilder.addNextIntent(new Intent());
        PendingIntent p = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
        return p;
    }

    public static void notify(final Context context, String musica, boolean onoff, int corrent, int max, boolean status) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_stat_player);
        builder.setCategory(Notification.CATEGORY_STATUS);
        builder.setAutoCancel(false);
        builder.setContentIntent(getPending(context));
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.notification_player);
            builder.setPriority(Notification.PRIORITY_MAX);
            builder.setContent(views);
            if (onoff) {
                views.setViewVisibility(R.id.np_btn_play, View.VISIBLE);
                if (status) {
                    views.setImageViewResource(R.id.np_btn_play, android.R.drawable.ic_media_pause);
                } else {
                    views.setImageViewResource(R.id.np_btn_play, android.R.drawable.ic_media_play);
                }
                views.setViewVisibility(R.id.np_btn_ant, View.VISIBLE);
                views.setViewVisibility(R.id.np_btn_pro, View.VISIBLE);
                views.setViewVisibility(R.id.np_progressbar, View.VISIBLE);
                views.setTextViewText(R.id.np_text, musica);
                views.setProgressBar(R.id.np_progressbar, max, corrent, false);
                views.setOnClickPendingIntent(R.id.np_btn_ant, PendingIntent.getBroadcast(context, 0, new Intent(ANTE), 0));
                views.setOnClickPendingIntent(R.id.np_btn_play, PendingIntent.getBroadcast(context, 0, new Intent(PLAY), 0));
                views.setOnClickPendingIntent(R.id.np_btn_pro, PendingIntent.getBroadcast(context, 0, new Intent(PROX), 0));
                views.setOnClickPendingIntent(R.id.np_btn_off, PendingIntent.getBroadcast(context, 0, new Intent(MUDA), 0));
                builder.setOngoing(true);
            } else {
                views.setViewVisibility(R.id.np_btn_ant, View.INVISIBLE);
                views.setViewVisibility(R.id.np_btn_play, View.INVISIBLE);
                views.setViewVisibility(R.id.np_btn_pro, View.INVISIBLE);
                views.setViewVisibility(R.id.np_progressbar, View.INVISIBLE);
                views.setTextViewText(R.id.np_text, "Ligue");
            }
        } else {
            if (onoff) {
                builder.setContentTitle(musica);
                builder.setProgress(max, corrent, false);
                builder.addAction(android.R.drawable.ic_media_previous, null, PendingIntent.getBroadcast(context, 0, new Intent(ANTE), 0));
                builder.addAction(android.R.drawable.ic_media_play, null, PendingIntent.getBroadcast(context, 0, new Intent(PLAY), 0));
                builder.addAction(android.R.drawable.ic_media_next, null, PendingIntent.getBroadcast(context, 0, new Intent(PROX), 0));
            } else {
                cancel(context);
            }
        }
        final NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(id, builder.build());
    }

    public static void cancel(Context context) {
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.cancel(id);
    }
}
