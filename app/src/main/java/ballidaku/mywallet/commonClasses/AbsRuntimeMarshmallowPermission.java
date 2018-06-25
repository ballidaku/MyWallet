package ballidaku.mywallet.commonClasses;

/**
 * Created by sharanpalsingh on 23/07/17.
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.View;

/**
 * Created by brst-pc20 on 11/8/16.
 */

public abstract class AbsRuntimeMarshmallowPermission extends AppCompatActivity
{
    private SparseIntArray mErrorString;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mErrorString = new SparseIntArray();
        context = this;
    }

    public abstract void onPermissionGranted(int requestCode);


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults)
        {
            permissionCheck = permissionCheck + permission;
        }

        if (grantResults.length > 0 && PackageManager.PERMISSION_GRANTED == permissionCheck)
        {
            onPermissionGranted(requestCode);
        }
        else
        {
            Snackbar.make(findViewById(android.R.id.content), mErrorString.get(requestCode), Snackbar.LENGTH_INDEFINITE).setAction("ENABLE", new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent();
                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.setData(Uri.parse("package:" + getPackageName()));
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(i);
                }
            }).show();
        }
    }

    public void requestAppPermissions(final String[] requestedPermissions, final int stringId, final int requestCode)
    {
        mErrorString.put(requestCode, stringId);

        int permissioncheck = PackageManager.PERMISSION_GRANTED;

        boolean showRequestedPermissions = false;
        for (String permission : requestedPermissions)
        {
            permissioncheck = permissioncheck + ContextCompat.checkSelfPermission(this, permission);
            showRequestedPermissions = showRequestedPermissions || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);

        }
        if (permissioncheck != PackageManager.PERMISSION_GRANTED)
        {
            if (showRequestedPermissions)
            {
                Snackbar.make(findViewById(android.R.id.content), stringId, Snackbar.LENGTH_INDEFINITE).setAction("GRANT", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        ActivityCompat.requestPermissions(AbsRuntimeMarshmallowPermission.this, requestedPermissions, requestCode);
                    }
                }).show();
            }
            else
            {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        }
        else
        {
            onPermissionGranted(requestCode);
        }
    }


}