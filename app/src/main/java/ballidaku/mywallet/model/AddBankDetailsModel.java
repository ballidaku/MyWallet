package ballidaku.mywallet.model;

import android.content.Context;

import ballidaku.mywallet.commonClasses.MySharedPreference;

public class AddBankDetailsModel
{
    private String TAG = AddBankDetailsModel.class.getSimpleName();
    private Context context;
    private AddBankDetailsModelCallBack addBankDetailsModelCallBack;
    private String userId;

    public AddBankDetailsModel(Context context, AddBankDetailsModelCallBack addBankDetailsModelCallBack)
    {
        this.context=context;
        this.addBankDetailsModelCallBack=addBankDetailsModelCallBack;

        userId = MySharedPreference.getInstance().getUserID(context);
    }




    /***********************************************************************/
    // Interface
    /***********************************************************************/
    public interface AddBankDetailsModelCallBack
    {
        void showSnackbarMsg(String string);
    }
}
