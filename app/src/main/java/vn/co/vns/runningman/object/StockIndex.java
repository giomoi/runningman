package vn.co.vns.runningman.object;

/**
 * Created by Thanh on 6/23/2017.
 */

public class StockIndex {
    private String nameIndex;
    private String pointIndex;
    private String rateIndex;
    private String weighIndex;
    private String valueIndex;
    private String incNumberStock;
    private String descNumberStock;
    private String nochangeNumberStock;

    public StockIndex(String nameIndex, String pointIndex, String rateIndex, String weighIndex, String valueIndex) {
        this.nameIndex = nameIndex;
        this.pointIndex = pointIndex;
        this.rateIndex = rateIndex;
        this.weighIndex = weighIndex;
        this.valueIndex = valueIndex;
    }

    public StockIndex(String nameIndex, String pointIndex, String rateIndex, String weighIndex, String valueIndex, String incNumberStock, String descNumberStock, String nochangeNumberStock) {
        this.nameIndex = nameIndex;
        this.pointIndex = pointIndex;
        this.rateIndex = rateIndex;
        this.weighIndex = weighIndex;
        this.valueIndex = valueIndex;
        this.incNumberStock = incNumberStock;
        this.descNumberStock = descNumberStock;
        this.nochangeNumberStock = nochangeNumberStock;
    }

    public String getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(String nameIndex) {
        this.nameIndex = nameIndex;
    }

    public String getPointIndex() {
        return pointIndex;
    }

    public void setPointIndex(String pointIndex) {
        this.pointIndex = pointIndex;
    }

    public String getRateIndex() {
        return rateIndex;
    }

    public void setRateIndex(String rateIndex) {
        this.rateIndex = rateIndex;
    }

    public String getWeighIndex() {
        return weighIndex;
    }

    public void setWeighIndex(String weighIndex) {
        this.weighIndex = weighIndex;
    }

    public String getValueIndex() {
        return valueIndex;
    }

    public void setValueIndex(String valueIndex) {
        this.valueIndex = valueIndex;
    }

    public String getIncNumberStock() {
        return incNumberStock;
    }

    public void setIncNumberStock(String incNumberStock) {
        this.incNumberStock = incNumberStock;
    }

    public String getDescNumberStock() {
        return descNumberStock;
    }

    public void setDescNumberStock(String descNumberStock) {
        this.descNumberStock = descNumberStock;
    }

    public String getNochangeNumberStock() {
        return nochangeNumberStock;
    }

    public void setNochangeNumberStock(String nochangeNumberStock) {
        this.nochangeNumberStock = nochangeNumberStock;
    }
}
