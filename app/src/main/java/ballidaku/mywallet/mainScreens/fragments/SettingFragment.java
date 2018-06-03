package ballidaku.mywallet.mainScreens.fragments;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.commonClasses.MySharedPreference;
import ballidaku.mywallet.databinding.FragmentSettingBinding;
import ballidaku.mywallet.frontScreens.LoginActivity;
import ballidaku.mywallet.mPin.activity.MPINActivity;



public class SettingFragment extends Fragment implements View.OnClickListener
{

    View view = null;

    Context context;

    FragmentSettingBinding fragmentSettingBinding;


    public SettingFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (view == null)
        {

            fragmentSettingBinding = DataBindingUtil.inflate(
                    inflater, R.layout.fragment_setting, container, false);
            view = fragmentSettingBinding.getRoot();

            context = getActivity();
            setUpIds();

        }

        return view;
    }


    public void setUpIds()
    {
        fragmentSettingBinding.cardViewChangeMPIN.setOnClickListener(this);
        fragmentSettingBinding.cardViewSignOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.cardViewChangeMPIN:

                Intent intent = new Intent(context, MPINActivity.class);
                intent.putExtra(MyConstant.MPIN, MyConstant.CHANGE_MPIN);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                break;


            case R.id.cardViewSignOut:

                MySharedPreference.getInstance().clearUserID(context);
                MySharedPreference.getInstance().clearMPIN(context);

                Intent intentSignOut = new Intent(context, LoginActivity.class);
                intentSignOut.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentSignOut);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                getActivity().finish();

                break;
        }
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
    }
}
