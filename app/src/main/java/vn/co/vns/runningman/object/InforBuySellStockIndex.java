package vn.co.vns.runningman.object;

/**
 * Created by thanhnv on 4/26/18.
 */

public class InforBuySellStockIndex {
    private String dateTransit;
    private String rate;
    private String volumeBuy;
    private String volumeSell;
    private String volumeBuyMinusSell;

    public String getDateTransit() {
        return dateTransit;
    }

    public void setDateTransit(String dateTransit) {
        this.dateTransit = dateTransit;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getVolumeBuy() {
        return volumeBuy;
    }

    public void setVolumeBuy(String volumeBuy) {
        this.volumeBuy = volumeBuy;
    }

    public String getVolumeSell() {
        return volumeSell;
    }

    public void setVolumeSell(String volumeSell) {
        this.volumeSell = volumeSell;
    }

    public String getVolumeBuyMinusSell() {
        return volumeBuyMinusSell;
    }

    public void setVolumeBuyMinusSell(String volumeBuyMinusSell) {
        this.volumeBuyMinusSell = volumeBuyMinusSell;
    }

    public void setValue(String value, int i) {
        //valuePrice = valuePrice.replaceAll(",","");
        if (i > 9) return;
        switch (i) {
            case 0:
                dateTransit = value;
                return;
            case 1:
                rate = value;
                return;
            case 3:
                volumeBuy = value;
                return;
            case 6:
                volumeSell= value;
                return;
            case 8:
                volumeBuyMinusSell= value;
                return;
            default:
                break;
        }
    }
}
