package ff.express.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ff.express.R;
import ff.express.base.BaseActivity;
import ff.express.base.Key;
import ff.express.listener.ClosethisActivity;
import ff.express.method.OkHttpUtil;
import ff.express.method.Utils;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by XY on 2017/9/28.
 */

public class YichangActivity extends BaseActivity implements View.OnClickListener,ClosethisActivity{
    TextView confirm_tv_yca,backtv,scanner_yca;
    EditText yuanyintv_yca,danhaotv_yca;
    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg2 == 1) {
                finish();
            } else {
            super.handleMessage(msg);
            try {
                DIalog dialog = new DIalog(YichangActivity.this);
                JSONArray jarray = new JSONArray(msg.obj.toString());
                JSONObject object = jarray.optJSONObject(0);
                String result = object.getString("state");
                if (result.equals("1")) {
                    dialog.ShowDIalog(YichangActivity.this, null);//俩按钮
                } else {
                    dialog.ShowDIalog(YichangActivity.this, object.getString("msg"));//一个按钮提示单号不存在点确定的
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }}

        }
    };

    @Override
    public void initViews() {
        setContentView(R.layout.yichangdanbaojing);
        danhaotv_yca = (EditText) findViewById(R.id.danhaotv_yca);
        scanner_yca = (TextView) findViewById(R.id.scanner_yca);
        yuanyintv_yca = (EditText) findViewById(R.id.yuanyintv_yca);
        confirm_tv_yca = (TextView) findViewById(R.id.confirm_tv_yca);
        backtv=(TextView) findViewById(R.id.backtv);

    }

    @Override
    public void initEvents() {
        scanner_yca.setOnClickListener(this);
        confirm_tv_yca.setOnClickListener(this);
        backtv.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scanner_yca:
                scanning();
                break;
            case R.id.confirm_tv_yca:
                paisong();
                break;
            case R.id.backtv:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 确定
     * 提交单号到服务器
     */
    private void paisong() {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(YichangActivity.this);
        //AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("确认提交吗？");
        dialog.setIcon(R.mipmap.message);
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestBody requestBody = new FormBody.Builder()
                        .add("id", Utils.getCache("uid"))
                        .add("code", danhaotv_yca.getText().toString())
                        .add("note", yuanyintv_yca.getText().toString()).build();
                Request request = new Request.Builder()
                        .url(Key.BASE_URL+"yc.ashx")
                        .post(requestBody)
                        .build();

                OkHttpUtil okHttpUtil = new OkHttpUtil(YichangActivity.this);
                okHttpUtil.enqueue(request, h, YichangActivity.this);
            }
        });
        dialog.create().show();


    }
    //检验拍照权限
    public void takePhoto() {
        if (ContextCompat.checkSelfPermission(YichangActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(YichangActivity.this, new String[]{Manifest.permission.CAMERA},1);
            }}
    }
    /**
     * 转跳到扫描页面
     */
    private void scanning() {

        Intent intent = new Intent(this, CaptureActivity.class);        //CaptureActivity是扫描的Activity类
        startActivityForResult(intent, 0);                            //当前扫描完条码或二维码后,会回调当前类的onActivityResult方法,
        takePhoto();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {    //判断回调
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");            //这就获取了扫描的内容了
            danhaotv_yca.setText(scanResult);
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
