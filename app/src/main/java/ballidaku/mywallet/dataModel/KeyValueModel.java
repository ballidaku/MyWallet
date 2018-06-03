package ballidaku.mywallet.dataModel;

import java.io.Serializable;

/**
 * Created by sharanpalsingh on 08/05/17.
 */

public class KeyValueModel implements Serializable
{

    public KeyValueModel()
    {
    }

    String key;

    UserBankDataModel userBankDataModel;


    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public UserBankDataModel getUserBankDataModel()
    {
        return userBankDataModel;
    }

    public void setUserBankDataModel(UserBankDataModel userBankDataModel)
    {
        this.userBankDataModel = userBankDataModel;
    }
}
