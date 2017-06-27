package utils;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.squareup.picasso.Picasso;

/**
 * Created by zpf on 2016/11/1.
 */
public class CowboyApplication extends Application {

    private static Context mContext;

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
