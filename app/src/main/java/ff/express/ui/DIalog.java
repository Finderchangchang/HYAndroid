package ff.express.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import ff.express.R;
import ff.express.listener.ClosethisActivity;
import ff.express.method.Utils;

/**
 * Created by XY on 2017/9/30.
 */

public class DIalog {
    ClosethisActivity mb;

    public DIalog(ClosethisActivity b) {
        mb = b;
    }

    public void ShowDIalog(Context c, String msg) {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(c);
        dialog.setTitle("提示");
        if (msg==null) {
            dialog.setMessage("提交成功，确认关闭此页面吗？");
            dialog.setIcon(R.mipmap.message);
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            dialog.setMessage(msg);
        }
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (msg==null) {//如果需要确认关闭页面 取消两个按钮的
                    mb.Finish(true);
                } else {//如果只需要点击确定 dialog消失
                    dialog.dismiss();
                }
            }
        });
        dialog.create().show();
    }
    public void Tixing(Context c, String msg) {
        AlertDialog.Builder dialog;
    dialog = new AlertDialog.Builder(c);
        dialog.setTitle("溫馨提示");
            dialog.setMessage(msg);
            dialog.setIcon(R.mipmap.message);
            dialog.setNegativeButton("不再提醒", new DialogInterface.OnClickListener() { //设置取消按钮
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Utils.putCache("tixing","1");
            dialog.dismiss();
        }
    });
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    });
        dialog.create().show();
}
}
