package ballidaku.mywallet.mainScreens.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.commonClasses.MySharedPreference;
import ballidaku.mywallet.databinding.FragmentSettingBinding;
import ballidaku.mywallet.frontScreens.LoginActivity;
import ballidaku.mywallet.mPin.activity.MPINActivity;
import ballidaku.mywallet.mainScreens.activities.MainActivity;
import ballidaku.mywallet.viewModel.SettingFragmentViewModel;
import ballidaku.mywallet.viewModel.ViewModelFactory;

public class SettingFragment extends Fragment implements SettingFragmentViewModel.SettingFragmentViewModelCallBack
{
     String TAG = SettingFragment.class.getSimpleName();

    View view = null;

    Context context;

    FragmentSettingBinding fragmentSettingBinding;
    //FirebaseAuth mAuth;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (view == null)
        {
            fragmentSettingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
            context = getActivity();
            SettingFragmentViewModel settingFragmentViewModel = ViewModelProviders.of(this,new ViewModelFactory<>(getActivity(), SettingFragment.this, fragmentSettingBinding,this)).get(SettingFragmentViewModel.class);
            fragmentSettingBinding.setViewModel(settingFragmentViewModel);

            view = fragmentSettingBinding.getRoot();

        }

        return view;
    }


    /***********************************************************************/
    // Interface Results from ViewModel class
    /***********************************************************************/


  /*  @Override
    public void showSnackbarMsg(String string)
    {
        CommonMethods.getInstance().showSnackbar(view, context, string);
    }*/

    @Override
    public void onChangePasscodeClicked()
    {
        Intent intent = new Intent(context, MPINActivity.class);
        intent.putExtra(MyConstant.MPIN, MyConstant.CHANGE_MPIN);
        startActivity(intent);
        ((MainActivity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    @Override
    public void onLogout()
    {
        MySharedPreference.getInstance().clearUserID(context);
        MySharedPreference.getInstance().clearMPIN(context);

        Intent intentSignOut = new Intent(context, LoginActivity.class);
        intentSignOut.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentSignOut);
        ((MainActivity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        ((MainActivity) context).finish();
    }
}
