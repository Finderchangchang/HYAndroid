package ff.express.method;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class OkHttpUtil {
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    Context mContext;

    public OkHttpUtil(Context context) {
        this.mContext = context;
    }

    static {
    }

    public static void enqueue(Request request, Handler h,Context c) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(c,"请检查网络连接",Toast.LENGTH_LONG);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String str = response.body().string();
                Message m = new Message();
                m.arg1 = 1;
                m.obj = str;
                h.sendMessage(m);
                Log.e("success", response + "");
                Log.e("body", str);


            }
        });
    }
}