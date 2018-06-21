package ballidaku.mywallet.mainScreens.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
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

    int UPDATE_DETAILS_RESPONSE = 3327;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activityShowOtherDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_show_other_detail);
        context = this;
        setUpIds();

       id = getIntent().getIntExtra(MyConstant.LIST_ITEM_ID, 0);
//       String type = getIntent().getStringExtra(MyConstant.TYPE);

    }


    @Override
    protected void onResume()
    {
        super.onResume();
        selectData();
    }

    private void selectData()
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

                    editTextTitle.setOnTouchListener(new MyTouchListener(editTextValue));
                    Log.e(TAG, "type  " + type);
                    if (type.equals(MyConstant.TEXT))
                    {
                        imageViewShow.setVisibility(View.GONE);
                        editTextValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    }
                    else
                    {
                        editTextValue.setTag(MyConstant.INVISIBLE);
                        editTextValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

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

    private void setUpIds()
    {
        setSupportActionBar(activityShowOtherDetailsBinding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
                intent.putExtra(MyConstant.LIST_ITEM_DATA, (Serializable) otherDetailsDataModel);
                intent.putExtra(MyConstant.FROM_WHERE, MyConstant.EDIT);
                startActivityForResult(intent, UPDATE_DETAILS_RESPONSE);

                break;

            case R.id.action_copy:

                copyContent();

                break;

            case R.id.action_share:

                shareContent();

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

    public String getData()
    {

        String copiedContent = "";

        for (int i = 0; i < editTextList.size(); i++)
        {
            if (editTextList.get(i).getCompoundDrawables()[2].getConstantState().equals(getResources().getDrawable(R.drawable.ic_check_selected).getConstantState()))
            {
                String content = editTextList.get(i).getTag().toString() + MyConstant.SPACE + editTextList.get(i).getText().toString();
                copiedContent += copiedContent.isEmpty() ? content : "\n" + content;

            }
        }

        //for copying single item
        if (!copiedContent.isEmpty() && !copiedContent.contains("\n"))
        {
            String[] singleContent = copiedContent.split(":");
            copiedContent = singleContent[1].replaceAll(" ", "");
        }

        return copiedContent;
    }

    public void copyContent()
    {
        String copiedContent = getData();

        if (!copiedContent.isEmpty())
        {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", copiedContent);
            clipboard.setPrimaryClip(clip);

            CommonMethods.getInstance().show_Toast(context, "Data Copied");
        }
        else
        {
            CommonMethods.getInstance().show_Toast(context, "Please select atleast single item");
        }

        //Log.e(TAG, copiedContent);
    }

    public void shareContent()
    {
        String sharedContent = getData();

        if (!sharedContent.isEmpty())
        {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Account Details");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, sharedContent);
            startActivity(Intent.createChooser(sharingIntent, "Share Using"));
        }
        else
        {
            CommonMethods.getInstance().show_Toast(context, "Please select atleast single item");
        }
    }

    public void deleteData()
    {

        new ExecuteQueryAsyncTask<>(context, otherDetailsDataModel, MyConstant.DELETE, new OnResultInterface<D>()
        {
            @Override
            public void OnCompleted(D data)
            {
                Log.e(TAG, data + "--");

                finish();

            }
        });
    }

    class MyTouchListener implements View.OnTouchListener
    {

        EditText editText;

        public MyTouchListener(EditText editText)
        {
            this.editText = editText;
            // this.editText.setEnabled(false);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            //  Log.e("hello", "hello1");

            if (event.getAction() == MotionEvent.ACTION_UP)
            {
//                if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
                if (!editText.getText().toString().trim().isEmpty())
                {

                    Drawable left = editText.getCompoundDrawables()[DRAWABLE_LEFT];
                    Drawable right;

                    Drawable alreadyRight = editText.getCompoundDrawables()[DRAWABLE_RIGHT];
                    Drawable unSelected = getResources().getDrawable(R.drawable.ic_check_unselected);

                    if (unSelected.getConstantState().equals(alreadyRight.getConstantState()))
                    {
                        right = getResources().getDrawable(R.drawable.ic_check_selected);
                    }
                    else
                    {
                        right = unSelected;
                    }

                    editText.setCompoundDrawablesWithIntrinsicBounds(left, null, right, null);

                    // Log.e("hello", "hello2");

                    return true;
                }
            }
            return false;
        }
    }

}
