package utils;

import android.app.Activity;

import cn.live9666.cowboy.R;

/**
 * Created by Administrator on 2016/11/13.
 */

public class ThemeUtil {

    public static void changeTheme(Activity activity) {

        if(SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.SP_DARK_MODE, false))
            activity.setTheme(R.style.AppThemeDark);
    }
}
