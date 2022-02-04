package vn.co.vns.runningman.object;

/**
 * Created by thanhnv on 10/17/16.
 */
public class StockBigVolume extends Stock{
    private float incdes;
    private float rate;
    private float average;
    private int percent;
    private int volume1;
    private int volume2;
    private int volume3;
    private int volume4;
    private int volume5;



    public StockBigVolume(String ticker, String date, float close, int volume, float incDes, float rate, float aver) {
        super(ticker, date, close, volume);
        this.incdes = incDes;
        this.rate = rate;
        this.average = aver;
    }

    public StockBigVolume() {

    }

    public float getIncDes() {
        return incdes;
    }

    public void setIncDes(float incDes) {
        this.incdes = incDes;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getAver() {
        return average;
    }

    public void setAver(float aver) {
        this.average = aver;
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

    public int getVolume4() {
        return volume4;
    }

    public void setVolume4(int volume4) {
        this.volume4 = volume4;
    }

    public int getVolume5() {
        return volume5;
    }

    public void setVolume5(int volume5) {
        this.volume5 = volume5;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
