package ballidaku.mywallet.roomDatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.roomDatabase.dataModel.OtherDetailsDataModel;

/**
 * Created by sharanpalsingh on 20/02/18.
 */

@Dao
public interface OtherDetailsDataModelDao
{


    @Query("SELECT * FROM " + MyConstant.OTHER_DETAILS + " WHERE user_id = :userId")
    List<OtherDetailsDataModel> getAllData(String userId);

    @Query("SELECT * FROM " + MyConstant.OTHER_DETAILS + " WHERE id = :id")
    OtherDetailsDataModel getOtherDataDetail(int id);

    @Insert
    long insert(OtherDetailsDataModel otherDetailsDataModel);

    @Update
    int update(OtherDetailsDataModel otherDetailsDataModel);

    @Delete
    int delete(OtherDetailsDataModel accountTypeDataModel);


    @Query("DELETE FROM "+MyConstant.OTHER_DETAILS)
    int deleteAllOtherDetail();

  /*  @Query("DELETE FROM " + MyConstant.OTHER_DETAILS + " WHERE id = :id")
    int deleteOtherDetail(int id);*/



}

