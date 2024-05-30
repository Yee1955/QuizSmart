package Class;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizsmart.R;

public class ExtensionMethod {
    public static void showCustomToast(String message, Context context) {
        // Inflate the custom toast layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.toast_custom, (ViewGroup) null);

        // Set the text and icon for the custom toast
        TextView text = layout.findViewById(R.id.custom_toast_text);
        text.setText(message);

        // Create and show the custom toast
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static Dialog showCustomDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.loading_custom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCancelable(false);
        dialog.show();
        dialog.findViewById(R.id.LoadingIcon).startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_360));
        return dialog;
    }
}
