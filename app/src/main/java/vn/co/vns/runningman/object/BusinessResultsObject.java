package vn.co.vns.runningman.object;

/**
 * Created by Thanh on 6/23/2017.
 */

public class BusinessResultsObject {

    private String ticker;
    private String year;
    private float eps;
    private float pe;
    private float volume;
    private float netRevenue;
    private float profitAfterTax;
    private float epsNotAdjusted;
    private float priceEnding;
    private float bookValue;

    public BusinessResultsObject(String ticker, String year) {
        this.ticker = ticker;
        this.year = year;
    }

    public BusinessResultsObject(String ticker, String year, float eps, float pe, float volume, float netRevenue, float profitAfterTax, float epsNotAdjusted, float priceEnding, float bookValue) {
        this.ticker = ticker;
        this.year = year;
        this.eps = eps;
        this.pe = pe;
        this.volume = volume;
        this.netRevenue = netRevenue;
        this.profitAfterTax = profitAfterTax;
        this.epsNotAdjusted = epsNotAdjusted;
        this.priceEnding = priceEnding;
        this.bookValue = bookValue;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public float getEps() {
        return eps;
    }

    public void setEps(float eps) {
        this.eps = eps;
    }

    public float getPe() {
        return pe;
    }

    public void setPe(float pe) {
        this.pe = pe;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getNetRevenue() {
        return netRevenue;
    }

    public void setNetRevenue(float netRevenue) {
        this.netRevenue = netRevenue;
    }

    public float getProfitAfterTax() {
        return profitAfterTax;
    }

    public void setProfitAfterTax(float profitAfterTax) {
        this.profitAfterTax = profitAfterTax;
    }

    public float getEpsNotAdjusted() {
        return epsNotAdjusted;
    }

    public void setEpsNotAdjusted(float epsNotAdjusted) {
        this.epsNotAdjusted = epsNotAdjusted;
    }

    public float getPriceEnding() {
        return priceEnding;
    }

    public void setPriceEnding(float priceEnding) {
        this.priceEnding = priceEnding;
    }

    public float getBookValue() {
        return bookValue;
    }

    public void setBookValue(float bookValue) {
        this.bookValue = bookValue;
    }
}
