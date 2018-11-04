package ballidaku.mywallet.viewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.MenuItem;

import ballidaku.mywallet.R;
import ballidaku.mywallet.databinding.ActivityMainBinding;
import ballidaku.mywallet.databinding.NavHeaderMainBinding;
import ballidaku.mywallet.mainScreens.activities.MainActivity;
import ballidaku.mywallet.model.MainActivityModel;

public class MainActivityViewModel extends ViewModel
{
    String TAG = MainActivityViewModel.class.getSimpleName();
    private MainActivityViewModelCallBack mainActivityViewModelCallBack;

    MainActivityViewModel(Context context, ActivityMainBinding activityMainBinding, MainActivityViewModelCallBack mainActivityViewModelCallBack)
    {
        this.mainActivityViewModelCallBack = mainActivityViewModelCallBack;

        MainActivityModel mainActivityModel = new MainActivityModel(context);

        NavHeaderMainBinding navHeaderMainBinding = DataBindingUtil.inflate(((MainActivity) context).getLayoutInflater(), R.layout.nav_header_main, activityMainBinding.navigationView, false);
        activityMainBinding.navigationView.addHeaderView(navHeaderMainBinding.getRoot());
        navHeaderMainBinding.setMainActivityModel(mainActivityModel);

        activityMainBinding.navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(activityMainBinding.navigationView.getMenu().getItem(0));
    }


    public boolean onNavigationItemSelected(MenuItem item)
    {
        mainActivityViewModelCallBack.onNavigationClicked(item);
        return true;
    }

    /***********************************************************************/
    // Interface

    /***********************************************************************/
    public interface MainActivityViewModelCallBack
    {
        void onNavigationClicked(MenuItem item);
    }


    /***********************************************************************/
    // Interface Results from Model class

    /***********************************************************************/

 /*   @Override
    public void onResult()
    {

    }*/
}
