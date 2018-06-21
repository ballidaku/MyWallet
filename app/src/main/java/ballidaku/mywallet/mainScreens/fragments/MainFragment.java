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

import java.util.ArrayList;

import ballidaku.mywallet.R;
import ballidaku.mywallet.adapters.MainFragmentAdapter;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.GridSpacingItemDecoration;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.databinding.FragmentMainBinding;
import ballidaku.mywallet.mainScreens.activities.AddBankDetails;
import ballidaku.mywallet.mainScreens.activities.AddOtherDetail;
import ballidaku.mywallet.roomDatabase.ExecuteQueryAsyncTask;
import ballidaku.mywallet.roomDatabase.OnResultInterface;
import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;
import ballidaku.mywallet.roomDatabase.dataModel.OtherDetailsDataModel;

import static ballidaku.mywallet.mainScreens.fragments.BankAccountsFragment.ADD_DETAILS_RESPONSE;

/**
 * Created by sharanpalsingh on 19/02/18.
 */

public class MainFragment<D> extends Fragment implements View.OnClickListener
{
    String TAG = MainFragment.class.getSimpleName();

    View view = null;
    Context context;

    MainFragmentAdapter<D> mainFragmentAdapter;
    FragmentMainBinding fragmentMainBinding;

    public MainFragment()
    {
        // Required empty public constructor
    }

    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
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
        }

        return view;
    }

    private void refresh()
    {
        final ArrayList<D> mainList = new ArrayList<>();

        new ExecuteQueryAsyncTask<>(context, new AccountDetailsDataModel(), MyConstant.GET_ALL, new OnResultInterface<D>()
        {
            @Override
            public void OnCompleted(D data)
            {
                Log.e(TAG, data + "--");
                mainList.addAll((ArrayList<D>) data);

                new ExecuteQueryAsyncTask<>(context, new OtherDetailsDataModel(), MyConstant.GET_ALL, new OnResultInterface<D>()
                {
                    @Override
                    public void OnCompleted(D data)
                    {
                        Log.e(TAG, data + "--");
                        mainList.addAll((ArrayList<D>) data);
                        setData(mainList);
                    }
                });
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        refresh();
    }

    // Set Data
    private void setData(ArrayList<D> mainList)
    {
        mainFragmentAdapter.addData(mainList);
    }

    public void setUpViews()
    {
        ArrayList<D> mainList = new ArrayList<>();
        mainFragmentAdapter = new MainFragmentAdapter<>(mainList, getContext());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        fragmentMainBinding.recycleViewHome.setLayoutManager(mLayoutManager);
        fragmentMainBinding.recycleViewHome.addItemDecoration(new GridSpacingItemDecoration(2, CommonMethods.getInstance().dpToPx(context, 5), true));
        fragmentMainBinding.recycleViewHome.setItemAnimator(new DefaultItemAnimator());
        fragmentMainBinding.recycleViewHome.setAdapter(mainFragmentAdapter);

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

                Intent intentOther = new Intent(context, AddOtherDetail.class);
                startActivity(intentOther);

                break;
        }
    }

}