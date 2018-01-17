package ff.express.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ff.express.R;
import ff.express.model.Expressmodel;
import ff.express.model.ExpressmodelMingxi;

/**
 * Created by XY on 2017/9/29.
 */

public class MingxiApter extends BaseAdapter{
    Context mContext;
    List<ExpressmodelMingxi> mList;
    LayoutInflater layoutinflater;
    public MingxiApter(Context context, List<ExpressmodelMingxi> list){
        mContext=context;
        layoutinflater=LayoutInflater.from(mContext);
        mList=list;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ExpressmodelMingxi model=mList.get(position);
        if(convertView==null){
            holder=new ViewHolder();
            convertView= layoutinflater.inflate(R.layout.mingxisinglelayout,null);
            holder.num= (TextView) convertView.findViewById(R.id.t2);
            holder.method= (TextView) convertView.findViewById(R.id.t4);
            holder.jname= (TextView) convertView.findViewById(R.id.t10);
            holder.jphone= (TextView) convertView.findViewById(R.id.t12);
            holder.sname= (TextView) convertView.findViewById(R.id.t14);
            holder.sphone= (TextView) convertView.findViewById(R.id.t16);
            holder.saddress= (TextView) convertView.findViewById(R.id.t18);
            holder.fee= (TextView) convertView.findViewById(R.id.t20);
            holder.zhongliang= (TextView) convertView.findViewById(R.id.t8);
            holder.time= (TextView) convertView.findViewById(R.id.t6);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.num.setText(model.getNum());
        holder.method.setText(model.getMethod());
        holder.jname.setText(model.getJname());
        holder.jphone.setText(model.getJphone());
        holder.sname.setText(model.getSname());
        holder.sphone.setText(model.getSphone());
        holder.saddress.setText(model.getSaddress());
        holder.fee.setText(model.getFee());
        holder.time.setText(model.getTime());
        holder.zhongliang.setText(model.getZhonglian());

        return convertView;
    }
    private class ViewHolder{
        TextView num,method;//单号num，方式method
        TextView time,zhongliang,jname,sname,jphone,sphone,saddress,fee;
       /* String num;//单号
        String time;//时间
        String method;//方式
        String zhonglian;//重量
        String jname;//寄件人姓名
        String sname;//收件人姓名
        String jphone;//寄件人电话
        String sphone;//收件电话
        String saddress;//收件人地址
        String fee;//费用*/
    }
}
