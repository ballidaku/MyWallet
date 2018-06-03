package ballidaku.mywallet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.dataModel.KeyValueModel;
import ballidaku.mywallet.dataModel.UserBankDataModel;

/**
 * Created by sharanpalsingh on 12/09/16.
 */
public class BankAccountsAdapter extends RecyclerView.Adapter<BankAccountsAdapter.ViewHolder>
{

    private String LOG_TAG = "BankAccountsAdapter";

    private ArrayList<KeyValueModel> userBankDataModelsList;
    // private ArrayList<Integer> iconList;
    private MyClickListener myClickListener;

    private Context context;




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView textView_BankName;
        TextView textView_BankOwner;
        TextView textView_BankAtm;

        public ViewHolder(View itemView)
        {

            super(itemView);
            textView_BankName = (TextView) itemView.findViewById(R.id.textView_BankName);
            textView_BankOwner = (TextView) itemView.findViewById(R.id.textView_BankOwner);
            textView_BankAtm = (TextView) itemView.findViewById(R.id.textView_BankAtm);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener)
    {

        this.myClickListener = myClickListener;
    }

    public BankAccountsAdapter(ArrayList<KeyValueModel> userBankDataModelsList, Context context)
    {

        this.userBankDataModelsList = userBankDataModelsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflater_bank_accounts_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        UserBankDataModel userBankDataModel=userBankDataModelsList.get(position).getUserBankDataModel();
        holder.textView_BankName.setText(dTD(userBankDataModel.getBank_name()));
        holder.textView_BankOwner.setText(dTD(userBankDataModel.getAccount_holder_name()));
       // holder.textView_BankAtm.setText(myUtil.setSpaceFormat(userBankDataModelsList.get(position).getAtmNumber()));
        holder.textView_BankAtm.setText(dTD(userBankDataModel.getAtm_number()));
    }

//    public void addItem(String dataObj, int index) {
//
//        //userBankDataModelsList.add(index, dataObj);
//
//        notifyItemInserted(index);
//    }


    // Decrypt Data
    public String dTD(String data)
    {
        return CommonMethods.getInstance().decrypt(context, data);
    }


    public void addItem(ArrayList<KeyValueModel> userBankDataModelsList)
    {
        this.userBankDataModelsList=userBankDataModelsList;
        notifyDataSetChanged();
    }

    public void deleteItem(int index)
    {

        userBankDataModelsList.remove(index);
        notifyItemRemoved(index);

    }

    @Override
    public int getItemCount()
    {
        return userBankDataModelsList.size();
    }

    public interface MyClickListener
    {

        public void onItemClick(int position, View v);
    }
}