package com.droiddevgeeks.railjourney.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.droiddevgeeks.railjourney.MainActivity;
import com.droiddevgeeks.railjourney.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Vampire on 2017-01-13.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);
        createNotification(remoteMessage.getNotification().getBody());
    }

    private void createNotification(String body)
    {
        Intent notificationIntent = new Intent(this , MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("NotificationMessage" , body);
        notificationIntent.putExtras(bundle);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0 , notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSound  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.applogo)
                .setContentTitle("railJourney notification")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager manager  = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0 , notification.build());

    }
}
