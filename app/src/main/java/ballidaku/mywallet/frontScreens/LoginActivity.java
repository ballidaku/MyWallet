package ballidaku.mywallet.frontScreens;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.databinding.ActivityLoginBinding;
import ballidaku.mywallet.viewModel.LoginActivityViewModel;
import ballidaku.mywallet.viewModel.ViewModelFactory;

public class LoginActivity extends AppCompatActivity implements LoginActivityViewModel.LoginActivityViewModelCallBack
{
    String TAG = LoginActivity.class.getSimpleName();

    Context context;
    ViewModelFactory viewModelFactory;

    ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = this;
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        viewModelFactory = new ViewModelFactory<>(context, LoginActivity.this, activityLoginBinding, this);
        activityLoginBinding.setViewModel(ViewModelProviders.of(this, viewModelFactory).get(LoginActivityViewModel.class));

    }

    /***********************************************************************/
    // Interface Results from ViewModel class
    /***********************************************************************/

    @Override
    public void onSignUpClicked()
    {
        startActivity(new Intent(context, SignUpActivity.class));
    }


    @Override
    public void showSnackbarMsg(String string)
    {
        CommonMethods.getInstance().showSnackbar(activityLoginBinding.getRoot(), context, string);
    }


}
