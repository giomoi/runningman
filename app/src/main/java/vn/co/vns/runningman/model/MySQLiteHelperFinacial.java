package vn.co.vns.runningman.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import vn.co.vns.runningman.object.BusinessResultsObject;
import vn.co.vns.runningman.object.Kqdq;
import vn.co.vns.runningman.object.PriceOnline;
import vn.co.vns.runningman.object.Stock;
import vn.co.vns.runningman.object.StockBigVolume;
import vn.co.vns.runningman.object.StockValue;
import vn.co.vns.runningman.object.StockVolumePriceAgreement;


public class MySQLiteHelperFinacial extends SQLiteOpenHelper {

	private final static String TAG = "MySQLiteHelper.";
	private Context context;

	// Database name
	private final static String DATABASE_NAME = "runningman";
	private static int DATABASE_VERSION = 1;


	// Category
	private final static String TABLE_BUSINESS_RESULTS_YEARS = "BUSINESS_RESULTS_YEARS";
	private final static String CL_STOCK_ID = "stockId";
	private final static String CL_TICKER = "ticker";
	private final static String CL_YEAR = "year";
	private final static String CL_EPS = "eps";
	private final static String CL_PE = "pe";
	private final static String CL_VOLUME = "volume";
	private final static String CL_NET_REVENUE = "net_revenue";
	private final static String CL_PROFIT_AFTER_TAX = "profit_after_tax";
	private final static String CL_EPS_NOT_ADJUSTED = "eps_not_adjusted";
	private final static String CL_PRICE_ENDING = "price_ending";
	private final static String CL_BOOK_VALUE = "book_value";
	//Stock
	private final static String SQL_TABLE_BUSINESS_RESULTS_YEARS = "create table " + TABLE_BUSINESS_RESULTS_YEARS + "("
			+ "  " + CL_TICKER + " varchar(5) not null"
			+ ", " + CL_STOCK_ID + " integer primary key"
            + ", " + CL_YEAR + " varchar(100) not null"
			+ ", " + CL_EPS + " real not null"
			+ ", " + CL_PE + " real not null"
			+ ", " + CL_NET_REVENUE + " real not null"
			+ ", " + CL_PROFIT_AFTER_TAX + " real not null"
			+ ", " + CL_PRICE_ENDING + " real not null"
			+ ", " + CL_BOOK_VALUE + " real not null"
			+ ", " + CL_VOLUME + " real not null"
			+ ", " + CL_EPS_NOT_ADJUSTED + " real not null"
			+ "  " + ")";


	public MySQLiteHelperFinacial(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(SQL_TABLE_BUSINESS_RESULTS_YEARS);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + SQL_TABLE_BUSINESS_RESULTS_YEARS);
		onCreate(db);
	}

	/**
	 * Insert table
	 */
	public boolean insertBusinessResults(ArrayList<BusinessResultsObject> listBussinessResults) {
		boolean isSuccess=false;
		Log.d("Insert BUSINESS_RESULTS", "Begin insert Data");
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		try {
			SQLiteStatement insert =
					db.compileStatement("insert into " + TABLE_BUSINESS_RESULTS_YEARS
							+ "(" + CL_TICKER
							+ "," + CL_YEAR
							+ "," + CL_EPS
							+ "," + CL_PE
							+ "," + CL_NET_REVENUE
							+ "," + CL_PROFIT_AFTER_TAX
							+ "," + CL_PRICE_ENDING
							+ "," + CL_BOOK_VALUE
							+ "," + CL_VOLUME
							+ "," + CL_EPS_NOT_ADJUSTED + ")"
							+" values " + "(?,?,?,?,?,?,?,?,?,?)");

			for (BusinessResultsObject objBusinessResult : listBussinessResults){
				//System.out.println(value.getStrId());

				insert.bindString(1, objBusinessResult.getTicker());
				insert.bindString(2, objBusinessResult.getYear());
				insert.bindDouble(3, objBusinessResult.getEps());
				insert.bindDouble(4, objBusinessResult.getPe());
				insert.bindDouble(5, objBusinessResult.getNetRevenue());
				insert.bindDouble(6, objBusinessResult.getProfitAfterTax());
				insert.bindDouble(7, objBusinessResult.getPriceEnding());
				insert.bindDouble(8, objBusinessResult.getBookValue());
				insert.bindDouble(9, objBusinessResult.getVolume());
				insert.bindDouble(10, objBusinessResult.getEpsNotAdjusted());
				insert.execute();
			}

			db.setTransactionSuccessful();
			isSuccess= true;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		} finally {
			db.endTransaction();
			if (db.isOpen()) {
				db.close();
				isSuccess= true;
			}
		}
		Log.d("Insert Stock:", "Finish insert Data Conversations");
		return isSuccess;
	}
}
