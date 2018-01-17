package ff.express.ui;

import android.text.TextUtils;

import butterknife.ButterKnife;
import ff.express.R;
import ff.express.base.BaseActivity;
import ff.express.method.Utils;


/**
 * 启动页面
 * Created by Administrator on 2016/12/5.
 */

public class StartActivity extends BaseActivity {
    @Override
    public void initViews() {
        setContentView(R.layout.startactivity);
    }

    @Override
    public void initEvents() {
        if (TextUtils.isEmpty(Utils.getCache("uid"))) {
            Utils.IntentPost(LoginActivity.class);
        } else {
            Utils.IntentPost(Firstpage.class);
        }
        finish();
    }
}
