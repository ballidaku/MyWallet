package ballidaku.mywallet.model;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.BaseObservable;
import android.os.IBinder;

import ballidaku.mywallet.commonClasses.MySharedPreference;
import ballidaku.mywallet.commonClasses.NotificationHelper;
import ballidaku.mywallet.mainScreens.activities.MainActivity;
import ballidaku.mywallet.services.KillNotificationsService;

/**
 * Created by sharanpalsingh on 26/03/17.
 */
public class MainActivityModel extends BaseObservable
{
    String TAG = String.class.getSimpleName();
    public Context context;
    private String userName;

    private String userEmail;

    public MainActivityModel(Context context)
    {
        this.context = context;
        this.userName = MySharedPreference.getInstance().getUserName(context);
        this.userEmail = MySharedPreference.getInstance().getUserEmail(context);

        notifyChange();

        ((MainActivity) context).getLifecycle().addObserver(new SomeObserver());

        /*Start service which kill notification on removed from stack*/
        context.bindService(new Intent(context, KillNotificationsService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    public String getUserName()
    {
        return userName;
    }

    public String getUserEmail()
    {
        return userEmail;
    }

    private class SomeObserver implements LifecycleObserver
    {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void onDestroy()
        {
            if (mConnection != null)
            {
                context.unbindService(mConnection);
            }
        }
    }

    /*Service  which kills notification on removed from stack*/
    private ServiceConnection mConnection = new ServiceConnection()
    {
        public void onServiceConnected(ComponentName className, IBinder binder)
        {
            ((KillNotificationsService.KillBinder) binder).service.startService(new Intent(context, KillNotificationsService.class));

            NotificationHelper.getInstance().showNotificationWithActionButton(context);
        }

        public void onServiceDisconnected(ComponentName className)
        {
        }

    };


    /***********************************************************************/
    // Interface
    /***********************************************************************/
   /* public interface MainActivityModelCallBack
    {
        void onResult();
    }*/


}
