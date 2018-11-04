package ballidaku.mywallet.roomDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;
import ballidaku.mywallet.roomDatabase.dataModel.OtherDetailsDataModel;

/**
 * Created by sharanpalsingh on 20/02/18.
 */

@Database(version = 1, entities = {AccountDetailsDataModel.class, OtherDetailsDataModel.class}, exportSchema = false)
public abstract class MyRoomDatabase extends RoomDatabase
{
    private static MyRoomDatabase instance;

    public abstract AccountDetailsDataModelDao accountDetailsDataModelDao();
    public abstract OtherDetailsDataModelDao otherDetailsDataModelDao();

    public static MyRoomDatabase getInstance(Context context)
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context, MyRoomDatabase.class, MyConstant.DATABASE_NAME).allowMainThreadQueries().build();
        }
        return instance;
    }

    public static void destroyInstance()
    {
        instance = null;
    }

    // public abstract RoomInterfaces.AccountDetailsDataModelDao getAccountTypeDataModelDao();
}
