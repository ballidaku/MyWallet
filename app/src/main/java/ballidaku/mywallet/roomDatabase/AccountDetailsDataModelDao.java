package ballidaku.mywallet.roomDatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;

/**
 * Created by sharanpalsingh on 20/02/18.
 */

@Dao
public interface AccountDetailsDataModelDao
{

//    @Query("SELECT * FROM " + MyConstant.ACCOUNT_DETAILS )
//    List<AccountDetailsDataModel> getAllData();


    @Query("SELECT * FROM " + MyConstant.ACCOUNT_DETAILS + " WHERE user_id = :userId")
    List<AccountDetailsDataModel> getAllData(String userId);

    @Query("SELECT * FROM " + MyConstant.ACCOUNT_DETAILS + " WHERE id = :id")
    AccountDetailsDataModel getAccountDetailsDataModelData(int id);

    @Insert
    long insert(AccountDetailsDataModel accountTypeDataModel);

    @Update
    int update(AccountDetailsDataModel accountTypeDataModel);

    @Delete
    int delete(AccountDetailsDataModel accountTypeDataModel);

    @Query("DELETE FROM "+MyConstant.ACCOUNT_DETAILS)
    int deleteAllAccountDetail();


    /*@Query("DELETE FROM " + MyConstant.ACCOUNT_DETAILS + " WHERE id = :id ")
    int deleteAccountDetail(int id);*/



}

