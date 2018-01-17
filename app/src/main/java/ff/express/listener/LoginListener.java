package ff.express.listener;

import android.content.Context;
import android.os.Handler;

import ff.express.base.Key;
import ff.express.method.OkHttpUtil;
import ff.express.method.Utils;
import ff.express.view.ILogin;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2016/11/26.
 */
interface ILoginMView {
    void login(String uid, String pwd, Handler h,Context context);//账号密码登录
}
interface RePwdMView {
    void repwd(String pwd,String npwd, Handler h,Context context);//账号密码登录
}
public class LoginListener implements ILoginMView ,RePwdMView{
    ILogin mView;



    @Override
    public void login(String uid, String pwd, Handler h, Context context) {
        RequestBody requestBody = new FormBody.Builder()
                .add("uid",uid)
                .add("pwd",pwd).build();

        Request request = new Request.Builder()
                .url(Key.BASE_URL+"log.ashx")
                .post(requestBody)
                .build();

        OkHttpUtil okHttpUtil = new OkHttpUtil(context);
        okHttpUtil.enqueue(request,h,context);


    }


    @Override
    public void repwd(String pwd, String npwd, Handler h, Context context) {
        RequestBody requestBody = new FormBody.Builder()
                .add("id",Utils.getCache("uid"))
                .add("pwd", pwd)
                .add("pwdNew",npwd).build();
        Request request = new Request.Builder()
                .url(Key.BASE_URL+"updatePass.ashx")
                .post(requestBody)
                .build();
        OkHttpUtil okHttpUtil = new OkHttpUtil(context);
        okHttpUtil.enqueue(request, h,context);
    }
}
