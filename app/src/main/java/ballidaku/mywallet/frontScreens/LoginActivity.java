package ballidaku.mywallet.frontScreens;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
        MyClickHandlers handlers = new MyClickHandlers(this);
        activityLoginBinding.setHandlers(handlers);
    }

    public class MyClickHandlers
    {
        Context context;
        MyClickHandlers(Context context)
        {
            this.context= context;
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

        }
    }


    private void check()
    {

        final String email = activityLoginBinding.editTextEmail.getText().toString().trim();
        final String password = activityLoginBinding.editTextPassword.getText().toString().trim();

        if (email.isEmpty())
        {
            CommonMethods.getInstance().show_snackbar(activityLoginBinding.getRoot(), context, getString(R.string.please_enter_email));
        }
        else if (!CommonMethods.getInstance().isValidEmail(email))
        {
            CommonMethods.getInstance().show_snackbar(activityLoginBinding.getRoot(), context, getString(R.string.please_enter_valid_email));
        }
        else if (password.isEmpty())
        {
            CommonMethods.getInstance().show_snackbar(activityLoginBinding.getRoot(), context, getString(R.string.please_enter_password));
        }
        else if (password.length() < 6)
        {
            CommonMethods.getInstance().show_snackbar(activityLoginBinding.getRoot(), context, getString(R.string.password_limit));
        }
        else
        {
            CommonMethods.getInstance().hideKeypad(this);
            CommonDialogs.getInstance().progressDialog(context);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {

                            if (task.isSuccessful())
                            {
                                Log.e(TAG, "signInWithEmail:success");
                                MyFirebase.getInstance().logInUser(context, email);
                            }
                            else
                            {
                                CommonDialogs.getInstance().dialog.dismiss();
                                // If sign in fails, display a message to the user.
                                Log.e(TAG, "createUserWithEmail:failure  ", task.getException());
                                CommonMethods.getInstance().show_snackbar(activityLoginBinding.getRoot(), context, task.getException().getMessage());
                            }
                        }
                    });
        }
    }


}
