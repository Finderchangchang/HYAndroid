package ff.express.method;

import android.content.Context;
import android.os.Handler;

import ff.express.base.Key;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by XY on 2017/9/30.
 */

public class getOrderNum {
    public void getnum(Handler h, Context context) {
        RequestBody requestBody = new FormBody.Builder()
                .add("id",Utils.getCache("uid")).build();
        Request request = new Request.Builder()
                .url(Key.BASE_URL+"getcount.ashx")
                .post(requestBody)
                .build();
        OkHttpUtil okHttpUtil = new OkHttpUtil(context);
        okHttpUtil.enqueue(request, h,context);
    }
}
