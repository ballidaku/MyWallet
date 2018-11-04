package ballidaku.mywallet.commonClasses;

/**
 * Created by sharanpalsingh on 22/12/17.
 */

public interface CommonInterfaces
{
    interface deleteDetail
    {
        void onDelete();
    }

    interface importData
    {
        void onImportConfirmation();
    }

    interface forgotPassword
    {
        void onSend(String email);
    }

    interface checkPasscode
    {
        void onSuccess();

        void onFailure();
    }
}
