package ff.express.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */

public class MessageModel1 {
    private String success;
    private String errorMsg;
    private String cityid;
    private String shopid;
    private String state;
    private String togoname;
    private String newordercount;
    private String sendtype;
    private String lat;
    private String lng;
    private String msg;
    /**
     * sendfee : 30.00
     */

    private String sendfee;



    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTogoname() {
        return togoname;
    }

    public void setTogoname(String togoname) {
        this.togoname = togoname;
    }

    public String getNewordercount() {
        return newordercount;
    }

    public void setNewordercount(String newordercount) {
        this.newordercount = newordercount;
    }

    public String getSendtype() {
        return sendtype;
    }

    public void setSendtype(String sendtype) {
        this.sendtype = sendtype;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getSendfee() {
        return sendfee;
    }

    public void setSendfee(String sendfee) {
        this.sendfee = sendfee;
    }

}
