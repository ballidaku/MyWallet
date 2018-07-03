package ballidaku.mywallet.frontScreens;

import android.content.Context;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.CommonDialogs;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.commonClasses.MyFirebase;
import ballidaku.mywallet.commonClasses.MySharedPreference;
import ballidaku.mywallet.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity
{
    String TAG = SignUpActivity.class.getSimpleName();
    Context context;
    FirebaseAuth mAuth;

    ActivitySignUpBinding activitySignUpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        setUpView();
    }

    private void setUpView()
    {
        context = this;
        mAuth = FirebaseAuth.getInstance();
        MySharedPreference.getInstance().saveToken(context, FirebaseInstanceId.getInstance().getToken());
        MyClickHandlers handlers = new MyClickHandlers(this);
        activitySignUpBinding.setHandlers(handlers);

    }

    public class MyClickHandlers
    {
        Context context;

        MyClickHandlers(Context context)
        {
            this.context = context;
        }

        public void onSignUpClicked(View view)
        {
            checkValidation();
        }

        public void onSignInClicked(View view)
        {
            finish();
        }
    }

    private void checkValidation()
    {

        final String name = activitySignUpBinding.editTextName.getText().toString().trim();
        final String email = activitySignUpBinding.editTextEmail.getText().toString().trim();
        final String password = activitySignUpBinding.editTextPassword.getText().toString().trim();
        String confirmPassword = activitySignUpBinding.editTextConfirmPassword.getText().toString().trim();
        final String phoneNumber = activitySignUpBinding.editTextPhone.getText().toString().trim();

        if (name.isEmpty())
        {
            CommonMethods.getInstance().showSnackbar(activitySignUpBinding.getRoot(), context, getString(R.string.please_enter_name));
        }
        else if (email.isEmpty())
        {
            CommonMethods.getInstance().showSnackbar(activitySignUpBinding.getRoot(), context, getString(R.string.please_enter_email));
        }
        else if (!CommonMethods.getInstance().isValidEmail(email))
        {
            CommonMethods.getInstance().showSnackbar(activitySignUpBinding.getRoot(), context, getString(R.string.please_enter_valid_email));
        }
        else if (phoneNumber.isEmpty())
        {
            CommonMethods.getInstance().showSnackbar(activitySignUpBinding.getRoot(), context, getString(R.string.please_enter_phone_number));
        }
        else if (!CommonMethods.getInstance().isValidMobile(phoneNumber))
        {
            CommonMethods.getInstance().showSnackbar(activitySignUpBinding.getRoot(), context, getString(R.string.number_limit));
        }
        else if (password.isEmpty())
        {
            CommonMethods.getInstance().showSnackbar(activitySignUpBinding.getRoot(), context, getString(R.string.please_enter_password));
        }
        else if (password.length() < 6)
        {
            CommonMethods.getInstance().showSnackbar(activitySignUpBinding.getRoot(), context, getString(R.string.password_limit));
        }
        else if (confirmPassword.isEmpty())
        {
            CommonMethods.getInstance().showSnackbar(activitySignUpBinding.getRoot(), context, getString(R.string.please_enter_confirm_password));
        }
        else if (confirmPassword.length() < 6)
        {
            CommonMethods.getInstance().showSnackbar(activitySignUpBinding.getRoot(), context, getString(R.string.confirm_password_limit));
        }
        else if (!password.equals(confirmPassword))
        {
            CommonMethods.getInstance().showSnackbar(activitySignUpBinding.getRoot(), context, getString(R.string.password_confirm_not_matched));
        }
        else
        {
            CommonMethods.getInstance().hideKeypad(this);
            CommonDialogs.getInstance().progressDialog(context);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e(TAG, "createUserWithEmail:success");
                        // FirebaseUser user = mAuth.getCurrentUser();
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put(MyConstant.USER_NAME, name);
                        hashMap.put(MyConstant.USER_EMAIL, email);
                        hashMap.put(MyConstant.USER_PHONE, phoneNumber);

                        MyFirebase.getInstance().createUser(context, hashMap);

                    }
                    else
                    {
                        CommonDialogs.getInstance().dialog.dismiss();

                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure  ", task.getException());

                        CommonMethods.getInstance().showSnackbar(activitySignUpBinding.getRoot(), context, task.getException().getMessage());
                    }

                }
            });
        }
    }
}