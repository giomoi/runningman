package vn.co.vns.runningman.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.dialog.DialogTickTicker;
import vn.co.vns.runningman.object.StockObject;
import vn.co.vns.runningman.object.Tick;
import vn.co.vns.runningman.util.Constant;
import vn.co.vns.runningman.util.SharedPreference;

/**
 * Created by hoangtuan on 4/7/17.
 */
public class PriceOnlineAdapter extends RecyclerView.Adapter<PriceOnlineAdapter.PriceOnlineHolder> {
    private ArrayList<StockObject> listItem = new ArrayList<>();
    private WeakReference<Context> mContext;
    private Context context;
    private String optionPriceboard;
    private int colorWhite = -1;
    private int colorRed= -1;
    private int colorGreen= -1;
    private int colorYellow= -1;
    private int colorBlue= -1;
    private int colorPurple= -1;


    public PriceOnlineAdapter(Context context){
        mContext = new WeakReference<Context>(context);
        this.context=context;
    }

    public void setListItem(ArrayList<StockObject> stockObjects,String optionPriceboard) {
        listItem.clear();
        listItem.addAll(stockObjects);
        this.optionPriceboard=optionPriceboard;
        notifyDataSetChanged();
    }

    public void setListSortPriority(ArrayList<StockObject> stockObjects,String optionPriceboard){
        listItem.clear();
        listItem.addAll(stockObjects);
        sortStockPriority(listItem);
        this.optionPriceboard=optionPriceboard;
        notifyDataSetChanged();

    }

