package ff.express.ui;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ff.express.R;
import ff.express.adapter.MingxiApter;
import ff.express.base.BaseActivity;
import ff.express.method.Utils;
import ff.express.model.ExpressmodelMingxi;

/**
 * Created by Administrator on 2017/10/1.
 */

public class MingxiActivity extends BaseActivity implements View.OnClickListener {
    TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20;
    TextView backtv_mingxi;
    TextView qj, zz, psz;//取件，中转，派送中
    //TextView state,time;//单号跟时间标题栏；
    List<ExpressmodelMingxi> list;
    MingxiApter adapter;
    String data;//大数据
    ListView mingxilistview;
    int b = 0;//设置默认显示取件状态


    @Override
    public void initViews() {
        setContentView(R.layout.mingxilayout);
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
        qj = (TextView) findViewById(R.id.qj);
        zz = (TextView) findViewById(R.id.zz);
        psz = (TextView) findViewById(R.id.psz);
        mingxilistview = (ListView) findViewById(R.id.mingxill);
        list = new ArrayList<>();
        // state= (TextView) findViewById(R.id.state);
        // time= (TextView) findViewById(R.id.time);
        //state.setText("快递单号");
        //time.setText("操作时间");
        backtv_mingxi = (TextView) findViewById(R.id.backtv_minggxi);
        data = Utils.getCache("data");
        putdata();
    }

    @Override
    public void initEvents() {
        qj.setOnClickListener(this);
        zz.setOnClickListener(this);
        psz.setOnClickListener(this);
        backtv_mingxi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        list=null;
        list = new ArrayList<>();
        switch (v.getId()) {
            case R.id.qj:
                putdata();
                b = 0;
                break;
            case R.id.zz:
                b = 1;
                putdata();
                break;
            case R.id.psz:
                b = 2;
                putdata();
                break;
            case R.id.backtv_minggxi:
                finish();
                break;
            default:
                break;
        }
    }


    /**
     * 给三个状态设置数据
     */
    private void putdata() {

        try {
            JSONArray jsona = new JSONArray(data);


            JSONObject jsonb = jsona.optJSONObject(b);//按不同的按钮显示不同的数据
            if (jsonb.getString("countNum").equals("0")) {
                mingxilistview.setVisibility(View.GONE);
                return;
            }else{mingxilistview.setVisibility(View.VISIBLE);}
            JSONArray jsonaa = new JSONArray(jsonb.getString("data"));//详细信息list列表
            for (int i = 0; i < jsonaa.length(); i++) {
                JSONObject jsonbb = jsonaa.optJSONObject(i);
                ExpressmodelMingxi model = new ExpressmodelMingxi();
                model.setNum(jsonbb.getString("快递单号"));
                model.setMethod(jsonbb.getString("结算方式"));
                model.setTime(jsonbb.getString("录入时间"));
                model.setZhonglian(jsonbb.getString("重量"));
                model.setJphone(jsonbb.getString("寄件人电话"));
                model.setJname(jsonbb.getString("寄件人姓名"));
                model.setSname(jsonbb.getString("收件人姓名"));
                model.setSphone(jsonbb.getString("收件人电话"));
                model.setSaddress(jsonbb.getString("收件人地址"));
                model.setFee(jsonbb.getString("费用"));
                list.add(model);

              /*  t2.setText(jsonbb.getString("快递单号"));
                t4.setText(jsonbb.getString("结算方式"));
                t6.setText(jsonbb.getString("录入时间"));
                t8.setText(jsonbb.getString("重量"));
                t10.setText(jsonbb.getString("寄件人姓名"));
                t12.setText(jsonbb.getString("寄件人电话"));
                t14.setText(jsonbb.getString("收件人姓名"));
                t16.setText(jsonbb.getString("收件人电话"));
                t18.setText(jsonbb.getString("收件人地址"));
                t20.setText(jsonbb.getString("费用"));*/
            }
            adapter = new MingxiApter(this, list);
            mingxilistview.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
