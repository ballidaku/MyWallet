package ballidaku.mywallet.roomDatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;

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