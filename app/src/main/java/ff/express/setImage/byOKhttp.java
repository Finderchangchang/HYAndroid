package ff.express.setImage;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class byOKhttp {

    public void upImage(URL url, Context context, Handler handler, String uri, int i){
        File file1;
        OkHttpClient mOkHttpClient=new OkHttpClient();
        if(i==1){
        file1 = new File(Environment.getExternalStorageDirectory()+"/tempImage.jpg");}
        else{
            file1 = new File(uri);
        }
        MultipartBody.Builder builder=new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("img","tempImage.jpg", RequestBody.create(MediaType.parse("image/png"),file1));//jpeg可能报错

        RequestBody requestBody=builder.build();
        Request request=new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call=mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Message m=new Message();
                        m.arg1=2;
                        handler.sendMessage(m);
                    }
                }).start();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Message m=new Message();
                        m.arg1=1;
                        handler.sendMessage(m);
                    }
                }).start();

            }
        });
    }
}