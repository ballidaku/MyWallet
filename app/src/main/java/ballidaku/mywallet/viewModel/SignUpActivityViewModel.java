package ballidaku.mywallet.viewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.view.View;

import ballidaku.mywallet.databinding.ActivitySignUpBinding;
import ballidaku.mywallet.model.SignUpActivityModel;

public class SignUpActivityViewModel extends ViewModel implements SignUpActivityModel.SignUpActivityModelCallBack
{
    private SignUpActivityViewModelCallBack signUpActivityViewModelCallBack;
    SignUpActivityModel signUpActivityModel;

    SignUpActivityViewModel(Context context, ActivitySignUpBinding binding, SignUpActivityViewModelCallBack signUpActivityViewModelCallBack)
    {
        this.signUpActivityViewModelCallBack = signUpActivityViewModelCallBack;
        signUpActivityModel = new SignUpActivityModel(context, binding, this);
    }


    public void onSignUpClicked(View view)
    {
        signUpActivityModel.checkValidation();
    }

    public void onSignInClicked(View view)
    {
        signUpActivityViewModelCallBack.onSignInClicked();
    }


    /***********************************************************************/
    // Interface
    /***********************************************************************/
    public interface SignUpActivityViewModelCallBack
    {
        void onSignInClicked();
        void showSnackbarMsg(String string);

    }

    /***********************************************************************/
    // Interface Results from Model class
    /***********************************************************************/

    @Override
    public void showSnackbarMsg(String string)
    {
        signUpActivityViewModelCallBack.showSnackbarMsg(string);
    }
}
