package ballidaku.mywallet.commonClasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

import ballidaku.mywallet.BuildConfig;
import ballidaku.mywallet.R;
import ballidaku.mywallet.mainScreens.activities.MainActivity;
import ballidaku.mywallet.mainScreens.activities.ShowBankDetails;
import ballidaku.mywallet.mainScreens.activities.ShowOtherDetail;
import ballidaku.mywallet.roomDatabase.ExecuteQueryAsyncTask;
import ballidaku.mywallet.roomDatabase.OnResultInterface;
import ballidaku.mywallet.roomDatabase.dataModel.AccountDetailsDataModel;
import ballidaku.mywallet.roomDatabase.dataModel.OtherDetailsDataModel;

/**
 * Created by sharanpalsingh on 05/03/17.
 */
public class CommonMethods<D>
{
    private String TAG = "CommonMethods";

    private static Toast toast;
    private static Snackbar snackbar;

    private static CommonMethods instance = new CommonMethods();

    public static CommonMethods getInstance()
    {
        return instance;
    }


    public void showToast(Context context, String text)
    {
        if (toast != null)
        {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);

        toast.show();
    }


    /*TO SHOW SNACKBAR*/

    public void showSnackbar(View view, Context context, String message)
    {

        if (snackbar != null)
        {
            snackbar.dismiss();
        }

        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        TextView tv = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        snackbar.show();

    }


   /* public void switchfragment(Fragment fromWhere, Fragment toWhere)
    {
        FragmentTransaction fragmentTransaction = fromWhere.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, toWhere);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }*/

    public void switchfragment(Context context, Fragment toWhere, boolean willStoreInStack)
    {

        FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, toWhere);
        if (willStoreInStack)
        {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

    }

