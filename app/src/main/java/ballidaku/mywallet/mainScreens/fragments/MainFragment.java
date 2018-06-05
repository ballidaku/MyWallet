package ballidaku.mywallet.mainScreens.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
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

import ballidaku.mywallet.R;
import ballidaku.mywallet.adapters.MainFragmentAdapter;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.GridSpacingItemDecoration;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.commonClasses.MyFirebase;
import ballidaku.mywallet.dataModel.KeyValueModel;
import ballidaku.mywallet.dataModel.UserBankDataModel;
import ballidaku.mywallet.databinding.FragmentMainBinding;
import ballidaku.mywallet.mainScreens.activities.AddBankDetails;

import static ballidaku.mywallet.mainScreens.fragments.BankAccountsFragment.ADD_DETAILS_RESPONSE;

/**
 * Created by sharanpalsingh on 19/02/18.
 */

public class MainFragment extends Fragment implements View.OnClickListener
{
    String TAG = MainFragment.class.getSimpleName();
    View view = null;
    Context context;
    MainFragmentAdapter mainFragmentAdapter;
    ArrayList<KeyValueModel> mainList = new ArrayList<>();

//    public static final int ADD_DETAILS_RESPONSE = 3316;

    FragmentMainBinding fragmentMainBinding;

    public MainFragment()
    {
        // Required empty public constructor
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (view == null)
        {
            fragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
            view = fragmentMainBinding.getRoot();

            context = getActivity();

            setUpViews();

//            setListener();
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

//                        UserBankDataModel userBankDataModel= child.getValue(UserBankDataModel.class);
//                        Log.e("Account_holder_name", "" +userBankDataModel.getAccountHolderName());
//                        Log.e("getAccountNumber", "" +userBankDataModel.getAccountNumber());
//                        Log.e("getAtmNumber", "" +userBankDataModel.getAtmNumber());
//                        Log.e("getBankName", "" +userBankDataModel.getBankName());
//                        Log.e("getCvv", "" +userBankDataModel.getCvv());
//                        Log.e("getIfsc", "" +userBankDataModel.getIfsc());
//                        Log.e("getNetBankingId", "" +userBankDataModel.getNetBankingId());
//                        Log.e("getValidFrom", "" +userBankDataModel.getValidFrom());
//                        Log.e("getValidThru", "" +userBankDataModel.getValidThru());
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
        mainFragmentAdapter.addItem(mainList);
    }


    public void setUpViews()
    {

        mainFragmentAdapter = new MainFragmentAdapter(mainList, getContext());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        fragmentMainBinding.recycleViewHome.setLayoutManager(mLayoutManager);
        fragmentMainBinding.recycleViewHome.addItemDecoration(new GridSpacingItemDecoration(2, CommonMethods.getInstance().dpToPx(context, 5), true));
        fragmentMainBinding.recycleViewHome.setItemAnimator(new DefaultItemAnimator());
        fragmentMainBinding.recycleViewHome.setAdapter(mainFragmentAdapter);


//        mainFragmentAdapter.setOnItemClickListener(new BankAccountsAdapter.MyClickListener()
//        {
//            @Override
//            public void onItemClick(int position, View v)
//            {
//
//                Intent intent = new Intent(getActivity(), ShowBankDetails.class);
//                intent.putExtra(MyConstant.LIST_ITEM_DATA, mainList.get(position));
//                startActivity(intent);
//            }
//        });

        fragmentMainBinding.floatingActionButtonBankDetails.setOnClickListener(this);
        fragmentMainBinding.floatingActionButtonOtherDetails.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.floatingActionButtonBankDetails:

                fragmentMainBinding.floatingActionMenu.close(true);

                Intent intent = new Intent(context, AddBankDetails.class);
                intent.putExtra(MyConstant.FROM_WHERE, MyConstant.NEW);
                startActivityForResult(intent, ADD_DETAILS_RESPONSE);

                break;

            case R.id.floatingActionButtonOtherDetails:
                fragmentMainBinding.floatingActionMenu.close(true);

                break;
        }
    }





//    public interface BankDetails
//    {
//        public void show_bank_details(HashMap<String, Object> map);
//    }

 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK && requestCode == ADD_DETAILS_RESPONSE)
        {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) data.getSerializableExtra("hashMap");

//            MyFirebase.getInstance().createBankDetails(context, hashMap);
        }
    }*/


}