package vn.co.vns.runningman.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.object.InforBuySellStockIndex;
import vn.co.vns.runningman.object.InforStockIndex;
import vn.co.vns.runningman.util.SharedPreference;

/**
 * Created by thanhnv on 4/27/18.
 */

public class PriceVolumeAgrementAdapter extends RecyclerView.Adapter<PriceVolumeAgrementAdapter.MyViewHolder> {
    private ArrayList<InforBuySellStockIndex> listInforBuySellSitockIndex;
    private Context mContext;
    private ArrayList<InforStockIndex> listInforStockIndex;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtDate, txtRateIndex, txtVolumeBuy, txtVolumeSell, txtVolumeBuyMinusSell, txtPriceClose, txtValue, txtVolume;

        public MyViewHolder(View view) {
            super(view);
            if (SharedPreference.getInstance().getBoolean("exchange", true)) { //Default
                txtDate = view.findViewById(R.id.txtDate);
                txtRateIndex = view.findViewById(R.id.txtRateIndex);
                txtVolumeBuy = view.findViewById(R.id.txtVolumeBuy);
                txtVolumeSell = view.findViewById(R.id.txtVolumeSell);
                txtVolumeBuyMinusSell = view.findViewById(R.id.txtVolumeBuyMinusSell);
            } else {
                txtDate = view.findViewById(R.id.txtDate);
                txtPriceClose = view.findViewById(R.id.txtPriceClose);
                txtRateIndex = view.findViewById(R.id.txtRateIndex);
                txtVolume = view.findViewById(R.id.txtVolume);
                txtValue = view.findViewById(R.id.txtValue);

            }
        }
    }

    public PriceVolumeAgrementAdapter(Context context, ArrayList<InforBuySellStockIndex> listInforBuySellSitockIndex) {
        this.mContext = context;
        this.listInforBuySellSitockIndex = listInforBuySellSitockIndex;
    }

    public void updatePriceVolumeAgrementAdapter(Context context, ArrayList<InforStockIndex> listInforStockIndex) {
        this.mContext = context;
        this.listInforStockIndex = listInforStockIndex;
        notifyDataSetChanged();
    }

    private View itemView;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (SharedPreference.getInstance().getBoolean("exchange", true)) { //Default
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_price_volume_agreement, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_price_volume_agreement1, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position%2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#f2f2f2"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        if (SharedPreference.getInstance().getBoolean("exchange", true)) { //Default
            InforBuySellStockIndex inforBuySellSitockIndex = listInforBuySellSitockIndex.get(position);
            holder.txtDate.setText(inforBuySellSitockIndex.getDateTransit().substring(0, 5));
            holder.txtRateIndex.setText(inforBuySellSitockIndex.getRate());
            if (inforBuySellSitockIndex.getVolumeBuy().length() > 2)
                holder.txtVolumeBuy.setText(inforBuySellSitockIndex.getVolumeBuy().substring(0, inforBuySellSitockIndex.getVolumeBuy().length() - 9));
            if (inforBuySellSitockIndex.getVolumeBuyMinusSell().length() > 7) {
                holder.txtVolumeBuyMinusSell.setText(inforBuySellSitockIndex.getVolumeBuyMinusSell().substring(0, inforBuySellSitockIndex.getVolumeBuyMinusSell().length() - 8));
            } else {
                holder.txtVolumeBuyMinusSell.setText("0");
            }
            if (inforBuySellSitockIndex.getVolumeBuy().length() > 2)
                holder.txtVolumeSell.setText(inforBuySellSitockIndex.getVolumeSell().substring(0, inforBuySellSitockIndex.getVolumeSell().length() - 9));
        } else {
            InforStockIndex inforStockIndex = listInforStockIndex.get(position);
            holder.txtDate.setText(inforStockIndex.getDateTransit().substring(0, 5));
            holder.txtRateIndex.setText(inforStockIndex.getRate());
            holder.txtPriceClose.setText(inforStockIndex.getPriceClose());
            holder.txtValue.setText(inforStockIndex.getValueTransit().substring(0, inforStockIndex.getValueTransit().length() - 13));
            holder.txtVolume.setText(inforStockIndex.getVolume().substring(0, inforStockIndex.getVolume().length() - 9));
        }
    }


    @Override
    public int getItemCount() {
        if (SharedPreference.getInstance().getBoolean("exchange", true)) { //Default
            if (listInforBuySellSitockIndex == null) return 0;
            return listInforBuySellSitockIndex.size();
        } else {
            if (listInforStockIndex == null) return 0;
            return listInforStockIndex.size();
        }
    }
}
