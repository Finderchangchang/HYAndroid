package ff.express.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import ff.express.R;
import ff.express.base.Key;
import ff.express.listener.ClosethisActivity;
import ff.express.method.OkHttpUtil;
import ff.express.method.Utils;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by XY on 2017/9/25.
 */

public class QujianActivity extends Activity implements View.OnClickListener , ClosethisActivity {
    TextView scanningtv,scannerbig_qja;
    EditText danhaotv;
    RadioGroup jiesuanfangshirg_qja;
    RadioButton rb1_xianjie, rb2_daofu, rb3_yuejie;
    TextView confirm_tv_qja,backtv;
    String jiesuanfangshi = "1";
    EditText shulianget;
    //Volleyhttp volleyhttp;
    Map<String, String> map;

    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg2 == 1) {
                finish();
            } else {
                DIalog dialog = new DIalog(QujianActivity.this);
                try {
                    JSONArray jarray = new JSONArray(msg.obj.toString());
                    JSONObject object = jarray.optJSONObject(0);
                    String result = object.getString("state");
                        dialog.ShowDIalog(QujianActivity.this, object.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }}
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qujian);
        danhaotv = (EditText) findViewById(R.id.danhaotv_qja);
        scanningtv = (TextView) findViewById(R.id.scanner_qja);
        jiesuanfangshirg_qja = (RadioGroup) findViewById(R.id.jiesuanfangshirg_qja);
        rb1_xianjie = (RadioButton) findViewById(R.id.rb1_xianjie);
        rb2_daofu = (RadioButton) findViewById(R.id.rb2_daofu);
        rb3_yuejie = (RadioButton) findViewById(R.id.rb3_yuejie);
        confirm_tv_qja = (TextView) findViewById(R.id.confirm_tv_qja);
        shulianget = (EditText) findViewById(R.id.shulianget);
        backtv=(TextView) findViewById(R.id.backtv);
        map = new HashMap<>();

        initEvents();
    }

    public void initEvents() {
        scanningtv.setOnClickListener(this);
        confirm_tv_qja.setOnClickListener(this);
        backtv.setOnClickListener(this);
        jiesuanfangshirg_qja.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb1_xianjie:
                        jiesuanfangshi = "1";
                        break;
                    case R.id.rb2_daofu:
                        jiesuanfangshi = "2";
                        break;
                    case R.id.rb3_yuejie:
                        jiesuanfangshi = "3";
                }
            }
        });

    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scanner_qja:
                scanning();
                break;
            case R.id.confirm_tv_qja:
                confirm();
                break;
            case R.id.backtv:
                finish();
                break;

            default:
                break;
        }
    }
    //检验拍照权限
    public void takePhoto() {
        if (ContextCompat.checkSelfPermission(QujianActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(QujianActivity.this, new String[]{Manifest.permission.CAMERA},1);
            }}
    }

    public void confirm(){
        RequestBody requestBody = new FormBody.Builder()
                .add("code", danhaotv.getText().toString())
                .add("type", jiesuanfangshi)
                .add("num", shulianget.getText().toString())
                .add("id", Utils.getCache("uid")).build();
        Request request = new Request.Builder()
                .url(Key.BASE_URL+"qj.ashx")
                .post(requestBody)
                .build();

        OkHttpUtil okHttpUtil = new OkHttpUtil(this);
        okHttpUtil.enqueue(request, h, QujianActivity.this);}
    /**
     * 转跳到扫描页面
     */
    private void scanning() {

        Intent intent = new Intent(this, CaptureActivity.class);        //CaptureActivity是扫描的Activity类
        startActivityForResult(intent, 0);                            //当前扫描完条码或二维码后,会回调当前类的onActivityResult方法,
        takePhoto();
    }


    /**
     * 重写onActivityResult 方法
     * 当前对象若以startActivityForResult方式转跳页面,当目标页面结束以后,会回调此方法
     *
     * @param requestCode 该参数就是  startActivityForResult(intent, 0); 中的第二个参数值
     * @param resultCode  是转跳的目标页面中,setResult(RESULT_OK, intent); 中的第一个参数值
     * @param data        转跳的Intent对象,可以传递数值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {    //判断回调
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");            //这就获取了扫描的内容了
            danhaotv.setText(scanResult);

        }
    }
    @Override
    public void Finish(boolean b) {
        if (b) {
            Message m = new Message();
            m.arg2 = 1;
            h.sendMessage(m);
        }
    }

}