    @Override
    public PriceOnlineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_price_online_item, parent, false);
        return new PriceOnlineHolder(itemView);
    }


    @Override
    public void onBindViewHolder(PriceOnlineHolder holder, final int position) {
        holder.bindView(listItem.get(position));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("Code: ", listItem.get(position).getCodeStock());
                final CharSequence[] items = {"Sắp xếp", "Đánh dấu", "etc1"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Lựa chọn");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item==0){
                            Log.d("Item 1:", String.valueOf(item));
                            SharedPreference.getInstance().putInt("orderby", Constant.SORT_PRIORITY);
                            SharedPreference.getInstance().putString("list_priority",SharedPreference.getInstance().getString("list_priority","")+"," +listItem.get(position).getCodeStock());
                            StockObject objTmp= new StockObject();
                            objTmp=listItem.get(position);
                            objTmp.setPriorityOrder(1);
                            listItem.set(position,objTmp);
                            sortStockPriority(listItem);
                            notifyDataSetChanged();
                            Log.d("List: ",SharedPreference.getInstance().getString("list_priority",""));
                        }else if(item==1){
                            Log.d("Item 2:", String.valueOf(item));
                            Tick tick =new Tick();
                            tick.setTicker(listItem.get(position).getCodeStock());
                            tick.setPriceCurrent(Float.parseFloat(listItem.get(position).getKhopLenhPrice()));
                            new DialogTickTicker(context,context.getResources().getString(R.string.title_tick_ticker),tick).show();
//                            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
//                            FragmenTickTicker fragment = FragmenTickTicker.newInstance();
//                            fragmentManager.beginTransaction().replace(R.id.base_container, fragment).commit();

                        }else{
                            Log.d("Item 3:", String.valueOf(item));
                        }
                    }
                });
                builder.show();

                return true;
            }
        });


    }

    public void sortStockPriority(ArrayList<StockObject> listStock){
        Collections.sort(listStock, new Comparator<StockObject>() {
            @Override
            public int compare(StockObject o1, StockObject o2) {
                return Float.compare(o2.getPriorityOrder(), o1.getPriorityOrder());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class PriceOnlineHolder extends RecyclerView.ViewHolder {
        private TextView txtBuy;
        private TextView txtSell;
        private TextView txtCode;
        private TextView txtTopPrice;
        private TextView txtBottomPrice;
        private TextView txtTCPrice;
        private TextView txtKLPrice;
        private TextView txtKLWeight;
        private TextView txtGapPrice;
        private TextView txtHighPrice;
        private TextView txtLowPrice;
        private TextView txtTotalWeight;
        private TextView txtNNBuying;
        private TextView txtNNSelling;
        private TextView txtPriceBuy1;
        private TextView txtPriceBuy2;
        private TextView txtPriceBuy3;
        private TextView txtWeighBuy1;
        private TextView txtWeighBuy2;
        private TextView txtWeighBuy3;
        private TextView txtPriceSell1;
        private TextView txtPriceSell2;
        private TextView txtPriceSell3;
        private TextView txtWeighSell1;
        private TextView txtWeighSell2;
        private TextView txtWeighSell3;

        public PriceOnlineHolder(View convertView) {
            super(convertView);
            txtBuy = convertView.findViewById(R.id.txtBuy);
            txtSell = convertView.findViewById(R.id.txtSell);
            txtCode = convertView.findViewById(R.id.txtCodeStock);
            txtTopPrice = convertView.findViewById(R.id.txtTopPrice);
            txtBottomPrice = convertView.findViewById(R.id.txtBottomPrice);
            txtTCPrice = convertView.findViewById(R.id.txtTCPrice);
            txtKLPrice = convertView.findViewById(R.id.txtKhopLenhPrice);
            txtKLWeight = convertView.findViewById(R.id.txtKhopLenhWeight);
            txtGapPrice = convertView.findViewById(R.id.txtGapPrice);
            txtHighPrice = convertView.findViewById(R.id.txtHighPrice);
            txtLowPrice = convertView.findViewById(R.id.txtLowPrice);
            txtTotalWeight = convertView.findViewById(R.id.txtTotalWeight);
            txtNNBuying = convertView.findViewById(R.id.txtNNBuying);
            txtNNSelling = convertView.findViewById(R.id.txtNNSelling);
            txtPriceBuy1 = convertView.findViewById(R.id.txtPriceBuy1);
            txtPriceBuy2 = convertView.findViewById(R.id.txtPriceBuy2);
            txtPriceBuy3 = convertView.findViewById(R.id.txtPriceBuy3);
            txtPriceSell1 = convertView.findViewById(R.id.txtPriceSell1);
            txtPriceSell2 = convertView.findViewById(R.id.txtPriceSell2);
            txtPriceSell3 = convertView.findViewById(R.id.txtPriceSell3);
            txtWeighBuy1 = convertView.findViewById(R.id.txtWeighBuy1);
            txtWeighBuy2 = convertView.findViewById(R.id.txtWeighBuy2);
            txtWeighBuy3 = convertView.findViewById(R.id.txtWeighBuy3);
            txtWeighSell1 = convertView.findViewById(R.id.txtWeighSell1);
            txtWeighSell2 = convertView.findViewById(R.id.txtWeighSell2);
            txtWeighSell3 = convertView.findViewById(R.id.txtWeighSell3);

        }

        public void bindView(StockObject item) {
            txtCode.setText(item.getCodeStock());
            txtTopPrice.setText(item.getTopPrice());
            txtBottomPrice.setText(item.getBottomPrice());
            txtTCPrice.setText(item.getTCPrice());
            txtKLPrice.setText(item.getKhopLenhPrice());
            txtKLWeight.setText(item.getKhopLenhWeight());
            txtGapPrice.setText(item.getGapPrice());
            txtHighPrice.setText(item.getHighPrice());
            txtLowPrice.setText(item.getLowPrice());
            if (optionPriceboard.equalsIgnoreCase("HastcStockBoard")) {
                txtTotalWeight.setText(item.getNNBuying());
            }else{
                txtTotalWeight.setText(item.getTotalWeight());
                txtNNBuying.setText(item.getNNBuying());
                txtNNSelling.setText(item.getNNSelling());
            }
            txtPriceBuy1.setText(item.getBuyingPrice1());
            txtPriceBuy2.setText(item.getBuyingPrice2());
            txtPriceBuy3.setText(item.getBuyingPrice3());
            txtPriceSell1.setText(item.getSellingPrice1());
            txtPriceSell2.setText(item.getSellingPrice2());
            txtPriceSell3.setText(item.getSellingPrice3());
            txtWeighBuy1.setText(item.getBuyingWeight1());
            txtWeighBuy2.setText(item.getBuyingWeight2());
            txtWeighBuy3.setText(item.getBuyingWeight3());
            txtWeighSell1.setText(item.getSellingWeight1());
            txtWeighSell2.setText(item.getSellingWeight2());
            txtWeighSell3.setText(item.getSellingWeight3());
            int color = getColorItem(item.getKhopLenhPrice(), item.getTCPrice(), item.getTopPrice(), item.getBottomPrice());
            txtKLPrice.setTextColor(color);
            txtKLWeight.setTextColor(color);
            txtCode.setTextColor(color);
            txtBuy.setTextColor(color);
            txtSell.setTextColor(color);
            txtNNBuying.setTextColor(color);
            txtNNSelling.setTextColor(color);
            txtGapPrice.setTextColor(color);
            color = getColorItem(item.getHighPrice(), item.getTCPrice(), item.getTopPrice(), item.getBottomPrice());
            txtHighPrice.setTextColor(color);
            color = getColorItem(item.getLowPrice(), item.getTCPrice(), item.getTopPrice(), item.getBottomPrice());
            txtLowPrice.setTextColor(color);
            color = getColorItem(item.getBuyingPrice1(), item.getTCPrice(), item.getTopPrice(), item.getBottomPrice());
            txtPriceBuy1.setTextColor(color);
            txtWeighBuy1.setTextColor(color);
            color = getColorItem(item.getBuyingPrice2(), item.getTCPrice(), item.getTopPrice(), item.getBottomPrice());
            txtPriceBuy2.setTextColor(color);
            txtWeighBuy2.setTextColor(color);
            color = getColorItem(item.getBuyingPrice3(), item.getTCPrice(), item.getTopPrice(), item.getBottomPrice());
            txtPriceBuy3.setTextColor(color);
            txtWeighBuy3.setTextColor(color);
            color = getColorItem(item.getSellingPrice1(), item.getTCPrice(), item.getTopPrice(), item.getBottomPrice());
            txtPriceSell1.setTextColor(color);
            txtWeighSell1.setTextColor(color);
            color = getColorItem(item.getSellingPrice2(), item.getTCPrice(), item.getTopPrice(), item.getBottomPrice());
            txtPriceSell2.setTextColor(color);
            txtWeighSell2.setTextColor(color);
            color = getColorItem(item.getSellingPrice3(), item.getTCPrice(), item.getTopPrice(), item.getBottomPrice());
            txtPriceSell3.setTextColor(color);
            txtWeighSell3.setTextColor(color);
        }

        private int getColorItem(String itemCompare, String tcPrice, String top, String bottom) {
            if(itemCompare.isEmpty()){
                if(colorWhite == -1) {
                    colorWhite = ContextCompat.getColor(mContext.get(), R.color.white);
                }
                return colorWhite;
            }
            if(itemCompare.equals(top)){
                if(colorPurple == -1) {
                    colorPurple = ContextCompat.getColor(mContext.get(), R.color.purple);
                }
                return colorPurple;
            }
            if(itemCompare.equals(bottom)){
                if(colorBlue == -1) {
                    colorBlue = ContextCompat.getColor(mContext.get(), R.color.blue);
                }
                return colorBlue;
            }
            try {
                if(itemCompare.equals("ATO") || itemCompare.equals("ATC")){
                    if(colorWhite == -1) {
                        colorWhite = ContextCompat.getColor(mContext.get(), R.color.white);
                    }
                    return colorWhite;
                }
                float numberCompare = Float.valueOf(itemCompare.replace(",", "."));
                float value = Float.valueOf(tcPrice.replace(",", "."));
                if (numberCompare == value) {
                    if(colorYellow == -1) {
                        colorYellow = ContextCompat.getColor(mContext.get(), R.color.yellow);
                    }
                    return colorYellow;
                } else if (numberCompare > value) {
                    if(colorGreen == -1) {
                        colorGreen = ContextCompat.getColor(mContext.get(), R.color.green);
                    }
                    return colorGreen;
                }
                if(colorRed == -1) {
                    colorRed = ContextCompat.getColor(mContext.get(), R.color.red);
                }
                return colorRed;
            }catch (Exception e){
                e.printStackTrace();
                if(colorWhite == -1) {
                    colorWhite = ContextCompat.getColor(mContext.get(), R.color.white);
                }
                return colorWhite;
            }
        }


    }
}
