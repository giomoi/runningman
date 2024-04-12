package vn.co.vns.runningman.object;

/**
 * Created by thanhnv on 5/24/18.
 */

public class InforVolumeValueStockIndex {
    private String dateTransit;
    private String rate;
    private String priceClose;
    private String volumeClose;
    private String valueClose;
    private String volumeRate;
    private String valueRate;
    private String priceOpen;
    private String priceHigh;
    private String priceLow;
    private Long averVolume10;
    private Long averAverVolume20;
    private  Long averValue10;
    private Long averValue20;
    private String volumeRate10;
    private String valueRate10;
    private String volumeRate20;
    private String valueRate20;

    public String getValueIndexClose() {
        return valueIndexClose;
    }

    public void setValueIndexClose(String valueIndexClose) {
        this.valueIndexClose = valueIndexClose;
    }

    public String getValueIndex() {
        return valueIndex;
    }

    public void setValueIndex(String valueIndex) {
        this.valueIndex = valueIndex;
    }

    private String valueIndexClose;
    private String valueIndex;

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

    public String getPriceClose() {
        return priceClose;
    }

    public void setPriceClose(String priceClose) {
        this.priceClose = priceClose;
    }

    public String getVolumeClose() {
        return volumeClose;
    }

    public void setVolumeClose(String volumeClose) {
        this.volumeClose = volumeClose;
    }

    public String getValueClose() {
        return valueClose;
    }

    public void setValueClose(String valueClose) {
        this.valueClose = valueClose;
    }

    public String getPriceOpen() {
        return priceOpen;
    }

    public void setPriceOpen(String priceOpen) {
        this.priceOpen = priceOpen;
    }

    public String getPriceHigh() {
        return priceHigh;
    }

    public void setPriceHigh(String priceHigh) {
        this.priceHigh = priceHigh;
    }

    public String getPriceLow() {
        return priceLow;
    }

    public void setPriceLow(String priceLow) {
        this.priceLow = priceLow;
    }

    public Long getAverVolume10() {
        return averVolume10;
    }

    public void setAverVolume10(Long averVolume10) {
        this.averVolume10 = averVolume10;
    }

    public Long getAverAverVolume20() {
        return averAverVolume20;
    }

    public void setAverAverVolume20(Long averAverVolume20) {
        this.averAverVolume20 = averAverVolume20;
    }

    public Long getAverValue10() {
        return averValue10;
    }

    public void setAverValue10(Long averValue10) {
        this.averValue10 = averValue10;
    }

    public Long getAverValue20() {
        return averValue20;
    }

    public void setAverValue20(Long averValue20) {
        this.averValue20 = averValue20;
    }

    public String getVolumeRate() {
        return volumeRate;
    }

    public void setVolumeRate(String volumeRate) {
        this.volumeRate = volumeRate;
    }

    public String getValueRate() {
        return valueRate;
    }

    public void setValueRate(String valueRate) {
        this.valueRate = valueRate;
    }

    public String getVolumeRate10() {
        return volumeRate10;
    }

    public void setVolumeRate10(String volumeRate10) {
        this.volumeRate10 = volumeRate10;
    }

    public String getValueRate10() {
        return valueRate10;
    }

    public void setValueRate10(String valueRate10) {
        this.valueRate10 = valueRate10;
    }

    public String getVolumeRate20() {
        return volumeRate20;
    }

    public void setVolumeRate20(String volumeRate20) {
        this.volumeRate20 = volumeRate20;
    }

    public String getValueRate20() {
        return valueRate20;
    }

    public void setValueRate20(String valueRate20) {
        this.valueRate20 = valueRate20;
    }

    public void setValue(String value, int i) {
        //valuePrice = valuePrice.replaceAll(",","");
        if (i > 10) return;
        switch (i) {
            case 0:
                dateTransit = value;
                return;
            case 2:
                rate = value;
                return;
            case 1:
                priceClose = value;
                return;
            case 8:
                priceOpen= value;
                return;
            case 9:
                priceHigh= value;
                return;
            case 10:
                priceLow= value;
                return;
            case 4:
                volumeClose= value;
                return;
            case 5:
                valueClose= value;
                return;
            default:
                break;
        }
    }
}
