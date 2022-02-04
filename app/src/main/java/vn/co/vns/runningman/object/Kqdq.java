package vn.co.vns.runningman.object;

/**
 * Created by hoangtuan on 3/3/17.
 */
public class Kqdq {
    public int get_id() {
        return _id;
    }

    public String getTicker() {
        return ticker;
    }

    public String getYear() {
        return year;
    }

    public String getContent() {
        return content;
    }

    public String getQ1() {
        return Q1;
    }

    public String getQ2() {
        return Q2;
    }

    public String getQ3() {
        return Q3;
    }

    public String getQ4() {
        return Q4;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setQ1(String q1) {
        Q1 = q1;
    }

    public void setQ2(String q2) {
        Q2 = q2;
    }

    public void setQ3(String q3) {
        Q3 = q3;
    }

    public void setQ4(String q4) {
        Q4 = q4;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    private int _id;
    private String ticker;
    private String year;
    private String content;
    private String Q1;
    private String Q2;
    private String Q3;
    private String Q4;
    private String Total;

    public Kqdq(){
        this.Q1 = "0";
        this.Q2 = "0";
        this.Q3 = "0";
        this.Q4 = "0";
    }

    public String toString(){
        return ticker+"-"+year + "-" + content +"-(" + Total + ": " + Q1 +", "+ Q2 +", "+ Q3 +", "+ Q4 +")";
    }
}
