package ballidaku.mywallet.viewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import java.util.Objects;

import ballidaku.mywallet.adapters.TextWatcherAdapter;
import ballidaku.mywallet.model.AddBankDetailsModel;

public class AddBankDetailsViewModel extends ViewModel implements AddBankDetailsModel.AddBankDetailsModelCallBack
{
    AddBankDetailsViewModelCallBack addBankDetailsViewModelCallBack;
    AddBankDetailsModel addBankDetailsModel;

    AddBankDetailsViewModel(Context context, AddBankDetailsViewModelCallBack addBankDetailsViewModelCallBack)
    {
        this.addBankDetailsViewModelCallBack = addBankDetailsViewModelCallBack;
        addBankDetailsModel = new AddBankDetailsModel(context, this);
    }


    public ObservableField<String> bankName = new ObservableField<>();
    public TextWatcher bankNameWatcher = new TextWatcherAdapter()
    {
        @Override
        public void afterTextChanged(Editable s)
        {
            if (!Objects.equals(bankName.get(), s.toString()))
            {
                bankName.set(s.toString());
            }
        }
    };




    /*Click Listeners*/

    public void onAddMoreFeildsClick(View v, Context context)
    {
        addBankDetailsViewModelCallBack.addMoreFeilds();
    }

    public void saveData()
    {

    }


    /***********************************************************************/
    // Interface

    /***********************************************************************/
    public interface AddBankDetailsViewModelCallBack
    {
        void addMoreFeilds();
    }


    /***********************************************************************/
    /*Interface Results from Model class*/

    /***********************************************************************/

    @Override
    public void showSnackbarMsg(String string)
    {

    }
}
