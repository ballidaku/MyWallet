package ballidaku.mywallet.frontScreens;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.databinding.ActivitySignUpBinding;
import ballidaku.mywallet.viewModel.SignUpActivityViewModel;
import ballidaku.mywallet.viewModel.ViewModelFactory;

public class SignUpActivity extends AppCompatActivity implements SignUpActivityViewModel.SignUpActivityViewModelCallBack
{

    Context context;


    ActivitySignUpBinding activitySignUpBinding;
    ViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        context = this;

        viewModelFactory = new ViewModelFactory<>(context, SignUpActivity.this, activitySignUpBinding, this);
        activitySignUpBinding.setViewModel(ViewModelProviders.of(this, viewModelFactory).get(SignUpActivityViewModel.class));

    }

    @Override
    public void onSignInClicked()
    {
        finish();
    }

    @Override
    public void showSnackbarMsg(String string)
    {
        CommonMethods.getInstance().showSnackbar(activitySignUpBinding.getRoot(), context, string);
    }


}