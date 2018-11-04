package ballidaku.mywallet.commonClasses;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import ballidaku.mywallet.R;
import ballidaku.mywallet.mainScreens.activities.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationHelper
{
    private static NotificationHelper instance = new NotificationHelper();

    public static NotificationHelper getInstance()
    {
        return instance;
    }

    public void showNotificationWithActionButton(Context context)
    {

        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.setAction(Intent.ACTION_MAIN);
        intent1.addCategory(Intent.CATEGORY_LAUNCHER);

        Intent intent2 = new Intent(context, MainActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle extras2 = new Bundle();
        extras2.putInt(MyConstant.FROM_WHERE, MyConstant.EXIT_ID);
        intent2.putExtras(extras2);


        PendingIntent resultPendingIntent1 = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent resultPendingIntent2 = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        String title = context.getString(R.string.app_name);
        String message = context.getString(R.string.app_running);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationHelperOreo notificationHelperOreo = new NotificationHelperOreo(context);
            notificationHelperOreo.notify(MyConstant.NOTIFICATION_ID, notificationHelperOreo.getNotification1(title, message, resultPendingIntent1, resultPendingIntent2));
        }
        else
        {
            Notification notification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_notification_small_app_icon)
                    .setColor(ContextCompat.getColor(context, R.color.colorNotificationBack))
                    .setWhen(0)
//                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent1)
                    .setOngoing(true)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification_small_app_icon))
                    .setContentText(message)
                    //.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .addAction(R.drawable.ic_exit, context.getString(R.string.exit), resultPendingIntent2)
                    .build();

            if (notificationManager != null)
            {
                notificationManager.notify(MyConstant.NOTIFICATION_ID, notification);
            }
        }
    }




}
