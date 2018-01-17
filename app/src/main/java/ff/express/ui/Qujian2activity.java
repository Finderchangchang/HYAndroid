package ff.express.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import ff.express.R;
import ff.express.adapter.DanhaoAdapter;
import ff.express.base.Key;
import ff.express.listener.ClosethisActivity;
import ff.express.method.OkHttpUtil;

import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ff.express.base.BaseActivity;
import ff.express.method.Utils;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by XY on 2017/9/26.
 */

public class Qujian2activity extends BaseActivity implements View.OnClickListener, ClosethisActivity {
    TextView Scannertv;
    EditText danhaotv;
    TextView confirmtv;
    ListView danhaolist;
    DanhaoAdapter adapter;
    static List<String> list;
    TextView title;
    String danhaostr = "";
    RadioButton rb1_xianjie, rb2_daofu, rb3_yuejie;
    EditText shulianget_qj;
    RadioGroup jiesuanfangshirg_qja;
    String jiesuanfangshi = "1";
    TextView backtv, addinlist_tv_ps;
    boolean b = false;
    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            super.handleMessage(msg);
            if (msg.arg2 == 1) {
                finish();
            } else {
                DIalog dialog = new DIalog(Qujian2activity.this);
                try {
                    JSONArray jarray = new JSONArray(msg.obj.toString());
                    JSONObject object = jarray.optJSONObject(0);
                    String result = object.getString("state");
                    if (result.equals("1")) {
                        dialog.ShowDIalog(Qujian2activity.this, null);//俩按钮
                    } else {
                        dialog.ShowDIalog(Qujian2activity.this, object.getString("msg"));//一个按钮提示单号不存在点确定的

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    @Override
    public void initViews() {
        setContentView(R.layout.qujian2);
        Scannertv = (TextView) findViewById(R.id.scannertv_psa);
        danhaotv = (EditText) findViewById(R.id.danhaotv_psa);
        confirmtv = (TextView) findViewById(R.id.confirm_tv_qja);
        danhaolist = (ListView) findViewById(R.id.lv_psa);
        backtv = (TextView) findViewById(R.id.backtv);
        addinlist_tv_ps = (TextView) findViewById(R.id.addinlist_tv_ps);
        jiesuanfangshirg_qja = (RadioGroup) findViewById(R.id.jiesuanfangshirg_qja);
        rb1_xianjie = (RadioButton) findViewById(R.id.rb1_xianjie);
        rb2_daofu = (RadioButton) findViewById(R.id.rb2_daofu);
        rb3_yuejie = (RadioButton) findViewById(R.id.rb3_yuejie);
        shulianget_qj = (EditText) findViewById(R.id.shulianget_qj);
        title = (TextView) findViewById(R.id.title);
        list = new ArrayList<>();
        title.setText("取件");
      /*  danhaolist.setOnItemClickListener((parent, view, position, id) -> {
            list.remove(position);
            adapter.notifyDataSetChanged();
        });*/
        adapter = new DanhaoAdapter(this, list);
        danhaolist.setAdapter(adapter);

    }

    @Override
    public void initEvents() {
        Scannertv.setOnClickListener(this);
        confirmtv.setOnClickListener(this);
        backtv.setOnClickListener(this);
        addinlist_tv_ps.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scannertv_psa:
                scanning();
                break;
            case R.id.confirm_tv_qja:
                paisong();
                break;
            case R.id.backtv:
                finish();
                break;
            case R.id.addinlist_tv_ps:
                list.add(danhaotv.getText().toString().trim());
                adapter.notifyDataSetChanged();
                danhaotv.setText("");
                //将输入法隐藏，mPasswordEditText 代表密码输入框
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(addinlist_tv_ps.getWindowToken(), 0);

            default:
                break;
        }

    }

    /**
     * 派送 提交单号到服务器
     */
    private void paisong() {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(Qujian2activity.this);
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
                //设置给强哥发送的所有单号，的的str
                for (int i = 0; i < list.size(); i++) {
                    if (i != list.size() - 1) {
                        danhaostr += list.get(i) + ",";
                    } else {
                        danhaostr += list.get(i);
                    }
                }
                if (list.size() < 1) {
                    danhaostr = danhaotv.getText().toString().trim();
                }
                if (danhaostr.equals("")) {
                    ToastShort("请添加单号");
                    return;
                }
                RequestBody requestBody = new FormBody.Builder()
                        .add("code", danhaostr)
                        .add("type", jiesuanfangshi)
                        .add("num", shulianget_qj.getText().toString())
                        .add("id", Utils.getCache("uid")).build();

                Request request = new Request.Builder()
                        .url(Key.BASE_URL + "qj.ashx")
                        .post(requestBody)
                        .build();

                OkHttpUtil okHttpUtil = new OkHttpUtil(Qujian2activity.this);
                okHttpUtil.enqueue(request, h, Qujian2activity.this);

            }


        });
        dialog.create().show();


    }

    //检验拍照权限
    public void takePhoto() {
        if (ContextCompat.checkSelfPermission(Qujian2activity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(Qujian2activity.this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
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
            if (!TextUtils.isEmpty(scanResult)) {
                for (int i = 0; i < scanResult.split(";").length; i++) {
                    list.add(scanResult.split(";")[i]);
                }
            }
            adapter.notifyDataSetChanged();
            //danhaotv.setText(scanResult);

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
