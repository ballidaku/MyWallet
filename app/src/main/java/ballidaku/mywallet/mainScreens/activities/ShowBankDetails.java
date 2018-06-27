package ballidaku.mywallet.mainScreens.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.CommonDialogs;
import ballidaku.mywallet.commonClasses.CommonInterfaces;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.databinding.ActivityShowBankDetailsBinding;
import ballidaku.mywallet.roomDatabase.ExecuteQueryAsyncTask;
import ballidaku.mywallet.roomDatabase.OnResultInterface;
import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;

public class ShowBankDetails<D> extends AppCompatActivity
{

    Context context;

    String TAG = ShowBankDetails.class.getSimpleName();

    AccountDetailsDataModel accountDetailsDataModel;
    int id;
    ArrayList<EditText> editTextList;


    ActivityShowBankDetailsBinding activityShowBankDetailsBinding;

    boolean isPasscodeVerified;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityShowBankDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_show_bank_details);

        context = this;

        setUpViews();

        id = getIntent().getIntExtra(MyConstant.LIST_ITEM_ID, 0);

        getDataFromDatabase();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpViews()
    {

        setSupportActionBar(activityShowBankDetailsBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        activityShowBankDetailsBinding.editTextBankName.setOnTouchListener(CommonMethods.getInstance().new MyTouchListener(context, activityShowBankDetailsBinding.editTextBankName));
        activityShowBankDetailsBinding.editTextAccountHolderName.setOnTouchListener(CommonMethods.getInstance().new MyTouchListener(context, activityShowBankDetailsBinding.editTextAccountHolderName));
        activityShowBankDetailsBinding.editTextAccountNumber.setOnTouchListener(CommonMethods.getInstance().new MyTouchListener(context, activityShowBankDetailsBinding.editTextAccountNumber));
        activityShowBankDetailsBinding.editTextIfscCode.setOnTouchListener(CommonMethods.getInstance().new MyTouchListener(context, activityShowBankDetailsBinding.editTextIfscCode));
        activityShowBankDetailsBinding.editTextAtmNumber.setOnTouchListener(CommonMethods.getInstance().new MyTouchListener(context, activityShowBankDetailsBinding.editTextAtmNumber));
        activityShowBankDetailsBinding.editTextCvv.setOnTouchListener(CommonMethods.getInstance().new MyTouchListener(context, activityShowBankDetailsBinding.editTextCvv));
        activityShowBankDetailsBinding.editTextValidFrom.setOnTouchListener(CommonMethods.getInstance().new MyTouchListener(context, activityShowBankDetailsBinding.editTextValidFrom));
        activityShowBankDetailsBinding.editTextValidThru.setOnTouchListener(CommonMethods.getInstance().new MyTouchListener(context, activityShowBankDetailsBinding.editTextValidThru));
        activityShowBankDetailsBinding.editTextNetBankingId.setOnTouchListener(CommonMethods.getInstance().new MyTouchListener(context, activityShowBankDetailsBinding.editTextNetBankingId));


    }

    private void getDataFromDatabase()
    {
        final AccountDetailsDataModel data = new AccountDetailsDataModel();
        data.setId(id);

        new ExecuteQueryAsyncTask<>(context, data, MyConstant.GET_ONE_ITEM, (OnResultInterface<D>) data1 ->
        {
            accountDetailsDataModel = (AccountDetailsDataModel) data1;
            refreshData();
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void refreshData()
    {

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

                    @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.inflater_view_more_feilds, null);

                    EditText editTextTitle = view.findViewById(R.id.editTextTitle);
                    final EditText editTextValue = view.findViewById(R.id.editTextValue);
                    final ImageView imageViewShow = view.findViewById(R.id.imageViewShow);
                    imageViewShow.setImageResource(R.drawable.ic_visibility_off);
                    imageViewShow.setTag(MyConstant.HIDDEN);

                    if (type.equals(MyConstant.TEXT))
                    {
                        imageViewShow.setVisibility(View.GONE);
                        editTextValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    }
                    else
                    {
                        editTextValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editTextValue.setMaxLines(1);
                    }

                    imageViewShow.setOnClickListener(view1 ->
                    {
                        String tag = (String) imageViewShow.getTag();
                        if (tag.equals(MyConstant.HIDDEN))
                        {

                            if (isPasscodeVerified)
                            {
                                imageViewShow.setTag(MyConstant.VISIBLE);
                                editTextValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                imageViewShow.setImageResource(R.drawable.ic_visibility_on);
                            }
                            else
                            {
                                CommonDialogs.getInstance().showPasscodeDialog(context, new CommonInterfaces.checkPasscode()
                                {
                                    @Override
                                    public void onSuccess()
                                    {
                                        isPasscodeVerified = true;
                                        imageViewShow.setTag(MyConstant.VISIBLE);
                                        editTextValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                        imageViewShow.setImageResource(R.drawable.ic_visibility_on);
                                    }

                                    @Override
                                    public void onFailure()
                                    {
                                        CommonMethods.getInstance().showToast(context, getString(R.string.passcode_mismatch));
                                    }
                                });
                            }
                        }
                        else
                        {
                            imageViewShow.setTag(MyConstant.HIDDEN);
                            editTextValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            imageViewShow.setImageResource(R.drawable.ic_visibility_off);
                        }
                    });

                    editTextTitle.setKeyListener(null);
                    editTextValue.setKeyListener(null);

                    editTextTitle.setText(title);
                    editTextValue.setText(value);

                    editTextValue.setTag(title);
                    editTextList.add(editTextValue);

                    editTextValue.setOnTouchListener(CommonMethods.getInstance().new MyTouchListener(context, editTextValue));

                    activityShowBankDetailsBinding.linearLayoutAddViews.addView(view);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
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

                editDetails();

                break;

            case R.id.action_copy:

                CommonMethods.getInstance().copyContent(context, CommonMethods.getInstance().getData(context, editTextList));

                break;

            case R.id.action_share:

                CommonMethods.getInstance().shareContent(context, CommonMethods.getInstance().getData(context, editTextList));

                break;

            case R.id.action_delete:

                CommonDialogs.getInstance().showDeleteAlertDialog(context, this::deleteData);

                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editDetails()
    {
        if (isPasscodeVerified)
        {
            editIntent();
        }
        else
        {
            CommonDialogs.getInstance().showPasscodeDialog(context, new CommonInterfaces.checkPasscode()
            {
                @Override
                public void onSuccess()
                {
                    isPasscodeVerified = true;
                    editIntent();
                }

                @Override
                public void onFailure()
                {
                    CommonMethods.getInstance().showToast(context, getString(R.string.passcode_mismatch));
                }
            });
        }
    }


    void editIntent()
    {
        Intent intent = new Intent(context, AddBankDetails.class);
        intent.putExtra(MyConstant.LIST_ITEM_DATA, accountDetailsDataModel);
        intent.putExtra(MyConstant.FROM_WHERE, MyConstant.EDIT);
        startActivityForResult(intent, MyConstant.UPDATE_DETAILS_RESPONSE);
    }

    public void deleteData()
    {

        new ExecuteQueryAsyncTask<>(context, accountDetailsDataModel, MyConstant.DELETE, (OnResultInterface<D>) data ->
        {
            Log.e(TAG,"onDelete "+data);
            finish();
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == MyConstant.UPDATE_DETAILS_RESPONSE)
        {
            getDataFromDatabase();
        }
    }


}
