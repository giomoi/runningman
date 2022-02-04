package vn.co.vns.runningman.util;

import java.util.ArrayList;

import vn.co.vns.runningman.object.InforVolumeValueStockIndex;
import vn.co.vns.runningman.object.Stock;

/**
 * Created by Nguyen Van Thanh on 3/11/2017.
 */

public class Singleton {

    private static Singleton Instance;
    private ArrayList<String> listTicker;
    private ArrayList<Stock> listStock=new ArrayList<>();
    private int numberScreen=0; // 0:Main screen 1: Big Stock Volume
    private int volumeBigVolume=100000;
    private int rateBigVolume=2;
    private int volumeBreakOut=0;
    private String endDateBreakOut=null;
    private InforVolumeValueStockIndex objInforVolumeValueStockIndex;

    private Singleton() {
        listTicker=new ArrayList<String>();
    }

    public static Singleton getInstance() {
        if (Instance == null) {
                    Instance = new Singleton();
            }
        return Instance;
    }

    public void setTicker(String ticker){
        listTicker.add(ticker);
    }

    public ArrayList<String> getTickers() {
        return this.listTicker;
    }

    public void setNumberScreen(int numberScreen){
            this.numberScreen=numberScreen;
    }
    public int getNumberScreen(){
        return this.numberScreen;
    }

    public ArrayList<Stock> getListStock() {
        return this.listStock;
    }

    public void setListStock(Stock stock) {
        listStock.add(stock);
    }

    public void setArrayListStock(ArrayList<Stock> arrayListStock){
        this.listStock=arrayListStock;
    }

    public int getVolumeBigVolume() {
        return volumeBigVolume;
    }

    public void setVolumeBigVolume(int volumeBigVolume) {
        this.volumeBigVolume = volumeBigVolume;
    }

    public int getRateBigVolume() {
        return rateBigVolume;
    }

    public void setRateBigVolume(int rateBigVolume) {
        this.rateBigVolume = rateBigVolume;
    }

    public int getVolumeBreakOut() {
        return volumeBreakOut;
    }

    public void setVolumeBreakOut(int volumeBreakOut) {
        this.volumeBreakOut = volumeBreakOut;
    }

    public String getEndDateBreakOut() {
        return endDateBreakOut;
    }

    public void setEndDateBreakOut(String endDateBreakOut) {
        this.endDateBreakOut = endDateBreakOut;
    }

    public InforVolumeValueStockIndex getObjInforVolumeValueStockIndex() {
        return objInforVolumeValueStockIndex;
    }

    public void setObjInforVolumeValueStockIndex(InforVolumeValueStockIndex objInforVolumeValueStockIndex) {
        this.objInforVolumeValueStockIndex = objInforVolumeValueStockIndex;
    }
}
