package vn.co.vns.runningman.object;

import vn.co.vns.runningman.util.SharedPreference;

/**
 * Created by hoangtuan on 4/7/17.
 */
public class StockObject {

    private String CodeStock;
    private String TopPrice;
    private String BottomPrice;
    private String TCPrice;
    private String BuyingPrice3;
    private String BuyingWeight3;
    private String BuyingPrice2;
    private String BuyingWeight2;
    private String BuyingPrice1;
    private String BuyingWeight1;
    private String KhopLenhPrice;
    private String KhopLenhWeight;
    private String GapPrice;
    private String SellingPrice3;
    private String SellingWeight3;
    private String SellingPrice2;
    private String SellingWeight2;
    private String SellingPrice1;
    private String SellingWeight1;
    private String HighPrice;
    private String LowPrice;
    private String TotalWeight;
    private String NNBuying;
    private String NNSelling;
    private String Rate;
    private Integer priorityOrder = 0;

    public StockObject() {

    }

    public String getCodeStock() {
        return CodeStock;
    }

    public void setCodeStock(String codeStock) {
        CodeStock = codeStock;
    }

    public String getTopPrice() {
        return TopPrice;
    }

    public void setTopPrice(String topPrice) {
        TopPrice = topPrice;
    }

    public String getBottomPrice() {
        return BottomPrice;
    }

    public void setBottomPrice(String bottomPrice) {
        BottomPrice = bottomPrice;
    }

    public String getTCPrice() {
        return TCPrice;
    }

    public void setTCPrice(String TCPrice) {
        this.TCPrice = TCPrice;
    }

    public String getBuyingPrice3() {
        return BuyingPrice3;
    }

    public void setBuyingPrice3(String buyingPrice3) {
        BuyingPrice3 = buyingPrice3;
    }

    public String getBuyingWeight3() {
        return BuyingWeight3;
    }

    public void setBuyingWeight3(String buyingWeight3) {
        BuyingWeight3 = buyingWeight3;
    }

    public String getBuyingPrice2() {
        return BuyingPrice2;
    }

    public void setBuyingPrice2(String buyingPrice2) {
        BuyingPrice2 = buyingPrice2;
    }

    public String getBuyingWeight2() {
        return BuyingWeight2;
    }

    public void setBuyingWeight2(String buyingWeight2) {
        BuyingWeight2 = buyingWeight2;
    }

    public String getBuyingPrice1() {
        return BuyingPrice1;
    }

    public void setBuyingPrice1(String buyingPrice1) {
        BuyingPrice1 = buyingPrice1;
    }

    public String getBuyingWeight1() {
        return BuyingWeight1;
    }

    public void setBuyingWeight1(String buyingWeight1) {
        BuyingWeight1 = buyingWeight1;
    }

    public String getKhopLenhPrice() {
        return KhopLenhPrice;
    }

    public void setKhopLenhPrice(String khopLenhPrice) {
        KhopLenhPrice = khopLenhPrice;
    }

    public String getKhopLenhWeight() {
        return KhopLenhWeight;
    }

    public void setKhopLenhWeight(String khopLenhWeight) {
        KhopLenhWeight = khopLenhWeight;
    }

    public String getGapPrice() {
        return GapPrice;
    }

    public void setGapPrice(String gapPrice) {
        GapPrice = gapPrice;
    }

    public String getSellingPrice3() {
        return SellingPrice3;
    }

    public void setSellingPrice3(String sellingPrice3) {
        SellingPrice3 = sellingPrice3;
    }

    public String getSellingWeight3() {
        return SellingWeight3;
    }

    public void setSellingWeight3(String sellingWeight3) {
        SellingWeight3 = sellingWeight3;
    }

    public String getSellingPrice2() {
        return SellingPrice2;
    }

    public void setSellingPrice2(String sellingPrice2) {
        SellingPrice2 = sellingPrice2;
    }

    public String getSellingWeight2() {
        return SellingWeight2;
    }

    public void setSellingWeight2(String sellingWeight2) {
        SellingWeight2 = sellingWeight2;
    }

    public String getSellingPrice1() {
        return SellingPrice1;
    }

    public void setSellingPrice1(String sellingPrice1) {
        SellingPrice1 = sellingPrice1;
    }

    public String getSellingWeight1() {
        return SellingWeight1;
    }

    public void setSellingWeight1(String sellingWeight1) {
        SellingWeight1 = sellingWeight1;
    }

    public String getHighPrice() {
        return HighPrice;
    }

    public void setHighPrice(String highPrice) {
        HighPrice = highPrice;
    }

    public String getLowPrice() {
        return LowPrice;
    }

    public void setLowPrice(String lowPrice) {
        LowPrice = lowPrice;
    }

    public String getTotalWeight() {
        return TotalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        TotalWeight = totalWeight;
    }

    public String getNNBuying() {
        return NNBuying;
    }

    public void setNNBuying(String NNBuying) {
        this.NNBuying = NNBuying;
    }

