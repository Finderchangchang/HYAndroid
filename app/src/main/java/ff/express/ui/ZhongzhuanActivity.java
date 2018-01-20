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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ff.express.R;
import ff.express.adapter.DanhaoAdapter;
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

public class ZhongzhuanActivity extends BaseActivity implements View.OnClickListener, ClosethisActivity {
    TextView Scannertv, addinlist_tv, backtv;
    EditText danhaotv;
    TextView confirmtv;
    ListView danhaolist;
    DanhaoAdapter adapter;
    static List<String> list;
    Map<String, String> map;
    String danhaostr = "";
    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg2 == 1) {
                finish();
            } else {
                DIalog dialog = new DIalog(ZhongzhuanActivity.this);
                try {
                    JSONArray jarray = new JSONArray(msg.obj.toString());
                    JSONObject object = jarray.optJSONObject(0);
                    String result = object.getString("state");
                    if (result.equals("1")) {

                        dialog.ShowDIalog(ZhongzhuanActivity.this, null);//俩按钮
                    } else {
                        dialog.ShowDIalog(ZhongzhuanActivity.this, object.getString("msg"));//一个按钮提示单号不存在点确定的
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void initViews() {
        setContentView(R.layout.zhongzhuan);
        Scannertv = (TextView) findViewById(R.id.scanner_zza);
        danhaotv = (EditText) findViewById(R.id.danhaotv_zza);
        confirmtv = (TextView) findViewById(R.id.confirm_tv_zza);
        danhaolist = (ListView) findViewById(R.id.list_zza);
        backtv = (TextView) findViewById(R.id.backtv);
        addinlist_tv = (TextView) findViewById(R.id.addinlist_tv);
        list = new ArrayList<>();
        /*danhaolist.setOnItemClickListener((parent, view, position, id) -> {
            list.remove(position);
            adapter.notifyDataSetChanged();
        });*///这里是以前写的点击删除当前item不知道现在还有没有用  没用就删了
        adapter = new DanhaoAdapter(this, list);
        danhaolist.setAdapter(adapter);
        danhaotv.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //这里注意要作判断处理，ActionDown、ActionUp都会回调到这里，不作处理的话就会调用两次
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
                    check(danhaotv.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
    }

    void check(String input) {
        boolean equ = false;
        for (int i = 0; i < list.size(); i++) {
            if (input.equals(list.get(i))) {
                equ = true;
                break;
            }
        }
        if (!equ) {//没有相同的数
            list.add(input);
        }
        adapter.notifyDataSetChanged();
        danhaotv.setText("");
    }

    @Override
    public void initEvents() {
        Scannertv.setOnClickListener(this);
        confirmtv.setOnClickListener(this);
        backtv.setOnClickListener(this);
        addinlist_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scanner_zza:
                scanning();
                break;
            case R.id.confirm_tv_zza:
                paisong();
                break;
            case R.id.backtv:
                finish();
                break;
            case R.id.addinlist_tv:
                list.add(danhaotv.getText().toString().trim());
                adapter.notifyDataSetChanged();
                danhaotv.setText("");
                //将输入法隐藏，mPasswordEditText 代表密码输入框
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(addinlist_tv.getWindowToken(), 0);

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

        //设置给强哥发送的所有单号，的的str
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                danhaostr += list.get(i) + ",";
            } else {
                danhaostr += list.get(i);
            }
        }
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(ZhongzhuanActivity.this);
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
                if (list.size() < 1) {
                    danhaostr = danhaotv.getText().toString().trim();
                }
                if (danhaostr.equals("")) {
                    ToastShort("请添加单号");
                    return;
                }

                RequestBody requestBody = new FormBody.Builder()
                        .add("id", Utils.getCache("uid"))
                        .add("codeList", danhaostr).build();
                Request request = new Request.Builder()
                        .url(Key.BASE_URL + "zz.ashx")
                        .post(requestBody)
                        .build();
                OkHttpUtil okHttpUtil = new OkHttpUtil(ZhongzhuanActivity.this);
                okHttpUtil.enqueue(request, h, ZhongzhuanActivity.this);

            }


        });
        dialog.create().show();


    }

    //检验拍照权限
    public void takePhoto() {
        if (ContextCompat.checkSelfPermission(ZhongzhuanActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(ZhongzhuanActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * 转跳到扫描页面
     */
    private void scanning() {
        Intent intent = new Intent(this, CaptureActivity.class);        //CaptureActivity是扫描的Activity类
        intent.putExtra("is_one", false);
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
                    check(scanResult.split(";")[i]);
                }
            }
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
