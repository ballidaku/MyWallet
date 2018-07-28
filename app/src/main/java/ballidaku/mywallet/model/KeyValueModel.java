package ballidaku.mywallet.model;

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

    UserBankModel userBankDataModel;


    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public UserBankModel getUserBankDataModel()
    {
        return userBankDataModel;
    }

    public void setUserBankDataModel(UserBankModel userBankDataModel)
    {
        this.userBankDataModel = userBankDataModel;
    }
}
