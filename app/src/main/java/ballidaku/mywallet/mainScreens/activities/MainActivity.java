package ballidaku.mywallet.mainScreens.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.commonClasses.MySharedPreference;
import ballidaku.mywallet.mainScreens.fragments.MainFragment;
import ballidaku.mywallet.mainScreens.fragments.SettingFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    String TAG = MainActivity.class.getSimpleName();
    Context context;
    DrawerLayout drawer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        setUpViews();
    }

    public void setUpViews()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        TextView textViewName = headerLayout.findViewById(R.id.textViewName);
        TextView textViewEmail = headerLayout.findViewById(R.id.textViewEmail);

        textViewName.setText(MySharedPreference.getInstance().getUserName(context));
        textViewEmail.setText(MySharedPreference.getInstance().getUserEmail(context));

        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {
            toolbar.setTitle(MyConstant.HOME);

            CommonMethods.getInstance().switchfragment(context, new MainFragment());
        }
        else if (id == R.id.nav_settings)
        {
            toolbar.setTitle(MyConstant.SETTINGS);
            CommonMethods.getInstance().switchfragment(context, new SettingFragment());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
