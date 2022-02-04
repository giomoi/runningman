package vn.co.vns.runningman.object;

/**
 * Created by hoangtuan on 5/10/17.
 */
public class PriceOnline {
    public int get_id() {
        return _id;
    }

    public String getTicker() {
        return ticker;
    }

    public String getMaxPrice() {
        return MaxPrice;
    }

    public String getDay() {
        return Day;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setMaxPrice(String maxPrice) {
        MaxPrice = maxPrice;
    }

    public void setDay(String day) {
        Day = day;
    }

    private int _id;
    private String ticker;
    private String MaxPrice;
    private String Day;

    public PriceOnline(){

    }
}
