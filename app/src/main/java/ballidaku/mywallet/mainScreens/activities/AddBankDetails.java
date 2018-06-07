package ballidaku.mywallet.mainScreens.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.List;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.FourDigitsCardTextWatcher;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.commonClasses.TwoDigitsCardTextWatcher;
import ballidaku.mywallet.dataModel.KeyValueModel;
import ballidaku.mywallet.dataModel.UserBankDataModel;
import ballidaku.mywallet.databinding.ActivityAddBankDetailsBinding;
import ballidaku.mywallet.roomDatabase.ExecuteQueryAsyncTask;
import ballidaku.mywallet.roomDatabase.MyRoomDatabase;
import ballidaku.mywallet.roomDatabase.OnResultInterface;
import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;


public class AddBankDetails<D> extends AppCompatActivity
{

    String TAG = AddBankDetails.class.getSimpleName();
    Context context;


    String fromWhere;
    KeyValueModel keyValueModel;
    UserBankDataModel userBankDataModel;
    String key;

    ActivityAddBankDetailsBinding activityAddBankDetailsBinding;
    String[] typeArray;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityAddBankDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_bank_details);

        context = this;

        setUpIds();
    }

    private void setUpIds()
    {
        typeArray = getResources().getStringArray(R.array.type);

        setSupportActionBar(activityAddBankDetailsBinding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        activityAddBankDetailsBinding.editTextAccountNumber.addTextChangedListener(new FourDigitsCardTextWatcher(activityAddBankDetailsBinding.editTextAccountNumber));
        activityAddBankDetailsBinding.editTextAtmNumber.addTextChangedListener(new FourDigitsCardTextWatcher(activityAddBankDetailsBinding.editTextAtmNumber));
        activityAddBankDetailsBinding.editTextIfscCode.addTextChangedListener(new FourDigitsCardTextWatcher(activityAddBankDetailsBinding.editTextIfscCode));

        activityAddBankDetailsBinding.editTextValidFrom.addTextChangedListener(new TwoDigitsCardTextWatcher(activityAddBankDetailsBinding.editTextValidFrom));
        activityAddBankDetailsBinding.editTextValidThru.addTextChangedListener(new TwoDigitsCardTextWatcher(activityAddBankDetailsBinding.editTextValidThru));

        fromWhere = getIntent().getStringExtra(MyConstant.FROM_WHERE);

        if (fromWhere.equals(MyConstant.EDIT))
        {
            activityAddBankDetailsBinding.toolbar.setTitle("UPDATE DETAILS");
            keyValueModel = (KeyValueModel) getIntent().getSerializableExtra(MyConstant.LIST_ITEM_DATA);
            userBankDataModel = keyValueModel.getUserBankDataModel();
            key = keyValueModel.getKey();

            setData();
        }
        else
        {
            activityAddBankDetailsBinding.toolbar.setTitle("ADD DETAILS");
        }

        activityAddBankDetailsBinding.cardViewAddMoreFeilds.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addMoreFeilds();
            }
        });

    }

    private void addMoreFeilds()
    {
        final View child = getLayoutInflater().inflate(R.layout.custom_add_more_feilds, null);
        MaterialSpinner spinnerType = child.findViewById(R.id.spinnerType);
        spinnerType.setItems(typeArray);

        ImageView imageViewCancel = (ImageView) child.findViewById(R.id.imageViewCancel);
        imageViewCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                activityAddBankDetailsBinding.linearLayoutAddViews.removeView(child);
//                Log.e(TAG,"count "+linearLayoutAddViews.getChildCount());

            }
        });

        activityAddBankDetailsBinding.linearLayoutAddViews.addView(child);
    }

    private void setData()
    {

        activityAddBankDetailsBinding.editTextBankName.setText(dTD(userBankDataModel.getBank_name()));
        activityAddBankDetailsBinding.editTextAccountHolderName.setText(dTD(userBankDataModel.getAccount_holder_name()));
        activityAddBankDetailsBinding.editTextAccountNumber.setText(dTD(userBankDataModel.getAccount_number()));
        activityAddBankDetailsBinding.editTextIfscCode.setText(dTD(userBankDataModel.getIfsc()));
        activityAddBankDetailsBinding.editTextAtmNumber.setText(dTD(userBankDataModel.getAtm_number()));
        activityAddBankDetailsBinding.editTextCvv.setText(dTD(userBankDataModel.getCvv()));
        activityAddBankDetailsBinding.editTextValidFrom.setText(dTD(userBankDataModel.getValid_from()));
        activityAddBankDetailsBinding.editTextValidThru.setText(dTD(userBankDataModel.getValid_thru()));
        activityAddBankDetailsBinding.editTextNetBankingId.setText(dTD(userBankDataModel.getNet_banking_id()));


        if (userBankDataModel.getAdditional_data() != null && !userBankDataModel.getAdditional_data().isEmpty())
        {
            String json = dTD(userBankDataModel.getAdditional_data());

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
                    EditText editTextValue = view.findViewById(R.id.editTextValue);
                    ImageView imageViewCancel = view.findViewById(R.id.imageViewCancel);

                    MaterialSpinner spinnerType = view.findViewById(R.id.spinnerType);

                    spinnerType.setItems(typeArray);

                    spinnerType.setSelectedIndex(type.equals(typeArray[0]) ? 0 : 1);

                    editTextTitle.setText(title);
                    editTextValue.setText(value);


                    imageViewCancel.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View views)
                        {
                            activityAddBankDetailsBinding.linearLayoutAddViews.removeView(view);
                        }
                    });


                    activityAddBankDetailsBinding.linearLayoutAddViews.addView(view);
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

    }

    private void saveData()
    {
        String bankName = activityAddBankDetailsBinding.editTextBankName.getText().toString().trim();
        String accountHolderName = activityAddBankDetailsBinding.editTextAccountHolderName.getText().toString().trim();
        String accountNumber = activityAddBankDetailsBinding.editTextAccountNumber.getText().toString().trim();
        String ifsc = activityAddBankDetailsBinding.editTextIfscCode.getText().toString().trim();
        String atmNumber = activityAddBankDetailsBinding.editTextAtmNumber.getText().toString().trim();
        String cvv = activityAddBankDetailsBinding.editTextCvv.getText().toString().trim();
        String validFrom = activityAddBankDetailsBinding.editTextValidFrom.getText().toString().trim();
        String validThru = activityAddBankDetailsBinding.editTextValidThru.getText().toString().trim();
        String netBankingId = activityAddBankDetailsBinding.editTextNetBankingId.getText().toString().trim();

        Log.e(TAG, "child count  " + activityAddBankDetailsBinding.linearLayoutAddViews.getChildCount());

        int count = activityAddBankDetailsBinding.linearLayoutAddViews.getChildCount();

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < count; i++)
        {
            View view = activityAddBankDetailsBinding.linearLayoutAddViews.getChildAt(i);

            EditText editTextTitle = view.findViewById(R.id.editTextTitle);
            EditText editTextValue = view.findViewById(R.id.editTextValue);

            MaterialSpinner spinnerType = view.findViewById(R.id.spinnerType);
//            spinnerType.setItems("Text", "Secret");


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

        AccountDetailsDataModel accountTypeDataModel = new AccountDetailsDataModel();
        accountTypeDataModel.setBankName(bankName);
        accountTypeDataModel.setAccountHolderName(accountHolderName);
        accountTypeDataModel.setAccountNumber(accountNumber);
        accountTypeDataModel.setIfsc(ifsc);
        accountTypeDataModel.setAtmNumber(atmNumber);
        accountTypeDataModel.setCvv(cvv);
        accountTypeDataModel.setValidFrom(validFrom);
        accountTypeDataModel.setValidThru(validThru);
        accountTypeDataModel.setNetBankingId(netBankingId);
        accountTypeDataModel.setAdditionalData(jsonArray.toString());


        new ExecuteQueryAsyncTask<>(context, accountTypeDataModel, MyConstant.INSERT, new OnResultInterface<D>()
        {
            @Override
            public void OnCompleted(D data)
            {
                Log.e(TAG,(String)data+"--");

                CommonMethods.getInstance().show_Toast(context,context.getResources().getString(R.string.saved_success));
                CommonMethods.getInstance().hideKeypad(AddBankDetails.this);
                finish();

            }
        });

        /*
        Log.e(TAG, "child count  " + activityAddBankDetailsBinding.linearLayoutAddViews.getChildCount());

        int count = activityAddBankDetailsBinding.linearLayoutAddViews.getChildCount();

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < count; i++)
        {
            View view = activityAddBankDetailsBinding.linearLayoutAddViews.getChildAt(i);

            EditText editTextTitle = view.findViewById(R.id.editTextTitle);
            EditText editTextValue = view.findViewById(R.id.editTextValue);

            MaterialSpinner spinnerType = view.findViewById(R.id.spinnerType);
//            spinnerType.setItems("Text", "Secret");


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

        map.put(MyConstant.ADDITIONAL_DATA, dTE(jsonArray.toString()));


        if (fromWhere.equals(MyConstant.EDIT))
        {
            Log.e(TAG, "KEY " + key);
            MyFirebase.getInstance().updateBankDetails(context, key, map, new MyInterfaces.UpdateDetails()
            {
                @Override
                public void onSuccess()
                {
                    CommonMethods.getInstance().show_Toast(context, "Details updated successfully");
                    Intent intent = new Intent();
                    intent.putExtra("hashMap", map);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
        else
        {
            Intent intent = new Intent();
            intent.putExtra("hashMap", map);
            setResult(RESULT_OK, intent);
            finish();
        }*/
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

    // Decrypt Data
    public String dTD(String data)
    {
        return CommonMethods.getInstance().decrypt(context, data);
    }

    // Encrypt Data
    public String dTE(String data)
    {
        return CommonMethods.getInstance().encrypt(context, data);
    }


}
