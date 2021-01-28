package cn.live9666.cowboy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.xutils.x;

import top.wefor.circularanim.CircularAnim;
import utils.ThemeUtil;

/**
 * Created by Administrator on 2016/11/13.
 */

public abstract class BaseActivity extends AppCompatActivity {


    protected Handler handler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            doMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThemeUtil.changeTheme(this);
        x.view().inject(this);
    }

    protected void doMessage(Message message) {


    }

    protected void showToast(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int resId) {

        showToast(getResources().getString(resId));
    }

    protected void openActivity(Intent intent) {

        startActivity(intent);
    }

    protected void openActivity(Class<?> clazz) {

        openActivity(new Intent(this, clazz));
    }

    protected void openActivity(Class<?> clazz, Bundle bundle) {

        Intent intent = new Intent(this, clazz);
        if(bundle != null) {
            intent.putExtras(bundle);
        }
        openActivity(intent);
    }

    protected void openActivityWithAnim(final Class<?> clazz, View view) {

        CircularAnim.fullActivity(this, view)
                .colorOrImageRes(R.color.colorAccent)
                .go(new CircularAnim.OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {

                        openActivity(clazz);
                    }
                });
    }

    protected void openActivityWithAnim(final Class<?> clazz, View view, final Bundle bundle) {

        CircularAnim.fullActivity(this, view)
                .colorOrImageRes(R.color.colorAccent)
                .go(new CircularAnim.OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {

                        openActivity(clazz, bundle);
                    }
                });
    }
}
