package ballidaku.mywallet.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;

import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.MyConstant;

import ballidaku.mywallet.databinding.InflaterBankAccountsItemBinding;
import ballidaku.mywallet.mainScreens.activities.ShowBankDetails;
import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;

/**
 * Created by sharanpalsingh on 19/02/18.
 */

public class MainFragmentAdapter<T> extends RecyclerView.Adapter<MainFragmentAdapter.ViewHolder>
{
    private ArrayList<T> arrayList;
    private Context context;

    public MainFragmentAdapter(ArrayList<T> arrayList, Context context)
    {

        this.arrayList = arrayList;
        this.context = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        InflaterBankAccountsItemBinding inflaterBankAccountsItemBinding;

        ViewHolder(InflaterBankAccountsItemBinding itemView)
        {
            super(itemView.getRoot());
            this.inflaterBankAccountsItemBinding = itemView;
        }
    }

    @NonNull
    @Override
    public MainFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        InflaterBankAccountsItemBinding inflaterBankAccountsItemBinding = InflaterBankAccountsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(inflaterBankAccountsItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainFragmentAdapter.ViewHolder holder, int position)
    {

        if (arrayList.get(position) instanceof AccountDetailsDataModel)
        {
            AccountDetailsDataModel accountDetailsDataModel = (AccountDetailsDataModel) arrayList.get(position);

            accountDetailsDataModel.setBankName(accountDetailsDataModel.getBankName());
            accountDetailsDataModel.setAccountHolderName(accountDetailsDataModel.getAccountHolderName());

            holder.inflaterBankAccountsItemBinding.setDataModel(accountDetailsDataModel);

            holder.inflaterBankAccountsItemBinding.setClickModel(new ClickViewModel(context, accountDetailsDataModel.getId()));
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return super.getItemViewType(position);

    }

    // Decrypt Data
    public String dTD(String data)
    {
        return CommonMethods.getInstance().decrypt(context, data);
    }

    public void addData(ArrayList<T> arrayList)
    {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }

    public class ClickViewModel<T>
    {

        Context context;
        int id;

        ClickViewModel(Context context, int id)
        {

            this.context = context;
            this.id = id;
        }

        public void handleClick(View view)
        {

            Intent intent = new Intent(context, ShowBankDetails.class);
            intent.putExtra(MyConstant.LIST_ITEM_ID, id);
            context.startActivity(intent);

        }
    }

}