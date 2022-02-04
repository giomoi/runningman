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
import vn.co.vns.runningman.object.StockBigVolume;

/**
 * Created by thanhnv on 10/17/16.
 */
public class StockBreakOutVolumeAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<StockBigVolume> arrayStock=new ArrayList<>();

    public StockBreakOutVolumeAdapter(Context mContext) {
        this.mContext=mContext;
    }

    public StockBreakOutVolumeAdapter(Context mContext, ArrayList<StockBigVolume> arrayStock) {
        this.mContext=mContext;
        this.arrayStock=arrayStock;
    }

    public void getDataStock(ArrayList<StockBigVolume> arrayStock){
        this.arrayStock = arrayStock;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return arrayStock.size();
    }


    @Override
    public StockBigVolume getItem(int position) {
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
            convertView = inflater.inflate(R.layout.adapter_listtop, parent, false);
            holder.ticker= convertView.findViewById(R.id.ticker);
            holder.rate= convertView.findViewById(R.id.rate);
            holder.volume= convertView.findViewById(R.id.volume);
            holder.price= convertView.findViewById(R.id.price);
            holder.incdes= convertView.findViewById(R.id.incDes);
            holder.aver= convertView.findViewById(R.id.aver);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StockBigVolume objStockBigVolume =  getItem(position);

        //if(arrayStock.size()>0){
        holder.ticker.setText(objStockBigVolume.getTicker());
        NumberFormat format = NumberFormat.getInstance();
        holder.volume.setText(format.format(objStockBigVolume.getVolume()));
        holder.rate.setText(NumberFormat.getInstance().format(objStockBigVolume.getRate()));
        holder.incdes.setText(String.format("%.1f",objStockBigVolume.getIncDes()));
        holder.price.setText(String.format("%.1f",objStockBigVolume.getClose()));
        holder.aver.setText(NumberFormat.getInstance().format(objStockBigVolume.getAver()));
        //}
        return convertView;
    }

    public class ViewHolder{
        public TextView ticker;
        public TextView volume;
        public TextView incdes;
        public TextView rate;
        public TextView price;
        public TextView aver;
    }
}
