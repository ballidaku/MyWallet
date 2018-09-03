package ballidaku.mywallet.commonClasses;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sharanpalsingh on 04/06/17.
 */

public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();


        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }

    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

Ballidaku