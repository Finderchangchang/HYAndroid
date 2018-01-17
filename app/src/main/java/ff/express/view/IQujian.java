package ff.express.view;

import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Created by XY on 2017/9/26.
 */

public interface IQujian {
    void qujianResult(boolean result, String errorMsg, JSONObject jsonObject, VolleyError error);
}
