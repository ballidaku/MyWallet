package ballidaku.mywallet.viewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.view.View;

import java.util.ArrayList;

import ballidaku.mywallet.databinding.FragmentMainBinding;
import ballidaku.mywallet.model.MainFragmentModel;

public class MainFragmentViewModel<D> extends ViewModel implements MainFragmentModel.MainFragmentModelCallBack
{
    private Context context;
    private FragmentMainBinding fragmentMainBinding;
    private MainFragmentViewModelCallBack mainFragmentViewModelCallBack;
    private MainFragmentModel mainFragmentModel;


    MainFragmentViewModel(Context context, FragmentMainBinding binding, MainFragmentViewModelCallBack mainFragmentViewModelCallBack)
    {
        this.context = context;
        this.fragmentMainBinding = binding;
        this.mainFragmentViewModelCallBack=mainFragmentViewModelCallBack;

        mainFragmentModel=new MainFragmentModel(this.context,this);
        mainFragmentModel.getData();

    }



    /***********************************************************************/
    // Click Listener
    /***********************************************************************/

    public void onBankDetailsClick(View v)
    {
        mainFragmentViewModelCallBack.clickBankDetails();
    }

    public void onOtherDetailsClick(View v)
    {
        mainFragmentViewModelCallBack.clickOtherDetails();
    }


    /***********************************************************************/
    // Interface
    /***********************************************************************/
    public interface MainFragmentViewModelCallBack {

        void setAdapter(ArrayList data);

        void clickBankDetails();

        void clickOtherDetails();


    }


    /***********************************************************************/
    // Interface Results from Model class
    /***********************************************************************/

    @Override
    public void onResult(ArrayList data)
    {
        mainFragmentViewModelCallBack.setAdapter(data);
    }


}
