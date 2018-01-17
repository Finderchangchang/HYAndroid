package ff.express.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import ff.express.R;
import ff.express.base.BaseActivity;
import ff.express.listener.LoginListener;
import ff.express.method.Utils;
import ff.express.method.getOrderNum;
import ff.express.view.ILogin;

/**
 * Created by Administrator on 2016/11/26.
 */

public class LoginActivity extends BaseActivity {

    LoginListener mListener;
    public static LoginActivity mIntail;
    EditText telEt;
    EditText pwdEt;
    TextView logintv;

    @Override
    public void initViews() {
        setContentView(R.layout.login);
        mListener = new LoginListener();
        mIntail = this;
        logintv = (TextView) findViewById(R.id.logintv);
        pwdEt = (EditText) findViewById(R.id.pwd_et);
        telEt = (EditText) findViewById(R.id.tel_et);
    }

    @Override
    public void initEvents() {
        logintv.setOnClickListener(v -> {
            if (TextUtils.isEmpty(pwdEt.getText().toString().trim()) || ("").equals(telEt.getText().toString().trim())) {
                ToastShort("密码不能为空或密码不能为空");
            } else {
                mListener.login(telEt.getText().toString().trim(), pwdEt.getText().toString().trim(), h, this);
            }
        });
    }

    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONArray jarray = new JSONArray(msg.obj.toString());
                JSONObject object = jarray.optJSONObject(0);
                String result = object.getString("state");
                if (result.equals("1")) {
                    JSONArray array = new JSONArray(object.getString("data"));
                    JSONObject objectt = array.optJSONObject(0);
                    Utils.putCache("uid", objectt.getString("编号"));
                    Utils.putCache("name",objectt.getString("用户姓名"));
                    ToastShort(object.getString("msg"));
                    Intent intent = new Intent(LoginActivity.this, Firstpage.class);
                    startActivity(intent);
                    finish();
                } else {
                    ToastShort(object.getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
