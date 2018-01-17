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

/**
 * Created by XY on 2017/9/29.
 */

public class ChaxunadApter extends BaseAdapter{
    Context mContext;
    List<Expressmodel> mList;
    LayoutInflater layoutinflater;
    public ChaxunadApter(Context context, List<Expressmodel> list){
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
        Expressmodel model=mList.get(position);
        if(convertView==null){
            holder=new ViewHolder();
            convertView= layoutinflater.inflate(R.layout.chaxunitemadapter,null);
            holder.t1= (TextView) convertView.findViewById(R.id.t1);
            holder.t2= (TextView) convertView.findViewById(R.id.t2);
            holder.t3= (TextView) convertView.findViewById(R.id.t3);
            holder.t4= (TextView) convertView.findViewById(R.id.t4);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.t1.setText(model.getName());
        holder.t2.setText(model.getMsg());
        holder.t3.setText(model.getState());
        holder.t4.setText(model.getTime());
        return convertView;
    }
    private class ViewHolder{
        TextView t1,t2,t3,t4;
    }
}
