package ballidaku.mywallet.mainScreens.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
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
import java.util.List;

import ballidaku.mywallet.R;
import ballidaku.mywallet.adapters.MainFragmentAdapter;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.GridSpacingItemDecoration;
import ballidaku.mywallet.commonClasses.MyFirebase;
import ballidaku.mywallet.commonClasses.MyRoomDatabase;
import ballidaku.mywallet.dataModel.AccountTypeDataModel;
import ballidaku.mywallet.dataModel.KeyValueModel;
import ballidaku.mywallet.dataModel.UserBankDataModel;
import ballidaku.mywallet.databinding.FragmentMainBinding;

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

    public static final int ADD_DETAILS_RESPONSE = 3316;

    FragmentMainBinding fragmentMainBinding;

    public MainFragment()
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
            fragmentMainBinding = DataBindingUtil.inflate(
                    inflater, R.layout.fragment_main, container, false);
            view = fragmentMainBinding.getRoot();

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


    public void setUpIds()
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

//                MyFirebase.getInstance().createBankDetails();

                createType();
                break;

            case R.id.floatingActionButtonOtherDetails:
                fragmentMainBinding.floatingActionMenu.close(true);

                break;
        }
    }


    @SuppressLint("StaticFieldLeak")
    void createType()
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                AccountTypeDataModel accountTypeDataModel = new AccountTypeDataModel();
                accountTypeDataModel.setName("Hello Details");

                MyRoomDatabase.getInstance(context).accountTypeDataModelDao().insert(accountTypeDataModel);

                List<AccountTypeDataModel> list= MyRoomDatabase.getInstance(context).accountTypeDataModelDao().getAllData();

                for (int i = 0; i <list.size() ; i++)
                {

                    Log.e(TAG,""+list.get(i).getId() +"  "+list.get(i).getName());
                }


                return null;
            }

          /*  @Override
            protected void onPostExecute(Integer agentsCount) {
                if (agentsCount > 0) {
                    //2: If it already exists then prompt user
                    Toast.makeText(Activity.this, "Agent already exists!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(Activity.this, "Agent does not exist! Hurray :)", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }*/
        };/*.execute();*/





    }


//    public interface BankDetails
//    {
//        public void show_bank_details(HashMap<String, Object> map);
//    }

/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK && requestCode == ADD_DETAILS_RESPONSE)
        {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) data.getSerializableExtra("hashMap");

            MyFirebase.getInstance().createBankDetails(context, hashMap);
        }
    }*/


}