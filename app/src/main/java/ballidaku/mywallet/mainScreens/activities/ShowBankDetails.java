package ballidaku.mywallet.mainScreens.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.CommonDialogs;
import ballidaku.mywallet.commonClasses.CommonInterfaces;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.commonClasses.MyFirebase;
import ballidaku.mywallet.dataModel.KeyValueModel;
import ballidaku.mywallet.dataModel.UserBankDataModel;
import ballidaku.mywallet.databinding.ActivityShowBankDetailsBinding;
import ballidaku.mywallet.roomDatabase.ExecuteQueryAsyncTask;
import ballidaku.mywallet.roomDatabase.OnResultInterface;
import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;

public class ShowBankDetails<D> extends AppCompatActivity
{

    Context context;

    String TAG = ShowBankDetails.class.getSimpleName();

    AccountDetailsDataModel accountDetailsDataModel;
    UserBankDataModel userBankDataModel;
    String key;
    ArrayList<EditText> editTextList;

    int UPDATE_DETAILS_RESPONSE = 3317;


    ActivityShowBankDetailsBinding activityShowBankDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityShowBankDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_show_bank_details);

        context = this;

        setUpIds();

        accountDetailsDataModel = (AccountDetailsDataModel) getIntent().getSerializableExtra(MyConstant.LIST_ITEM_DATA);

        setData();
    }

    private void setData()
    {
        refreshData();


        activityShowBankDetailsBinding.editTextBankName.setOnTouchListener(new MyTouchListener(activityShowBankDetailsBinding.editTextBankName));
        activityShowBankDetailsBinding.editTextAccountHolderName.setOnTouchListener(new MyTouchListener(activityShowBankDetailsBinding.editTextAccountHolderName));
        activityShowBankDetailsBinding.editTextAccountNumber.setOnTouchListener(new MyTouchListener(activityShowBankDetailsBinding.editTextAccountNumber));
        activityShowBankDetailsBinding.editTextIfscCode.setOnTouchListener(new MyTouchListener(activityShowBankDetailsBinding.editTextIfscCode));
        activityShowBankDetailsBinding.editTextAtmNumber.setOnTouchListener(new MyTouchListener(activityShowBankDetailsBinding.editTextAtmNumber));
        activityShowBankDetailsBinding.editTextCvv.setOnTouchListener(new MyTouchListener(activityShowBankDetailsBinding.editTextCvv));
        activityShowBankDetailsBinding.editTextValidFrom.setOnTouchListener(new MyTouchListener(activityShowBankDetailsBinding.editTextValidFrom));
        activityShowBankDetailsBinding.editTextValidThru.setOnTouchListener(new MyTouchListener(activityShowBankDetailsBinding.editTextValidThru));
        activityShowBankDetailsBinding.editTextNetBankingId.setOnTouchListener(new MyTouchListener(activityShowBankDetailsBinding.editTextNetBankingId));


        editTextList = new ArrayList<>();

        editTextList.add(activityShowBankDetailsBinding.editTextBankName);
        editTextList.add(activityShowBankDetailsBinding.editTextAccountHolderName);
        editTextList.add(activityShowBankDetailsBinding.editTextAccountNumber);
        editTextList.add(activityShowBankDetailsBinding.editTextIfscCode);
        editTextList.add(activityShowBankDetailsBinding.editTextAtmNumber);
        editTextList.add(activityShowBankDetailsBinding.editTextCvv);
        editTextList.add(activityShowBankDetailsBinding.editTextValidFrom);
        editTextList.add(activityShowBankDetailsBinding.editTextValidThru);
        editTextList.add(activityShowBankDetailsBinding.editTextNetBankingId);


    }

    private void refreshData()
    {

        activityShowBankDetailsBinding.editTextBankName.setText(accountDetailsDataModel.getBankName());
        activityShowBankDetailsBinding.editTextAccountHolderName.setText(accountDetailsDataModel.accountHolderName);
        activityShowBankDetailsBinding.editTextAccountNumber.setText(accountDetailsDataModel.accountNumber);
        activityShowBankDetailsBinding.editTextIfscCode.setText(accountDetailsDataModel.getIfsc());
        activityShowBankDetailsBinding.editTextAtmNumber.setText(accountDetailsDataModel.getAtmNumber());
        activityShowBankDetailsBinding.editTextCvv.setText(accountDetailsDataModel.getCvv());
        activityShowBankDetailsBinding.editTextValidFrom.setText(accountDetailsDataModel.getValidFrom());
        activityShowBankDetailsBinding.editTextValidThru.setText(accountDetailsDataModel.getValidThru());
        activityShowBankDetailsBinding.editTextNetBankingId.setText(accountDetailsDataModel.getNetBankingId());

        if (accountDetailsDataModel.getAdditionalData() != null && !accountDetailsDataModel.getAdditionalData().isEmpty())
        {

            String json = accountDetailsDataModel.getAdditionalData();
            activityShowBankDetailsBinding.linearLayoutAddViews.removeAllViews();

            try
            {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString(MyConstant.TITLE);
                    String value = jsonObject.getString(MyConstant.VALUE);
                    String type = jsonObject.getString(MyConstant.TYPE);

                    final View view = getLayoutInflater().inflate(R.layout.custom_view_more_feilds, null);

                    EditText editTextTitle = view.findViewById(R.id.editTextTitle);
                    final EditText editTextValue = view.findViewById(R.id.editTextValue);
                    ImageView imageViewShow = view.findViewById(R.id.imageViewShow);

                    Log.e(TAG, "type  " + type);
                    if (type.equals(MyConstant.TEXT))
                    {
                        imageViewShow.setVisibility(View.GONE);
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


                    activityShowBankDetailsBinding.linearLayoutAddViews.addView(view);
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }


    // Decrypt Data
   public String dTD(String data)
   {
        return CommonMethods.getInstance().decrypt(context, data); }


    private void setUpIds()
    {
        setSupportActionBar(activityShowBankDetailsBinding.toolbar);

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

                Intent intent = new Intent(context, AddBankDetails.class);
                intent.putExtra(MyConstant.LIST_ITEM_DATA, (Serializable) accountDetailsDataModel);
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
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Account Details");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharedContent);
            startActivity(Intent.createChooser(sharingIntent, "Share Using"));
        }
        else
        {
            CommonMethods.getInstance().show_Toast(context, "Please select atleast single item");
        }
    }


    public void deleteData()
    {

        new ExecuteQueryAsyncTask<>(context, accountDetailsDataModel, MyConstant.DELETE, new OnResultInterface<D>() {
            @Override
            public void OnCompleted(D data) {
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK && requestCode == UPDATE_DETAILS_RESPONSE)
        {
            HashMap<String, Object> map = (HashMap<String, Object>) data.getSerializableExtra("hashMap");

            userBankDataModel = new UserBankDataModel((String) map.get(MyConstant.VALID_THRU),
                    (String) map.get(MyConstant.ACCOUNT_HOLDER_NAME),
                    (String) map.get(MyConstant.VALID_FROM),
                    (String) map.get(MyConstant.CVV),
                    (String) map.get(MyConstant.IFSC),
                    (String) map.get(MyConstant.BANK_NAME),
                    (String) map.get(MyConstant.NET_BANKING_ID),
                    (String) map.get(MyConstant.ATM_NUMBER),
                    (String) map.get(MyConstant.ACCOUNT_NUMBER),
                    (String) map.get(MyConstant.ADDITIONAL_DATA));

         //   keyValueModel.setUserBankDataModel(userBankDataModel);

            refreshData();

        }
    }
}
