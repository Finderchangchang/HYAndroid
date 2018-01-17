package ff.express.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ff.express.R;

/**
 * Created by XY on 2017/9/26.
 */

public class DanhaoAdapter extends BaseAdapter{
    List<String> mlist;
    Context mContext;
    public DanhaoAdapter(Context context, List<String> list){
        mlist=list;
        mContext=context;

    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.danhaoitem,null);
            holder.danhaotv= (TextView) convertView.findViewById(R.id.danhaotv_dhi);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.danhaotv.setText("快递编号:"+(mlist.get(position)).toString());
        holder.danhaotv.setOnClickListener(v -> {
            AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
            dialog.setIcon(R.mipmap.message).setTitle("提示").setMessage("确认删除编号"+mlist.get(position)+"吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mlist.remove(position);
                    DanhaoAdapter.this.notifyDataSetChanged();
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
           dialog.create().show();
        });
        return convertView;
    }
    private class ViewHolder{
        TextView danhaotv;
    }
}
