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

import com.google.firebase.auth.FirebaseAuth;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.CommonDialogs;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.commonClasses.MySharedPreference;
import ballidaku.mywallet.databinding.FragmentSettingBinding;
import ballidaku.mywallet.frontScreens.LoginActivity;
import ballidaku.mywallet.mPin.activity.MPINActivity;
import ballidaku.mywallet.mainScreens.activities.MainActivity;

public class SettingFragment extends Fragment
{

    View view = null;

    Context context;

    FragmentSettingBinding fragmentSettingBinding;
    FirebaseAuth mAuth;


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
        mAuth = FirebaseAuth.getInstance();

        MyClickHandlers handlers = new MyClickHandlers(context);
        fragmentSettingBinding.setHandlers(handlers);
    }

    public class MyClickHandlers
    {
        Context context;

        MyClickHandlers(Context context)
        {
            this.context = context;
        }

        public void onChangeLoginPasswordClicked(View view)
        {
            CommonDialogs.getInstance().progressDialog(context);
            mAuth.sendPasswordResetEmail(MySharedPreference.getInstance().getUserEmail(context))
                    .addOnCompleteListener(task ->
                    {
                        CommonDialogs.getInstance().dismissDialog();
                        if (task.isSuccessful())
                        {
                            CommonDialogs.getInstance().showMessageDialog(context, getString(R.string.email_reset));
                        }
                        else
                        {
                            CommonDialogs.getInstance().showMessageDialog(context, getString(R.string.retry_again));
                        }
                    });

        }

        public void onChangePasscodeClicked(View view)
        {
            Intent intent = new Intent(context, MPINActivity.class);
            intent.putExtra(MyConstant.MPIN, MyConstant.CHANGE_MPIN);
            startActivity(intent);
            ((MainActivity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }

        public void onExportDataToOtherClicked(View view)
        {
            CommonMethods.getInstance().getAllDatabaseData(context,MyConstant.EXPORT_TO_OTHER_APPS,null);
        }

        public void onExportDataToLocalStorageClicked(View view)
        {
            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ((MainActivity) context).requestAppPermissions(permission, R.string.permission, MyConstant.WRITE_FILE_REQUEST);
        }

        public void onImportDataClicked(View view)
        {
            CommonDialogs.getInstance().showImportAlertDialog(context, () ->
            {
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                ((MainActivity) context).requestAppPermissions(permission, R.string.permission, MyConstant.READ_FILE_REQUEST);
            });
        }


        public void onSignOutClicked(View view)
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
}
