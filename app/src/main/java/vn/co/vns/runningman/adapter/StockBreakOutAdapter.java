package vn.co.vns.runningman.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.object.Stock;
import vn.co.vns.runningman.util.Utils;

/**
 * Created by thanhnv on 10/17/16.
 */
public class StockBreakOutAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<Stock> arrayStock=new ArrayList<>();

    public StockBreakOutAdapter(Context mContext) {
        this.mContext=mContext;
    }

    public StockBreakOutAdapter(Context mContext, ArrayList<Stock> arrayStock) {
        this.mContext=mContext;
        this.arrayStock=arrayStock;
    }

    public void getDataStock(ArrayList<Stock> arrayStock){
        this.arrayStock = arrayStock;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return arrayStock.size();
    }


    @Override
    public Stock getItem(int position) {
        return arrayStock.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;

        if (convertView == null) {
            holder=new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_break_out_volume, parent, false);
            holder.ticker= convertView.findViewById(R.id.ticker);
            holder.date_current= convertView.findViewById(R.id.date_current);
            holder.price_current= convertView.findViewById(R.id.price_current);
            holder.price_higest_price= convertView.findViewById(R.id.price_higest_price);
            holder.date_higest_price= convertView.findViewById(R.id.date_higest_price);
//            holder.aver=(TextView) convertView.findViewById(R.id.aver);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Stock objStockBigVolume =  getItem(position);

        //if(arrayStock.size()>0){
        holder.ticker.setText(objStockBigVolume.getTicker());
        NumberFormat format = NumberFormat.getInstance();
        holder.date_current.setText(Utils.convertStringToDateVN(objStockBigVolume.getDate()));
        holder.price_higest_price.setText(String.format("%.2f",(objStockBigVolume.getClose()-objStockBigVolume.getOpen())));
        holder.date_higest_price.setText(NumberFormat.getInstance().format(objStockBigVolume.getVolume()));
        holder.price_current.setText(String.format("%.2f",objStockBigVolume.getClose()));
//        holder.aver.setText(NumberFormat.getInstance().format(objStockBigVolume.getAver()));
        //}
        return convertView;
    }

    public class ViewHolder{
        public TextView ticker;
        public TextView date_current;
        public TextView price_current;
        public TextView price_higest_price;
        public TextView date_higest_price;
    }
}
