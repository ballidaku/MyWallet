package ballidaku.mywallet.mPin;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ballidaku.mywallet.R;

/**
 * Created by aritraroy on 31/05/16.
 */
public class PinLockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private static final int VIEW_TYPE_NUMBER = 0;
    private static final int VIEW_TYPE_DELETE = 1;
    private static final int VIEW_TYPE_RESET = 2;

    private Context mContext;
    private CustomizationOptionsBundle mCustomizationOptionsBundle;
    private OnNumberClickListener mOnNumberClickListener;
    private OnResetClickListener onResetClickListener;
    private OnDeleteClickListener mOnDeleteClickListener;
    private int mPinLength;

    private int[] mKeyValues;

    PinLockAdapter(Context context)
    {
        this.mContext = context;
        this.mKeyValues = getAdjustKeyValues(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0});
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_NUMBER)
        {
            View view = inflater.inflate(R.layout.layout_number_item, parent, false);
            viewHolder = new NumberViewHolder(view);
        }
        else if (viewType == VIEW_TYPE_DELETE)
        {
            View view = inflater.inflate(R.layout.layout_delete_item, parent, false);
            viewHolder = new DeleteViewHolder(view);
        }
        else
        {
            View view = inflater.inflate(R.layout.layout_delete_item, parent, false);
            viewHolder = new ResetViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {

        if (holder.getItemViewType() == VIEW_TYPE_NUMBER)
        {
            NumberViewHolder vh1 = (NumberViewHolder) holder;
            configureNumberButtonHolder(vh1, position);
        }
        else if (holder.getItemViewType() == VIEW_TYPE_DELETE)
        {
            DeleteViewHolder vh2 = (DeleteViewHolder) holder;
            configureDeleteButtonHolder(vh2);
        }
        else if (holder.getItemViewType() == VIEW_TYPE_RESET)
        {
            ResetViewHolder vh3 = (ResetViewHolder) holder;
            configureResetButtonHolder(vh3);
        }
    }

    private void configureNumberButtonHolder(NumberViewHolder holder, int position)
    {
        if (holder != null)
        {
//            if (position == 9)
//            {
////                holder.mNumberButton.setVisibility(View.GONE);
//                holder.mNumberButton.setText("Reset");
//                holder.mNumberButton.setTag("reset");
//            }
//            else
//            {
            holder.mNumberButton.setText(String.valueOf(mKeyValues[position]));
            holder.mNumberButton.setVisibility(View.VISIBLE);
            holder.mNumberButton.setTag(mKeyValues[position]);
//            }

            if (mCustomizationOptionsBundle != null)
            {
                holder.mNumberButton.setTextColor(mCustomizationOptionsBundle.getTextColor());
                if (mCustomizationOptionsBundle.getButtonBackgroundDrawable() != null)
                {
                    holder.mNumberButton.setBackground(mCustomizationOptionsBundle.getButtonBackgroundDrawable());
                }

                holder.mNumberButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCustomizationOptionsBundle.getTextSize());


//                Log.e("POS ",""+position);
//                if (position == 9)
//                {
////                    Log.e("POS IN ",""+position);
//                    holder.mNumberButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
//                }
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mCustomizationOptionsBundle.getButtonSize(), mCustomizationOptionsBundle.getButtonSize());
                holder.mNumberButton.setLayoutParams(params);
            }
        }
    }

    private void configureResetButtonHolder(ResetViewHolder holder)
    {
        if (holder != null)
        {

            if (mCustomizationOptionsBundle != null)
            {
                holder.mButtonImage.setColorFilter(mCustomizationOptionsBundle.getTextColor(), PorterDuff.Mode.SRC_ATOP);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80);
                holder.mButtonImage.setLayoutParams(params);
            }
        }
    }

    private void configureDeleteButtonHolder(DeleteViewHolder holder)
    {
        if (holder != null)
        {
            if (mCustomizationOptionsBundle.isShowDeleteButton() && mPinLength > 0)
            {
                holder.mButtonImage.setVisibility(View.VISIBLE);
                if (mCustomizationOptionsBundle.getDeleteButtonDrawable() != null)
                {
                    holder.mButtonImage.setImageDrawable(mCustomizationOptionsBundle.getDeleteButtonDrawable());
                }
                holder.mButtonImage.setColorFilter(mCustomizationOptionsBundle.getTextColor(), PorterDuff.Mode.SRC_ATOP);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mCustomizationOptionsBundle.getDeleteButtonSize(),mCustomizationOptionsBundle.getDeleteButtonSize());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(90, 90);

//                Log.e("getDeleteButtonSize ",""+mCustomizationOptionsBundle.getDeleteButtonSize());
                holder.mButtonImage.setLayoutParams(params);
            }
            else
            {
                holder.mButtonImage.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return 12;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == getItemCount() - 1)
        {
            return VIEW_TYPE_DELETE;
        }
        else if (position == getItemCount() - 3)
        {
            return VIEW_TYPE_RESET;
        }
        return VIEW_TYPE_NUMBER;
    }

    public int getPinLength()
    {
        return mPinLength;
    }

    public void setPinLength(int pinLength)
    {
        this.mPinLength = pinLength;
    }

    public int[] getKeyValues()
    {
        return mKeyValues;
    }

    public void setKeyValues(int[] keyValues)
    {
        this.mKeyValues = getAdjustKeyValues(keyValues);
        notifyDataSetChanged();
    }

    private int[] getAdjustKeyValues(int[] keyValues)
    {
        int[] adjustedKeyValues = new int[keyValues.length + 1];
        for (int i = 0; i < keyValues.length; i++)
        {
            if (i < 9)
            {
                adjustedKeyValues[i] = keyValues[i];
            }
            else
            {
                adjustedKeyValues[i] = -1;
                adjustedKeyValues[i + 1] = keyValues[i];
            }
        }
        return adjustedKeyValues;
    }

    public OnNumberClickListener getOnItemClickListener()
    {
        return mOnNumberClickListener;
    }

    public void setOnItemClickListener(OnNumberClickListener onNumberClickListener)
    {
        this.mOnNumberClickListener = onNumberClickListener;
    }


    public void setOnResetClickListener(OnResetClickListener onResetClickListener)
    {
        this.onResetClickListener = onResetClickListener;
    }

    public OnDeleteClickListener getOnDeleteClickListener()
    {
        return mOnDeleteClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener)
    {
        this.mOnDeleteClickListener = onDeleteClickListener;
    }

    public CustomizationOptionsBundle getCustomizationOptions()
    {
        return mCustomizationOptionsBundle;
    }

    public void setCustomizationOptions(CustomizationOptionsBundle customizationOptionsBundle)
    {
        this.mCustomizationOptionsBundle = customizationOptionsBundle;
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder
    {
        Button mNumberButton;

        NumberViewHolder(final View itemView)
        {
            super(itemView);
            mNumberButton = itemView.findViewById(R.id.button);
            mNumberButton.setOnClickListener(v ->
            {
                if (mOnNumberClickListener != null)
                {
                    mOnNumberClickListener.onNumberClicked((Integer) v.getTag());
                }
            });
        }
    }

    public class ResetViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout mDeleteButton;
        ImageView mButtonImage;

        ResetViewHolder(final View itemView)
        {
            super(itemView);
            mDeleteButton = itemView.findViewById(R.id.button);
            mButtonImage = itemView.findViewById(R.id.buttonImage);
            mButtonImage.setImageResource(R.drawable.ic_reset);

            mDeleteButton.setOnClickListener(v ->
            {
                if (onResetClickListener != null)
                {
                    onResetClickListener.onResetClicked();
                }
            });
        }
    }

    public class DeleteViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout mDeleteButton;
        ImageView mButtonImage;

        DeleteViewHolder(final View itemView)
        {
            super(itemView);
            mDeleteButton =  itemView.findViewById(R.id.button);
            mButtonImage =  itemView.findViewById(R.id.buttonImage);

            if (mCustomizationOptionsBundle.isShowDeleteButton() && mPinLength > 0)
            {
                mDeleteButton.setOnClickListener(v ->
                {
                    Log.e("DELETE Adapter", "setOnClickListener");

                    if (mOnDeleteClickListener != null)
                    {
                        mOnDeleteClickListener.onDeleteClicked();
                    }
                });

                mDeleteButton.setOnLongClickListener(v ->
                {
                    Log.e("DELETE Adapter", "setOnLongClickListener");
                    if (mOnDeleteClickListener != null)
                    {
                        mOnDeleteClickListener.onDeleteLongClicked();
                    }
                    return true;
                });

                mDeleteButton.setOnTouchListener(new View.OnTouchListener()
                {
                    private Rect rect;

                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {

                        Log.e("DELETE Adapter", "onTouch");


                        if (event.getAction() == MotionEvent.ACTION_DOWN)
                        {
                            mButtonImage.setColorFilter(mCustomizationOptionsBundle
                                    .getDeleteButtonPressesColor());
                            rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP)
                        {
                            mButtonImage.clearColorFilter();
                        }
                        if (event.getAction() == MotionEvent.ACTION_MOVE)
                        {
                            if (!rect.contains(v.getLeft() + (int) event.getX(),
                                    v.getTop() + (int) event.getY()))
                            {
                                mButtonImage.clearColorFilter();
                            }
                        }
                        return false;
                    }
                });
            }
        }
    }

    public interface OnNumberClickListener
    {
        void onNumberClicked(int keyValue);
    }

    public interface OnDeleteClickListener
    {
        void onDeleteClicked();

        void onDeleteLongClicked();
    }

    public interface OnResetClickListener
    {
        void onResetClicked();
    }

}
