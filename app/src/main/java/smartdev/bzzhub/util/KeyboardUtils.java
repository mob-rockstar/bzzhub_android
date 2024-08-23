package smartdev.bzzhub.util;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardUtils {
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = (activity).getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null){
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void openKeyboard(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null){
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
