package ballidaku.mywallet.commonClasses;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ballidaku.mywallet.dataModel.AccountTypeDataModel;

/**
 * Created by sharanpalsingh on 20/02/18.
 */


@Dao
public interface AccountTypeDataModelDao
{

    @Query("SELECT * FROM accountType")
    List<AccountTypeDataModel> getAllData();

    @Insert
    void insert(AccountTypeDataModel accountTypeDataModel);

    @Update
    void update(AccountTypeDataModel accountTypeDataModel);

    @Delete
    void delete(AccountTypeDataModel accountTypeDataModel);
}

