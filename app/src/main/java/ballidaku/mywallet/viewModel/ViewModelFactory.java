package ballidaku.mywallet.viewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import ballidaku.mywallet.databinding.ActivityLoginBinding;
import ballidaku.mywallet.databinding.ActivityMainBinding;
import ballidaku.mywallet.databinding.ActivitySignUpBinding;
import ballidaku.mywallet.databinding.FragmentMainBinding;
import ballidaku.mywallet.databinding.FragmentSettingBinding;
import ballidaku.mywallet.frontScreens.LoginActivity;
import ballidaku.mywallet.frontScreens.SignUpActivity;
import ballidaku.mywallet.mainScreens.activities.AddBankDetails;
import ballidaku.mywallet.mainScreens.activities.MainActivity;
import ballidaku.mywallet.mainScreens.fragments.MainFragment;
import ballidaku.mywallet.mainScreens.fragments.SettingFragment;

public class ViewModelFactory<B, A, I> extends ViewModelProvider.NewInstanceFactory
{
    private Context context;
    private A activityOrFragemnt;
    private B binding;
    private I myInterface;

    public ViewModelFactory(Context context, A activityOrFragemnt, B binding, I myInterface)
    {
        this.context = context;
        this.activityOrFragemnt = activityOrFragemnt;
        this.binding = binding;
        this.myInterface = myInterface;
    }

    public ViewModelFactory(Context context, A activityOrFragemnt, I myInterface)
    {
        this.context = context;
        this.activityOrFragemnt = activityOrFragemnt;
        this.myInterface = myInterface;
    }

    @NonNull
    @Override
    @SuppressWarnings({"unchecked"})
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
    {
        if (activityOrFragemnt instanceof MainActivity)
        {
            return (T) new MainActivityViewModel(context, (ActivityMainBinding) binding, (MainActivityViewModel.MainActivityViewModelCallBack) myInterface);
        }
        else if (activityOrFragemnt instanceof LoginActivity)
        {
            return (T) new LoginActivityViewModel(context, (ActivityLoginBinding) binding, (LoginActivityViewModel.LoginActivityViewModelCallBack) myInterface);
        }
        else if (activityOrFragemnt instanceof SignUpActivity)
        {
            return (T) new SignUpActivityViewModel(context, (ActivitySignUpBinding) binding, (SignUpActivityViewModel.SignUpActivityViewModelCallBack) myInterface);
        }
        else if (activityOrFragemnt instanceof MainFragment)
        {
            return (T) new MainFragmentViewModel(context, (FragmentMainBinding) binding, (MainFragmentViewModel.MainFragmentViewModelCallBack) myInterface);
        }
        else if (activityOrFragemnt instanceof SettingFragment)
        {
            return (T) new SettingFragmentViewModel(context, (FragmentSettingBinding) binding, (SettingFragmentViewModel.SettingFragmentViewModelCallBack) myInterface);
        }
        else if (activityOrFragemnt instanceof AddBankDetails)
        {
            return (T) new AddBankDetailsViewModel(context, (AddBankDetailsViewModel.AddBankDetailsViewModelCallBack) myInterface);
        }
        else
        {
            return super.create(modelClass);
        }
    }


}
