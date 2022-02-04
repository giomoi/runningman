package vn.co.vns.runningman.object;

/**
 * Created by thanhnv on 10/5/16.
 */
public class Stock {
    private int stockId;
    private String ticker;
    private String date;
    private float open;
    private float high;
    private float low;
    private float close;
    private int volume;
    private int isplace;

    public Stock() {
    }

    public Stock(int isPlace, int stockId, String ticker, String date, float open, float high, float low, float close, int volume) {
        this.isplace = isPlace;
        this.stockId = stockId;
        this.ticker = ticker;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public Stock(String ticker, String date, float open, float high, float low, float close, int volume, int isPlace) {
        this.ticker = ticker;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.isplace = isPlace;
    }

    public Stock(String ticker, String date,float close, int volume) {
        this.ticker = ticker;
        this.date = date;
        this.close = close;
        this.volume = volume;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getIsPlace() {
        return isplace;
    }

    public void setIsPlace(int isPlace) {
        this.isplace = isPlace;
    }
}
