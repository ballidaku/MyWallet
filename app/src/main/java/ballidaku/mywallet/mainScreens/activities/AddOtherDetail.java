package ballidaku.mywallet.mainScreens.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.databinding.ActivityAddOtherDetailBinding;
import ballidaku.mywallet.roomDatabase.ExecuteQueryAsyncTask;
import ballidaku.mywallet.roomDatabase.OnResultInterface;
import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;
import ballidaku.mywallet.roomDatabase.dataModel.OtherDetailsDataModel;

public class AddOtherDetail<D> extends AppCompatActivity implements View.OnClickListener
{
    String TAG = AddOtherDetail.class.getSimpleName();

    ActivityAddOtherDetailBinding activityAddOtherDetailBinding;

    String[] typeArray;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activityAddOtherDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_other_detail);

        context = this;

        setUpViews();

        setListener();

    }

    private void setUpViews()
    {
        typeArray = getResources().getStringArray(R.array.type);
        setSupportActionBar(activityAddOtherDetailBinding.toolbarOther);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        addMoreFeilds();
    }

    private void setListener()
    {
        activityAddOtherDetailBinding.cardViewAddMoreFields.setOnClickListener(this);
    }

    private void addMoreFeilds()
    {
        final View child = getLayoutInflater().inflate(R.layout.custom_add_more_feilds, null);
        MaterialSpinner spinnerType = child.findViewById(R.id.spinnerType);
        spinnerType.setItems(typeArray);

        ImageView imageViewCancel = child.findViewById(R.id.imageViewCancel);
        imageViewCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                activityAddOtherDetailBinding.lineraLayoutAddView.removeView(child);

            }
        });

        activityAddOtherDetailBinding.lineraLayoutAddView.addView(child);
    }

    private void saveData()
    {
        String headingName = activityAddOtherDetailBinding.editTextValue.getText().toString().trim();

        if (headingName.isEmpty())
        {
            CommonMethods.getInstance().show_Toast(context, "Header should not be empty");
            return;
        }

        int count = activityAddOtherDetailBinding.lineraLayoutAddView.getChildCount();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < count; i++)
        {
            View view = activityAddOtherDetailBinding.lineraLayoutAddView.getChildAt(i);

            EditText editTextTitle = view.findViewById(R.id.editTextTitle);
            EditText editTextValue = view.findViewById(R.id.editTextValue);

            MaterialSpinner spinnerType = view.findViewById(R.id.spinnerType);

            String title = editTextTitle.getText().toString().trim();
            String value = editTextValue.getText().toString().trim();
            String type = typeArray[spinnerType.getSelectedIndex()];

            if (title.isEmpty() || value.isEmpty())
            {
                CommonMethods.getInstance().show_Toast(context, "Title or Value data should not be empty");
                return;
            }

            JSONObject jsonObject = new JSONObject();
            try
            {
                jsonObject.put(MyConstant.TITLE, title);
                jsonObject.put(MyConstant.VALUE, value);
                jsonObject.put(MyConstant.TYPE, type);

                jsonArray.put(jsonObject);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            Log.e(TAG, "DATA  " + jsonArray);

        }

        OtherDetailsDataModel otherDetailsDataModel = new OtherDetailsDataModel();
        otherDetailsDataModel.setHeading(headingName);
        otherDetailsDataModel.setData(jsonArray.toString());

        /*When we insert the data*/
        new ExecuteQueryAsyncTask<>(context, otherDetailsDataModel, MyConstant.INSERT, new OnResultInterface<D>()
        {
            @Override
            public void OnCompleted(D data)
            {
                Log.e(TAG, (String) data + "--");

                CommonMethods.getInstance().show_Toast(context, context.getResources().getString(R.string.saved_success));
                CommonMethods.getInstance().hideKeypad(AddOtherDetail.this);
                finish();

            }
        });
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.cardViewAddMoreFields:

                addMoreFeilds();

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.add_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:

                finish();

                break;

            case R.id.action_save:

                saveData();

                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
