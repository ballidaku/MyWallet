package ballidaku.mywallet.services;

/**
 * Created by sharanpalsingh on 05/03/17.
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import ballidaku.mywallet.commonClasses.MySharedPreference;


/**
 Created by sharan on 7/6/16. */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.e(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);

    }



    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server


        MySharedPreference.getInstance().saveToken(getApplicationContext(),token);

        /*OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("Token", token).build();

        Request request = new Request.Builder().url("http://dgc.a2hosted.com/android/Register.php").post(body).build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }
}