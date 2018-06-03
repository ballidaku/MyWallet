package ballidaku.mywallet.commonClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import ballidaku.mywallet.R;
import ballidaku.mywallet.mPin.activity.MPINActivity;

/**
 * Created by sharanpalsingh on 05/03/17.
 */
public class MyFirebase
{

    String TAG = MyFirebase.class.getSimpleName();

    public static MyFirebase instance = null;

    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();



    public static MyFirebase getInstance()
    {
        if (instance == null)
        {
            instance = new MyFirebase();
        }

        return instance;
    }


    void dismissDialog()
    {
        if (CommonDialogs.getInstance().dialog.isShowing())
        {
            CommonDialogs.getInstance().dialog.dismiss();
        }
    }


    public void createUser(final Context context, final HashMap<String, Object> map)
    {

        map.put(MyConstant.FCM_TOKEN, MySharedPreference.getInstance().getToken(context));

        final String userID = root.child(MyConstant.USERS).push().getKey();

        root.child(MyConstant.USERS).child(userID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    map.put(MyConstant.USER_ID, userID);

                    //MySharedPreference.getInstance().saveUser(context, map);

                    CommonMethods.getInstance().show_Toast(context, "User created successfully");

                  /*  Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);

                    ((Activity) context).finish();*/

                    openActivity(context, map);
                }
            }
        });
    }

    public void logInUser(final Context context, final String email)
    {


        root.child(MyConstant.USERS).orderByChild(MyConstant.USER_EMAIL).equalTo(email).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    Log.e(TAG, "User Key " + child.getKey());

                    HashMap<String, Object> hashMap = (HashMap<String, Object>) child.getValue();

                    hashMap.put(MyConstant.USER_ID, child.getKey());

                    Log.e(TAG, "User hashMap " + hashMap);

                    openActivity(context, hashMap);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(TAG, "ERROR     " + databaseError.getMessage());
            }
        });
    }


    private void openActivity(Context context, HashMap<String, Object> result)
    {

        dismissDialog();

        MySharedPreference.getInstance().saveUser(context, result);

//        Intent intent = new Intent(context,  MainActivity.class );
        Intent intent = new Intent(context, MPINActivity.class);
        intent.putExtra(MyConstant.MPIN, MyConstant.EMPTY);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        ((Activity) context).finish();
    }


  /*  public void checkUserCredentials(final Context context, final HashMap<String, String> map)
    {
        root.child(MyConstant.USERS).orderByChild(MyConstant.USER_NAME).equalTo(map.get(MyConstant.USER_NAME)).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                boolean isChildThere = false;
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    isChildThere = true;
//                    Log.e("User key", child.getKey());
//                    Log.d("User ref", child.getRef().toString());
                    Log.e("User val", child.getValue().toString());

                    UserDataModel userDataModel = child.getValue(UserDataModel.class);

                    //Log.e("User ID", child.getKey());
                    //Log.e("User Name", userDataModel.getUser_name());
                    Log.e("User Password", myUtil.decrypt(userDataModel.getPassword(), map.get(MyConstant.PASSWORD)));


                    if (myUtil.decrypt(userDataModel.getPassword(), map.get(MyConstant.PASSWORD)).equals(map.get(MyConstant.PASSWORD)))
                    {
                        MySharedPreference.getInstance().saveUser(context, child.getKey(), userDataModel.getUser_name(), map.get(MyConstant.PASSWORD));

                        CommonMethods.show_Toast(context, "Login successfull");
//
                        context.startActivity(new Intent(context, MainActivity.class));

                        ((Activity) context).finish();
                    }
                    else
                    {
                        CommonMethods.show_Toast(context, "Password did not match");
                    }


                }

                if (!isChildThere)
                {
                    CommonMethods.show_Toast(context, "User Name does not exists");
                }
                Log.e(TAG, "CHILD there     " + isChildThere);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(TAG, "ERROR     " + databaseError.getMessage());
            }
        });
    }*/


    public void createAccountType(Context context, final HashMap<String, Object> map)
    {

        String userID = MySharedPreference.getInstance().getUserID(context);

        root.child(MyConstant.DETAILS).child(userID).child(MyConstant.BANK_DETAILS).push().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {

//                    MySharedPreference.getInstance().saveUser(context, userID, result.get(MyConstant.USER_NAME).toString());
//
//                    CommonMethods.show_Toast(context, "User created successfully");
//
//                    Intent intent=new Intent(context, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    context.startActivity(intent);
//
//                    ((Activity) context).finish();
                }
            }
        });
    }


    public void createBankDetails(Context context, final HashMap<String, Object> map)
    {

        String userID = MySharedPreference.getInstance().getUserID(context);

        root.child(MyConstant.DETAILS).child(userID).child(MyConstant.BANK_DETAILS).push().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {

//                    MySharedPreference.getInstance().saveUser(context, userID, result.get(MyConstant.USER_NAME).toString());
//
//                    CommonMethods.show_Toast(context, "User created successfully");
//
//                    Intent intent=new Intent(context, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    context.startActivity(intent);
//
//                    ((Activity) context).finish();
                }
            }
        });
    }

    public void updateBankDetails(final Context context, String itemID, final HashMap<String, Object> map, final MyInterfaces.UpdateDetails updateDetails)
    {

        String userID = MySharedPreference.getInstance().getUserID(context);

        root.child(MyConstant.DETAILS).child(userID).child(MyConstant.BANK_DETAILS).child(itemID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    updateDetails.onSuccess();

                }
            }
        });
    }

    public Task<Void> deleteBankDetails(Context context, String key)
    {

        String userID = MySharedPreference.getInstance().getUserID(context);

        return root.child(MyConstant.DETAILS).child(userID).child(MyConstant.BANK_DETAILS).child(key).removeValue();
    }


    // To get User account details
    public Query getMyAccountDetails(Context context)
    {
        return root.child(MyConstant.DETAILS).child(MySharedPreference.getInstance().getUserID(context));
    }


}
