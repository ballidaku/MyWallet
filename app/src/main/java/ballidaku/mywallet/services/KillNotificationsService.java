package ballidaku.mywallet.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import ballidaku.mywallet.commonClasses.MyConstant;

public class KillNotificationsService extends Service
{
    public class KillBinder extends Binder
    {
        public final Service service;
        public KillBinder(Service service)
        {
            this.service = service;
        }
    }

    private NotificationManager mNM;
    private final IBinder mBinder = new KillBinder(this);

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return Service.START_STICKY;
    }

    @Override
    public void onCreate()
    {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        assert mNM != null;
        mNM.cancel(MyConstant.NOTIFICATION_ID);
    }
}
