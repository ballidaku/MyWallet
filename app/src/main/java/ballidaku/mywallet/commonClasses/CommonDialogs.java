package ballidaku.mywallet.commonClasses;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import ballidaku.mywallet.R;
import ballidaku.mywallet.mPin.IndicatorDots;
import ballidaku.mywallet.mPin.PinLockListener;
import ballidaku.mywallet.mPin.PinLockView;


/**
 * Created by sharanpalsingh on 05/03/17.
 */
public class CommonDialogs
{
    String TAG= CommonDialogs.class.getSimpleName();

    public Dialog dialog;

    private static CommonDialogs instance = new CommonDialogs();

    public static CommonDialogs getInstance()
    {
        return instance;
    }


    private void dismissDialog()
    {
        if (dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }
    }

    public void showForgotPasswordDialog(Context context, CommonInterfaces.forgotPassword forgotPassword)
    {
        dismissDialog();
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_forgot_password);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        EditText editTextForgotPassword = dialog.findViewById(R.id.editTextForgotPassword);
        TextInputLayout textInputLayoutEmail = dialog.findViewById(R.id.textInputLayoutEmail);

        dialog.findViewById(R.id.textViewCancel).setOnClickListener(view -> dismissDialog());

        dialog.findViewById(R.id.textViewSend).setOnClickListener(view ->
        {
            final String email = editTextForgotPassword.getText().toString().trim();
            if (email.isEmpty())
            {
                textInputLayoutEmail.setError(context.getString(R.string.please_enter_email));
            }
            else if (!CommonMethods.getInstance().isValidEmail(email))
            {
                textInputLayoutEmail.setError(context.getString(R.string.please_enter_valid_email));
            }
            else
            {
                dismissDialog();
                forgotPassword.onSend(email);
            }
        });

        editTextForgotPassword.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

                String email=editable.toString();
                if (email.isEmpty())
                {
                    textInputLayoutEmail.setError(context.getString(R.string.please_enter_email));
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                else if (!CommonMethods.getInstance().isValidEmail(email))
                {
                    textInputLayoutEmail.setError(context.getString(R.string.please_enter_valid_email));
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                else
                {
                    textInputLayoutEmail.setErrorEnabled(false);
                }
            }
        });
    }

    private PinLockView mPinLockView;
    private String savedPasscode ;
    private CommonInterfaces.checkPasscode checkPasscode;
    public void showPasscodeDialog(Context context, CommonInterfaces.checkPasscode checkPasscode)
    {
        this.checkPasscode=checkPasscode;

        dismissDialog();
        dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_passcode);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        mPinLockView = dialog.findViewById(R.id.pin_lock_view);
        IndicatorDots mIndicatorDots = dialog.findViewById(R.id.indicator_dots);


        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);

        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));

        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FIXED);

        savedPasscode =  MySharedPreference.getInstance().getMPIN(context);
    }

    private PinLockListener mPinLockListener = new PinLockListener()
    {
        @Override
        public void onComplete(String pin)
        {

            if(savedPasscode.equals(pin))
            {
                dismissDialog();
                checkPasscode.onSuccess();
            }
            else
            {
                checkPasscode.onFailure();
            }
        }

        @Override
        public void onEmpty()
        {
//            Log.e(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin)
        {
//            Log.e(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }

        @Override
        public void onReset()
        {
            mPinLockView.resetPinLockView();
        }
    };


    public void progressDialog(Context context)
    {
        dismissDialog();
        dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        dialog.show();
    }

    public void showMessageDialog(Context context, String message)
    {
        dismissDialog();
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_show_message);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView textViewTitle = dialog.findViewById(R.id.textViewTitle);
        textViewTitle.setText(Html.fromHtml(message));
        dialog.findViewById(R.id.textViewOk).setOnClickListener(view -> dialog.dismiss());
    }


    public void showDeleteAlertDialog(Context context, final CommonInterfaces.deleteDetail deleteDetail)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.delete_entry))
                .setMessage(context.getString(R.string.delete_confirmation))
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteDetail.onDelete())
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void showImportAlertDialog(Context context, final CommonInterfaces.importData importData)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.import_data))
                .setMessage(context.getString(R.string.import_confirmation))
                .setPositiveButton(android.R.string.yes, (dialog, which) -> importData.onImportConfirmation())
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
