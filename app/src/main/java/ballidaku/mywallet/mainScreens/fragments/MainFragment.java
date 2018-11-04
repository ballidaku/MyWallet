package ballidaku.mywallet.mainScreens.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import ballidaku.mywallet.mainScreens.activities.MainActivity;
import ballidaku.mywallet.viewModel.MainFragmentViewModel;
import ballidaku.mywallet.viewModel.ViewModelFactory;


/**
 * Created by sharanpalsingh on 19/02/18.
 */

public class MainFragment<D> extends Fragment implements MainFragmentViewModel.MainFragmentViewModelCallBack
{
    String TAG = MainFragment.class.getSimpleName();
    View view = null;
    Context context;
    MainFragmentAdapter<D> mainFragmentAdapter;
    FragmentMainBinding fragmentMainBinding;
    MainFragmentViewModel mainFragmentViewModel;

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

            mainFragmentViewModel = ViewModelProviders.of(this, new ViewModelFactory<>(getActivity(), MainFragment.this, fragmentMainBinding, this)).get(MainFragmentViewModel.class);
            fragmentMainBinding.setViewModel(mainFragmentViewModel);

            view = fragmentMainBinding.getRoot();
            context = getActivity();
            setUpViews();
        }
        return view;
    }

    public void setUpViews()
    {
        ArrayList<D> mainList = new ArrayList<>();
        mainFragmentAdapter = new MainFragmentAdapter<>(context, mainList, fragmentMainBinding.floatingActionMenu);

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

        fragmentMainBinding.recycleViewHome.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();

                boolean endHasBeenReached = lastVisible + 5 >= totalItemCount;
                if (totalItemCount > 0 && endHasBeenReached)
                {
                    fragmentMainBinding.floatingActionMenu.setVisibility(View.GONE);
                }
                else
                {
                    fragmentMainBinding.floatingActionMenu.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {

//                if (newState == RecyclerView.SCROLL_STATE_IDLE)
//                {
//                    fragmentMainBinding.floatingActionMenu.setVisibility(View.VISIBLE);
//                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }

    public void refreshData()
    {
        mainFragmentViewModel.getDataFromDatabase();
    }


    /***********************************************************************/
    // Interface Results from ViewModel class

    /***********************************************************************/

    @Override
    public void setAdapter(ArrayList data)
    {
        mainFragmentAdapter.addData(data);
    }

    @Override
    public void clickBankDetails()
    {
        fragmentMainBinding.floatingActionMenu.close(true);
        Intent intent = new Intent(context, AddBankDetails.class);
        intent.putExtra(MyConstant.FROM_WHERE, MyConstant.NEW);
        ((MainActivity) context).startActivityForResult(intent, MyConstant.Any_UPDATE_CODE);
    }

    @Override
    public void clickOtherDetails()
    {
        fragmentMainBinding.floatingActionMenu.close(true);
        Intent intentOther = new Intent(context, AddOtherDetail.class);
        intentOther.putExtra(MyConstant.FROM_WHERE, MyConstant.NEW);
        ((MainActivity) context).startActivityForResult(intentOther, MyConstant.Any_UPDATE_CODE);
    }
}