package ballidaku.mywallet.mainScreens.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import ballidaku.mywallet.R;
import ballidaku.mywallet.adapters.BankAccountsAdapter;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.GridSpacingItemDecoration;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.commonClasses.MyFirebase;
import ballidaku.mywallet.dataModel.KeyValueModel;
import ballidaku.mywallet.dataModel.UserBankDataModel;
import ballidaku.mywallet.databinding.FragmentBankAccountsBinding;
import ballidaku.mywallet.mainScreens.activities.AddBankDetails;
import ballidaku.mywallet.mainScreens.activities.ShowBankDetails;


public class BankAccountsFragment extends Fragment implements View.OnClickListener
{

    String TAG = BankAccountsFragment.class.getSimpleName();

    View view = null;

    Context context;


    BankAccountsAdapter homeFragmentAdapter;

    ArrayList<KeyValueModel> mainList = new ArrayList<>();

    public static final int ADD_DETAILS_RESPONSE = 3316;

    FragmentBankAccountsBinding fragmentBankAccountsBinding;

    public BankAccountsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (view == null)
        {
            fragmentBankAccountsBinding = DataBindingUtil.inflate(
                    inflater, R.layout.fragment_bank_accounts, container, false);
            view = fragmentBankAccountsBinding.getRoot();

            context = getActivity();

            setUpIds();

            setListener();
        }

        return view;
    }


    // Set Listener
    public void setListener()
    {
        Query query = MyFirebase.getInstance().getMyAccountDetails(context);

        query.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                ArrayList<KeyValueModel> userBankDataModelsList = new ArrayList<KeyValueModel>();

                for (DataSnapshot child : dataSnapshot.getChildren())
                {

                    Log.e("Subscribers", "" + child.getValue());

                    KeyValueModel keyValueModel = new KeyValueModel();
                    keyValueModel.setKey(child.getKey());
                    keyValueModel.setUserBankDataModel(child.getValue(UserBankDataModel.class));


                    userBankDataModelsList.add(keyValueModel);

                       /* UserBankDataModel userBankDataModel= child.getValue(UserBankDataModel.class);
                        Log.e("Account_holder_name", "" +userBankDataModel.getAccountHolderName());
                        Log.e("getAccountNumber", "" +userBankDataModel.getAccountNumber());
                        Log.e("getAtmNumber", "" +userBankDataModel.getAtmNumber());
                        Log.e("getBankName", "" +userBankDataModel.getBankName());
                        Log.e("getCvv", "" +userBankDataModel.getCvv());
                        Log.e("getIfsc", "" +userBankDataModel.getIfsc());
                        Log.e("getNetBankingId", "" +userBankDataModel.getNetBankingId());
                        Log.e("getValidFrom", "" +userBankDataModel.getValidFrom());
                        Log.e("getValidThru", "" +userBankDataModel.getValidThru());*/
                }
                setData(userBankDataModelsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }


    // Set Data
    private void setData(ArrayList<KeyValueModel> userBankDataModelsList)
    {
        mainList = userBankDataModelsList;
        homeFragmentAdapter.addItem(mainList);
    }


    public void setUpIds()
    {
        homeFragmentAdapter = new BankAccountsAdapter(mainList, getContext());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        fragmentBankAccountsBinding.recycleViewHome.setLayoutManager(mLayoutManager);
        fragmentBankAccountsBinding.recycleViewHome.addItemDecoration(new GridSpacingItemDecoration(2, CommonMethods.getInstance().dpToPx(context, 5), true));
        fragmentBankAccountsBinding.recycleViewHome.setItemAnimator(new DefaultItemAnimator());
        fragmentBankAccountsBinding.recycleViewHome.setAdapter(homeFragmentAdapter);


        homeFragmentAdapter.setOnItemClickListener(new BankAccountsAdapter.MyClickListener()
        {
            @Override
            public void onItemClick(int position, View v)
            {

                Intent intent = new Intent(getActivity(), ShowBankDetails.class);
                intent.putExtra(MyConstant.LIST_ITEM_DATA, mainList.get(position));
                startActivity(intent);
            }
        });

        fragmentBankAccountsBinding.floatingActionButtonBankDetails.setOnClickListener(this);
        fragmentBankAccountsBinding.floatingActionButtonOtherDetails.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {


        switch (v.getId())
        {
            case R.id.floatingActionButtonBankDetails:

                fragmentBankAccountsBinding.floatingActionMenu.close(true);

                Intent intent = new Intent(context, AddBankDetails.class);
                intent.putExtra(MyConstant.FROM_WHERE, MyConstant.NEW);
                startActivityForResult(intent, ADD_DETAILS_RESPONSE);
                break;

            case R.id.floatingActionButtonOtherDetails:
                fragmentBankAccountsBinding.floatingActionMenu.close(true);

                break;
        }
    }


//    public interface BankDetails
//    {
//        public void show_bank_details(HashMap<String, Object> map);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK && requestCode == ADD_DETAILS_RESPONSE)
        {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) data.getSerializableExtra("hashMap");

            MyFirebase.getInstance().createBankDetails(context, hashMap);
        }
    }


}
