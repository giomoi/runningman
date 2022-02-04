package vn.co.vns.runningman.object;

/**
 * Created by hoangtuan on 6/1/17.
 */
public class StockValue {
    public int get_id() {
        return _id;
    }

    public String getTicker() {
        return Ticker;
    }

    public String getNearByMin() {
        return NearByMin;
    }

    public String getNearByMax() {
        return NearByMax;
    }

    public String getWeight10Days() {
        return Weight10Days;
    }

    public String getThiGiaVon() {
        return ThiGiaVon;
    }

    public String getCPLuuHanh() {
        return CPLuuHanh;
    }

    public String getCPNiemYet() {
        return CPNiemYet;
    }

    public String getTyLeCoTuc_ThiGia() {
        return TyLeCoTuc_ThiGia;
    }

    public String getGDKHQ() {
        return GDKHQ;
    }

    public String getEPS_4Quy() {
        return EPS_4Quy;
    }

    public String getEPS_Nam() {
        return EPS_Nam;
    }

    public String getROA() {
        return ROA;
    }

    public String getROE() {
        return ROE;
    }

    public String getDonBayTC() {
        return DonBayTC;
    }

    public String getPE() {
        return PE;
    }

    public String getPB() {
        return PB;
    }

    public String getBEta() {
        return Beta;
    }

    private int _id;

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setTicker(String ticker) {
        Ticker = ticker;
    }

    public void setNearByMin(String nearByMin) {
        NearByMin = nearByMin;
    }

    public void setNearByMax(String nearByMax) {
        NearByMax = nearByMax;
    }

    public void setWeight10Days(String weight10Days) {
        Weight10Days = weight10Days;
    }

    public void setThiGiaVon(String thiGiaVon) {
        ThiGiaVon = thiGiaVon;
    }

    public void setCPLuuHanh(String CPLuuHanh) {
        this.CPLuuHanh = CPLuuHanh;
    }

    public void setCPNiemYet(String CPNiemYet) {
        this.CPNiemYet = CPNiemYet;
    }

    public void setTyLeCoTuc_ThiGia(String tyLeCoTuc_ThiGia) {
        TyLeCoTuc_ThiGia = tyLeCoTuc_ThiGia;
    }

    public void setGDKHQ(String GDKHQ) {
        this.GDKHQ = GDKHQ;
    }

    public void setEPS_4Quy(String EPS_4Quy) {
        this.EPS_4Quy = EPS_4Quy;
    }

    public void setEPS_Nam(String EPS_Nam) {
        this.EPS_Nam = EPS_Nam;
    }

    public void setROA(String ROA) {
        this.ROA = ROA;
    }

    public void setROE(String ROE) {
        this.ROE = ROE;
    }

    public void setDonBayTC(String donBayTC) {
        DonBayTC = donBayTC;
    }

    public void setPE(String PE) {
        this.PE = PE;
    }

    public void setPB(String PB) {
        this.PB = PB;
    }

    public void setBEta(String BEta) {
        this.Beta = BEta;
    }

    private String Ticker;
    private String NearByMin;
    private String NearByMax;
    private String Weight10Days;
    private String ThiGiaVon;
    private String CPLuuHanh;
    private String CPNiemYet;
    private String TyLeCoTuc_ThiGia;
    private String GDKHQ;
    private String EPS_4Quy;
    private String EPS_Nam;
    private String ROA;
    private String ROE;
    private String DonBayTC;
    private String PE;
    private String PB;
    private String Beta;

    public String getHtmlChart() {
        return __htmlChart;
    }

    public void setHtmlChart(String htmlChart) {
        this.__htmlChart = htmlChart;
    }

    private String __htmlChart;

    public String getStockName() {
        return __StockName;
    }

    public void setStockName(String __StockName) {
        this.__StockName = __StockName;
    }

    private String __StockName;

    public void setValue(String valuePrice, int i) {
        //valuePrice = valuePrice.replaceAll(",","");
        if(i>16) return;
        switch (i){
            case 0:
                NearByMin = valuePrice;
                return;
            case 1:
                NearByMax = valuePrice;
                return;
            case 2:
                Weight10Days = valuePrice;
                return;
            case 3:
                ThiGiaVon = valuePrice;
                return;
            case 4:
                CPLuuHanh = valuePrice;
                return;
            case 5:
                CPNiemYet = valuePrice;
                return;
            case 6:
                TyLeCoTuc_ThiGia = valuePrice;
                return;
            case 7:
                GDKHQ = valuePrice;
                return;
            case 8:
                EPS_4Quy = valuePrice;
                return;
            case 9:
                EPS_Nam = valuePrice;
                return;
            case 10:
                ROA = valuePrice;
                return;
            case 11:
                ROE = valuePrice;
                return;
            case 12:
                DonBayTC = valuePrice;
                return;
            case 13:
                PE = valuePrice;
                return;
            case 14:
                PB = valuePrice;
                return;
            case 15:
                Beta = valuePrice;
                return;
            default:
                break;
        }
    }
}
