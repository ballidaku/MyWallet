package ballidaku.mywallet.mainScreens.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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
import ballidaku.mywallet.roomDatabase.dataModel.OtherDetailsDataModel;

public class AddOtherDetail<D> extends AppCompatActivity implements View.OnClickListener
{
    String TAG = AddOtherDetail.class.getSimpleName();

    ActivityAddOtherDetailBinding activityAddOtherDetailBinding;

    String[] typeArray;

    Context context;

    String fromWhere;

    OtherDetailsDataModel otherDetailsDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityAddOtherDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_other_detail);

        context = this;

        setUpViews();
    }

    private void setUpViews()
    {
        activityAddOtherDetailBinding.cardViewAddMoreFields.setOnClickListener(this);

        typeArray = getResources().getStringArray(R.array.type);
        setSupportActionBar(activityAddOtherDetailBinding.toolbarOther);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fromWhere = getIntent().getStringExtra(MyConstant.FROM_WHERE);

        if (fromWhere.equals(MyConstant.EDIT))
        {
            activityAddOtherDetailBinding.toolbarOther.setTitle("UPDATE OTHER DETAILS");
            otherDetailsDataModel = (OtherDetailsDataModel) getIntent().getSerializableExtra(MyConstant.LIST_ITEM_DATA);

            setData();
        }
        else if (fromWhere.equals(MyConstant.NEW))
        {
            activityAddOtherDetailBinding.toolbarOther.setTitle("ADD OTHER DETAILS");
            addMoreFeilds();
        }
    }


    private void setData()
    {
        activityAddOtherDetailBinding.editTextValue.setText(otherDetailsDataModel.getHeading());
        if (otherDetailsDataModel.getData() != null && !otherDetailsDataModel.getData().isEmpty())
        {
            String json = otherDetailsDataModel.getData();
            try
            {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString(MyConstant.TITLE);
                    String value = jsonObject.getString(MyConstant.VALUE);
                    String type = jsonObject.getString(MyConstant.TYPE);

                    final View view = getLayoutInflater().inflate(R.layout.custom_add_more_feilds, null);

                    EditText editTextTitle = view.findViewById(R.id.editTextTitle);
                    final EditText editTextValue = view.findViewById(R.id.editTextValue);
                    ImageView imageViewCancel = view.findViewById(R.id.imageViewCancel);

                    if (type.equalsIgnoreCase(MyConstant.TEXT))
                    {
                        editTextValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        editTextValue.setMaxLines(6);
                    }
                    else
                    {
                        editTextValue.setInputType(InputType.TYPE_CLASS_TEXT);
                        editTextValue.setMinLines(1);
                        editTextValue.setMaxLines(1);
                    }

                    MaterialSpinner spinnerType = view.findViewById(R.id.spinnerType);
                    spinnerType.setItems(typeArray);
                    spinnerType.setSelectedIndex(type.equals(typeArray[0]) ? 0 : 1);

                    editTextTitle.setText(title);
                    editTextValue.setText(value);

                    spinnerType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(MaterialSpinner view, int position, long id, Object item)
                        {
                            if (position == 0)//Text
                            {
                                editTextValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                editTextValue.setMaxLines(6);
                            }
                            else
                            {
                                editTextValue.setInputType(InputType.TYPE_CLASS_TEXT);
                                editTextValue.getText().clear();
                                editTextValue.setMinLines(1);
                                editTextValue.setMaxLines(1);
                            }
                        }
                    });

                    imageViewCancel.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View views)
                        {
                            activityAddOtherDetailBinding.lineraLayoutAddView.removeView(view);
                        }
                    });

                    activityAddOtherDetailBinding.lineraLayoutAddView.addView(view);
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void addMoreFeilds()
    {
        final View child = getLayoutInflater().inflate(R.layout.custom_add_more_feilds, null);
        MaterialSpinner spinnerType = child.findViewById(R.id.spinnerType);
        spinnerType.setItems(typeArray);

        final EditText editTextValue = child.findViewById(R.id.editTextValue);
        editTextValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editTextValue.setMaxLines(6);

        spinnerType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item)
            {
                if(position==0)//Text
                {
                    editTextValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    editTextValue.setMaxLines(6);
                }
                else
                {
                    editTextValue.setInputType(InputType.TYPE_CLASS_TEXT);
                    editTextValue.getText().clear();
                    editTextValue.setMinLines(1);
                    editTextValue.setMaxLines(1);
                }
            }
        });


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

            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            Log.e(TAG, "DATA  " + jsonArray);

        }

        OtherDetailsDataModel otherDetailsValueDataModel = new OtherDetailsDataModel();
        otherDetailsValueDataModel.setHeading(headingName);
        otherDetailsValueDataModel.setData(jsonArray.toString());
        if (fromWhere.equals(MyConstant.EDIT))
        {
            otherDetailsValueDataModel.setId(otherDetailsDataModel.getId());
            new ExecuteQueryAsyncTask<>(context, otherDetailsValueDataModel, MyConstant.UPDATE, new OnResultInterface<D>()
            {
                @Override
                public void OnCompleted(D data)
                {
                    CommonMethods.getInstance().show_Toast(context, context.getResources().getString(R.string.update_success));
                    CommonMethods.getInstance().hideKeypad(AddOtherDetail.this);

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
        else
        {
            /*When we insert the data*/
            new ExecuteQueryAsyncTask<>(context, otherDetailsValueDataModel, MyConstant.INSERT, new OnResultInterface<D>()
            {
                @Override
                public void OnCompleted(D data)
                {
                    CommonMethods.getInstance().show_Toast(context, context.getResources().getString(R.string.saved_success));
                    CommonMethods.getInstance().hideKeypad(AddOtherDetail.this);
                    finish();

                }
            });
        }
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
