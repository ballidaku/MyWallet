package ballidaku.mywallet.roomDatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;
import ballidaku.mywallet.roomDatabase.dataModel.OtherDetailsDataModel;

/**
 * Created by sharanpalsingh on 20/02/18.
 */

@Dao
public interface OtherDetailsDataModelDao
{


    @Query("SELECT * FROM " + MyConstant.ACCOUNT_DETAILS)
    List<AccountDetailsDataModel> getAllData();

    @Query("SELECT * FROM " + MyConstant.ACCOUNT_DETAILS + " WHERE id = :id")
    AccountDetailsDataModel getAAccountDetailsDataModelllData(int id);

    @Insert
    long insert(OtherDetailsDataModel otherDetailsDataModel);

    @Update
    int update(OtherDetailsDataModel otherDetailsDataModel);

    @Delete
    void delete(AccountDetailsDataModel accountTypeDataModel);

    @Query("DELETE FROM " + MyConstant.ACCOUNT_DETAILS + " WHERE id = :id")
    int deleteAccountDetail(int id);



}
