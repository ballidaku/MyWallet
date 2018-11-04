package ballidaku.mywallet.commonClasses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by sharanpalsingh on 26/03/17.
 */
public class MySharedPreference
{

    public final String PreferenceName = "MyWalletPreference";


    public MySharedPreference()
    {
    }

    public static MySharedPreference instance = null;

    public static MySharedPreference getInstance()
    {
        if (instance == null)
        {
            instance = new MySharedPreference();
        }

        return instance;
    }


     private SharedPreferences getPreference(Context context)
    {
        return context.getSharedPreferences(PreferenceName, Activity.MODE_PRIVATE);
    }


    public void saveToken(Context context, String fcmToken)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.FCM_TOKEN, fcmToken);
        editor.apply();
    }


    public String getToken(Context context)
    {
        return getPreference(context).getString(MyConstant.FCM_TOKEN, "");
    }


    public void saveUser(Context context, HashMap<String, Object> map)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.USER_ID, (String) map.get(MyConstant.USER_ID));
        editor.putString(MyConstant.USER_NAME, (String) map.get(MyConstant.USER_NAME));
        editor.putString(MyConstant.USER_EMAIL, (String) map.get(MyConstant.USER_EMAIL));
        editor.putString(MyConstant.USER_PHONE, (String) map.get(MyConstant.USER_PHONE));
        editor.apply();
    }

    public String getUserID(Context context)
    {
        return getPreference(context).getString(MyConstant.USER_ID, "");
    }

    public void clearUserID(Context context)
    {
        getPreference(context).edit().putString(MyConstant.USER_ID, "").apply();
    }

    public String getUserName(Context context)
    {
        return getPreference(context).getString(MyConstant.USER_NAME, "");
    }

    public String getUserEmail(Context context)
    {
        return getPreference(context).getString(MyConstant.USER_EMAIL, "");
    }

    public String getUserPhone(Context context)
    {
        return getPreference(context).getString(MyConstant.USER_PHONE, "");
    }

    public String getMPIN(Context context)
    {
        return getPreference(context).getString(MyConstant.MPIN, "");
    }

    public void clearMPIN(Context context)
    {
        getPreference(context).edit().putString(MyConstant.MPIN, "").apply();
    }

    public void saveMPIN(Context context, String mpin)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.MPIN, mpin);
        editor.apply();
    }
}
