package vn.co.vns.runningman.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import vn.co.vns.runningman.object.Kqdq;
import vn.co.vns.runningman.object.PriceOnline;
import vn.co.vns.runningman.object.Stock;
import vn.co.vns.runningman.object.StockBigVolume;
import vn.co.vns.runningman.object.StockValue;
import vn.co.vns.runningman.object.StockVolumePriceAgreement;


public class MySQLiteHelper extends SQLiteOpenHelper {

    private final static String TAG = "MySQLiteHelper.";
    private Context context;

    // Database name
    private final static String DATABASE_NAME = "runningman";
    private static int DATABASE_VERSION = 1;


    // Category
    private final static String TABLE_STOCK_HN = "STOCK_HN";
    private final static String CL_STOCK_ID = "stockId";
    private final static String CL_TICKER = "ticker";
    private final static String CL_DATE = "date";
    private final static String CL_OPEN = "open";
    private final static String CL_HIGH = "high";
    private final static String CL_LOW = "low";
    private final static String CL_CLOSE = "close";
    private final static String CL_VOLUME = "volume";
    private final static String CL_IS_PLACE = "isplace";
    //Stock
    private final static String SQL_CREATE_TABLE_STOCK_HN = "create table " + TABLE_STOCK_HN + "("
            + "  " + CL_TICKER + " varchar(5) not null"
            + ", " + CL_STOCK_ID + " integer primary key"
            + ", " + CL_DATE + " varchar(100) not null"
            + ", " + CL_OPEN + " real not null"
            + ", " + CL_HIGH + " real not null"
            + ", " + CL_LOW + " real not null"
            + ", " + CL_CLOSE + " real not null"
            + ", " + CL_VOLUME + " integer not null"
            + ", " + CL_IS_PLACE + " integer not null"
            + "  " + ")";
    //Big Volume
    private final static String TABLE_STOCK_BIG_VOLUME = "STOCK_BIG_VOLUME";
    private final static String CL_AVERAGE = "average";
    private final static String CL_RATE = "rate";
    //private final static String CL_PERCENT = "percent";
    private final static String CL_INC_DES = "incdes";
    private final static String CL_VOLUME1 = "volume1";
    private final static String CL_VOLUME2 = "volume2";
    private final static String CL_VOLUME3 = "volume3";
    private final static String CL_VOLUME4 = "volume4";
    private final static String CL_VOLUME5 = "volume5";

    private final static String SQL_CREATE_TABLE_STOCK_BIG_VOLUME = "create table " + TABLE_STOCK_BIG_VOLUME + "("
            + "  " + CL_TICKER + " varchar(5) not null"
            + ", " + CL_STOCK_ID + " integer primary key"
            + ", " + CL_DATE + " varchar(100) not null"
            + ", " + CL_INC_DES + " real not null"
            + ", " + CL_RATE + " real not null"
            + ", " + CL_AVERAGE + " real not null"
            + ", " + CL_CLOSE + " real not null"
            //	+ ", " + CL_PERCENT + " integer null"
            + ", " + CL_VOLUME + " integer not null"
            + ", " + CL_VOLUME1 + " integer not null"
            + ", " + CL_VOLUME2 + " integer not null"
            + ", " + CL_VOLUME3 + " integer not null"
            + ", " + CL_VOLUME4 + " integer not null"
            + ", " + CL_VOLUME5 + " integer not null"
            + ", " + CL_IS_PLACE + " integer not null"
            + "  " + ")";

    //Volume Price Agreement
    private final static String TABLE_STOCK_VOLUME_PRICE_AGREEMENT = "STOCK_VOLUME_PRICE_AGREEMENT";
    private final static String CL_AVERAGE_VOLUME_PRICE = "average";
    private final static String CL_INC_DES_VOLUME_PRICE = "incdes";
    private final static String CL_PRICE1_VOLUME_PRICE = "price1";
    private final static String CL_PRICE2_VOLUME_PRICE = "price2";
    private final static String CL_PRICE3_VOLUME_PRICE = "price3";
    private final static String CL_VOLUME1_VOLUME_PRICE = "volume1";
    private final static String CL_VOLUME2_VOLUME_PRICE = "volume2";
    private final static String CL_VOLUME3_VOLUME_PRICE = "volume3";

