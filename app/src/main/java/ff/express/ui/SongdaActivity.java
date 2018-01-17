package ff.express.ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import ff.express.R;
import ff.express.base.BaseActivity;
import ff.express.base.Key;
import ff.express.listener.ClosethisActivity;
import ff.express.method.OkHttpUtil;
import ff.express.method.Utils;
import ff.express.model.MessageModel;
import ff.express.model.MessageModel1;
import ff.express.setImage.MD5;
import ff.express.setImage.compressImage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XY on 2017/9/28.
 */
public class SongdaActivity extends BaseActivity implements View.OnClickListener, ClosethisActivity {
    File outputImage;//存储照片的临时文件夹
    String pickname;//照片名字
    Uri imageUri;//照片路径
    Map<String, String> map;//发送照片
    String url;
    Bitmap bitmap;
    ImageView pictor_iv_sda;
    private int Firstflag = 1;
    TextView scannertv_sda, qianshoutv_sda, paizhaotv_sda, backtv;
    EditText danhaotv_sda;
    private final int TATE_PHOTO = 1;
    boolean Photo = false;
    Bitmap bm;
    @Override
    public void initViews() {
        setContentView(R.layout.songda);
        pictor_iv_sda = (ImageView) findViewById(R.id.pictor_iv_sda);
        danhaotv_sda = (EditText) findViewById(R.id.danhaotv_sda);
        scannertv_sda = (TextView) findViewById(R.id.scannertv_sda);
        qianshoutv_sda = (TextView) findViewById(R.id.qianshoutv_sda);
        paizhaotv_sda = (TextView) findViewById(R.id.paizhaotv_sda);
        danhaotv_sda.requestFocus(danhaotv_sda.getText().length());
        backtv = (TextView) findViewById(R.id.backtv);
        url = Key.BASE_URL + "sd.ashx";
        takePhoto();
    }

    @Override
    public void initEvents() {
        paizhaotv_sda.setOnClickListener(this);
        qianshoutv_sda.setOnClickListener(this);
        scannertv_sda.setOnClickListener(this);
        backtv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paizhaotv_sda:
                paizhao();
                break;
            case R.id.qianshoutv_sda:
                sendPhoto();
                break;
            case R.id.scannertv_sda://记得写扫描
                scanning();
                break;
            case R.id.backtv:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 转跳到扫描页面
     */
    private void scanning() {

        Intent intent = new Intent(this, CaptureActivity.class);        //CaptureActivity是扫描的Activity类
        startActivityForResult(intent, 0);                            //当前扫描完条码或二维码后,会回调当前类的onActivityResult方法,
    }

    //检验拍照权限
    public void takePhoto() {
        if (ContextCompat.checkSelfPermission(SongdaActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(SongdaActivity.this, new String[]{Manifest.permission.CAMERA},1);
            }}
    }
    /**
     * 拍照
     */
    private void paizhao() {
        Photo = true;
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Toast.makeText(SongdaActivity.this, "请检测sd是否可用", Toast.LENGTH_SHORT).show();
            return;

        }
        outputImage = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
        try {

            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
      /*      imageUri = Uri.fromFile(outputImage);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);*/
          //  takePhoto();
            topictor();
            //startActivityForResult(intent, TATE_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //打开相机进行拍照
    public void topictor() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this, "ff.express.fileProvider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);

        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TATE_PHOTO);
    }
    //发送照片
    private void sendPhoto() {
        if (Photo) {
            oldSendPhoto();
            //upImage();//新发送照片方法
        } else {
            oldSendPhoto();
        }
    }