    public void hideKeypad(Activity activity)
    {
        View view = activity.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Converting dp to pixel
     */
    public int dpToPx(Context context, int dp)
    {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public String setSpaceFormat(String value)
    {
        value = value.replaceAll(" ", "");
        DecimalFormat df;

        if (value.length() == 19)
        {
            df = new DecimalFormat("####,####,####,####,###");
        }
        else
        {
            df = new DecimalFormat("####,####,####,####,####,####");
        }

        return value.trim().isEmpty() ? "" : df.format(Long.parseLong(value)).replaceAll(",", " ");

    }

    private String encrypt(Context context, String message)
    {
        return encrypt(message, MySharedPreference.getInstance().getUserEmail(context));
    }

    private String encrypt(String message, String password)
    {
        String encryptedMsg = "";
        try
        {
            encryptedMsg = AESCrypt.encrypt(password, message);
        }
        catch (GeneralSecurityException e)
        {
            //handle error
        }

        return encryptedMsg;
    }


    private String decrypt(Context context, String message)
    {
        return decrypt(message, MySharedPreference.getInstance().getUserEmail(context));
    }

    private String decrypt(String encryptedMsg, String password)
    {
        String messageAfterDecrypt = "";

        try
        {
            messageAfterDecrypt = AESCrypt.decrypt(password, encryptedMsg);
        }
        catch (GeneralSecurityException e)
        {
            //handle error - could be due to incorrect password or tampered encryptedMsg
        }

        return messageAfterDecrypt;
    }

    public boolean isValidEmail(CharSequence target)
    {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean isValidMobile(String phone)
    {
        boolean check = false;
        check = !Pattern.matches("[a-zA-Z]+", phone) && phone.length() >= 6;
        return check;
    }


    /**********************************************************************************************/
    /*Copy Content*/

    /**********************************************************************************************/
    public void copyContent(Context context, String data)
    {

        if (!data.isEmpty())
        {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip2 = ClipData.newPlainText(context.getString(R.string.copied_text), data);
            clipboard.setPrimaryClip(clip2);

            CommonMethods.getInstance().showToast(context, context.getString(R.string.data_copied));
        }
        else
        {
            CommonMethods.getInstance().showToast(context, context.getString(R.string.select_atleast_single));
        }
    }

    /**********************************************************************************************/
    /*Share Content*/

    /**********************************************************************************************/
    public void shareContent(Context context, String data)
    {

        if (!data.isEmpty())
        {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.account_details));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, data);
            context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.share_using)));
        }
        else
        {
            CommonMethods.getInstance().showToast(context, context.getString(R.string.select_atleast_single));
        }
    }


    /**********************************************************************************************/
    /*Copy Content*/

    /**********************************************************************************************/
    public class MyTouchListener implements View.OnTouchListener
    {
        EditText editText;
        Context context;

        public MyTouchListener(Context context, EditText editText)
        {
            this.editText = editText;
            this.context = context;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                if (!editText.getText().toString().trim().isEmpty())
                {

                    Drawable left = editText.getCompoundDrawables()[DRAWABLE_LEFT];
                    Drawable right;

                    Drawable alreadyRight = editText.getCompoundDrawables()[DRAWABLE_RIGHT];
                    Drawable unSelected = context.getResources().getDrawable(R.drawable.ic_check_unselected);

                    right = unSelected.getConstantState().equals(alreadyRight.getConstantState()) ? context.getResources().getDrawable(R.drawable.ic_check_selected) : unSelected;

                    String[] type = ((String) editText.getTag()).split(MyConstant.SEPRATER);

                    boolean isPasscodeVerified = false;
                    if (context instanceof ShowBankDetails)
                    {
                        isPasscodeVerified = ShowBankDetails.isPasscodeVerified;
                    }
                    else if (context instanceof ShowOtherDetail)
                    {
                        isPasscodeVerified = ShowOtherDetail.isPasscodeVerified;
                    }

                    if (type.length == 2 && type[1].equals(MyConstant.SECRET) && !isPasscodeVerified)
                    {
                        CommonDialogs.getInstance().showPasscodeDialog(context, new CommonInterfaces.checkPasscode()
                        {
                            @Override
                            public void onSuccess()
                            {
                                editText.setCompoundDrawablesWithIntrinsicBounds(left, null, right, null);
                                if (context instanceof ShowBankDetails)
                                {
                                    ShowBankDetails.isPasscodeVerified = true;
                                }
                                else if (context instanceof ShowOtherDetail)
                                {
                                    ShowOtherDetail.isPasscodeVerified = true;
                                }
                            }

                            @Override
                            public void onFailure()
                            {
                                CommonMethods.getInstance().showToast(context, context.getString(R.string.passcode_mismatch));
                            }
                        });
                    }
                    else
                    {
                        editText.setCompoundDrawablesWithIntrinsicBounds(left, null, right, null);
                    }


                    return true;
                }
            }
            return false;
        }
    }


    /**********************************************************************************************/
    /*Content to share and copy*/

    /**********************************************************************************************/
    public String getData(Context context, ArrayList<EditText> editTextArrayList)
    {
        StringBuilder copiedContent = new StringBuilder();
        for (int i = 0; i < editTextArrayList.size(); i++)
        {
            if (editTextArrayList.get(i).getCompoundDrawables()[2].getConstantState().equals(context.getResources().getDrawable(R.drawable.ic_check_selected).getConstantState()))
            {
                String[] tags = ((String) editTextArrayList.get(i).getTag()).split(MyConstant.SEPRATER);
                String content = tags[0] + MyConstant.SPACE + editTextArrayList.get(i).getText().toString();
                copiedContent.append((copiedContent.length() == 0) ? content : "\n" + content);
            }
        }

        //for copying single item
        if ((copiedContent.length() > 0) && !copiedContent.toString().contains("\n"))
        {
            String[] singleContent = copiedContent.toString().split(":");
//            copiedContent = singleContent[1].replaceAll(" ", "");
            copiedContent = new StringBuilder(singleContent[1]);
        }

        return copiedContent.toString().trim();
    }

    /**********************************************************************************************/
    /*Get all database in json format*/

    /**********************************************************************************************/
    public void getAllDatabaseData(final Context context, String fromWhere, Uri uri)
    {
        final Gson gson = new Gson();
        new ExecuteQueryAsyncTask<>(context, new AccountDetailsDataModel(), MyConstant.GET_ALL, (OnResultInterface<D>) data ->
        {

            final String bankDetails = gson.toJson(data);

            new ExecuteQueryAsyncTask<>(context, new OtherDetailsDataModel(), MyConstant.GET_ALL, (OnResultInterface<D>) data1 ->
            {
                String otherDetails = gson.toJson(data1);
                String concatData = bankDetails + MyConstant.SEPRATER + otherDetails;

                if (fromWhere.equalsIgnoreCase(MyConstant.EXPORT_TO_OTHER_APPS))
                {
                    writeToFile(context, concatData);
                }
                else if (fromWhere.equalsIgnoreCase(MyConstant.EXPORT_TO_EXTERNAL_STORAGE))
                {
                    writeDataToPredefinedPath(context, concatData, uri);
                }
            });
        });

    }

    /**********************************************************************************************/
    /*Get data file name*/

    /**********************************************************************************************/

    public String getDataFileName(Context context)
    {
        return context.getString(R.string.app_name) + ".txt";
    }

    /**********************************************************************************************/
    /*Write json data to .txt file and store in files in internal storage in files*/

    /**********************************************************************************************/
    private void writeToFile(Context context, String data)
    {

        try
        {
            String encyptedData = CommonMethods.getInstance().encrypt(context, data);
            String fileName = getDataFileName(context);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(encyptedData);
            outputStreamWriter.close();

            File directory = context.getFilesDir();
            File file = new File(directory, fileName);
//            Log.e(TAG, "" + file.getAbsolutePath());
            shareFileToOtherDrive(context, file);

        }
        catch (IOException e)
        {
            Log.e(TAG, "Exception File write failed: " + e.toString());
        }
    }

    /**********************************************************************************************/
    /*Write  data to predefined .txt file */

    /**********************************************************************************************/
    private void writeDataToPredefinedPath(Context context, String concatData, Uri uri)
    {
        String encyptedData = CommonMethods.getInstance().encrypt(context, concatData);
        try
        {
            //String path = getRealPathFromURI(context, uri);
            String path = PathUtil.getPath(context, uri);

            FileOutputStream fos = new FileOutputStream(path);
            fos.write(encyptedData.getBytes());
            fos.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (URISyntaxException ignored)
        {
        }
    }


    /**********************************************************************************************/
    /*Read json data from external file*/

    /**********************************************************************************************/

    public void readDataFromExternalFile(Context context, Uri uri)
    {
        String myData = "";
        try
        {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            assert inputStream != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String strLine;
            while ((strLine = br.readLine()) != null)
            {
                myData = myData + strLine;
            }
            inputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        String decryptedData = CommonMethods.getInstance().decrypt(context, myData);
//        Log.e(TAG, "decryptedData : " + decryptedData);
        if (!decryptedData.isEmpty())
        {
            saveStringDataToDatabase(context, decryptedData);
        }
        else
        {
            CommonDialogs.getInstance().showMessageDialog(context, context.getString(R.string.not_relevant_data));
        }
    }

    /**********************************************************************************************/
    /*Save data to database*/

    /**********************************************************************************************/
    private int dataCopiedSize = 0;

    private void saveStringDataToDatabase(final Context context, String data)
    {

        /*Delete all account details*/
        AccountDetailsDataModel accountDetailsDataModelEmpty = new AccountDetailsDataModel();
        new ExecuteQueryAsyncTask<>(context, accountDetailsDataModelEmpty, MyConstant.DELETE_ALL, (OnResultInterface<D>) data1 ->
        {
            //Log.e(TAG, "Delete all account details : " + data);
        });


        /*Delete all other details*/
        OtherDetailsDataModel otherDetailsValueDataModel = new OtherDetailsDataModel();
        new ExecuteQueryAsyncTask<>(context, otherDetailsValueDataModel, MyConstant.DELETE_ALL, (OnResultInterface<D>) data12 ->
        {
            //Log.e(TAG, "Delete all other details : " + data);
        });


        //Log.e(TAG, "DATA FROM FILE : " + data);
        String[] dataArray = data.split(MyConstant.SEPRATER);

        String accountDetails = dataArray[0];
        String otherDetails = dataArray[1];

        JsonParser jsonParser = new JsonParser();
        Gson gson = new Gson();

        JsonElement jsonElement = jsonParser.parse(accountDetails);
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        JsonElement jsonElement2 = jsonParser.parse(otherDetails);
        JsonArray jsonArray2 = jsonElement2.getAsJsonArray();

        final int totalSize = jsonArray.size() + jsonArray2.size();
        dataCopiedSize = 0;

        for (int i = 0; i < jsonArray.size(); i++)
        {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

            AccountDetailsDataModel accountDetailsDataModel = gson.fromJson(jsonObject, AccountDetailsDataModel.class);
//            accountDetailsDataModel.setUserId(MySharedPreference.getInstance().getUserID(context));
            accountDetailsDataModel.setId(0);
            new ExecuteQueryAsyncTask<>(context, accountDetailsDataModel, MyConstant.INSERT, (OnResultInterface<D>) data14 ->
            {
                dataCopiedSize++;
                if (dataCopiedSize == totalSize)
                {
                    CommonMethods.getInstance().showToast(context, context.getString(R.string.data_copied_successfully));
                }
            });
        }

        for (int i = 0; i < jsonArray2.size(); i++)
        {
            JsonObject jsonObject = jsonArray2.get(i).getAsJsonObject();
            OtherDetailsDataModel otherDetailsDataModel = gson.fromJson(jsonObject, OtherDetailsDataModel.class);
//            otherDetailsDataModel.setUserId(MySharedPreference.getInstance().getUserID(context));
            otherDetailsDataModel.setId(0);
            new ExecuteQueryAsyncTask<>(context, otherDetailsDataModel, MyConstant.INSERT, (OnResultInterface<D>) data13 ->
            {
                dataCopiedSize++;
                if (dataCopiedSize == totalSize)
                {
                    CommonMethods.getInstance().showToast(context, context.getString(R.string.data_copied_successfully));
                }
            });
        }

    }

    /**********************************************************************************************/
    /*Share file to other apps*/

    /**********************************************************************************************/
    private void shareFileToOtherDrive(Context context, File path)
    {
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        if (path.exists())
        {
            intentShareFile.setType("text/plain");
            Uri fileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, path);
            intentShareFile.putExtra(Intent.EXTRA_STREAM, fileUri);
            intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
    }

    /**********************************************************************************************/
    /*Share file to Storage access framework*/

    /**********************************************************************************************/

    public void shareFileToLocalStorage(Context context)
    {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, getDataFileName(context));
        ((MainActivity) context).startActivityForResult(intent, MyConstant.REQUEST_CODE_OPEN_DIRECTORY);
    }
    /**********************************************************************************************/
    /*Get file from Device*/

    /**********************************************************************************************/
    public void getFileFromDevice(Context context)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");
        ((MainActivity) context).startActivityForResult(intent, MyConstant.PICKFILE_REQUEST_CODE);
    }

    /**********************************************************************************************/
    /*Get file extenstion*/

    /**********************************************************************************************/
    public String getMimeType(Context context, Uri uri)
    {
        String extension;
        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT))
        {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        }
        else
        {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }
        return extension;
    }
}
