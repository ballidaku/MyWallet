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

public class MainFragmentModel<D> extends BaseObservable
{

    private MainFragmentModelCallBack mainFragmentModelCallBack;
    private Context context;


    public MainFragmentModel(Context context, MainFragmentModelCallBack mainFragmentModelCallBack)
    {
        this.mainFragmentModelCallBack = mainFragmentModelCallBack;
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    public void getData()
    {
        ArrayList<D> mainList = new ArrayList<>();
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

                mainFragmentModelCallBack.onResult(mainList);
            });
        });
    }


    /***********************************************************************/
    // Interface
    /***********************************************************************/
    public interface MainFragmentModelCallBack
    {
        void onResult(ArrayList data);
    }

}
