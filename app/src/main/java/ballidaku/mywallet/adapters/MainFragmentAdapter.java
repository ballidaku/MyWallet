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

/**
 * Created by sharanpalsingh on 19/02/18.
 */

public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.ViewHolder>
{

    private String LOG_TAG = MainFragmentAdapter.class.getSimpleName();

    private ArrayList<KeyValueModel> userBankDataModelsList;
    // private ArrayList<Integer> iconList;
    private BankAccountsAdapter.MyClickListener myClickListener;

    private Context context;
    int width;

    public MainFragmentAdapter(ArrayList<KeyValueModel> userBankDataModelsList, Context context)
    {

        this.userBankDataModelsList = userBankDataModelsList;
        this.context = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView textViewTitle;
        TextView textViewTitleCount;

        public ViewHolder(View itemView)
        {

            super(itemView);
            textViewTitle =  itemView.findViewById(R.id.textViewTitle);
            textViewTitleCount = itemView.findViewById(R.id.textViewTitleCount);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(BankAccountsAdapter.MyClickListener myClickListener)
    {

        this.myClickListener = myClickListener;
    }


    @Override
    public MainFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflater_main_fragment_item, parent, false);

        return new MainFragmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainFragmentAdapter.ViewHolder holder, int position)
    {
//        holder.frameLayoutMain.setBackgroundColor(colors[position]);

//        UserBankDataModel userBankDataModel = userBankDataModelsList.get(position).getUserBankDataModel();
//        holder.textView_BankName.setText(dTD(userBankDataModel.getBank_name()));
//        holder.textView_BankOwner.setText(dTD(userBankDataModel.getAccount_holder_name()));
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
        this.userBankDataModelsList = userBankDataModelsList;
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
//        return 5;
        return userBankDataModelsList.size();
    }

    public interface MyClickListener
    {

        public void onItemClick(int position, View v);
    }
}