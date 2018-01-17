package ff.express.ui;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import ff.express.R;
import ff.express.adapter.ChaxunadApter;
import ff.express.base.BaseActivity;
import ff.express.base.Key;
import ff.express.listener.ClosethisActivity;
import ff.express.method.OkHttpUtil;
import ff.express.method.Utils;
import ff.express.model.Expressmodel;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by XY on 2017/9/28.
 */

public class GongdanchaxunActivity extends BaseActivity implements View.OnClickListener, ClosethisActivity {
    TextView scanner_gdca, confirm_tv_gdca,backtv_gongdanchaxun;
    TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20;
    LinearLayout chaxunll;
    EditText danhaotv_gdca;
    List<Expressmodel> list ;
    ListView chaxunlv;
    ChaxunadApter adapter;
    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg2 == 1) {
                finish();
            } else {
                DIalog dialog = new DIalog(GongdanchaxunActivity.this);
            try {
                Log.e("jsonbb", msg.toString());
                JSONArray jsona = new JSONArray(msg.obj.toString());
                JSONObject jsonb = jsona.optJSONObject(0);
                String state=jsonb.getString("state");
                if(state.equals("1")){//成功的数据
                JSONArray jsonaa = new JSONArray(jsonb.get("data").toString());
                JSONObject jsonbb = jsonaa.optJSONObject(0);
                t2.setText(jsonbb.getString("快递单号"));
                t4.setText(jsonbb.getString("结算方式"));
                t6.setText(jsonbb.getString("当前状态"));
                t8.setText(jsonbb.getString("重量"));
                t10.setText(jsonbb.getString("寄件人姓名"));
                t12.setText(jsonbb.getString("寄件人电话"));
                t14.setText(jsonbb.getString("收件人姓名"));
                t16.setText(jsonbb.getString("收件人电话"));
                t18.setText(jsonbb.getString("收件人地址"));
                t20.setText(jsonbb.getString("费用"));
                JSONArray jsonaaa=new JSONArray(jsonbb.getString("stateList"));//list数据内容
                list=new ArrayList<>();
                if(jsonaaa!=null){
                    chaxunlv.setVisibility(View.VISIBLE);//如果列表里面有数据就显示
                for(int i=0;i<jsonaaa.length();i++){//遍历list需要加入的字段
                    JSONObject jsonbbb=jsonaaa.optJSONObject(i);
                    Expressmodel model=new Expressmodel();
                    model.setName(jsonbbb.getString("操作员"));
                    model.setState(jsonbbb.getString("日志内容"));
                    model.setMsg(jsonbbb.getString("状态名称"));
                    model.setTime(jsonbbb.getString("操作时间"));
                    list.add(model);
                }
                adapter=new ChaxunadApter(GongdanchaxunActivity.this,list);
                chaxunlv.setAdapter(adapter);}}else{
                    dialog.ShowDIalog(GongdanchaxunActivity.this, jsonb.getString("msg"));//一个按钮提示单号不存在点确定的
                }
            } catch (Exception e) {
                e.printStackTrace();
            }}

        }
    };

    @Override
    public void initViews() {
        setContentView(R.layout.gongdanchaxun);
        danhaotv_gdca = (EditText) findViewById(R.id.danhaotv_gdca);
        scanner_gdca = (TextView) findViewById(R.id.scanner_gdca);
        confirm_tv_gdca = (TextView) findViewById(R.id.confirm_tv_gdca);
        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        t3 = (TextView) findViewById(R.id.t3);
        t4 = (TextView) findViewById(R.id.t4);
        t5 = (TextView) findViewById(R.id.t5);
        t6 = (TextView) findViewById(R.id.t6);
        t7 = (TextView) findViewById(R.id.t7);
        t8 = (TextView) findViewById(R.id.t8);
        t9 = (TextView) findViewById(R.id.t9);
        t10 = (TextView) findViewById(R.id.t10);
        t11 = (TextView) findViewById(R.id.t11);
        t12 = (TextView) findViewById(R.id.t12);
        t13 = (TextView) findViewById(R.id.t13);
        t14 = (TextView) findViewById(R.id.t14);
        t15 = (TextView) findViewById(R.id.t15);
        t16 = (TextView) findViewById(R.id.t16);
        t17 = (TextView) findViewById(R.id.t17);
        t18 = (TextView) findViewById(R.id.t18);
        t19 = (TextView) findViewById(R.id.t19);
        t20 = (TextView) findViewById(R.id.t20);
        chaxunlv= (ListView) findViewById(R.id.chaxunlv);
        chaxunll= (LinearLayout) findViewById(R.id.chaxunll);
        backtv_gongdanchaxun= (TextView) findViewById(R.id.backtv_gongdanchaxun);


    }

    @Override
    public void initEvents() {
        scanner_gdca.setOnClickListener(this);
        confirm_tv_gdca.setOnClickListener(this);
        list = new ArrayList<>();
        backtv_gongdanchaxun.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_tv_gdca:
                chaxun();
                break;
            case R.id.scanner_gdca:
                scanning();
                break;
            case R.id.backtv_gongdanchaxun:
                finish();
                break;
            default:
                break;
        }
    }
    //检验拍照权限
    public void takePhoto() {
        if (ContextCompat.checkSelfPermission(GongdanchaxunActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(GongdanchaxunActivity.this, new String[]{Manifest.permission.CAMERA},1);
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


    /**
     * 派送 提交单号到服务器
     */
    private void chaxun() {
        chaxunll.setVisibility(View.VISIBLE);
        RequestBody requestBody = new FormBody.Builder()
                .add("id", Utils.getCache("uid"))
                .add("code", danhaotv_gdca.getText().toString()).build();

        Request request = new Request.Builder()
                .url(Key.BASE_URL+"getdd.ashx")
                .post(requestBody)
                .build();

        OkHttpUtil okHttpUtil = new OkHttpUtil(GongdanchaxunActivity.this);
        okHttpUtil.enqueue(request, h,GongdanchaxunActivity.this);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {    //判断回调
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");            //这就获取了扫描的内容了


            danhaotv_gdca.setText(scanResult);

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
