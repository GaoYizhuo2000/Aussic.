package au.edu.anu.Aussic.controller.Runtime.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author: u7516507, Evan Cheung
 */

public class Functions {
    public static String makeImageUrl(int width, int height, String rawUrl){
        StringBuilder out = new StringBuilder();
        out.append(rawUrl);
        int wIndex = out.indexOf("{w}");
        if (wIndex != -1) {
            out.replace(wIndex, wIndex + 3, String.valueOf(width));
        }

        int hIndex = out.indexOf("{h}");
        if (hIndex != -1) {
            out.replace(hIndex, hIndex + 3, String.valueOf(height));
        }

        return out.toString();
    }

    public static String adjustLength(String text){
        if(text.length() > 18) return text.substring(0, 17) + "...";
        else return text;
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
