package ballidaku.mywallet.viewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.view.View;

import ballidaku.mywallet.databinding.FragmentSettingBinding;
import ballidaku.mywallet.model.SettingFragmentModel;

public class SettingFragmentViewModel extends ViewModel implements SettingFragmentModel.SettingFragmentModelCallBack
{

    SettingFragmentModel settingFragmentModel;
    SettingFragmentViewModelCallBack settingFragmentViewModelCallBack;


    public SettingFragmentViewModel(Context context, FragmentSettingBinding binding, SettingFragmentViewModelCallBack settingFragmentViewModelCallBack)
    {
        settingFragmentModel = new SettingFragmentModel(context, this);
        this.settingFragmentViewModelCallBack=settingFragmentViewModelCallBack;

    }


    /***********************************************************************/
    // Click Listener

    /***********************************************************************/

    public void onChangeLoginPasswordClicked(View view)
    {
        settingFragmentModel.onChangeLoginPasswordClicked();
    }

    public void onChangePasscodeClicked(View view)
    {
        settingFragmentViewModelCallBack.onChangePasscodeClicked();
    }

    public void onExportDataToOtherClicked(View view)
    {
        settingFragmentModel.onExportDataToOtherClicked();
    }

    public void onExportDataToLocalStorageClicked(View view)
    {
        settingFragmentModel.onExportDataToLocalStorageClicked();
    }

    public void onImportDataClicked(View view)
    {
        settingFragmentModel.onImportDataClicked();
    }


    public void onSignOutClicked(View view)
    {
        settingFragmentModel.onSignOutClicked();
    }




    /***********************************************************************/
    // Interface

    /***********************************************************************/
    public interface SettingFragmentViewModelCallBack
    {
//        void showSnackbarMsg(String string);
        void onChangePasscodeClicked();
        void onLogout();
    }


    /***********************************************************************/
    // Interface Results from Model class

    /***********************************************************************/
 /*   @Override
    public void showSnackbarMsg(String string)
    {
        settingFragmentViewModelCallBack.showSnackbarMsg(string);
    }*/

    @Override
    public void onLogout()
    {
        settingFragmentViewModelCallBack.onLogout();
    }
}
