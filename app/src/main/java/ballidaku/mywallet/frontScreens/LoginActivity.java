package ballidaku.mywallet.frontScreens;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.CommonDialogs;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.MyFirebase;
import ballidaku.mywallet.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity
{
    String TAG = LoginActivity.class.getSimpleName();

    Context context;

    FirebaseAuth mAuth;

    ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setUpView();
    }

    private void setUpView()
    {
        context = this;
        mAuth = FirebaseAuth.getInstance();

        activityLoginBinding.imageViewPasswordShowHide.setTag(false);
        MyClickHandlers handlers = new MyClickHandlers(this);
        activityLoginBinding.setHandlers(handlers);
    }

    public class MyClickHandlers
    {
        Context context;

        MyClickHandlers(Context context)
        {
            this.context = context;
        }

        public void onSignInClicked(View view)
        {
            check();
        }

        public void onSignUpClicked(View view)
        {
            startActivity(new Intent(context, SignUpActivity.class));
        }

        public void onForgotClicked(View view)
        {
            CommonDialogs.getInstance().showForgotPasswordDialog(context, LoginActivity.this::forgotPassword);
        }

        public void onPasswordShowHideClicked(View view)
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
    }

    private void check()
    {

        final String email = activityLoginBinding.editTextEmail.getText().toString().trim();
        final String password = activityLoginBinding.editTextPassword.getText().toString().trim();

        if (email.isEmpty())
        {
            CommonMethods.getInstance().showSnackbar(activityLoginBinding.getRoot(), context, getString(R.string.please_enter_email));
        }
        else if (!CommonMethods.getInstance().isValidEmail(email))
        {
            CommonMethods.getInstance().showSnackbar(activityLoginBinding.getRoot(), context, getString(R.string.please_enter_valid_email));
        }
        else if (password.isEmpty())
        {
            CommonMethods.getInstance().showSnackbar(activityLoginBinding.getRoot(), context, getString(R.string.please_enter_password));
        }
        else if (password.length() < 6)
        {
            CommonMethods.getInstance().showSnackbar(activityLoginBinding.getRoot(), context, getString(R.string.password_limit));
        }
        else
        {
            CommonMethods.getInstance().hideKeypad(this);
            CommonDialogs.getInstance().progressDialog(context);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task ->
            {
                if (task.isSuccessful())
                {
                    MyFirebase.getInstance().logInUser(context, email);
                }
                else
                {
                    CommonDialogs.getInstance().dialog.dismiss();
                    CommonMethods.getInstance().showSnackbar(activityLoginBinding.getRoot(), context, task.getException().getMessage());
                }
            });
        }
    }


    public void forgotPassword(String email)
    {

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task ->
                {
                    if (task.isSuccessful())
                    {
                        CommonDialogs.getInstance().showMessageDialog(context, getString(R.string.email_reset));
                    }
                    else
                    {
                        CommonDialogs.getInstance().showMessageDialog(context, getString(R.string.retry_again));
                    }
                });
    }

}
