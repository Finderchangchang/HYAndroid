package ff.express.model;

/**
 * Created by XY on 2017/9/29.
 */

public class ExpressmodelMingxi {

    String num;//单号
    String time;//时间
    String method;//方式
    String zhongliang;//重量
    String jname;//寄件人姓名
    String sname;//收件人姓名
    String jphone;//寄件人电话
    String sphone;//收件电话
    String saddress;//收件人地址
    String fee;//费用

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getZhonglian() {
        return zhongliang;
    }

    public void setZhonglian(String zhonglian) {
        this.zhongliang = zhonglian;
    }

    public String getJname() {
        return jname;
    }

    public void setJname(String jname) {
        this.jname = jname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getJphone() {
        return jphone;
    }

    public void setJphone(String jphone) {
        this.jphone = jphone;
    }

    public String getSphone() {
        return sphone;
    }

    public void setSphone(String sphone) {
        this.sphone = sphone;
    }

    public String getSaddress() {
        return saddress;
    }

    public void setSaddress(String saddress) {
        this.saddress = saddress;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
