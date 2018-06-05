package ballidaku.mywallet.commonClasses;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

import ballidaku.mywallet.R;
import ballidaku.mywallet.roomDatabase.MyRoomDatabase;
import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;

/**
 * Created by sharanpalsingh on 05/03/17.
 */
public class CommonMethods
{
    private String TAG = "CommonMethods";

    public static Toast toast;
    public static Snackbar snackbar;

    private static CommonMethods instance = new CommonMethods();

    public static CommonMethods getInstance()
    {
        return instance;
    }


    public void show_Toast(Context context, String text)
    {
        if (toast != null)
        {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);

        toast.show();
    }


    /*TO SHOW SNACKBAR*/

    public void show_snackbar(View view, Context context, String message)
    {

        if (snackbar != null)
        {
            snackbar.dismiss();
        }

        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        snackbar.show();

    }


    public void switchfragment(Fragment fromWhere, Fragment toWhere)
    {
        FragmentTransaction fragmentTransaction = fromWhere.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, toWhere);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void switchfragment(Context context, Fragment toWhere)
    {

        FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, toWhere);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void hideKeypad(Activity activity)
    {
        View view = activity.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Converting dp to pixel
     */
    public int dpToPx(Context context, int dp)
    {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public String setSpaceFormat(String value)
    {
        value = value.replaceAll(" ", "");
        DecimalFormat df;

        if (value.length() == 19)
        {
            df = new DecimalFormat("####,####,####,####,###");
        }
        else
        {
            df = new DecimalFormat("####,####,####,####,####,####");
        }

        return value.trim().isEmpty() ? "" : df.format(Long.parseLong(value)).replaceAll(",", " ");

    }

    public String encrypt(Context context, String message)
    {
        return encrypt(message, MySharedPreference.getInstance().getUserEmail(context));
    }

    public String encrypt(String message, String password)
    {
        String encryptedMsg = "";
        try
        {
            encryptedMsg = AESCrypt.encrypt(password, message);
        }
        catch (GeneralSecurityException e)
        {
            //handle error
        }

        return encryptedMsg;
    }


    public String decrypt(Context context, String message)
    {
        return decrypt(message, MySharedPreference.getInstance().getUserEmail(context));
    }

    public String decrypt(String encryptedMsg, String password)
    {
        String messageAfterDecrypt = "";

        try
        {
            messageAfterDecrypt = AESCrypt.decrypt(password, encryptedMsg);
        }
        catch (GeneralSecurityException e)
        {
            //handle error - could be due to incorrect password or tampered encryptedMsg
        }

        return messageAfterDecrypt;
    }

    public boolean isValidEmail(CharSequence target)
    {
        if (target == null)
        {
            return false;
        }
        else
        {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean isValidMobile(String phone)
    {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone))
        {
//            if(phone.length() < 6 || phone.length() > 13) {
            if (phone.length() != 10)
            {
                check = false;
            }
            else
            {
                check = true;
            }
        }
        else
        {
            check = false;
        }
        return check;
    }

    class MyAsyncTask<T> extends AsyncTask<Void, Void, Void>
    {
        Context context;
        T data;
        public MyAsyncTask(Context context,T data)
        {
            this.context=context;
            this.data=data;
            execute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            MyRoomDatabase.getInstance(context).accountDetailsDataModelDao().insert((AccountDetailsDataModel) data);

            List<AccountDetailsDataModel> list = MyRoomDatabase.getInstance(context).accountDetailsDataModelDao().getAllData();

            for (int i = 0; i < list.size(); i++)
            {

                Log.e(TAG, "" + list.get(i).getId());
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {

        }
    }


}