    private final static String SQL_CREATE_TABLE_STOCK_VOLUME_PRICE_AGREEMENT = "create table " + TABLE_STOCK_VOLUME_PRICE_AGREEMENT + "("
            + "  " + CL_TICKER + " varchar(5) not null"
            + ", " + CL_STOCK_ID + " integer primary key"
            + ", " + CL_DATE + " varchar(100) not null"
            + ", " + CL_INC_DES_VOLUME_PRICE + " real not null"
            + ", " + CL_AVERAGE_VOLUME_PRICE + " real not null"
            + ", " + CL_CLOSE + " real not null"
            + ", " + CL_VOLUME + " integer not null"
            + ", " + CL_VOLUME1_VOLUME_PRICE + " integer not null"
            + ", " + CL_VOLUME2_VOLUME_PRICE + " integer not null"
            + ", " + CL_VOLUME3_VOLUME_PRICE + " integer not null"
            + ", " + CL_PRICE1_VOLUME_PRICE + " real not null"
            + ", " + CL_PRICE2_VOLUME_PRICE + " real not null"
            + ", " + CL_PRICE3_VOLUME_PRICE + " real not null"
            + ", " + CL_IS_PLACE + " integer not null"
            + "  " + ")";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE_TABLE_STOCK_HN);
            db.execSQL(SQL_CREATE_TABLE_STOCK_BIG_VOLUME);
            db.execSQL(SQL_CREATE_TABLE_STOCK_VOLUME_PRICE_AGREEMENT);
            db.execSQL(new SqlIO().toTable(Kqdq.class));
            db.execSQL(new SqlIO().toTable(PriceOnline.class));
            //db.execSQL(new SqlIO().toTable(StockValue.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SQL_CREATE_TABLE_STOCK_HN);
        onCreate(db);
    }

    public void addFincialItem(Kqdq item) {
        ContentValues values = createKQDQValue(item);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("KQDQ", null, values);
        db.close();
    }

    public ContentValues createKQDQValue(Kqdq item) {
        ContentValues values = new ContentValues();
        values.put("ticker", item.getTicker());
        values.put("year", item.getYear());
        values.put("content", item.getContent());
        values.put("Q1", item.getQ1());
        values.put("Q2", item.getQ2());
        values.put("Q3", item.getQ3());
        values.put("Q4", item.getQ4());
        values.put("Total", item.getTotal());
        return values;
    }

    public void addStockValue(StockValue stockValue) {
        ContentValues values = createStockValue(stockValue);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("STOCKVALUE", null, values);
        db.close();
    }

    private ContentValues createStockValue(StockValue stockValue) {
        ContentValues values = new ContentValues();
        values.put("Ticker", stockValue.getTicker());
        values.put("NearByMin", stockValue.getNearByMin());
        values.put("NearByMax", stockValue.getNearByMax());
        values.put("Weight10Days", stockValue.getWeight10Days());
        values.put("ThiGiaVon", stockValue.getThiGiaVon());
        values.put("CPLuuHanh", stockValue.getCPLuuHanh());
        values.put("CPNiemYet", stockValue.getCPNiemYet());
        values.put("TyLeCoTuc_ThiGia", stockValue.getTyLeCoTuc_ThiGia());
        values.put("GDKHQ", stockValue.getGDKHQ());
        values.put("EPS_4Quy", stockValue.getEPS_4Quy());
        values.put("EPS_Nam", stockValue.getEPS_Nam());
        values.put("ROA", stockValue.getROA());
        values.put("ROE", stockValue.getROE());
        values.put("DonBayTC", stockValue.getDonBayTC());
        values.put("PE", stockValue.getPE());
        values.put("PB", stockValue.getPB());
        values.put("Beta", stockValue.getBEta());
        return values;
    }

    /**
     * First Start Application
     */
    public boolean insertStock(ArrayList<Stock> arrStocks) {
        boolean isSuccess = false;
        Log.d("Insert Stock:", "Begin insert Data Conversations");
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            SQLiteStatement insert =
                    db.compileStatement("insert into " + TABLE_STOCK_HN
                            + "(" + CL_TICKER
                            + "," + CL_DATE
                            + "," + CL_OPEN
                            + "," + CL_HIGH
                            + "," + CL_LOW
                            + "," + CL_CLOSE
                            + "," + CL_VOLUME
                            + "," + CL_IS_PLACE + ")"
                            + " values " + "(?,?,?,?,?,?,?,?)");

            for (Stock objStock : arrStocks) {
                //bind the 1-indexed ?'s to the values specified

                //System.out.println(value.getStrId());

                insert.bindString(1, objStock.getTicker());
                insert.bindString(2, objStock.getDate());
                insert.bindDouble(3, objStock.getOpen());
                insert.bindDouble(4, objStock.getHigh());
                insert.bindDouble(5, objStock.getLow());
                insert.bindDouble(6, objStock.getClose());
                insert.bindDouble(7, objStock.getVolume());
                insert.bindLong(8, objStock.getIsPlace());
                insert.execute();
            }

            db.setTransactionSuccessful();
            isSuccess = true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            db.endTransaction();
            if (db.isOpen()) {
                db.close();
                isSuccess = true;
            }
        }
        Log.d("Insert Stock:", "Finish insert Data Conversations");
        return isSuccess;
    }

    /**
     * get list max price of stock .
     */
    public ArrayList<Stock> getListMMaxPriceStock(String currentDate, String endDate) {
        ArrayList<Stock> arrStocks = null;
        String strSQL = "SELECT MAX( " + CL_CLOSE + " ) AS PRICE, " + CL_TICKER + " FROM " + TABLE_STOCK_HN + " WHERE  LENGTH(" + CL_TICKER + ")=3 AND " + CL_DATE + "!=" + currentDate;
        if (endDate != null) strSQL = strSQL + " AND " + CL_DATE + ">=" + endDate;
        strSQL = strSQL + " GROUP BY " + CL_TICKER;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Log.d("getListMMaxPriceStock", strSQL);
            Stock objStock = null;
            Cursor cursor = db.rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                arrStocks = new ArrayList<Stock>();
                do {
                    objStock = new Stock();
                    objStock.setTicker(cursor.getString(1));
                    objStock.setClose(Float.valueOf(cursor.getString(0)));
                    arrStocks.add(objStock);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("getListMMaxPriceStock", "Error: " + e.getMessage());
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
        return arrStocks;
    }

    /**
     * get list stock for insert other tables.
     */
    public ArrayList<Stock> getStockDate(String dateMax, Integer volume, Float startPrice, Float endPrice) {
        ArrayList<Stock> arrStocks = null;
        String strSQL = "SELECT * FROM " + TABLE_STOCK_HN + " WHERE  LENGTH(" + CL_TICKER + ")=3 AND " + CL_DATE + "= '" + dateMax + "'";
        if (volume >= 0) strSQL = strSQL + " AND " + CL_VOLUME + ">=" + volume;
        if (startPrice >= 0) strSQL = strSQL + " AND " + CL_CLOSE + ">=" + startPrice;
        if (endPrice > 0) strSQL = strSQL + " AND " + CL_CLOSE + "<=" + endPrice;
        strSQL = strSQL + " ORDER BY " + CL_DATE + " DESC ";
        Log.d(TAG, strSQL);
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Log.d("VolumePriceAgreement", strSQL);
            arrStocks = new ArrayList<Stock>();
            Cursor cursor = db.rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                do {
                    Stock objStock = new Stock();
                    objStock.setStockId(Integer.parseInt(cursor.getString(1)));
                    objStock.setTicker(cursor.getString(0));
                    objStock.setDate(cursor.getString(2));
                    objStock.setOpen(Float.valueOf(cursor.getString(3)));
                    objStock.setHigh(Float.valueOf(cursor.getString(4)));
                    objStock.setLow(Float.valueOf(cursor.getString(5)));
                    objStock.setClose(Float.valueOf(cursor.getString(6)));
                    objStock.setVolume(Integer.parseInt(cursor.getString(7)));
                    objStock.setIsPlace(Integer.parseInt(cursor.getString(8)));
                    arrStocks.add(objStock);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("VolumePriceAgreement", "Error: " + e.getMessage());
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
        return arrStocks;
    }

    /**
     * get list stock transaction BigVolume.
     */
    public ArrayList<StockBigVolume> getStockDateBigVolume(String dateMax, Integer volume, Float startPrice, Float endPrice) {
        ArrayList<StockBigVolume> arrStocks = null;
        String strSQL = "SELECT * FROM " + TABLE_STOCK_HN + " INNER JOIN " + TABLE_STOCK_BIG_VOLUME + " ON " + TABLE_STOCK_HN + "." + CL_TICKER + "=" + TABLE_STOCK_BIG_VOLUME + "." + CL_TICKER + " WHERE  LENGTH(" + TABLE_STOCK_HN + "." + CL_TICKER + ")=3 AND " + TABLE_STOCK_HN + "." + CL_DATE + "= '" + dateMax + "'";
        strSQL = strSQL + " AND " + TABLE_STOCK_BIG_VOLUME + "." + CL_RATE + ">=2";
        if (volume >= 0)
            strSQL = strSQL + " AND " + TABLE_STOCK_HN + "." + CL_VOLUME + ">=" + volume;
        if (startPrice >= 0)
            strSQL = strSQL + " AND " + TABLE_STOCK_HN + "." + CL_CLOSE + ">=" + startPrice;
        if (endPrice > 0)
            strSQL = strSQL + " AND " + TABLE_STOCK_HN + "." + CL_CLOSE + "<=" + endPrice;
        strSQL = strSQL + " ORDER BY " + TABLE_STOCK_HN + "." + CL_DATE + " DESC ";
        Log.d(TAG, strSQL);
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Log.d("getStockDateBigVolume", strSQL);
            arrStocks = new ArrayList<StockBigVolume>();
            Cursor cursor = db.rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                do {
                    StockBigVolume objStock = new StockBigVolume();
                    objStock.setStockId(Integer.parseInt(cursor.getString(1)));
                    objStock.setTicker(cursor.getString(0));
                    objStock.setDate(cursor.getString(2));
                    objStock.setOpen(Float.valueOf(cursor.getString(3)));
                    objStock.setHigh(Float.valueOf(cursor.getString(4)));
                    objStock.setLow(Float.valueOf(cursor.getString(5)));
                    objStock.setClose(Float.valueOf(cursor.getString(6)));
                    objStock.setVolume(Integer.parseInt(cursor.getString(7)));
                    objStock.setAver(Float.valueOf(cursor.getString(14)));
                    objStock.setIsPlace(Integer.parseInt(cursor.getString(8)));
                    objStock.setIncDes(Float.valueOf(cursor.getString(12))); //rate
                    objStock.setRate(Float.valueOf(cursor.getString(13))); //2
                    arrStocks.add(objStock);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("VolumePriceAgreement", "Error: " + e.getMessage());
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
        return arrStocks;
    }

    /**
     * Big Volume
     */
    public ArrayList<StockBigVolume> getListStockBigVolume(int volume, int rate) {
        ArrayList<StockBigVolume> arrStocks = null;
        //String strSQL="";
        String strSQL = "SELECT * FROM " + TABLE_STOCK_BIG_VOLUME + " WHERE 1=1 ";
        if (volume != -1) {
            strSQL = strSQL + " AND " + CL_VOLUME + ">=" + volume;
        }
        if (rate != -1) {
            strSQL = strSQL + " AND " + CL_RATE + ">=" + rate;
        }
        strSQL = strSQL + " ORDER BY " + CL_RATE + " DESC";
        Log.d(TAG, strSQL);
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Log.d("getListStockBigVolume", strSQL);
            StockBigVolume objStock = null;
            Cursor cursor = db.rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                arrStocks = new ArrayList<StockBigVolume>();
                do {
                    objStock = new StockBigVolume();
                    objStock.setStockId(Integer.parseInt(cursor.getString(1)));
                    objStock.setTicker(cursor.getString(0));
                    objStock.setDate(cursor.getString(2));
                    objStock.setIncDes(Float.valueOf(cursor.getString(3)));
                    objStock.setRate(Float.parseFloat(cursor.getString(4)));
                    Log.d("Rate 1: ", cursor.getString(4));
                    objStock.setAver(Float.valueOf(cursor.getString(5)));
                    objStock.setClose(Float.valueOf(cursor.getString(6)));
                    //objStock.setVolume(Integer.parseInt(String.valueOf((cursor.getString(7)!=null) ? cursor.getString(7) : 0)));
                    //objStock.setPercent(Integer.parseInt(cursor.getString(7)));
                    objStock.setVolume(Integer.parseInt(cursor.getString(7)));
                    objStock.setVolume1(Integer.parseInt(cursor.getString(8)));
                    objStock.setVolume2(Integer.parseInt(cursor.getString(9)));
                    objStock.setVolume3(Integer.parseInt(cursor.getString(10)));
                    objStock.setVolume4(Integer.parseInt(cursor.getString(11)));
                    objStock.setVolume5(Integer.parseInt(cursor.getString(12)));
                    objStock.setIsPlace(Integer.parseInt(cursor.getString(13)));
                    arrStocks.add(objStock);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("getListStockBigVolume", "Error: " + e.getMessage());
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
        Log.e("Finish BigVolume", "OK ");
        return arrStocks;
    }

    /*
     *
     * get ticker for Object StockBigVolume
     * */
    public StockBigVolume getInforStock(String ticker, int numberDay) {
        long volomeTotal = 0;
        int count = 0;
        float priceCurrent = 0;
        float volumeCurrent = 0;
        float priceBeforeCurrent = 0;
        Log.d("getInforStock", "Start OK");
//        String strSQL = "SELECT  ifnull(" + CL_TICKER + ", '" + ticker + "') as ID, " + CL_STOCK_ID + ",A." + CL_DATE + ", " + CL_OPEN + ", " + CL_HIGH + ", " + CL_LOW + " , " + CL_CLOSE + ", ifnull( " + CL_VOLUME + ", 0) as Value, " + CL_IS_PLACE + " from ((select distinct " + CL_DATE + " from " + TABLE_STOCK_HN + " WHERE " + CL_TICKER + "!='E1VFVN31'  ORDER BY " + CL_DATE + " DESC ) A LEFT JOIN (SELECT * FROM " + TABLE_STOCK_HN + " WHERE " + CL_TICKER + "='" + ticker + "' ORDER BY " + CL_DATE + " DESC) B ON A." + CL_DATE + "=B." + CL_DATE + ") limit " + numberDay;
        //String strSQL="SELECT "+ CL_TICKER + "," + CL_DATE  + " FROM "  + TABLE_STOCK_HN + " WHERE "+ CL_TICKER +" = 'E1VFVN31'";
        String strSQL = "SELECT  ifnull(" + CL_TICKER + ", '" + ticker + "') as ID, " + CL_STOCK_ID + "," + CL_DATE + ", " + CL_OPEN + ", " + CL_HIGH + ", " + CL_LOW + " , " + CL_CLOSE + ", ifnull( " + CL_VOLUME + ", 0) as Value, " + CL_IS_PLACE + " FROM " + TABLE_STOCK_HN + " WHERE " + CL_TICKER + "=SUBSTR('" + ticker + "',1,3) ORDER BY " + CL_DATE + " DESC";
        Log.d(TAG, strSQL);
        SQLiteDatabase db = this.getReadableDatabase();
        StockBigVolume objStock = new StockBigVolume();
        try {
//            Log.d("getInforStock", strSQL);
            Cursor cursor = db.rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                do {
                    count++;
                    //Log.e("getInforStock"," STT: " + count + " Code: " + cursor.getString(0) + " Date: " + cursor.getString(1));
                    Log.e("getInforStock", " STT: " + count + " Code: " + cursor.getString(0) + " Date: " + cursor.getString(2) + " Price: " + cursor.getString(6) + " Volume: " + cursor.getString(7));
                    if (count == 1) {
                        objStock.setStockId(Integer.parseInt(cursor.getString(1)));
                        objStock.setTicker(cursor.getString(0));
                        objStock.setDate(cursor.getString(2));
                        objStock.setOpen(Float.valueOf(cursor.getString(3)));
                        objStock.setHigh(Float.valueOf(cursor.getString(4)));
                        objStock.setLow(Float.valueOf(cursor.getString(5)));
                        objStock.setClose(Float.valueOf(cursor.getString(6)));
                        objStock.setVolume(Integer.parseInt(cursor.getString(7)));
                        objStock.setIsPlace(Integer.parseInt(cursor.getString(8)));
                        priceCurrent = Float.valueOf(cursor.getString(6));
                        volumeCurrent = Integer.parseInt(cursor.getString(7));
                    } else if (count == 2) {
                        priceBeforeCurrent = (cursor.getString(6) != null) ? Float.valueOf(cursor.getString(6)) : 0;
                        objStock.setIncDes(priceCurrent - priceBeforeCurrent);
                        objStock.setVolume1(Integer.parseInt(cursor.getString(7)));
                    } else if (count == 3) {
                        objStock.setVolume2(Integer.parseInt(cursor.getString(7)));
                    } else if (count == 4) {
                        objStock.setVolume3(Integer.parseInt(cursor.getString(7)));
                    } else if (count == 5) {
                        objStock.setVolume4(Integer.parseInt(cursor.getString(7)));
                    } else if (count == 6) {
                        objStock.setVolume5(Integer.parseInt(cursor.getString(7)));
                    }
                    if (count != 1)
                        volomeTotal += Integer.parseInt(cursor.getString(7));
                    if (count == numberDay + 1) {
                        float averVolume = (float) (volomeTotal / numberDay);
                        objStock.setRate((float) Math.round(volumeCurrent / averVolume));
                        objStock.setAver(averVolume);
                        break;
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("getInforStock", "Error: " + e.getMessage());
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
        return objStock;
    }

    /*
     *get information price and volume of stock agrement
     *
     * */
    public StockVolumePriceAgreement getInforStockVolumePriceAgreement(String ticker) {
        long volomeTotal = 0;
        int count = 0;
        int countNumberDay = 10;
        float priceCurrent = 0;
        float priceBeforeCurrent = 0;
        String strSQL = "SELECT * FROM " + TABLE_STOCK_HN + " WHERE " + CL_TICKER + "='" + ticker + "' ORDER BY " + CL_DATE + " DESC ";
        SQLiteDatabase db = this.getReadableDatabase();
        StockVolumePriceAgreement objStockVolumePriceAgreement = new StockVolumePriceAgreement();
        try {
            Log.d("VolumePriceAgreement", strSQL);
            Cursor cursor = db.rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                do {
                    count++;
                    if (count == 1) {
                        objStockVolumePriceAgreement.setStockId(Integer.parseInt(cursor.getString(1)));
                        objStockVolumePriceAgreement.setTicker(cursor.getString(0));
                        objStockVolumePriceAgreement.setDate(cursor.getString(2));
                        objStockVolumePriceAgreement.setOpen(Float.valueOf(cursor.getString(3)));
                        objStockVolumePriceAgreement.setHigh(Float.valueOf(cursor.getString(4)));
                        objStockVolumePriceAgreement.setLow(Float.valueOf(cursor.getString(5)));
                        objStockVolumePriceAgreement.setClose(Float.valueOf(cursor.getString(6)));
                        objStockVolumePriceAgreement.setVolume(Integer.parseInt(cursor.getString(7)));
                        objStockVolumePriceAgreement.setIsPlace(Integer.parseInt(cursor.getString(8)));
                        priceCurrent = Float.valueOf(cursor.getString(6));
                    } else if (count == 2) {
                        priceBeforeCurrent = Float.valueOf(cursor.getString(6));
                        objStockVolumePriceAgreement.setIncDes(priceCurrent - priceBeforeCurrent);
                        objStockVolumePriceAgreement.setVolume1(Integer.parseInt(cursor.getString(7)));
                        objStockVolumePriceAgreement.setPrice1(Float.valueOf(cursor.getString(6)));
                    } else if (count == 3) {
                        objStockVolumePriceAgreement.setVolume2(Integer.parseInt(cursor.getString(7)));
                        objStockVolumePriceAgreement.setPrice2(Float.valueOf(cursor.getString(6)));
                    } else if (count == 4) {
                        objStockVolumePriceAgreement.setVolume3(Integer.parseInt(cursor.getString(7)));
                        objStockVolumePriceAgreement.setPrice3(Float.valueOf(cursor.getString(6)));
                    }
                    if (count != 1)
                        volomeTotal += Integer.parseInt(cursor.getString(7));
                    if (count == countNumberDay + 1) {
                        float averVolume = (float) (volomeTotal / countNumberDay);
                        objStockVolumePriceAgreement.setAver(averVolume);
                        break;
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("VolumePriceAgreement", "Error: " + e.getMessage());
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
        return objStockVolumePriceAgreement;
    }

    /**
     * Insert table Big volume
     */
    public void insertStockBigVolume(ArrayList<Stock> arrStocks) {

        Log.d("Insert StockBigVolume:", "Begin insert Data Conversations");
        if (arrStocks != null) {
            for (Stock objStock : arrStocks) {
                StockBigVolume objBigStock = getInforStock(objStock.getTicker(), 10);
                if (objBigStock.getAver() > 0) {
                    SQLiteDatabase db = this.getWritableDatabase();
                    db.beginTransaction();
                    try {
                        SQLiteStatement insert =
                                db.compileStatement("insert into " + TABLE_STOCK_BIG_VOLUME
                                        + "(" + CL_TICKER
                                        + "," + CL_STOCK_ID
                                        + "," + CL_DATE
                                        + "," + CL_INC_DES
                                        + "," + CL_RATE
                                        + "," + CL_AVERAGE
                                        + "," + CL_CLOSE
                                        //+ "," + CL_PERCENT
                                        + "," + CL_VOLUME
                                        + "," + CL_VOLUME1
                                        + "," + CL_VOLUME2
                                        + "," + CL_VOLUME3
                                        + "," + CL_VOLUME4
                                        + "," + CL_VOLUME5
                                        + "," + CL_IS_PLACE + ")"
                                        + " values " + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

                        insert.bindString(1, objBigStock.getTicker());
                        insert.bindLong(2, objBigStock.getStockId());
                        insert.bindString(3, objBigStock.getDate());
                        insert.bindDouble(4, objBigStock.getIncDes());
                        insert.bindDouble(5, objBigStock.getRate());
                        Log.d("Rate: ", String.valueOf(objBigStock.getRate()));
                        insert.bindDouble(6, objBigStock.getAver());
                        insert.bindDouble(7, objBigStock.getClose());
                        //insert.bindDouble(7, 1);
                        insert.bindDouble(8, objBigStock.getVolume());
                        insert.bindLong(9, objBigStock.getVolume1());
                        insert.bindDouble(10, objBigStock.getVolume2());
                        insert.bindDouble(11, objBigStock.getVolume3());
                        insert.bindDouble(12, objBigStock.getVolume4());
                        insert.bindDouble(13, objBigStock.getVolume5());
                        insert.bindLong(14, objBigStock.getIsPlace());
                        insert.execute();
                        Log.d("Insert StockBigVolume: ", objBigStock.getTicker());
                        db.setTransactionSuccessful();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    } finally {
                        db.endTransaction();
                        if (db.isOpen()) {
                            db.close();
                        }
                    }
                    //}
                    db.close();
                }
            }
        }
        Log.d("Insert StockBigVolume:", "Finish insert Data Conversations");
    }

    /**
     * Insert table volume price agreement
     */
    public void insertStockVolumePriceAgreement(ArrayList<Stock> arrStocks) {

        Log.d("Insert Stock:", "Begin insert Data Conversations");

        if (arrStocks != null) {
            for (Stock objStock : arrStocks) {
                StockVolumePriceAgreement objStockVolumePriceAgreement = getInforStockVolumePriceAgreement(objStock.getTicker());
                SQLiteDatabase db = this.getWritableDatabase();
                db.beginTransaction();
                try {
                    SQLiteStatement insert =
                            db.compileStatement("insert into " + TABLE_STOCK_VOLUME_PRICE_AGREEMENT
                                    + "(" + CL_TICKER
                                    //+ "," + CL_STOCK_ID
                                    + "," + CL_DATE
                                    + "," + CL_INC_DES
                                    + "," + CL_AVERAGE
                                    + "," + CL_CLOSE
                                    + "," + CL_VOLUME
                                    + "," + CL_PRICE1_VOLUME_PRICE
                                    + "," + CL_PRICE2_VOLUME_PRICE
                                    + "," + CL_PRICE3_VOLUME_PRICE
                                    + "," + CL_VOLUME1_VOLUME_PRICE
                                    + "," + CL_VOLUME2_VOLUME_PRICE
                                    + "," + CL_VOLUME3_VOLUME_PRICE
                                    + "," + CL_IS_PLACE + ")"
                                    + " values " + "(?,?,?,?,?,?,?,?,?,?,?,?,?)");

                    insert.bindString(1, objStockVolumePriceAgreement.getTicker());
                    //insert.bindLong(2, objStockVolumePriceAgreement.getStockId());
                    insert.bindString(2, objStockVolumePriceAgreement.getDate());
                    insert.bindDouble(3, objStockVolumePriceAgreement.getIncDes());
                    insert.bindDouble(4, objStockVolumePriceAgreement.getAver());
                    insert.bindDouble(5, objStockVolumePriceAgreement.getClose());
                    insert.bindDouble(6, objStockVolumePriceAgreement.getVolume());
                    insert.bindDouble(7, objStockVolumePriceAgreement.getPrice1());
                    insert.bindDouble(8, objStockVolumePriceAgreement.getPrice2());
                    insert.bindDouble(9, objStockVolumePriceAgreement.getPrice3());
                    insert.bindLong(10, objStockVolumePriceAgreement.getVolume1());
                    insert.bindDouble(11, objStockVolumePriceAgreement.getVolume2());
                    insert.bindDouble(12, objStockVolumePriceAgreement.getVolume3());
                    insert.bindLong(13, objStockVolumePriceAgreement.getIsPlace());
                    insert.execute();
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                } finally {
                    db.endTransaction();
                    if (db.isOpen()) {
                        db.close();
                    }
                }
                //}
                db.close();
            }
        }
        Log.d("Insert Stock:", "Finish insert Data Conversations");
    }

    /**
     * Select Volume price Agreement
     */
    public ArrayList<StockVolumePriceAgreement> getListStockVolumePriceAgreement() {
        ArrayList<StockVolumePriceAgreement> arrStockVolumePriceAgreements = null;
        String strSQL = "SELECT * FROM " + TABLE_STOCK_VOLUME_PRICE_AGREEMENT + " ORDER BY " + CL_INC_DES + " DESC LIMIT 30";
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Log.d("VolumePriceAgreement", strSQL);
            StockVolumePriceAgreement objStock = null;
            Cursor cursor = db.rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                arrStockVolumePriceAgreements = new ArrayList<StockVolumePriceAgreement>();
                do {
                    objStock = new StockVolumePriceAgreement();
                    objStock.setStockId(Integer.parseInt(cursor.getString(1)));
                    objStock.setTicker(cursor.getString(0));
                    objStock.setDate(cursor.getString(2));
                    objStock.setIncDes(Float.valueOf(cursor.getString(3)));
                    objStock.setAver(Float.valueOf(cursor.getString(4)));
                    objStock.setClose(Float.valueOf(cursor.getString(5)));
                    objStock.setPrice1(Float.valueOf(cursor.getString(6)));
                    objStock.setPrice2(Float.valueOf(cursor.getString(7)));
                    objStock.setPrice3(Float.valueOf(cursor.getString(8)));
                    objStock.setVolume(Integer.parseInt(cursor.getString(9)));
                    objStock.setVolume1(Integer.parseInt(cursor.getString(10)));
                    objStock.setVolume2(Integer.parseInt(cursor.getString(11)));
                    objStock.setVolume3(Integer.parseInt(cursor.getString(12)));
                    objStock.setIsPlace(Integer.parseInt(cursor.getString(13)));
                    arrStockVolumePriceAgreements.add(objStock);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("VolumePriceAgreement", "Error: " + e.getMessage());
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
        Log.e("Finish BigVolume", "OK ");
        return arrStockVolumePriceAgreements;
    }

    /**
     * Select Volume price Agreement
     */
    public String getMaxDayUpdate() {
        String strSQL = "SELECT " + CL_DATE + " FROM " + TABLE_STOCK_HN + " ORDER BY " + CL_DATE + " DESC ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(strSQL, null);
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursor.getString(0);
            } else {
                return null;
            }
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public void deleteDataTable(String nameTable) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + nameTable); //delete all rows in a table
        db.close();
    }

    public void deleteStock(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STOCK_HN + " WHERE " + CL_DATE + "='" + date + "'"); //delete all rows in a table
        db.close();
    }
}
