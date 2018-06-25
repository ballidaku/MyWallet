package ballidaku.mywallet.mainScreens.fragments;

import android.Manifest;
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
import ballidaku.mywallet.commonClasses.CommonDialogs;
import ballidaku.mywallet.commonClasses.CommonInterfaces;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.commonClasses.MySharedPreference;
import ballidaku.mywallet.databinding.FragmentSettingBinding;
import ballidaku.mywallet.frontScreens.LoginActivity;
import ballidaku.mywallet.mPin.activity.MPINActivity;
import ballidaku.mywallet.mainScreens.activities.MainActivity;

public class SettingFragment extends Fragment implements View.OnClickListener
{

    View view = null;

    Context context;

    FragmentSettingBinding fragmentSettingBinding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (view == null)
        {
            fragmentSettingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
            view = fragmentSettingBinding.getRoot();
            context = getActivity();
            setUpViews();
        }

        return view;
    }

    public void setUpViews()
    {
        fragmentSettingBinding.cardViewChangeMPIN.setOnClickListener(this);
        fragmentSettingBinding.cardViewExportData.setOnClickListener(this);
        fragmentSettingBinding.cardViewImportData.setOnClickListener(this);
        fragmentSettingBinding.cardViewSignOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.cardViewChangeMPIN:

                Intent intent = new Intent(context, MPINActivity.class);
                intent.putExtra(MyConstant.MPIN, MyConstant.CHANGE_MPIN);
                startActivity(intent);
                ((MainActivity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                break;


            case R.id.cardViewExportData:
                CommonMethods.getInstance().getAllDatabaseData(context);
                break;


            case R.id.cardViewImportData:
                CommonDialogs.getInstance().showImportAlertDialog(context, new CommonInterfaces.importData()
                {
                    @Override
                    public void onImportConfirmation()
                    {
                        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        ((MainActivity) context).requestAppPermissions(permission, R.string.permission, MyConstant.READ_FILE_REQUEST);
                    }
                });
                break;

            case R.id.cardViewSignOut:

                MySharedPreference.getInstance().clearUserID(context);
                MySharedPreference.getInstance().clearMPIN(context);

                Intent intentSignOut = new Intent(context, LoginActivity.class);
                intentSignOut.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentSignOut);
                ((MainActivity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                ((MainActivity) context).finish();

                break;
        }
    }
}
