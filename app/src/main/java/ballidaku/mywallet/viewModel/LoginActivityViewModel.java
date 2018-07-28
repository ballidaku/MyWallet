package ballidaku.mywallet.viewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.view.View;

import ballidaku.mywallet.databinding.ActivityLoginBinding;
import ballidaku.mywallet.model.LoginActivityModel;

public class LoginActivityViewModel extends ViewModel implements LoginActivityModel.LoginActivityModelCallBack
{


    private LoginActivityViewModelCallBack activityViewModelCallBack;
    private LoginActivityModel loginActivityModel;

    LoginActivityViewModel(Context context, ActivityLoginBinding binding, LoginActivityViewModelCallBack activityViewModelCallBack)
    {
        this.activityViewModelCallBack=activityViewModelCallBack;
        loginActivityModel=new LoginActivityModel(context,binding,this);
    }

    public void onSignInClicked(View view)
    {
        loginActivityModel.onSignInClicked();
    }

    public void onSignUpClicked(View view)
    {
        activityViewModelCallBack.onSignUpClicked();
    }

    public void onForgotClicked(View view)
    {
        loginActivityModel.onForgotClicked();
    }

    public void onPasswordShowHideClicked(View view)
    {
        loginActivityModel.onPasswordShowHideClicked();
    }



    /***********************************************************************/
    // Interface
    /***********************************************************************/
    public interface LoginActivityViewModelCallBack
    {
        void onSignUpClicked();

        void showSnackbarMsg(String string);
    }


    /***********************************************************************/
    // Interface Results from Model class
    /***********************************************************************/

    @Override
    public void showSnackbarMsg(String string)
    {
        activityViewModelCallBack.showSnackbarMsg(string);
    }
}
