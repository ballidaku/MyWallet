package ballidaku.mywallet.dataModel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by sharanpalsingh on 20/02/18.
 */

@Entity(tableName = "accountType")
public class AccountTypeDataModel
{
    @PrimaryKey(autoGenerate = true)
    public  int id;

    @ColumnInfo(name = "name")
    public  String name;



//    public AccountTypeDataModel( String name)
//    {
//        this.name = name;
//    }


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
