package chenanze.com.slidingupanimationview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by duian on 2016/11/3.
 */

public class ScreenUtils {

    public static int getScreenHeight(View view) {
        DisplayMetrics dm = view.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int getScreenWidth(View view) {
        DisplayMetrics dm = view.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getWindowsHeight(View view) {
        Rect windowsRect = getWindowsRect(view);
        return windowsRect.bottom - windowsRect.top - getNavigationBarHeight(view);
    }

    public static int getWindowsWidth(View view) {
        Rect windowsRect = getWindowsRect(view);
        return windowsRect.right - windowsRect.left;
    }

    public static int getStatusBarHeight(View view) {
        return (int)Math.ceil(18.5 * view.getResources().getDisplayMetrics().density);
    }

    public static int getStatusBarHeight(Context context) {
        return (int)Math.ceil(25 * context.getResources().getDisplayMetrics().density);
    }

    public static int getNavigationBarHeight(View view) {
        int navigationBarHight = 0;
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
        if (!hasHomeKey) { //判断没有HOME键时存在导航栏
            Resources resources = view.getResources();
            navigationBarHight = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
        }
        return navigationBarHight;
    }

    private static Rect getWindowsRect(View view) {
        Rect windowRect = new Rect();
        view.getWindowVisibleDisplayFrame(windowRect);
        return windowRect;
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
