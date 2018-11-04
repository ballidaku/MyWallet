package ballidaku.mywallet.frontScreens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import ballidaku.mywallet.R;
import ballidaku.mywallet.mPin.activity.MPINActivity;
import ballidaku.mywallet.commonClasses.MyConstant;
import ballidaku.mywallet.commonClasses.MySharedPreference;

public class Splash extends AppCompatActivity
{

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;

        countDownTimer.start();
    }

    CountDownTimer countDownTimer = new CountDownTimer(2000, 1000)
    {

        public void onTick(long millisUntilFinished)
        {

        }

        public void onFinish()
        {
            if (MySharedPreference.getInstance().getUserID(context).isEmpty())
            {
                Intent intent = new Intent(context, LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
            else
            {
                String mpin = MySharedPreference.getInstance().getMPIN(context);

                Intent intent = new Intent(context, MPINActivity.class);
                intent.putExtra(MyConstant.MPIN, mpin.isEmpty() ? MyConstant.EMPTY : MyConstant.CHECK_MPIN);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        }
    };
}
