package ballidaku.mywallet.roomDatabase.dataModel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;

import java.io.Serializable;

import ballidaku.mywallet.commonClasses.MyConstant;

/**
 * Created by sharanpalsingh on 20/02/18.
 */

@Entity(tableName = MyConstant.OTHER_DETAILS)
public class OtherDetailsDataModel extends BaseObservable implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = MyConstant.USER_ID)
    public String userId;

    @ColumnInfo(name = MyConstant.HEADING)
    public String heading;

    @ColumnInfo(name = MyConstant.DATA)
    public String data;

    public String type;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getHeading()
    {
        return heading;
    }

    public void setHeading(String heading)
    {
        this.heading = heading;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }
}
