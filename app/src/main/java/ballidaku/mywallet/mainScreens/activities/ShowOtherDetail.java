package ballidaku.mywallet.mainScreens.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.CommonDialogs;
import ballidaku.mywallet.commonClasses.CommonInterfaces;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.databinding.ActivityShowOtherDetailBinding;
import ballidaku.mywallet.roomDatabase.ExecuteQueryAsyncTask;
import ballidaku.mywallet.roomDatabase.OnResultInterface;
import ballidaku.mywallet.roomDatabase.dataModel.OtherDetailsDataModel;

public class ShowOtherDetail<D> extends AppCompatActivity
{
    Context context;
    String TAG = ShowOtherDetail.class.getSimpleName();

    OtherDetailsDataModel otherDetailsDataModel;
    ActivityShowOtherDetailBinding activityShowOtherDetailsBinding;

    ArrayList<EditText> editTextList=new ArrayList<>();

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activityShowOtherDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_show_other_detail);
        context = this;
        setUpViews();

       id = getIntent().getIntExtra(MyConstant.LIST_ITEM_ID, 0);
//       String type = getIntent().getStringExtra(MyConstant.TYPE);

        getDataFromDatabase();

    }

    private void setUpViews()
    {
        setSupportActionBar(activityShowOtherDetailsBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    private void getDataFromDatabase()
    {

        final OtherDetailsDataModel data = new OtherDetailsDataModel();
        data.setId(id);

        new ExecuteQueryAsyncTask<>(context, data, MyConstant.GET_ONE_ITEM, new OnResultInterface<D>()
        {
            @Override
            public void OnCompleted(D data)
            {
                otherDetailsDataModel = (OtherDetailsDataModel) data;
                refreshData();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void refreshData()
    {
        activityShowOtherDetailsBinding.editTextHeadingValue.setText(otherDetailsDataModel.getHeading());

        if (otherDetailsDataModel.getData() != null && !otherDetailsDataModel.getData().isEmpty())
        {
            String json = otherDetailsDataModel.getData();
            activityShowOtherDetailsBinding.linearLayoutAddViews.removeAllViews();

            try
            {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString(MyConstant.TITLE);
                    String value = jsonObject.getString(MyConstant.VALUE);
                    String type = jsonObject.getString(MyConstant.TYPE);

                    final View view = getLayoutInflater().inflate(R.layout.inflater_view_more_feilds, null);

                    EditText editTextTitle = view.findViewById(R.id.editTextTitle);
                    final EditText editTextValue = view.findViewById(R.id.editTextValue);
                    ImageView imageViewShow = view.findViewById(R.id.imageViewShow);


                    if (type.equals(MyConstant.TEXT))
                    {
                        imageViewShow.setVisibility(View.GONE);
                        editTextValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    }
                    else
                    {
                        editTextValue.setTag(MyConstant.INVISIBLE);
                        editTextValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editTextValue.setMaxLines(1);

                    }

                    imageViewShow.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            String tag = (String) editTextValue.getTag();
                            if (tag.equals(MyConstant.INVISIBLE))
                            {
                                editTextValue.setTag(MyConstant.VISIBLE);
                                editTextValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            }
                            else
                            {
                                editTextValue.setTag(MyConstant.INVISIBLE);
                                editTextValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            }
                        }
                    });

                    editTextTitle.setKeyListener(null);
                    editTextValue.setKeyListener(null);

                    editTextTitle.setText(title);
                    editTextValue.setText(value);

                    editTextValue.setTag(title);
                    editTextList.add(editTextValue);

                    editTextValue.setOnTouchListener(CommonMethods.getInstance().new MyTouchListener(context, editTextValue));

                    activityShowOtherDetailsBinding.linearLayoutAddViews.addView(view);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    // Decrypt Data
    public String dTD(String data)
    {
        return CommonMethods.getInstance().decrypt(context, data);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.show_bank_details, menu);
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

            case R.id.action_edit:

                Intent intent = new Intent(context, AddOtherDetail.class);
                intent.putExtra(MyConstant.LIST_ITEM_DATA,otherDetailsDataModel);
                intent.putExtra(MyConstant.FROM_WHERE, MyConstant.EDIT);
                startActivityForResult(intent, MyConstant.UPDATE_OTHER_DETAILS_RESPONSE);

                break;

            case R.id.action_copy:

                CommonMethods.getInstance().copyContent(context, CommonMethods.getInstance().getData(context,editTextList));

                break;

            case R.id.action_share:

                CommonMethods.getInstance().shareContent(context, CommonMethods.getInstance().getData(context,editTextList));

                break;

            case R.id.action_delete:

                CommonDialogs.getInstance().showDeleteAlertDialog(context, new CommonInterfaces.deleteDetail()
                {
                    @Override
                    public void onDelete()
                    {
                        deleteData();
                    }
                });

                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteData()
    {
        new ExecuteQueryAsyncTask<>(context, otherDetailsDataModel, MyConstant.DELETE, new OnResultInterface<D>()
        {
            @Override
            public void OnCompleted(D data)
            {
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == MyConstant.UPDATE_OTHER_DETAILS_RESPONSE)
        {
            getDataFromDatabase();
        }
    }

}
