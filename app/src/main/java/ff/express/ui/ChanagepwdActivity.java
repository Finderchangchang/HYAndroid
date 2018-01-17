package ff.express.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ff.express.R;
import ff.express.base.BaseActivity;
import ff.express.listener.LoginListener;
import ff.express.method.Utils;
import ff.express.view.ILogin;

/**
 * Created by Administrator on 2016/11/26.
 */

public class ChanagepwdActivity extends BaseActivity {

    LoginListener mListener;
    public static ChanagepwdActivity mIntail;
    EditText PwdEt;
    EditText NewpwdEt;
    TextView confirm_tv_cpa;

    @Override
    public void initViews() {
        setContentView(R.layout.changepwd);
        mListener = new LoginListener();
        mIntail = this;
        confirm_tv_cpa = (TextView) findViewById(R.id.confirm_tv_cpa);
        PwdEt = (EditText) findViewById(R.id.pwd_et_xa);
        NewpwdEt = (EditText) findViewById(R.id.repwd_ed_xa);
    }

    @Override
    public void initEvents() {
        confirm_tv_cpa.setOnClickListener(v -> {
            if (TextUtils.isEmpty(PwdEt.getText().toString().trim()) || ("").equals(NewpwdEt.getText().toString().trim())) {
                ToastShort("帐号不能为空或密码不能为空");
            } else {
                mListener.repwd(PwdEt.getText().toString().trim(), NewpwdEt.getText().toString().trim(), h, this);
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
                if (result.equals("1")) {//修改密码成功
                    ToastShort(object.getString("msg"));
                    Intent intent = new Intent(ChanagepwdActivity.this, Firstpage.class);
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
