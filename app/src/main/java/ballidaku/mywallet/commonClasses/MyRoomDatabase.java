package ballidaku.mywallet.commonClasses;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ballidaku.mywallet.dataModel.AccountTypeDataModel;

/**
 * Created by sharanpalsingh on 20/02/18.
 */

@Database(version = 1, entities = {AccountTypeDataModel.class})
public abstract class MyRoomDatabase extends RoomDatabase
{
    private static final String DB_NAME = "MyWalletDatabase.db";

    private static MyRoomDatabase instance;

    public abstract AccountTypeDataModelDao accountTypeDataModelDao();

    public static MyRoomDatabase getInstance(Context context)
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(
                    context,
                    MyRoomDatabase.class,
                    DB_NAME)
                    .allowMainThreadQueries().build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

   // public abstract RoomInterfaces.AccountTypeDataModelDao getAccountTypeDataModelDao();
}
