package ballidaku.mywallet.roomDatabase;

import android.content.Context;
import android.os.AsyncTask;

import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;

public class DatabaseQueryAsyncTask<D> extends AsyncTask<Void, Void, Void>
{
    Context context;
    D data;
//    T type;
    AfterQueryExecutedInterface afterQueryExecutedInterface;

    public DatabaseQueryAsyncTask(Context context, D data, /*T type,*/ AfterQueryExecutedInterface afterQueryExecutedInterface)
    {
        this.context = context;
        this.data = data;
        this.afterQueryExecutedInterface=afterQueryExecutedInterface;
        execute();
    }



    @Override
    protected  Void doInBackground(Void... params)
    {
        if(data instanceof AccountDetailsDataModel)
        {

           /* if(type.equals(MyConstant.INSERT))
            {
            // return  (R)(Long)MyRoomDatabase.getInstance(context).accountDetailsDataModelDao().insert((AccountDetailsDataModel) data);
            }
            else if(type.equals(MyConstant.INSERT))
            {
            //    return (R)MyRoomDatabase.getInstance(context).accountDetailsDataModelDao().getAllData();

            }*/
        }
        else
        {


        }


        return null;
    }

    @Override
    protected void onPostExecute(Void data)
    {
     //   afterQueryExecutedInterface.onCompleted(data);

    }
}