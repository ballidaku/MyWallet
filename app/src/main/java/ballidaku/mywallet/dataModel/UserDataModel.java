package ballidaku.mywallet.dataModel;

/**
 * Created by sharanpalsingh on 26/03/17.
 */
public class UserDataModel
{
    String user_name;
    String password;
    String fcm_token;


    public UserDataModel()
    {

    }

    public UserDataModel(String user_name, String password, String fcm_token)
    {
        this.user_name = user_name;
        this.password = password;
        this.fcm_token = fcm_token;
    }

    public String getUser_name()
    {
        return user_name;
    }

    public String getPassword()
    {
        return password;
    }

    public String getFcm_token()
    {
        return fcm_token;
    }
}
