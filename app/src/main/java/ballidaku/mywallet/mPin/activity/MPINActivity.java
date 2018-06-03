package ballidaku.mywallet.mPin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import ballidaku.mywallet.R;
import ballidaku.mywallet.mPin.IndicatorDots;
import ballidaku.mywallet.mPin.PinLockListener;
import ballidaku.mywallet.mPin.PinLockView;
import ballidaku.mywallet.mainScreens.activities.MainActivity;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.commonClasses.MySharedPreference;


public class MPINActivity extends AppCompatActivity
{

    public static final String TAG = "PinLockView";

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;

    String TYPE = "";

    TextView profile_name;

    String firstTime = "";
    String resetTime = "";

    Context context;

    String savedMPIN="";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                  WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_mpin);

        context = this;

        if(getIntent().getStringExtra(MyConstant.MPIN).equals(MyConstant.EMPTY))
        {
            TYPE = MyConstant.FIRST_TIME;
        }
        else if(getIntent().getStringExtra(MyConstant.MPIN).equals(MyConstant.CHECK_MPIN))
        {
            TYPE = MyConstant.CHECK_MPIN;
            savedMPIN=MySharedPreference.getInstance().getMPIN(context);
        }
        else if(getIntent().getStringExtra(MyConstant.MPIN).equals(MyConstant.CHANGE_MPIN))
        {
            TYPE = MyConstant.CHANGE_MPIN;
            savedMPIN=MySharedPreference.getInstance().getMPIN(context);
        }

        profile_name = (TextView) findViewById(R.id.profile_name);

        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        //mPinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        //mPinLockView.enableLayoutShuffling();

        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));

        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);


        setHeading();
    }

    void setHeading()
    {
        switch (TYPE)
        {

            case MyConstant.FIRST_TIME:
            case MyConstant.NEW_PIN_AFTER_CHANGE:
                profile_name.setText("Enter your new MPIN");
                break;

            case MyConstant.CONFIRM_FIRST_TIME:
            case MyConstant.CONFIRM_PIN_AFTER_CHANGE:
                profile_name.setText("Confirm your new MPIN");
                break;

            case MyConstant.CHECK_MPIN:
                profile_name.setText("Enter your MPIN");
                break;

            case MyConstant.CHANGE_MPIN:
                profile_name.setText("Enter your current MPIN");
                break;
        }
    }


    void setReset()
    {
        mPinLockView.resetPinLockView();

        switch (TYPE)
        {
            case MyConstant.FIRST_TIME:
            case MyConstant.CONFIRM_FIRST_TIME:

                firstTime = "";
                TYPE = MyConstant.FIRST_TIME;
                setHeading();
                break;

            case MyConstant.CHECK_MPIN:

                setHeading();

                break;


            case MyConstant.CHANGE_MPIN:

                setHeading();

                break;


            case MyConstant.NEW_PIN_AFTER_CHANGE:
            case MyConstant.CONFIRM_PIN_AFTER_CHANGE:

                resetTime="";
                TYPE = MyConstant.NEW_PIN_AFTER_CHANGE;
                setHeading();

                break;

        }
    }


    private PinLockListener mPinLockListener = new PinLockListener()
    {
        @Override
        public void onComplete(String pin)
        {
            Log.e(TAG, "Pin complete: " + pin);

            if (TYPE.equals(MyConstant.FIRST_TIME))
            {
                firstTime = pin;
                mPinLockView.resetPinLockView();
                TYPE = MyConstant.CONFIRM_FIRST_TIME;
                setHeading();
            }
            else if (TYPE.equals(MyConstant.CONFIRM_FIRST_TIME) && firstTime.equals(pin))
            {
                Toast.makeText(context, "MPIN saved successfully", Toast.LENGTH_SHORT).show();
                MySharedPreference.getInstance().saveMPIN(context,pin);

                goToMainActivity();


               /* TYPE = MyConstant.CHECK_MPIN;
                setReset();*/
            }
            else if (TYPE.equals(MyConstant.CONFIRM_FIRST_TIME) && !firstTime.equals(pin))
            {
                Toast.makeText(context, "Password didn't match", Toast.LENGTH_SHORT).show();

            }
            else if (TYPE.equals(MyConstant.CHECK_MPIN) && savedMPIN.equals(pin))
            {
                //Toast.makeText(context, "Successfully Loged IN", Toast.LENGTH_SHORT).show();

               /* TYPE = MyConstant.CHAMGE_PIN;
                setReset();*/

               goToMainActivity();

            }
            else if (TYPE.equals(MyConstant.CHECK_MPIN) && !savedMPIN.equals(pin))
            {
                Toast.makeText(context, "Password didn't match", Toast.LENGTH_SHORT).show();

            }
            else if (TYPE.equals(MyConstant.CHANGE_MPIN) && savedMPIN.equals(pin))
            {
                TYPE = MyConstant.NEW_PIN_AFTER_CHANGE;
                setReset();
            }
            else if (TYPE.equals(MyConstant.CHANGE_MPIN) && !savedMPIN.equals(pin))
            {
                Toast.makeText(context, "Your password didn't match with old one", Toast.LENGTH_SHORT).show();
            }
            else if (TYPE.equals(MyConstant.NEW_PIN_AFTER_CHANGE))
            {

                resetTime = pin;
                TYPE = MyConstant.CONFIRM_PIN_AFTER_CHANGE;
                setHeading();
                mPinLockView.resetPinLockView();
            }
            else if (TYPE.equals(MyConstant.CONFIRM_PIN_AFTER_CHANGE) && resetTime.equals(pin))
            {
                Toast.makeText(context, "MPIN changed successfully", Toast.LENGTH_SHORT).show();
                MySharedPreference.getInstance().saveMPIN(context,pin);
               /* firstTime = pin;
                TYPE = MyConstant.CHECK_MPIN;
                setReset();*/
               finish();

            }
            else if (TYPE.equals(MyConstant.CONFIRM_PIN_AFTER_CHANGE) && !resetTime.equals(pin))
            {
                Toast.makeText(context, "Your password didn't match", Toast.LENGTH_SHORT).show();
            }
        }


       public void goToMainActivity()
        {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            finish();
        }

        @Override
        public void onEmpty()
        {
            Log.e(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin)
        {
            Log.e(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }

        @Override
        public void onReset()
        {
            Log.e("Reset", "Hello");
            setReset();

        }
    };
}
