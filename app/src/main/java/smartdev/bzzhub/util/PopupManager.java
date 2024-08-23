package smartdev.bzzhub.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import smartdev.bzzhub.R;

import java.util.Objects;

public class PopupManager {

    public static void showLogoutDialog(Context context) {
        Dialog dialog = new Dialog(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        final View view = layoutInflater.inflate(R.layout.dialog_email_link_sent, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(v -> {
            dialog.dismiss();
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setCancelable(false);
        dialog.show();
    }
}
