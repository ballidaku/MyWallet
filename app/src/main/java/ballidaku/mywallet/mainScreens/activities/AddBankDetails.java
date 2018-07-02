package ballidaku.mywallet.mainScreens.activities;

import android.annotation.SuppressLint;
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
import ballidaku.mywallet.commonClasses.FourDigitsCardTextWatcher;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.commonClasses.MySharedPreference;
import ballidaku.mywallet.commonClasses.TwoDigitsCardTextWatcher;
import ballidaku.mywallet.databinding.ActivityAddBankDetailsBinding;
import ballidaku.mywallet.roomDatabase.ExecuteQueryAsyncTask;
import ballidaku.mywallet.roomDatabase.OnResultInterface;
import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;

public class AddBankDetails<D> extends AppCompatActivity implements View.OnClickListener
{

    String TAG = AddBankDetails.class.getSimpleName();
    Context context;

    String fromWhere;
    AccountDetailsDataModel accountDetailsDataModel;

    ActivityAddBankDetailsBinding activityAddBankDetailsBinding;
    String[] typeArray;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityAddBankDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_bank_details);

        context = this;
        userId= MySharedPreference.getInstance().getUserID(context);

        setUpViews();
    }

    private void setUpViews()
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
            activityAddBankDetailsBinding.toolbar.setTitle(getString(R.string.update_details));
            accountDetailsDataModel = (AccountDetailsDataModel) getIntent().getSerializableExtra(MyConstant.LIST_ITEM_DATA);

            setData();
        }
        else
        {
            activityAddBankDetailsBinding.toolbar.setTitle(getString(R.string.add_details));
        }
        activityAddBankDetailsBinding.cardViewAddMoreFeilds.setOnClickListener(this);


    }

    private void addMoreFeilds()
    {
        @SuppressLint("InflateParams") final View child = getLayoutInflater().inflate(R.layout.custom_add_more_feilds, null);
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
                activityAddBankDetailsBinding.linearLayoutAddViews.removeView(child);

            }
        });

        activityAddBankDetailsBinding.linearLayoutAddViews.addView(child);
    }

    private void setData()
    {

        activityAddBankDetailsBinding.editTextBankName.setText(accountDetailsDataModel.getBankName());
        activityAddBankDetailsBinding.editTextAccountHolderName.setText(accountDetailsDataModel.getAccountHolderName());
        activityAddBankDetailsBinding.editTextAccountNumber.setText(accountDetailsDataModel.getAccountNumber());
        activityAddBankDetailsBinding.editTextIfscCode.setText(accountDetailsDataModel.getIfsc());
        activityAddBankDetailsBinding.editTextAtmNumber.setText(accountDetailsDataModel.getAtmNumber());
        activityAddBankDetailsBinding.editTextCvv.setText(accountDetailsDataModel.getCvv());
        activityAddBankDetailsBinding.editTextValidFrom.setText(accountDetailsDataModel.getValidFrom());
        activityAddBankDetailsBinding.editTextValidThru.setText(accountDetailsDataModel.getValidThru());
        activityAddBankDetailsBinding.editTextNetBankingId.setText(accountDetailsDataModel.getNetBankingId());
        if (accountDetailsDataModel.getAdditionalData() != null && !accountDetailsDataModel.getAdditionalData().isEmpty())
        {
            String json = accountDetailsDataModel.getAdditionalData();

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

                    if(type.equalsIgnoreCase(MyConstant.TEXT))
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
                CommonMethods.getInstance().showToast(context, getString(R.string.title_value_validation));
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
        }

        AccountDetailsDataModel accountTypeLocalDataModel = new AccountDetailsDataModel();
        accountTypeLocalDataModel.setBankName(bankName);
        accountTypeLocalDataModel.setAccountHolderName(accountHolderName);
        accountTypeLocalDataModel.setAccountNumber(accountNumber);
        accountTypeLocalDataModel.setIfsc(ifsc);
        accountTypeLocalDataModel.setAtmNumber(atmNumber);
        accountTypeLocalDataModel.setCvv(cvv);
        accountTypeLocalDataModel.setValidFrom(validFrom);
        accountTypeLocalDataModel.setValidThru(validThru);
        accountTypeLocalDataModel.setNetBankingId(netBankingId);
        accountTypeLocalDataModel.setAdditionalData(jsonArray.toString());
        accountTypeLocalDataModel.setUserId(userId);

        if (fromWhere.equals(MyConstant.EDIT))  /*When we have to update the data*/
        {
            accountTypeLocalDataModel.setId(accountDetailsDataModel.getId());

            new ExecuteQueryAsyncTask<>(context, accountTypeLocalDataModel, MyConstant.UPDATE, new OnResultInterface<D>()
            {
                @Override
                public void OnCompleted(D data)
                {
                    CommonMethods.getInstance().showToast(context, context.getString(R.string.update_success));
                    CommonMethods.getInstance().hideKeypad(AddBankDetails.this);

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
        else /*When we insert the data*/
        {
            new ExecuteQueryAsyncTask<>(context, accountTypeLocalDataModel, MyConstant.INSERT, new OnResultInterface<D>()
            {
                @Override
                public void OnCompleted(D data)
                {
                    CommonMethods.getInstance().showToast(context, context.getResources().getString(R.string.saved_success));
                    CommonMethods.getInstance().hideKeypad(AddBankDetails.this);
                    finish();
                }
            });
        }
    }


    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.cardViewAddMoreFeilds:
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
