package com.arpithasomayaji.chatroomapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.arpithasomayaji.chatroomapplication.UserProfileScreen.UserProfileScreen;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by arpitha.somayaji on 11/13/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data= remoteMessage.getData();
        String notificationTitle = remoteMessage.getNotification().getTitle();
        String notificationMessage = remoteMessage.getNotification().getBody();

        String clickAction = remoteMessage.getNotification().getClickAction();

//        String fromUserId = remoteMessage.getData().get("from_user_id");

//        String notificationTitle = data.get("title");
//        String notificationMessage = data.get("message");
//
//        String clickAction = data.get("click_action");

        String fromUserId = data.get("from_user_id");


        Intent resultIntent = new Intent(this, UserProfileScreen.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.putExtra("from_user_id", fromUserId);
        resultIntent.setAction(fromUserId);

        TaskStackBuilder taskStackBuilder=TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(resultIntent);


        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.oval)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationMessage)
                        .setContentIntent(resultPendingIntent)
                        .setSound(defaultSoundUri)
                        .setAutoCancel(true)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH);

        int notificationId = (int) System.currentTimeMillis();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, mBuilder.build());

    }
}
