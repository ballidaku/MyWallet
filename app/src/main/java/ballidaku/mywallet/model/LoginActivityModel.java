package ballidaku.mywallet.model;

import android.content.Context;
import android.text.InputType;

import com.google.firebase.auth.FirebaseAuth;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.CommonDialogs;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.MyFirebase;
import ballidaku.mywallet.databinding.ActivityLoginBinding;
import ballidaku.mywallet.frontScreens.LoginActivity;

public class LoginActivityModel
{

    private Context context;
    private LoginActivityModelCallBack activityModelCallBack;
    private ActivityLoginBinding activityLoginBinding;
    private FirebaseAuth mAuth;

    public LoginActivityModel(Context context, ActivityLoginBinding activityLoginBinding, LoginActivityModelCallBack activityModelCallBack)
    {
        this.context=context;
        this.activityModelCallBack=activityModelCallBack;
        this.activityLoginBinding=activityLoginBinding;
        mAuth = FirebaseAuth.getInstance();
    }


    public void onSignInClicked()
    {
        checkValidation();
    }

    private void checkValidation()
    {

        final String email = activityLoginBinding.editTextEmail.getText().toString().trim();
        final String password = activityLoginBinding.editTextPassword.getText().toString().trim();

        if (email.isEmpty())
        {
            activityModelCallBack.showSnackbarMsg(context.getString(R.string.please_enter_email));
        }
        else if (!CommonMethods.getInstance().isValidEmail(email))
        {
            activityModelCallBack.showSnackbarMsg(context.getString(R.string.please_enter_valid_email));
        }
        else if (password.isEmpty())
        {
            activityModelCallBack.showSnackbarMsg(context.getString(R.string.please_enter_password));
        }
        else if (password.length() < 6)
        {
            activityModelCallBack.showSnackbarMsg(context.getString(R.string.password_limit));
        }
        else
        {
            CommonMethods.getInstance().hideKeypad((LoginActivity)context);
            CommonDialogs.getInstance().progressDialog(context);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task ->
            {
                if (task.isSuccessful())
                {
                    MyFirebase.getInstance().logInUser(context, email);
                }
                else
                {
                    CommonDialogs.getInstance().dialog.dismiss();
                    activityModelCallBack.showSnackbarMsg(task.getException().getMessage());
                }
            });
        }
    }

    public void onPasswordShowHideClicked()
    {
        boolean isVisible = (boolean) activityLoginBinding.imageViewPasswordShowHide.getTag();
        if (isVisible)
        {
            activityLoginBinding.editTextPassword.setInputType( InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
            activityLoginBinding.imageViewPasswordShowHide.setTag(false);
            activityLoginBinding.imageViewPasswordShowHide.setImageResource(R.drawable.ic_visibility_off);

        }
        else
        {
            activityLoginBinding.editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            activityLoginBinding.imageViewPasswordShowHide.setTag(true);
            activityLoginBinding.imageViewPasswordShowHide.setImageResource(R.drawable.ic_visibility_on);
        }
        activityLoginBinding.editTextPassword.setTextAppearance(context, R.style.EditTextTheme);
    }
    
    public void onForgotClicked()
    {
        CommonDialogs.getInstance().showForgotPasswordDialog(context, LoginActivityModel.this::forgotPassword);
    }


    private void forgotPassword(String email)
    {
        CommonDialogs.getInstance().progressDialog(context);
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task ->
                {
                    CommonDialogs.getInstance().dismissDialog();
                    if (task.isSuccessful())
                    {
                        CommonDialogs.getInstance().showMessageDialog(context, context.getString(R.string.email_reset));
                    }
                    else
                    {
                        CommonDialogs.getInstance().showMessageDialog(context, context.getString(R.string.retry_again));
                    }
                });
    }

    /***********************************************************************/
    // Interface
    /***********************************************************************/
    public interface LoginActivityModelCallBack
    {
        void showSnackbarMsg(String string);
    }
}
