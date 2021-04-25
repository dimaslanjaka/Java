package android;

import android.content.Context;

/**
 * Helper class to make Toasts with.
 */
public class Toast {

    /**
     * Creates a Toast.
     *
     * @param context The context.
     * @param isLong  Whether the Toast should have a Toast.LENGTH_LONG or Toast.LENGTH_SHORT.
     * @param text    The text to display in the Toast.
     */
    public static void make(Context context, boolean isLong, String text) {
        int duration = isLong ? android.widget.Toast.LENGTH_LONG : android.widget.Toast.LENGTH_SHORT;
        android.widget.Toast.makeText(context, text, duration).show();
    }

    /**
     * Creates a Toast with duration = Toast.LENGTH_LONG.
     *
     * @param context The context.
     * @param text    The text to display in the Toast.
     */
    public static void make(Context context, String text) {
        make(context, true, text);
    }
}
