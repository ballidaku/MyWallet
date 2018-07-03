package ballidaku.mywallet.commonClasses;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import ballidaku.mywallet.R;

public class NotificationHelperOreo extends ContextWrapper
{
    private NotificationManager notifManager;
    public static final String CHANNEL_ONE_ID = "com.swipr.ONE";
    public static final String CHANNEL_ONE_NAME = "Channel One";
//    public static final String CHANNEL_TWO_ID = "com.swipr.TWO";
//    public static final String CHANNEL_TWO_NAME = "Channel Two";

//Create your notification channels//

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationHelperOreo(Context base)
    {
        super(base);
        createChannels();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels()
    {

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME, notifManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setShowBadge(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(notificationChannel);

        /*NotificationChannel notificationChannel2 = new NotificationChannel(CHANNEL_TWO_ID,
                  CHANNEL_TWO_NAME, notifManager.IMPORTANCE_DEFAULT);
        notificationChannel2.enableLights(false);
        notificationChannel2.enableVibration(true);
        notificationChannel2.setLightColor(Color.RED);
        notificationChannel2.setShowBadge(false);
        getManager().createNotificationChannel(notificationChannel2);*/

    }

    //Create the notification that’ll be posted to Channel One//
    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationCompat.Builder getNotification1(String title, String body, PendingIntent resultPendingIntent1, PendingIntent resultPendingIntent2)
    {
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ONE_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setOngoing(true)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setSmallIcon(R.drawable.ic_notification_small_app_icon)
                .setContentIntent(resultPendingIntent1)
            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_small_app_icon))
//                .setAutoCancel(true)
            .addAction(R.drawable.ic_exit, getBaseContext().getString(R.string.exit), resultPendingIntent2);
    }


    public void notify(int id, NotificationCompat.Builder notification)
    {
        getManager().notify(id, notification.build());
    }

    //Send your notifications to the NotificationManager system service//
    private NotificationManager getManager()
    {
        if (notifManager == null)
        {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notifManager;
    }
}