    public String getNNSelling() {
        return NNSelling;
    }

    public void setNNSelling(String NNSelling) {
        this.NNSelling = NNSelling;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public Integer getPriorityOrder() {
        return priorityOrder;
    }

    public void setPriorityOrder(Integer priorityOrder) {
        this.priorityOrder = priorityOrder;
    }

    public void setValue(String valuePrice, int i) {
        //valuePrice = valuePrice.replaceAll(",","");
        if (i > 36) return;
        switch (i) {
            case 0:
                CodeStock = valuePrice;
                if (SharedPreference.getInstance().getString("list_priority", "").contains(valuePrice))
                    priorityOrder = 1;
                return;
            case 2:
                TopPrice = valuePrice;
                return;
            case 3:
                BottomPrice = valuePrice;
                return;
            case 1:
                TCPrice = valuePrice;
                return;
            case 8:
                BuyingPrice3 = valuePrice;
                return;
            case 9:
                BuyingWeight3 = valuePrice;
                return;
            case 10:
                BuyingPrice2 = valuePrice;
                return;
            case 11:
                BuyingWeight2 = valuePrice;
                return;
            case 12:
                BuyingPrice1 = valuePrice;
                return;
            case 13:
                BuyingWeight1 = valuePrice;
                return;
            case 14:
                KhopLenhPrice = valuePrice;
                return;
            case 15:
                KhopLenhWeight = valuePrice;
//                Rate = valuePrice;
//                Rate = ((Float.valueOf(("".equalsIgnoreCase(KhopLenhPrice)) ? "0" : KhopLenhPrice) - Float.valueOf(("".equalsIgnoreCase(TCPrice)) ? "0" : TCPrice)) * 100) / (Float.valueOf(("".equalsIgnoreCase(TCPrice) ? "0" : TCPrice)));
                return;
            case 16:
                GapPrice = valuePrice;
                return;
            case 18:
                GapPrice = valuePrice;
                Rate = valuePrice;
                return;
            case 20:
                SellingPrice1 = valuePrice;
                return;
            case 21:
                SellingWeight1 = valuePrice;
                return;
            case 22:
                SellingPrice2 = valuePrice;
                return;
            case 23:
                SellingWeight2 = valuePrice;
                return;
            case 24:
                SellingPrice3 = valuePrice;
                return;
            case 25:
                SellingWeight3 = valuePrice;
                return;
            case 26:
                HighPrice = valuePrice;
                return;
            case 28:
                LowPrice = valuePrice;
                return;
//            case 24:
//                return;
            case 4:
                TotalWeight = valuePrice;
                return;
            case 32:
                NNBuying = valuePrice;
                return;
            case 33:
                NNSelling = valuePrice;
                return;
//            case 27:
//                NNBuying = valuePrice;
//                return;
//            case 28:
//                NNSelling = valuePrice;
            default:
                break;
        }
    }

    public void setValueHSX1(String valuePrice, int i) {
        //valuePrice = valuePrice.replaceAll(",","");
        if (i > 42) return;
        switch (i) {
            case 0:
                CodeStock = valuePrice;
                if (SharedPreference.getInstance().getString("list_priority", "").contains(valuePrice))
                    priorityOrder = 1;
                return;
            case 1:
                TCPrice = valuePrice;
                return;
            case 2:
                TopPrice = valuePrice;
                return;
            case 3:
                BottomPrice = valuePrice;
                return;
            case 4:
                BuyingPrice3 = valuePrice;
                return;
            case 5:
                BuyingWeight3 = valuePrice; //String.valueOf(Integer.parseInt(valuePrice)*100);
                return;
            case 6:
                BuyingPrice2 = valuePrice;
                return;
            case 7:
                BuyingWeight2 = valuePrice;
                return;
            case 8:
                BuyingPrice1 = valuePrice;
                return;
            case 9:
                BuyingWeight1 = valuePrice;
                return;
            case 12:
                Rate = valuePrice;
//                if(valuePrice!="")
//                    Rate=Float.valueOf(valuePrice);
//                else
//                    Rate=0.0f;
//                Rate = ((Float.valueOf(("".equalsIgnoreCase(KhopLenhPrice)) ? "0" : KhopLenhPrice) - Float.valueOf(("".equalsIgnoreCase(TCPrice)) ? "0" : TCPrice)) * 100) / (Float.valueOf(("".equalsIgnoreCase(TCPrice) ? "0" : TCPrice)));
//                Rate = (("".equalsIgnoreCase(valuePrice)) ? "0" : valuePrice);
//                -0.30 -2.82%
                return;
            case 10:
                KhopLenhPrice = valuePrice;
//                Rate = ((Float.valueOf(("".equalsIgnoreCase(KhopLenhPrice)) ? "0" : KhopLenhPrice) - Float.valueOf(("".equalsIgnoreCase(TCPrice)) ? "0" : TCPrice)) * 100) / (Float.valueOf(("".equalsIgnoreCase(TCPrice) ? "0" : TCPrice)));
                return;
            case 11:
                KhopLenhWeight = valuePrice;
//                Rate = ((Float.valueOf(("".equalsIgnoreCase(KhopLenhPrice)) ? "0" : KhopLenhPrice) - Float.valueOf(("".equalsIgnoreCase(TCPrice)) ? "0" : TCPrice)) * 100) / (Float.valueOf(("".equalsIgnoreCase(TCPrice) ? "0" : TCPrice)));
                return;
            case 19:
                TotalWeight = valuePrice;
                return;
            case 13:
                SellingPrice1 = valuePrice;
                return;
            case 14:
                SellingWeight1 = valuePrice;
                return;
            case 15:
                SellingPrice2 = valuePrice;
                return;
            case 16:
                SellingWeight2 = valuePrice;
                return;
            case 17:
                SellingPrice3 = valuePrice;
                return;
            case 18:
                SellingWeight3 = valuePrice;
                return;
            case  20:
                Rate = valuePrice;
                GapPrice = valuePrice;
                return;
            case 23:
                HighPrice = valuePrice;
                return;
            case 24:
                LowPrice = valuePrice;
                return;
//            case 23:
//                TotalWeight = valuePrice;
//                GapPrice = valuePrice;
//                return;
            case 25:
                NNBuying = valuePrice;
                return;
            case 26:
                NNSelling = valuePrice;
                return;
            default:
                break;
        }
    }

    public void setValueHNX1(String valuePrice, int i) {
        //valuePrice = valuePrice.replaceAll(",","");
        if (i > 28) return;
        switch (i) {
            case 0:
                CodeStock = valuePrice;
                if (SharedPreference.getInstance().getString("list_priority", "").contains(valuePrice))
                    priorityOrder = 1;
                return;
            case 1:
                TCPrice = valuePrice;
                return;
            case 2:
                TopPrice = valuePrice;
                return;
            case 3:
                BottomPrice = valuePrice;
                return;
            case 4:
                BuyingPrice3 = valuePrice;
                return;
            case 5:
                BuyingWeight3 = valuePrice; //String.valueOf(Integer.parseInt(valuePrice)*100);
                return;
            case 6:
                BuyingPrice2 = valuePrice;
                return;
            case 7:
                BuyingWeight2 = valuePrice;
                return;
            case 8:
                BuyingPrice1 = valuePrice;
                return;
            case 9:
                BuyingWeight1 = valuePrice;
                return;
            case 12:
                Rate = valuePrice;
//                Rate = valuePrice.split(" ")[1];
//                    Rate=Float.valueOf(valuePrice);
//                else
//                    Rate=0.0f;
//                Rate = ((Float.valueOf(("".equalsIgnoreCase(KhopLenhPrice)) ? "0" : KhopLenhPrice) - Float.valueOf(("".equalsIgnoreCase(TCPrice)) ? "0" : TCPrice)) * 100) / (Float.valueOf(("".equalsIgnoreCase(TCPrice) ? "0" : TCPrice)));
//                Rate = (("".equalsIgnoreCase(valuePrice)) ? "0" : valuePrice);
//                -0.30 -2.82%
                return;
            case 10:
                KhopLenhPrice = valuePrice;
//                Rate = ((Float.valueOf(("".equalsIgnoreCase(KhopLenhPrice)) ? "0" : KhopLenhPrice) - Float.valueOf(("".equalsIgnoreCase(TCPrice)) ? "0" : TCPrice)) * 100) / (Float.valueOf(("".equalsIgnoreCase(TCPrice) ? "0" : TCPrice)));
                return;
            case 11:
                KhopLenhWeight = valuePrice;
//                Rate = ((Float.valueOf(("".equalsIgnoreCase(KhopLenhPrice)) ? "0" : KhopLenhPrice) - Float.valueOf(("".equalsIgnoreCase(TCPrice)) ? "0" : TCPrice)) * 100) / (Float.valueOf(("".equalsIgnoreCase(TCPrice) ? "0" : TCPrice)));
                return;
            case 19:
                TotalWeight = valuePrice;
                return;
            case 13:
                SellingPrice1 = valuePrice;
                return;
            case 14:
                SellingWeight1 = valuePrice;
                return;
            case 15:
                SellingPrice2 = valuePrice;
                return;
            case 16:
                SellingWeight2 = valuePrice;
                return;
            case 17:
                SellingPrice3 = valuePrice;
                return;
            case 18:
                SellingWeight3 = valuePrice;
                return;
            case  20:
//                Rate = valuePrice;
//                GapPrice = valuePrice;
                return;
            case 23:
                HighPrice = valuePrice;
                return;
            case 24:
                LowPrice = valuePrice;
                return;
//            case 23:
//                TotalWeight = valuePrice;
//                GapPrice = valuePrice;
//                return;
            case 25:
                NNBuying = valuePrice;
                return;
            case 26:
                NNSelling = valuePrice;
                return;
            default:
                break;
        }
    }
}
