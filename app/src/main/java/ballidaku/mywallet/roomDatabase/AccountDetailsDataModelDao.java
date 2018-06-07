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
/* @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND "
           + "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);
*/



    @Query("SELECT * FROM "+ MyConstant.ACCOUNT_DETAILS)
    List<AccountDetailsDataModel> getAllData();

    @Insert
    long insert(AccountDetailsDataModel accountTypeDataModel);

    @Update
    void update(AccountDetailsDataModel accountTypeDataModel);

    @Delete
    void delete(AccountDetailsDataModel accountTypeDataModel);


    @Query("DELETE FROM " + MyConstant.ACCOUNT_DETAILS + " WHERE id = :id")
    int deleteAccountDetail(int id);
}

