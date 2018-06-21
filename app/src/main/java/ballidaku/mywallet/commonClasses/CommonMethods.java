package ballidaku.mywallet.commonClasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import ballidaku.mywallet.R;

/**
 * Created by sharanpalsingh on 05/03/17.
 */
public class CommonMethods {
    private String TAG = "CommonMethods";

    public static Toast toast;
    public static Snackbar snackbar;

    private static CommonMethods instance = new CommonMethods();

    public static CommonMethods getInstance() {
        return instance;
    }


    public void show_Toast(Context context, String text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);

        toast.show();
    }


    /*TO SHOW SNACKBAR*/

    public void show_snackbar(View view, Context context, String message) {

        if (snackbar != null) {
            snackbar.dismiss();
        }

        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        snackbar.show();

    }


    public void switchfragment(Fragment fromWhere, Fragment toWhere) {
        FragmentTransaction fragmentTransaction = fromWhere.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, toWhere);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void switchfragment(Context context, Fragment toWhere) {

        FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, toWhere);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void hideKeypad(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Converting dp to pixel
     */
    public int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public String setSpaceFormat(String value) {
        value = value.replaceAll(" ", "");
        DecimalFormat df;

        if (value.length() == 19) {
            df = new DecimalFormat("####,####,####,####,###");
        } else {
            df = new DecimalFormat("####,####,####,####,####,####");
        }

        return value.trim().isEmpty() ? "" : df.format(Long.parseLong(value)).replaceAll(",", " ");

    }

    public String encrypt(Context context, String message) {
        return encrypt(message, MySharedPreference.getInstance().getUserEmail(context));
    }

    public String encrypt(String message, String password) {
        String encryptedMsg = "";
        try {
            encryptedMsg = AESCrypt.encrypt(password, message);
        } catch (GeneralSecurityException e) {
            //handle error
        }

        return encryptedMsg;
    }


    public String decrypt(Context context, String message) {
        return decrypt(message, MySharedPreference.getInstance().getUserEmail(context));
    }

    public String decrypt(String encryptedMsg, String password) {
        String messageAfterDecrypt = "";

        try {
            messageAfterDecrypt = AESCrypt.decrypt(password, encryptedMsg);
        } catch (GeneralSecurityException e) {
            //handle error - could be due to incorrect password or tampered encryptedMsg
        }

        return messageAfterDecrypt;
    }

    public boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean isValidMobile(String phone) {
        boolean check = false;
        check = !Pattern.matches("[a-zA-Z]+", phone) && phone.length() == 10;
        return check;
    }


    /**********************************************************************************************/
    /*Copy Content*/

    /**********************************************************************************************/
    public void copyContent(Context context, String data) {

        if (!data.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip2 = ClipData.newPlainText(context.getString(R.string.copied_text), data);
            clipboard.setPrimaryClip(clip2);

            CommonMethods.getInstance().show_Toast(context, context.getString(R.string.data_copied));
        } else {
            CommonMethods.getInstance().show_Toast(context, context.getString(R.string.select_atleast_single));
        }
    }

    /**********************************************************************************************/
    /*Share Content*/

    /**********************************************************************************************/
    public void shareContent(Context context, String data) {

        if (!data.isEmpty()) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.account_details));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, data);
            context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.share_using)));
        } else {
            CommonMethods.getInstance().show_Toast(context, context.getString(R.string.select_atleast_single));
        }
    }


    /**********************************************************************************************/
    /*Copy Content*/

    /**********************************************************************************************/
    public class MyTouchListener implements View.OnTouchListener {
        EditText editText;
        Context context;

        public MyTouchListener(Context context, EditText editText) {
            this.editText = editText;
            this.context = context;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if (event.getAction() == MotionEvent.ACTION_UP) {
//                if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
                if (!editText.getText().toString().trim().isEmpty()) {

                    Drawable left = editText.getCompoundDrawables()[DRAWABLE_LEFT];
                    Drawable right;

                    Drawable alreadyRight = editText.getCompoundDrawables()[DRAWABLE_RIGHT];
                    Drawable unSelected = context.getResources().getDrawable(R.drawable.ic_check_unselected);

                    if (unSelected.getConstantState().equals(alreadyRight.getConstantState())) {
                        right = context.getResources().getDrawable(R.drawable.ic_check_selected);
                    } else {
                        right = unSelected;
                    }
                    editText.setCompoundDrawablesWithIntrinsicBounds(left, null, right, null);
                    return true;
                }
            }
            return false;
        }
    }

}
