package vn.co.vns.runningman.object;

import vn.co.vns.runningman.VolleyAPI.BaseView;

/**
 * Created by Thanh on 7/9/2017.
 */

public class Tick extends BaseView {
//    private String Ticker;
    private Float 	PriceCurrent;
    private Float PriceTarget;
    private Integer OptionTarget;
    private String 	Owe="";
    private String 	EPSYear="";
    private String 	EPSQuaterYear="";
    private String 	BookValue="";
    private String 	Organization="";
    private String 		Room="";
    private String 		Content="";
    private Integer FlagDel;

//    public String getTicker() {
//        return Ticker;
//    }
//
//    public void setTicker(String ticker) {
//        Ticker = ticker;
//    }

    public Float getPriceCurrent() {
        return PriceCurrent;
    }

    public void setPriceCurrent(Float priceCurrent) {
        PriceCurrent = priceCurrent;
    }

    public Float getPriceTarget() {
        return PriceTarget;
    }

    public void setPriceTarget(Float priceTarget) {
        PriceTarget = priceTarget;
    }

    public Integer getOptionTarget() {
        return OptionTarget;
    }

    public void setOptionTarget(Integer optionTarget) {
        OptionTarget = optionTarget;
    }

    public String getOwe() {
        return Owe;
    }

    public void setOwe(String owe) {
        Owe = owe;
    }

    public String getEPSYear() {
        return EPSYear;
    }

    public void setEPSYear(String EPSYear) {
        this.EPSYear = EPSYear;
    }

    public String getEPSQuaterYear() {
        return EPSQuaterYear;
    }

    public void setEPSQuaterYear(String EPSQuaterYear) {
        this.EPSQuaterYear = EPSQuaterYear;
    }

    public String getBookValue() {
        return BookValue;
    }

    public void setBookValue(String bookValue) {
        BookValue = bookValue;
    }

    public String getOrganization() {
        return Organization;
    }

    public void setOrganization(String organization) {
        Organization = organization;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Integer getFlagDel() {
        return FlagDel;
    }

    public void setFlagDel(Integer flagDel) {
        FlagDel = flagDel;
    }

}
