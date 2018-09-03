package ballidaku.mywallet.commonClasses;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by sharanpalsingh on 01/04/17.
 */
public class FourDigitsCardTextWatcher implements TextWatcher
{

    private static final char SPACE = ' ';
    private EditText mEditText;

    public FourDigitsCardTextWatcher(EditText editText)
    {
        mEditText = editText;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        mEditText.setError(null);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }





    @Override
    public void afterTextChanged(Editable s)
    {
//        // Remove spacing char
//        if (s.length() > 0 && (s.length() % 5) == 0)
//        {
//            final char c = s.charAt(s.length() - 1);
//            if (SPACE == c)
//            {
//                s.delete(s.length() - 1, s.length());
//            }
//        }
//        // Insert char where needed.
//        if (s.length() > 0 && (s.length() % 5) == 0)
//        {
//            char c = s.charAt(s.length() - 1);
//            // Only if its a digit where there should be a space we insert a space
//            if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(SPACE)).length <= 4)
//            {
//                s.insert(s.length() - 1, String.valueOf(SPACE));
//                Log.e("ABC",""+s.toString());
//            }
//        }

        String initial = s.toString();
        // remove all non-digits characters
        //String processed = initial.replaceAll("\\D", "");
        String processed = initial.replaceAll(" ", "");
        // insert a space after all groups of 4 digits that are followed by another digit
        processed = processed.replaceAll("(\\d{4})(?=\\d)", "$1 ");
        // to avoid stackoverflow errors, check that the processed is different from what's already
        //  there before setting
        if (!initial.equals(processed)) {
            // set the value
            s.replace(0, initial.length(), processed);
        }


    }
}