    //老发送方法
    public void oldSendPhoto() {
        if (map == null) {
            map = new HashMap<String, String>();
            map.put("id", Utils.getCache("uid"));
            if(Photo){
            map.put("ext", "jpg");}else{
                map.put("ext","");
            }

            map.put("code", danhaotv_sda.getText().toString());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(bitmap!=null){
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);}
        Message m = new Message();
        Observable.create(subscriber -> {
            subscriber.onNext(communication01(url, map, baos.toByteArray()));
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                /*MessageModel1 model = new Gson().fromJson(o.toString(), MessageModel1.class);
                if (("1").equals(model.getState())) {
                    m.arg1 = 1;
                    Firstflag = 1;
                } else {
                    ToastShort(model.getMsg());
                }
                hh.sendMessage(m);*/
                    try {
                        JSONArray jarray = new JSONArray(o.toString());
                        JSONObject object = jarray.optJSONObject(0);

                        String result = object.getString("state");

                        if (result.equals("1")) {
                            ToastShort(object.getString("msg"));
                        } else {
                            ToastShort(object.getString("msg"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                    ToastShort("error898");
                });
    }

    /**
     * 派送 提交单号到服务器
     */
    private void paisong() {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(SongdaActivity.this);
        //AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("确认提交吗？");
        dialog.setIcon(R.mipmap.message);
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestBody requestBody = new FormBody.Builder()
                        .add("id", Utils.getCache("uid"))
                        .add("ext","")
                        .add("code", danhaotv_sda.getText().toString()).build();
                Request request = new Request.Builder()
                        .url(Key.BASE_URL + "sd.ashx")
                        .post(requestBody)
                        .build();
                OkHttpUtil okHttpUtil = new OkHttpUtil(SongdaActivity.this);
                okHttpUtil.enqueue(request, h, SongdaActivity.this);
            }
        });
        dialog.create().show();


    }

    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg2 == 1) {
                finish();
            } else {
                DIalog dialog = new DIalog(SongdaActivity.this);
                try {
                    JSONArray jarray = new JSONArray(msg.obj.toString());
                    JSONObject object = jarray.optJSONObject(0);
                    String result = object.getString("state");
                    if (result.equals("1")) {
                        dialog.ShowDIalog(SongdaActivity.this, null);//俩按钮
                    } else {
                        dialog.ShowDIalog(SongdaActivity.this, object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
    };

    Handler hh = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    /**
     * 最原始传照片方法
     */
    public String communication01(String urlString, Map<String, String> map, byte[] bytt) {
        String result = "";

        String end = "\r\n";
        if (!urlString.equals("")) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setDoInput(true);// 允许输入
                conn.setDoOutput(true);// 允许输出
                conn.setUseCaches(false);// 不使用Cache
                conn.setConnectTimeout(6000);// 6秒钟连接超时
                conn.setReadTimeout(6000);// 6秒钟读数据超时
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Charset", "UTF-8");
                //StringBuilder localStringBuilder1 = new StringBuilder();
                Iterator localIterator = map.entrySet().iterator();

                while (true) {
                    if (!localIterator.hasNext()) {
                        break;
                    }

                    Map.Entry localEntry = (Map.Entry) localIterator.next();
                    conn.setRequestProperty((String) localEntry.getKey(), URLEncoder.encode((String) localEntry.getValue(),
                            "UTF-8"));
                }
                /*
                 * conn.setRequestProperty("id", "1");
				 * conn.setRequestProperty("type", "1");
				 * conn.setRequestProperty("ext", "jpg");
				 */
                // / type=1 表示上传商家图片 id 表示商家编号
                // / type=2 表示上传菜品图片 id 表示菜品编号
                // / ext=jpg 表示后缀名

                DataOutputStream dos = new DataOutputStream(
                        conn.getOutputStream());
                dos.write(bytt, 0, bytt.length);
                dos.writeBytes(end);
                dos.flush();
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);
                result = br.readLine();
                String s = "";
//                listener.action(260, result, Tag);
            } catch (Exception e) {
                result = "{\"ret\":\"898\"}";
//                listener.action(258, result, Tag);
            }
        }
        return result;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TATE_PHOTO://拍照获得照片
                int x = 0;
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                   long size= getBitmapsize(bitmap);
                    Log.e("size:",size+"");
                    if(size>658*3600){
                  /*  compressImage compressImage = new compressImage();
                    bitmap = compressImage.getBitmapFromUrl(bitmap, bitmap.getWidth()/1.1, bitmap.getHeight()/1.1);
                    long size2=getBitmapsize(bitmap);
                    Log.e("size2:",size2+"");*/
                   bitmap= dobitmap(bitmap);}

                    paizhaotv_sda.setText("重拍");
                    pictor_iv_sda.setImageBitmap(bitmap);
                    pickname = getNonceStr();
                }
                break;
            case 0:
                if (resultCode == RESULT_OK) {    //判断回调
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");            //这就获取了扫描的内容了
                    danhaotv_sda.setText(scanResult);
                }
            default:
                break;
        }
    }

    public  Bitmap dobitmap(Bitmap bitmap1){
        if(getBitmapsize(bitmap1)>(3600*3600)){
            compressImage compressImage = new compressImage();
            bitmap = compressImage.getBitmapFromUrl(bitmap, bitmap.getWidth()/1.2, bitmap.getHeight()/1.2);
            Log.e("bitmap:",getBitmapsize(bitmap1)+"");
            dobitmap(bitmap);

        }else{
            return bitmap;
        }
        //long size=getBitmapsize(bitmap);

        return bitmap;
    }


     //bitmap大小
    public long getBitmapsize(Bitmap mbitmap){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return mbitmap.getByteCount();
        }
        // Pre HC-MR1
        return mbitmap.getRowBytes() * mbitmap.getHeight();
    }

    /**
     * 生成随机号，防重发
     */
    private String getNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

    @Override
    public void Finish(boolean b) {
        if (b) {
            Message m = new Message();
            m.arg2 = 1;
            h.sendMessage(m);
        }
    }

    /**
     * 上传图片
     */
    private void upImage() {
        OkHttpClient mOkHttpClent = new OkHttpClient();
        File file = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("img", pickname + ".jpg",
                        RequestBody.create(MediaType.parse("image/png"), outputImage))
                .addFormDataPart("id", Utils.getCache("uid"))
                .addFormDataPart("ext", "jpg")
                .addFormDataPart("code", danhaotv_sda.getText().toString());
        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = mOkHttpClent.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SongdaActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Toast.makeText(SongdaActivity.this, "成功", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
