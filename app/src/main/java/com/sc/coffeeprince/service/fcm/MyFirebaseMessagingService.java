package com.sc.coffeeprince.service.fcm;

/**
 * Created by fopa on 2017-09-23.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sc.coffeeprince.R;
import com.sc.coffeeprince.activity.HomeActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        String title = null;
        String message = null;
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            try {
                title = URLDecoder.decode(remoteMessage.getData().get("title"),"utf-8");
                message = URLDecoder.decode(remoteMessage.getData().get("message"),"utf-8");
                sendNotification(title,message);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }

        if (remoteMessage.getNotification() != null) {
            try {
                String test = URLDecoder.decode(remoteMessage.getNotification().getBody(),"utf-8");
                Log.d(TAG, "Message Notification Body: " + test);
                sendNotification(test,"hihi");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        // Check if message contains a notification payload.
        /*if (remoteMessage.getNotification() != null) {
            try {
                String test = URLDecoder.decode(remoteMessage.getNotification().getBody(),"utf-8");
                Log.d(TAG, "Message Notification Body: " + test);
                sendNotification(test);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //sendNotification(remoteMessage.getNotification().getBody());
        }*/

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageTitle,String messageBody) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_local_cafe_white_24dp)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}