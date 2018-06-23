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


/**
 * Created by sharanpalsingh on 19/02/18.
 */

public class MainFragment<D> extends Fragment implements View.OnClickListener
{
    String TAG = MainFragment.class.getSimpleName();
    View view = null;
    Context context;
    MainFragmentAdapter mainFragmentAdapter;
    ArrayList<D> mainList = new ArrayList<>();

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
        }
        return view;
    }


    @Override
    public void onResume()
    {
        super.onResume();
        refresh();
    }

    public void setUpViews()
    {
        mainFragmentAdapter = new MainFragmentAdapter(mainList, getContext());

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        fragmentMainBinding.recycleViewHome.setLayoutManager(mLayoutManager);
        fragmentMainBinding.recycleViewHome.addItemDecoration(new GridSpacingItemDecoration(2, CommonMethods.getInstance().dpToPx(context, 5), true));
        fragmentMainBinding.recycleViewHome.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                switch (mainFragmentAdapter.getItemViewType(position))
                {
                    case MainFragmentAdapter.TYPE_HEADER_BANK_DETAILS:
                        return 2;

                    case MainFragmentAdapter.TYPE_ITEM_BANK_DETAILS:
                        return 1;
                    case MainFragmentAdapter.TYPE_HEADER_OTHER_DETAILS:
                        return 2;

                    case MainFragmentAdapter.TYPE_ITEM_OTHER_DETAILS:
                        return 1;
                    default:
                        return 1;
                }
            }
        });
        fragmentMainBinding.recycleViewHome.setAdapter(mainFragmentAdapter);
        fragmentMainBinding.floatingActionButtonBankDetails.setOnClickListener(this);
        fragmentMainBinding.floatingActionButtonOtherDetails.setOnClickListener(this);
    }


    private void refresh()
    {
        mainList.clear();
        new ExecuteQueryAsyncTask<>(context, new AccountDetailsDataModel(), MyConstant.GET_ALL, new OnResultInterface<D>()
        {
            @Override
            public void OnCompleted(D data)
            {
                Log.e(TAG, data + "--");

                if (((ArrayList<D>) data).size() > 0)
                {
                    final AccountDetailsDataModel accountDetailsDataModel = new AccountDetailsDataModel();
                    accountDetailsDataModel.setType(getResources().getString(R.string.bank_detail));
                    ((ArrayList<D>) data).add(0, (D) accountDetailsDataModel);

                }
                mainList.addAll((ArrayList<D>) data);

                new ExecuteQueryAsyncTask<>(context, new OtherDetailsDataModel(), MyConstant.GET_ALL, new OnResultInterface<D>()
                {
                    @Override
                    public void OnCompleted(D data)
                    {
                        Log.e(TAG, data + "--");

                        if (((ArrayList<D>) data).size() > 0)
                        {
                            final OtherDetailsDataModel otherDetailsDataModel = new OtherDetailsDataModel();
                            otherDetailsDataModel.setType(getResources().getString(R.string.other_detail));
                            ((ArrayList<D>) data).add(0, (D) otherDetailsDataModel);
                        }

                        mainList.addAll((ArrayList<D>) data);
                        mainFragmentAdapter.addData(mainList);
                    }
                });
            }
        });
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
                startActivity(intent);

                break;

            case R.id.floatingActionButtonOtherDetails:
                fragmentMainBinding.floatingActionMenu.close(true);

                Intent intentOther = new Intent(context, AddOtherDetail.class);
                intentOther.putExtra(MyConstant.FROM_WHERE, MyConstant.NEW);
                startActivity(intentOther);

                break;
        }
    }
}