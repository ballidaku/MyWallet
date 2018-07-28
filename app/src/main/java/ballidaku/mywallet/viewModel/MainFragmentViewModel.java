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
    private MainFragmentCallBack mainFragmentCallBack;
    private MainFragmentModel mainFragmentModel;


    MainFragmentViewModel(Context context, FragmentMainBinding binding, MainFragmentCallBack mainFragmentCallBack)
    {
        this.context = context;
        this.fragmentMainBinding = binding;
        this.mainFragmentCallBack=mainFragmentCallBack;

        mainFragmentModel=new MainFragmentModel(this.context,this);
        mainFragmentModel.getData();

    }



    /***********************************************************************/
    // Click Listener
    /***********************************************************************/

    public void onBankDetailsClick(View v)
    {
        mainFragmentCallBack.clickBankDetails();
    }

    public void onOtherDetailsClick(View v)
    {
        mainFragmentCallBack.clickOtherDetails();
    }


    /***********************************************************************/
    // Interface
    /***********************************************************************/
    public interface MainFragmentCallBack {

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
        mainFragmentCallBack.setAdapter(data);
    }


}
