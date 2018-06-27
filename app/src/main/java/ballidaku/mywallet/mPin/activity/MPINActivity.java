package ballidaku.mywallet.mPin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ballidaku.mywallet.R;
import ballidaku.mywallet.commonClasses.CommonMethods;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.commonClasses.MySharedPreference;
import ballidaku.mywallet.mPin.IndicatorDots;
import ballidaku.mywallet.mPin.PinLockListener;
import ballidaku.mywallet.mPin.PinLockView;
import ballidaku.mywallet.mainScreens.activities.MainActivity;


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

    String savedMPIN = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpin);

        context = this;

        if (getIntent().getStringExtra(MyConstant.MPIN).equals(MyConstant.EMPTY))
        {
            TYPE = MyConstant.FIRST_TIME;
        }
        else if (getIntent().getStringExtra(MyConstant.MPIN).equals(MyConstant.CHECK_MPIN))
        {
            TYPE = MyConstant.CHECK_MPIN;
            savedMPIN = MySharedPreference.getInstance().getMPIN(context);
        }
        else if (getIntent().getStringExtra(MyConstant.MPIN).equals(MyConstant.CHANGE_MPIN))
        {
            TYPE = MyConstant.CHANGE_MPIN;
            savedMPIN = MySharedPreference.getInstance().getMPIN(context);
        }

        profile_name = findViewById(R.id.profile_name);
        mPinLockView = findViewById(R.id.pin_lock_view);
        mIndicatorDots = findViewById(R.id.indicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);

        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));

        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FIXED);


        setHeading();
    }

    void setHeading()
    {
        switch (TYPE)
        {

            case MyConstant.FIRST_TIME:
            case MyConstant.NEW_PIN_AFTER_CHANGE:
                profile_name.setText(getString(R.string.enter_new_passcode));
                break;

            case MyConstant.CONFIRM_FIRST_TIME:
            case MyConstant.CONFIRM_PIN_AFTER_CHANGE:
                profile_name.setText(getString(R.string.confirm_new_passcode));
                break;

            case MyConstant.CHECK_MPIN:
                profile_name.setText(getString(R.string.enter_passcode));
                break;

            case MyConstant.CHANGE_MPIN:
                profile_name.setText(R.string.enter_current_passcode);
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

                resetTime = "";
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
            if (TYPE.equals(MyConstant.FIRST_TIME))
            {
                firstTime = pin;
                mPinLockView.resetPinLockView();
                TYPE = MyConstant.CONFIRM_FIRST_TIME;
                setHeading();
            }
            else if (TYPE.equals(MyConstant.CONFIRM_FIRST_TIME) && firstTime.equals(pin))
            {
                CommonMethods.getInstance().showToast(context, getString(R.string.passcode_saved));
                MySharedPreference.getInstance().saveMPIN(context, pin);
                goToMainActivity();
            }
            else if (TYPE.equals(MyConstant.CONFIRM_FIRST_TIME) && !firstTime.equals(pin))
            {
                CommonMethods.getInstance().showToast(context, getString(R.string.passcode_mismatch));
            }
            else if (TYPE.equals(MyConstant.CHECK_MPIN) && savedMPIN.equals(pin))
            {
                goToMainActivity();
            }
            else if (TYPE.equals(MyConstant.CHECK_MPIN) && !savedMPIN.equals(pin))
            {
                CommonMethods.getInstance().showToast(context, getString(R.string.passcode_mismatch));
            }
            else if (TYPE.equals(MyConstant.CHANGE_MPIN) && savedMPIN.equals(pin))
            {
                TYPE = MyConstant.NEW_PIN_AFTER_CHANGE;
                setReset();
            }
            else if (TYPE.equals(MyConstant.CHANGE_MPIN) && !savedMPIN.equals(pin))
            {
                CommonMethods.getInstance().showToast(context, getString(R.string.passcode_mismatch_with_old));
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
                CommonMethods.getInstance().showToast(context, getString(R.string.passcode_changed));
                MySharedPreference.getInstance().saveMPIN(context, pin);
                finish();
            }
            else if (TYPE.equals(MyConstant.CONFIRM_PIN_AFTER_CHANGE) && !resetTime.equals(pin))
            {
                CommonMethods.getInstance().showToast(context, getString(R.string.passcode_mismatch));
            }
        }


        void goToMainActivity()
        {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            finish();
        }

        @Override
        public void onEmpty()
        {
            //Log.e(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin)
        {
            //Log.e(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }

        @Override
        public void onReset()
        {
            //Log.e("Reset", "Hello");
            setReset();

        }
    };
}
