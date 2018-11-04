package ballidaku.mywallet.model;

import android.Manifest;
import android.content.Context;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.CommonDialogs;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.commonClasses.MySharedPreference;
import ballidaku.mywallet.mainScreens.activities.MainActivity;

public class SettingFragmentModel
{
    Context context;
    SettingFragmentModelCallBack settingFragmentModelCallBack;
    FirebaseAuth mAuth;

    public SettingFragmentModel(Context context, SettingFragmentModelCallBack settingFragmentModelCallBack)
    {
        this.context = context;
        this.settingFragmentModelCallBack = settingFragmentModelCallBack;
        mAuth = FirebaseAuth.getInstance();

    }

    public void onChangeLoginPasswordClicked()
    {
        CommonDialogs.getInstance().showMessageDialog(context, MyConstant.CHANGE_PASSWORD, onPasswordChangeClickListener);
    }

    public void onExportDataToOtherClicked()
    {
        CommonMethods.getInstance().getAllDatabaseData(context, MyConstant.EXPORT_TO_OTHER_APPS, null);
    }

    public void onExportDataToLocalStorageClicked()
    {
        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ((MainActivity) context).requestAppPermissions(permission, R.string.permission, MyConstant.WRITE_FILE_REQUEST);
    }

    public void onImportDataClicked()
    {
        CommonDialogs.getInstance().showImportAlertDialog(context, () ->
        {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ((MainActivity) context).requestAppPermissions(permission, R.string.permission, MyConstant.READ_FILE_REQUEST);
        });
    }

    public void onSignOutClicked()
    {
        CommonDialogs.getInstance().showMessageDialog(context, MyConstant.LOGOUT, onLogoutClickListener);
    }

    private View.OnClickListener onLogoutClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            settingFragmentModelCallBack.onLogout();
        }
    };

    private View.OnClickListener onPasswordChangeClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            CommonDialogs.getInstance().progressDialog(context);
            mAuth.sendPasswordResetEmail(MySharedPreference.getInstance().getUserEmail(context))
                    .addOnCompleteListener(task ->
                    {
                        CommonDialogs.getInstance().dismissDialog();
                        if (task.isSuccessful())
                        {
                            CommonDialogs.getInstance().showMessageDialog(context, context.getString(R.string.email_reset));
                        }
                        else
                        {
                            CommonDialogs.getInstance().showMessageDialog(context, context.getString(R.string.retry_again));
                        }
                    });
        }
    };

    /***********************************************************************/
    // Interface
    /***********************************************************************/
    public interface SettingFragmentModelCallBack
    {
//        void showSnackbarMsg(String string);

        void onLogout();
    }
}
