package ballidaku.mywallet.services;

/*class KillNotificationsService : Service()
{

    private var mNM: NotificationManager? = null
    private val mBinder = KillBinder(this)

    inner class KillBinder(val service: Service) : Binder()

    override fun onBind(intent: Intent): IBinder?
    {
        return mBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        return Service.START_STICKY
    }

    override fun onCreate()
    {
        mNM = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        assert(mNM != null)
        mNM!!.cancel(MyConstant.NOTIFICATION_ID)
    }
}*/


import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import ballidaku.mywallet.commonClasses.MyConstant;

public class KillNotificationsService extends Service
{

    private NotificationManager mNM;
    private final IBinder mBinder = new KillBinder(this);

    public class KillBinder extends Binder
    {
        public final Service service;
        KillBinder(Service service)
        {
            this.service = service;
        }
    }

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