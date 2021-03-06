package ballidaku.mywallet.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.mainScreens.activities.MainActivity;
import ballidaku.mywallet.mainScreens.activities.ShowBankDetails;
import ballidaku.mywallet.mainScreens.activities.ShowOtherDetail;
import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;
import ballidaku.mywallet.roomDatabase.dataModel.OtherDetailsDataModel;

/**
 * Created by sharanpalsingh on 19/02/18.
 */

public class MainFragmentAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    String TAG= MainFragmentAdapter.class.getSimpleName();
    private ArrayList<T> arrayList;
    private Context context;
    FloatingActionMenu floatingActionMenu;

    public static final int TYPE_HEADER_BANK_DETAILS = 0;
    public static final int TYPE_ITEM_BANK_DETAILS = 1;
    public static final int TYPE_HEADER_OTHER_DETAILS = 2;
    public static final int TYPE_ITEM_OTHER_DETAILS = 3;

    public MainFragmentAdapter(Context context, ArrayList<T> arrayList, FloatingActionMenu floatingActionMenu)
    {
        this.arrayList = arrayList;
        this.context = context;
        this.floatingActionMenu=floatingActionMenu;
    }

    class HeaderViewHolder<T> extends RecyclerView.ViewHolder
    {
        public View View;
        private final TextView textViewHeaderName;

        HeaderViewHolder(View itemView)
        {
            super(itemView);
            View = itemView;
            textViewHeaderName =  View.findViewById(R.id.textViewHeaderName);
        }
    }

    class ItemViewHolder<T> extends RecyclerView.ViewHolder
    {
        public View View;
        private final TextView textViewBankName;
        private final TextView textViewBankOwner;
        private final TextView textViewBankHeader;
        private final TextView textViewAccountHeader;

        ItemViewHolder(View itemView)
        {
            super(itemView);
            View = itemView;
            textViewBankName = View.findViewById(R.id.textViewBankName);
            textViewBankOwner = View.findViewById(R.id.textViewBankOwner);
            textViewBankHeader = View.findViewById(R.id.textViewBankHeader);
            textViewAccountHeader = View.findViewById(R.id.textViewAccountHeader);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        if (viewType == TYPE_HEADER_BANK_DETAILS)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.inflater_main_fragment_header, parent, false);
            return new HeaderViewHolder(view);
        }
        else if (viewType == TYPE_ITEM_BANK_DETAILS)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.inflater_main_fragment_item, parent, false);
            return new ItemViewHolder(view);
        }
        else if (viewType == TYPE_HEADER_OTHER_DETAILS)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.inflater_main_fragment_header, parent, false);
            return new HeaderViewHolder(view);
        }
        else if (viewType == TYPE_ITEM_OTHER_DETAILS)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.inflater_main_fragment_item, parent, false);
            return new ItemViewHolder(view);
        }

        return null;
    }

    @Override
    @SuppressLint("RecyclerView")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position)
    {
        if (holder instanceof ItemViewHolder)
        {
            if (arrayList.get(position) instanceof AccountDetailsDataModel)
            {
                AccountDetailsDataModel accountDetailsDataModel = (AccountDetailsDataModel) arrayList.get(position);
//                Log.e(TAG,"UserId "+accountDetailsDataModel.getUserId());
                ((ItemViewHolder) holder).textViewBankName.setText(accountDetailsDataModel.getBankName());
                ((ItemViewHolder) holder).textViewBankOwner.setText(accountDetailsDataModel.getAccountHolderName());

                ((ItemViewHolder) holder).textViewAccountHeader.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).textViewBankOwner.setVisibility(View.VISIBLE);
            }
            else if (arrayList.get(position) instanceof OtherDetailsDataModel)
            {
                OtherDetailsDataModel otherDetailsDataModel = (OtherDetailsDataModel) arrayList.get(position);
                ((ItemViewHolder) holder).textViewBankName.setText(otherDetailsDataModel.getHeading());
                ((ItemViewHolder) holder).textViewBankHeader.setText(context.getResources().getString(R.string.heading_name));

                ((ItemViewHolder) holder).textViewAccountHeader.setVisibility(View.GONE);
                ((ItemViewHolder) holder).textViewBankOwner.setVisibility(View.GONE);

            }

            holder.itemView.setOnClickListener(v ->
            {

                floatingActionMenu.close(true);
                boolean isBankDetails= arrayList.get(position) instanceof AccountDetailsDataModel;

                Intent intent = new Intent(context, isBankDetails ? ShowBankDetails.class : ShowOtherDetail.class);
                intent.putExtra(MyConstant.LIST_ITEM_ID, isBankDetails ? ((AccountDetailsDataModel) arrayList.get(position)).getId() : ((OtherDetailsDataModel) arrayList.get(position)).getId());
                intent.putExtra(MyConstant.TYPE, isBankDetails ? MyConstant.BANK_DETAILS : MyConstant.OTHER_DETAILS);
                ((MainActivity)context).startActivityForResult(intent,MyConstant.Any_UPDATE_CODE);
            });
        }

        else if (holder instanceof HeaderViewHolder)
        {
            if (arrayList.get(position) instanceof AccountDetailsDataModel)
            {
                AccountDetailsDataModel accountDetailsDataModel = (AccountDetailsDataModel) arrayList.get(position);
                ((HeaderViewHolder) holder).textViewHeaderName.setText(accountDetailsDataModel.getType());

            }
            else if (arrayList.get(position) instanceof OtherDetailsDataModel)
            {
                OtherDetailsDataModel otherDetailsDataModel = (OtherDetailsDataModel) arrayList.get(position);
                ((HeaderViewHolder) holder).textViewHeaderName.setText(otherDetailsDataModel.getType());

            }
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if (arrayList.get(position) instanceof AccountDetailsDataModel && ((AccountDetailsDataModel) arrayList.get(position)).getType() != null)
        {
            return TYPE_HEADER_BANK_DETAILS;
        }
        else if (arrayList.get(position) instanceof AccountDetailsDataModel && ((AccountDetailsDataModel) arrayList.get(position)).getType() == null)
        {
            return TYPE_ITEM_BANK_DETAILS;
        }
        else if (arrayList.get(position) instanceof OtherDetailsDataModel && ((OtherDetailsDataModel) arrayList.get(position)).getType() != null)
        {
            return TYPE_HEADER_OTHER_DETAILS;
        }
        else if (arrayList.get(position) instanceof OtherDetailsDataModel && ((OtherDetailsDataModel) arrayList.get(position)).getType() == null)
        {
            return TYPE_ITEM_OTHER_DETAILS;
        }

        return super.getItemViewType(position);
    }

    public void addData(ArrayList<T> arrayList)
    {
        this.arrayList.clear();
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }



}