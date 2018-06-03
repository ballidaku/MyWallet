package ballidaku.mywallet.commonClasses;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ballidaku.mywallet.dataModel.AccountTypeDataModel;

/**
 * Created by sharanpalsingh on 28/12/17.
 */

public interface MyInterfaces
{
    interface UpdateDetails
    {
        void onSuccess();
    }



}
