package ballidaku.mywallet.model;

import android.content.Context;
import android.databinding.BaseObservable;

import java.util.ArrayList;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.roomDatabase.ExecuteQueryAsyncTask;
import ballidaku.mywallet.roomDatabase.OnResultInterface;
import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;
import ballidaku.mywallet.roomDatabase.dataModel.OtherDetailsDataModel;
import ballidaku.mywallet.viewModel.MainFragmentViewModel;

public class MainFragmentModel<D> extends BaseObservable
{

    private MainFragmentViewModel mainFragmentViewModel;
    private Context context;
    private ArrayList<D> mainList = new ArrayList<>();

    public MainFragmentModel(Context context, MainFragmentViewModel mainFragmentViewModel)
    {
        this.mainFragmentViewModel=mainFragmentViewModel;
        this.context=context;
    }

    @SuppressWarnings("unchecked")
    public void getData()
    {
        mainList.clear();
        new ExecuteQueryAsyncTask<>(context, new AccountDetailsDataModel(), MyConstant.GET_ALL, (OnResultInterface<D>) data ->
        {
            if (((ArrayList<D>) data).size() > 0)
            {
                final AccountDetailsDataModel accountDetailsDataModel = new AccountDetailsDataModel();
                accountDetailsDataModel.setType(context.getString(R.string.bank_detail));
                ((ArrayList<D>) data).add(0, (D) accountDetailsDataModel);

            }
            mainList.addAll((ArrayList<D>) data);

            new ExecuteQueryAsyncTask<>(context, new OtherDetailsDataModel(), MyConstant.GET_ALL, (OnResultInterface<D>) data1 ->
            {
                if (((ArrayList<D>) data1).size() > 0)
                {
                    final OtherDetailsDataModel otherDetailsDataModel = new OtherDetailsDataModel();
                    otherDetailsDataModel.setType(context.getString(R.string.other_detail));
                    ((ArrayList<D>) data1).add(0, (D) otherDetailsDataModel);
                }

                mainList.addAll((ArrayList<D>) data1);

                mainFragmentViewModel.onResult(mainList);
            });
        });
    }


    /***********************************************************************/
    // Interface
    /***********************************************************************/
    public interface MainFragmentModelCallBack {

        void onResult(ArrayList data);


    }
}
