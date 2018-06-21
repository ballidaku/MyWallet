package ballidaku.mywallet.roomDatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;
import ballidaku.mywallet.roomDatabase.dataModel.OtherDetailsDataModel;

@SuppressLint("StaticFieldLeak")
public class ExecuteQueryAsyncTask<T> extends AsyncTask<Void, Void, T>
{
    private Context context;
    private T data;
    private String type;
    private OnResultInterface onResultInterface;

    public ExecuteQueryAsyncTask(Context context, T data, String type, OnResultInterface onResultInterface)
    {
        this.context = context;
        this.data = data;
        this.type = type;
        this.onResultInterface = onResultInterface;

        execute();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected T doInBackground(Void... params)
    {
        if (data instanceof AccountDetailsDataModel)
        {
            if (type.equalsIgnoreCase(MyConstant.INSERT))
            {
                return (T) String.valueOf(MyRoomDatabase.getInstance(context).accountDetailsDataModelDao().insert((AccountDetailsDataModel) data));
            }
            else if (type.equalsIgnoreCase(MyConstant.GET_ALL))
            {
                return (T) MyRoomDatabase.getInstance(context).accountDetailsDataModelDao().getAllData();
            }
            else if (type.equalsIgnoreCase(MyConstant.DELETE))
            {
                return (T) String.valueOf(MyRoomDatabase.getInstance(context).accountDetailsDataModelDao().deleteAccountDetail(((AccountDetailsDataModel) data).getId()));
            }
            else if (type.equalsIgnoreCase(MyConstant.UPDATE))
            {
                return (T) String.valueOf(MyRoomDatabase.getInstance(context).accountDetailsDataModelDao().update((AccountDetailsDataModel) data));
            }
            else if (type.equalsIgnoreCase(MyConstant.GET_ONE_ITEM))
            {
                return (T) MyRoomDatabase.getInstance(context).accountDetailsDataModelDao().getAccountDetailsDataModelData(((AccountDetailsDataModel) data).getId());
            }
        }

        else if (data instanceof OtherDetailsDataModel)
        {
            if (type.equalsIgnoreCase(MyConstant.INSERT))
            {
                return (T) String.valueOf(MyRoomDatabase.getInstance(context).otherDetailsDataModelDao().insert((OtherDetailsDataModel) data));
            }

            else if (type.equalsIgnoreCase(MyConstant.GET_ALL))
            {
                return (T) MyRoomDatabase.getInstance(context).otherDetailsDataModelDao().getAllData();
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onPostExecute(T result)
    {
        onResultInterface.OnCompleted(result);
    }
}