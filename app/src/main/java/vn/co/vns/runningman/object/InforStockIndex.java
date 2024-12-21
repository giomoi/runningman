package vn.co.vns.runningman.object;

/**
 * Created by thanhnv on 6/26/18.
 */

public class InforStockIndex {
    private String dateTransit;
    private String rate;
    private String valueTransit;
    private String volume;
    private String priceClose;
    private String priceOpen;
    private String priceHigh;
    private String priceLow;
    private String valueAgree;
    private String volumeAgree;
    private String valueAgree10;
    private String rate10;
    private String exchange; // -1 giam, 1 tang, 0 khong tang khong giam. xac dinh tang hay giam so voi gia tri giao dich trung binh 10 session.

    public String getDateTransit() {
        return dateTransit;
    }

    public String getRate() {
        return rate;
    }

    public String getValueTransit() {
        return valueTransit;
    }

    public String getVolume() {
        return volume;
    }

    public String getPriceClose() {
        return priceClose;
    }

    public String getPriceOpen() {
        return priceOpen;
    }

    public String getPriceHigh() {
        return priceHigh;
    }

    public String getPriceLow() {
        return priceLow;
    }

    public String getValueAgree() {
        return valueAgree;
    }

    public String getVolumeAgree() {
        return volumeAgree;
    }

    public void setValue(String value, int i) {
        //valuePrice = valuePrice.replaceAll(",","");
        if (i > 11) return;
        switch (i) {
            case 0:
                dateTransit = value;
                return;
            case 1:
                priceClose = value;
                return;
            case 3:
                rate = value;
                return;
            case 4:
                volume= value;
                return;
            case 5:
                valueTransit= value;
                return;
            case 6:
                volumeAgree= value;
                return;
            case 7:
                valueAgree= value;
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
            case 11:
                valueAgree10= value;
                return;
            case 12:
                rate10= value;
                return;
            case 13:
                exchange= value;
                return;
            default:
                break;
        }
    }
}
