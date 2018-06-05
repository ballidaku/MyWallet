package ballidaku.mywallet.services;

/**
 * Created by sharanpalsingh on 05/03/17.
 */
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 Created by sharan on 7/6/16. */
public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "remoteMessage: " + remoteMessage.getData().toString());
        //        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        //Calling method to generate notification
        //        sendNotification(remoteMessage.getNotification().getBody());

       // sendNotification(remoteMessage.getData().get("message"));
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
   /* private void sendNotification(String messageBody)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri                        defaultSoundUri     = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Firebase Push Notification").setContentText(messageBody).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }*/
}