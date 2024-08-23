package smartdev.bzzhub.ui.base;

import android.content.Context;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.util.KeyboardUtils;


public class BaseFragment extends Fragment {

    public AppCompatActivity currentActivity;
    public boolean isVisibleToUser = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        currentActivity = BzzApp.getCurrentActivity();
    }

    public void hideKeyboard(){
        KeyboardUtils.hideKeyboard(currentActivity);
    }

    public void openKeyboard(EditText editText){
        KeyboardUtils.openKeyboard(currentActivity,editText);
                }

public void onError(Throwable throwable) {
        try {
        if (currentActivity != null && !currentActivity.isFinishing()) {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity,R.style.DefaultAlertButtonStyle);
        builder.setTitle(R.string.str_error)
        .setMessage(throwable.getMessage())
        .setPositiveButton("Ok", (dialogInterface, i) -> {
        }).create().show();
        throwable.printStackTrace();
        }
        }catch (Exception e){
        e.printStackTrace();
        }
        }

@Override
public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        }

        }
