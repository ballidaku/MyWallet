package ballidaku.mywallet.mainScreens.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.AbsRuntimeMarshmallowPermission;
import ballidaku.mywallet.commonClasses.CommonDialogs;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.databinding.ActivityMainBinding;
import ballidaku.mywallet.mainScreens.fragments.MainFragment;
import ballidaku.mywallet.mainScreens.fragments.SettingFragment;
import ballidaku.mywallet.viewModel.MainActivityViewModel;
import ballidaku.mywallet.viewModel.ViewModelFactory;

public class MainActivity extends AbsRuntimeMarshmallowPermission implements MainActivityViewModel.MainActivityViewModelCallBack
{
    String TAG = MainActivity.class.getSimpleName();
    Context context;

    ActivityMainBinding activityMainBinding;
    private Fragment fragment;
    ViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = this;


        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModelFactory = new ViewModelFactory<>(context, MainActivity.this, activityMainBinding, this);
        activityMainBinding.setViewModel(ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel.class));

        setUpViews();
    }

    private void setUpViews()
    {
        ((MainActivity) context).setSupportActionBar(activityMainBinding.include.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle((MainActivity) context, activityMainBinding.drawerLayout, activityMainBinding.include.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        activityMainBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }


    void onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean willStoreInStack = false;
        if (id == R.id.nav_home)
        {
            activityMainBinding.include.toolbar.setTitle(MyConstant.HOME);
            fragment = new MainFragment();
        }
        else if (id == R.id.nav_settings)
        {
            activityMainBinding.include.toolbar.setTitle(MyConstant.SETTINGS);
            fragment = new SettingFragment();
        }

        CommonMethods.getInstance().switchfragment(context, fragment, willStoreInStack);

        activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
    }


    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        if (getIntent() != null)
        {
            if (getIntent().getIntExtra(MyConstant.FROM_WHERE, 0) == MyConstant.EXIT_ID)
            {
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                assert manager != null;
                manager.cancel(MyConstant.NOTIFICATION_ID);
                finishAffinity();
                System.exit(0);
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        if (activityMainBinding.drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
            Log.e(TAG, "INDEX " + index);
            if (index == 0)
            {
                finish();
            }
            else
            {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == MyConstant.PICKFILE_REQUEST_CODE)
        {
            Uri selectedFileUri = data.getData();
            assert selectedFileUri != null;
            String type = CommonMethods.getInstance().getMimeType(context, selectedFileUri);
            if (type.equalsIgnoreCase("txt"))
            {
                CommonMethods.getInstance().readDataFromExternalFile(context, selectedFileUri);
            }
            else
            {
                CommonDialogs.getInstance().showMessageDialog(context, getString(R.string.not_valid_file));
            }
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == MyConstant.REQUEST_CODE_OPEN_DIRECTORY)
        {
            Uri uri = data.getData();
            if (uri != null)
            {
                CommonMethods.getInstance().getAllDatabaseData(context, MyConstant.EXPORT_TO_EXTERNAL_STORAGE, uri);
            }
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == MyConstant.Any_UPDATE_CODE) // To add bank or other details
        {
            if (fragment instanceof MainFragment)
            {
                ((MainFragment)fragment).refreshData();
            }
        }
    }

    @Override
    public void onPermissionGranted(int requestCode)
    {
        if (requestCode == MyConstant.READ_FILE_REQUEST)
        {
            CommonMethods.getInstance().getFileFromDevice(context);
        }
        else if (requestCode == MyConstant.WRITE_FILE_REQUEST)
        {
            CommonMethods.getInstance().shareFileToLocalStorage(context);
        }
    }


    /***********************************************************************/
    // Interface Results from ViewModel class

    /***********************************************************************/
    @Override
    public void onNavigationClicked(MenuItem item)
    {
        onNavigationItemSelected(item);
    }
}
