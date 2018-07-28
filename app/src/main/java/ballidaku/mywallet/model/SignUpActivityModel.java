package ballidaku.mywallet.model;

import android.content.Context;
import android.util.Log;

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
import ballidaku.mywallet.frontScreens.SignUpActivity;

public class SignUpActivityModel
{
    String TAG = SignUpActivityModel.class.getSimpleName();
    private Context context;
    private ActivitySignUpBinding activitySignUpBinding;
    private SignUpActivityModelCallBack signUpActivityModelCallBack;
    FirebaseAuth mAuth;

    public SignUpActivityModel(Context context, ActivitySignUpBinding activitySignUpBinding, SignUpActivityModelCallBack signUpActivityModelCallBack)
    {
        this.activitySignUpBinding = activitySignUpBinding;
        this.signUpActivityModelCallBack = signUpActivityModelCallBack;
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        MySharedPreference.getInstance().saveToken(context, FirebaseInstanceId.getInstance().getToken());
    }

    public void checkValidation()
    {

        final String name = activitySignUpBinding.editTextName.getText().toString().trim();
        final String email = activitySignUpBinding.editTextEmail.getText().toString().trim();
        final String password = activitySignUpBinding.editTextPassword.getText().toString().trim();
        String confirmPassword = activitySignUpBinding.editTextConfirmPassword.getText().toString().trim();
        final String phoneNumber = activitySignUpBinding.editTextPhone.getText().toString().trim();

        if (name.isEmpty())
        {
            signUpActivityModelCallBack.showSnackbarMsg(context.getString(R.string.please_enter_name));
        }
        else if (email.isEmpty())
        {
            signUpActivityModelCallBack.showSnackbarMsg(context.getString(R.string.please_enter_email));
        }
        else if (!CommonMethods.getInstance().isValidEmail(email))
        {
            signUpActivityModelCallBack.showSnackbarMsg(context.getString(R.string.please_enter_valid_email));
        }
        else if (phoneNumber.isEmpty())
        {
            signUpActivityModelCallBack.showSnackbarMsg(context.getString(R.string.please_enter_phone_number));
        }
        else if (!CommonMethods.getInstance().isValidMobile(phoneNumber))
        {
            signUpActivityModelCallBack.showSnackbarMsg(context.getString(R.string.number_limit));
        }
        else if (password.isEmpty())
        {
            signUpActivityModelCallBack.showSnackbarMsg(context.getString(R.string.please_enter_password));
        }
        else if (password.length() < 6)
        {
            signUpActivityModelCallBack.showSnackbarMsg(context.getString(R.string.password_limit));
        }
        else if (confirmPassword.isEmpty())
        {
            signUpActivityModelCallBack.showSnackbarMsg(context.getString(R.string.please_enter_confirm_password));
        }
        else if (confirmPassword.length() < 6)
        {
            signUpActivityModelCallBack.showSnackbarMsg(context.getString(R.string.confirm_password_limit));
        }
        else if (!password.equals(confirmPassword))
        {
            signUpActivityModelCallBack.showSnackbarMsg(context.getString(R.string.password_confirm_not_matched));
        }
        else
        {
            CommonMethods.getInstance().hideKeypad((SignUpActivity)context);
            CommonDialogs.getInstance().progressDialog(context);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task ->
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
                    signUpActivityModelCallBack.showSnackbarMsg(task.getException().getMessage());
                }

            });
        }
    }

    /***********************************************************************/
    // Interface

    /***********************************************************************/
    public interface SignUpActivityModelCallBack
    {
        void showSnackbarMsg(String string);
    }
}
