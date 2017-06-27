package cn.live9666.cowboy;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.vs_input)
    private ViewSwitcher vsInput;

    @ViewInject(R.id.acet_login_username)
    private AppCompatEditText etLoginUsername;

    @ViewInject(R.id.acet_login_password)
    private AppCompatEditText etLoginPassword;

    @ViewInject(R.id.acet_register_username)
    private AppCompatEditText etRegisterUsername;

    @ViewInject(R.id.acet_register_password)
    private AppCompatEditText etRegisterPassword;

    @ViewInject(R.id.acet_register_password_confirm)
    private AppCompatEditText etRegisterPwdConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Event(R.id.iv_back)
    private void back(View view) {

        onBackPressed();
    }

    @Event(R.id.tv_switch_view)
    private void loginSwitch(View view) {

        TextView tv = (TextView) view;
        tv.setAnimation(AnimationUtils.loadAnimation(this, R.anim.alpha_0_1));
        boolean isLogin = tv.getText()
                .equals(getResources().getString(R.string.login_register));
        if (isLogin) {

            vsInput.setDisplayedChild(1);
            tv.setText(getResources().getString(R.string.login_login));
        } else {

            vsInput.setDisplayedChild(0);
            tv.setText(getResources().getString(R.string.login_register));
        }

    }

    @Event(R.id.btn_login)
    private void login(View view) {

        String sUsername = etLoginUsername.getText().toString().trim();
        if(sUsername.isEmpty()) {

            etLoginUsername.setError("Username不能为空！");
            return;
        }

        String sPassword = etLoginPassword.getText().toString().trim();
        if(sPassword.isEmpty()) {

            etLoginPassword.setError("Password不能为空！");
            return;
        }

        Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Event(R.id.btn_register)
    private void register(View view) {

        String sUsername = etRegisterUsername.getText().toString().trim();
        if(sUsername.isEmpty()) {

            etRegisterUsername.setError("Username不能为空！");
            return;
        }

        String sPassword = etRegisterPassword.getText().toString().trim();
        if(sPassword.isEmpty()) {

            etRegisterPassword.setError("Password不能为空！");
            return;
        }

        String sPwdConfirm = etRegisterPwdConfirm.getText().toString().trim();
        if(sPwdConfirm.isEmpty()) {

            etRegisterPwdConfirm.setError("请再次输入密码");
            return;
        }

        if(!sPassword.equals(sPwdConfirm)) {

            etRegisterPwdConfirm.setError("两次密码输入不一致！");
            return;
        }

        Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
        this.finish();
    }


}
