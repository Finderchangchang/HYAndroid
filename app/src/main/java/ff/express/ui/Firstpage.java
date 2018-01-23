package ff.express.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ff.express.R;
import ff.express.base.BaseActivity;
import ff.express.listener.ClosethisActivity;
import ff.express.method.Utils;
import ff.express.method.getOrderNum;


/**
 * Created by XY on 2017/9/20.
 */

public class Firstpage extends BaseActivity implements View.OnClickListener, ClosethisActivity {
    TextView qujiantv;
    TextView paijiantv;
    TextView qianshoutv;
    TextView gongdanchaxuntv;
    TextView zhongzhuantv;
    TextView yichangchulitv;
    TextView mingxi_tv_fa;//明細
    TextView ggmmtv, tcdltv,name;//popview内的控件
    Button popwindowbt;
    View popview;
    getOrderNum Ordernum;
    TextView qj_tv_fa, zz_tv_fa, pj_tv_fa, frash_tv_fa;
    String tixing;
    int i = 1;

    @Override
    public void initViews() {
        setContentView(R.layout.firstpage);
        qujiantv = (TextView) findViewById(R.id.qujiantv);
        paijiantv = (TextView) findViewById(R.id.paijiantv);
        qianshoutv = (TextView) findViewById(R.id.qianshoutv);
        gongdanchaxuntv = (TextView) findViewById(R.id.gongdanchaxuntv);
        zhongzhuantv = (TextView) findViewById(R.id.zhongzhuantv);
        yichangchulitv = (TextView) findViewById(R.id.yichangchulitv);
        qj_tv_fa = (TextView) findViewById(R.id.qj_tv_fa);
        zz_tv_fa = (TextView) findViewById(R.id.zz_tv_fa);
        pj_tv_fa = (TextView) findViewById(R.id.pj_tv_fa);
        frash_tv_fa = (TextView) findViewById(R.id.frash_tv_fa);//刷新
        mingxi_tv_fa = (TextView) findViewById(R.id.mingxi_tv_fa);
        popwindowbt = (Button) findViewById(R.id.popwindowbt);
        popwindowbt.setOnClickListener(this);
    }

    @Override
    public void initEvents() {
        qujiantv.setOnClickListener(this);
        paijiantv.setOnClickListener(this);
        zhongzhuantv.setOnClickListener(this);
        qianshoutv.setOnClickListener(this);
        yichangchulitv.setOnClickListener(this);
        gongdanchaxuntv.setOnClickListener(this);
        frash_tv_fa.setOnClickListener(this);
        mingxi_tv_fa.setOnClickListener(this);
        //为了第一次不显示“刷新成功”
        Ordernum = new getOrderNum();
        Ordernum.getnum(h, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tixing = Utils.getCache("tixing");
        if (!tixing.equals("1")) {
            AlertDialog.Builder dialog;
            dialog = new AlertDialog.Builder(this);
            dialog.setTitle("溫馨提示");
            dialog.setMessage("为了节省您的流量，若要观看明细，每次操作快递单后请手动刷新");
            dialog.setIcon(R.mipmap.message);

            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Utils.putCache("tixing","1");
                    dialog.dismiss();
                }
            });
            dialog.create().show();
        }

    }

    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (i > 1) {
                    Toast.makeText(Firstpage.this, "刷新成功", Toast.LENGTH_SHORT).show();
                }
                JSONArray jarray = new JSONArray(msg.obj.toString());
                JSONObject object = jarray.optJSONObject(0);
                String result = object.getString("state");
                JSONArray dataarray = new JSONArray(object.getString("data"));
                Utils.putCache("data", dataarray.toString());//把取得的数据放入系统
                JSONObject datab1 = dataarray.optJSONObject(0);
                qj_tv_fa.setText("取件数量:" + datab1.getString("countNum"));
                JSONObject datab2 = dataarray.optJSONObject(1);
                zz_tv_fa.setText("运输数量:" + datab2.getString("countNum"));
                JSONObject datab3 = dataarray.optJSONObject(2);
                pj_tv_fa.setText("库存数量:" + datab3.getString("countNum"));
                if (result.equals("1")) {

                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void getNum() {
        //获得数量
        i = 2;//区分登陆获得还是刷新获得，以便提示刷新成功
        Ordernum = new getOrderNum();
        Ordernum.getnum(h, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qujiantv:
                Intent intent = new Intent(this, Qujian2activity.class);
                startActivity(intent);
                break;
            case R.id.paijiantv:
                Intent psintent = new Intent(this, PaisongActivity.class);
                startActivity(psintent);
                break;
            case R.id.zhongzhuantv:
                Intent zhongzhuanitnent = new Intent(this, ZhongzhuanActivity.class);
                startActivity(zhongzhuanitnent);
                break;
            case R.id.yichangchulitv:
                Intent yichangintent = new Intent(this, YichangActivity.class);
                startActivity(yichangintent);
                break;
            case R.id.qianshoutv:
                Intent songdaintent = new Intent(this, SongdaActivity.class);
                startActivity(songdaintent);
                break;
            case R.id.gongdanchaxuntv:
                Intent gongdanintent = new Intent(this, GongdanchaxunActivity.class);
                startActivity(gongdanintent);
                break;
            case R.id.popwindowbt:
                ShowPopwindow();
                break;
            case R.id.frash_tv_fa://刷新
                getNum();
                break;
            case R.id.ggmmtv://更换密码
                Intent genghuanpwdintent = new Intent(this, ChanagepwdActivity.class);
                startActivity(genghuanpwdintent);
                break;
            case R.id.tcdltv://退出登录
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                Utils.putCache("uid", "");
                break;
            case R.id.mingxi_tv_fa://明细
                Intent mingxiinteng = new Intent(this, MingxiActivity.class);
                startActivity(mingxiinteng);
            default:
                break;

        }
    }


    private void ShowPopwindow() {
        popview = LayoutInflater.from(this).inflate(R.layout.popwindowlayout, null);
        View rootview = LayoutInflater.from(Firstpage.this).inflate(R.layout.firstpage, null);
        PopupWindow popwindow = new PopupWindow(this);
        popwindow.setContentView(popview);
        popwindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popwindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popwindow.setOutsideTouchable(true);  //设置点击屏幕其它地方弹出框消失
        ggmmtv = (TextView) popview.findViewById(R.id.ggmmtv);
        tcdltv = (TextView) popview.findViewById(R.id.tcdltv);
        name= (TextView) popview.findViewById(R.id.name_pop);
        ggmmtv.setOnClickListener(this);
        tcdltv.setOnClickListener(this);
        name.setText(Utils.getCache("name"));
        popwindow.showAsDropDown(popwindowbt, 0, 0);
    }

    @Override
    public void Finish(boolean b) {

    }
}
