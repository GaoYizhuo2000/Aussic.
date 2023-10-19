package au.edu.anu.Aussic.controller.Runtime.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author: u7516507, Evan
 * A utility class providing a collection of static functions for various purposes.
 */

public class Functions {

    /**
     * Constructs a URL by replacing placeholders {w} and {h} with given width and height values.
     *
     * @param width  The width to replace the {w} placeholder.
     * @param height The height to replace the {h} placeholder.
     * @param rawUrl The raw URL containing placeholders to be replaced.
     * @return A URL string with the placeholders replaced by the provided width and height.
     */
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

    /**
     * Cut the provided text to a maximum length of 18 characters.
     * If the text is truncated, "..." is appended at the end.
     *
     * @param text The original text.
     * @return A string that is either the original text or a truncated version of it.
     */
    public static String adjustLength(String text){
        if(text.length() > 18) return text.substring(0, 17) + "...";
        else return text;
    }

    /**
     * Hides the on-screen keyboard for a given activity.
     *
     * @param activity The activity in which to hide the keyboard.
     */
    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
