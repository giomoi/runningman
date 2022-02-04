package vn.co.vns.runningman.object;

/**
 * Created by thanhnv on 11/29/16.
 */
public class StockVolumePriceAgreement extends Stock{
    private int volume1;
    private int volume2;
    private int volume3;
    private float price1;
    private float price2;
    private float price3;
    private float incDes;
    private float aver;

    public StockVolumePriceAgreement(String ticker, String date, float close, int volume, float aver, float incDes, float price1, float price2, float price3, int volume1, int volume2, int volume3) {
        super(ticker, date, close, volume);
        this.aver = aver;
        this.incDes = incDes;
        this.price1 = price1;
        this.price2 = price2;
        this.price3 = price3;
        this.volume1 = volume1;
        this.volume2 = volume2;
        this.volume3 = volume3;
    }

    public StockVolumePriceAgreement() {

    }

    public float getAver() {
        return aver;
    }

    public void setAver(float aver) {
        this.aver = aver;
    }

    public float getIncDes() {
        return incDes;
    }

    public void setIncDes(float incDes) {
        this.incDes = incDes;
    }

    public float getPrice1() {
        return price1;
    }

    public void setPrice1(float price1) {
        this.price1 = price1;
    }

    public float getPrice2() {
        return price2;
    }

    public void setPrice2(float price2) {
        this.price2 = price2;
    }

    public float getPrice3() {
        return price3;
    }

    public void setPrice3(float price3) {
        this.price3 = price3;
    }

    public int getVolume1() {
        return volume1;
    }


    public void setVolume1(int volume1) {
        this.volume1 = volume1;
    }

    public int getVolume2() {
        return volume2;
    }

    public void setVolume2(int volume2) {
        this.volume2 = volume2;
    }

    public int getVolume3() {
        return volume3;
    }

    public void setVolume3(int volume3) {
        this.volume3 = volume3;
    }

}
