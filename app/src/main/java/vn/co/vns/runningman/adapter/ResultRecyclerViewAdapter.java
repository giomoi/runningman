package vn.co.vns.runningman.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.VolleyAPI.TickAPI;
import vn.co.vns.runningman.dialog.DialogTickTicker;
import vn.co.vns.runningman.dialog.DialogViewTickTicker;
import vn.co.vns.runningman.interfaces.SimpeBaseCallback;
import vn.co.vns.runningman.object.Tick;

/**
 * Created by Thanh on 7/9/2017.
 */

public class ResultRecyclerViewAdapter extends RecyclerView.Adapter<ResultRecyclerViewAdapter.CustomViewHolder> {

    private List<Tick> listResults;
    private Context mContext;

    public ResultRecyclerViewAdapter(Context context, List<Tick> listResults) {
        this.listResults = listResults;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_list_results,null, false);
        LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.adapter_list_results, viewGroup,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        if (position%2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#f2f2f2"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        final Tick tick=listResults.get(position);
        holder.txtTicker.setText(tick.getTicker());
        holder.txtPriceCurrent.setText(String.valueOf(tick.getPriceCurrent()));
        holder.txtTarget.setText(String.valueOf(tick.getPriceTarget()));
        holder.txtBookValue.setText((tick.getBookValue()));
//        holder.txtOwe.setText((null!=tick.getOwe()) ? tick.getOwe() : "");
        if(null!=tick.getOwe()) holder.txtOwe.setText(tick.getOwe());
        holder.txtContent.setText(tick.getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogViewTickTicker(mContext,mContext.getResources().getString(R.string.title_view_tick_ticker),tick).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Log.d("Code: ", listResults.get(position).getCodeStock());
                final CharSequence[] items = {"Sửa", "Xoá"};
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Lựa chọn");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item==0){
                            Log.d("Item 1:", String.valueOf(item));
                            new DialogTickTicker(mContext,mContext.getResources().getString(R.string.title_tick_ticker),tick).show();
                        }else if(item==1){
                            Log.d("Item 2:", String.valueOf(item));
                            TickAPI.getInstance().deleteTickTicker(tick.getTicker(), new SimpeBaseCallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(mContext,mContext.getResources().getString(R.string.common_content_delete),Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(mContext,mContext.getString(R.string.common_content_error),Toast.LENGTH_SHORT).show();
                                }
                            });
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

    @Override
    public int getItemCount() {
        return (null != listResults ? listResults.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtPriceCurrent;
        protected TextView txtTicker;
        protected TextView txtTarget;
        protected TextView txtBookValue;
        protected TextView txtOwe;
        protected TextView txtContent;


        public CustomViewHolder(View view) {
            super(view);
            this.txtTicker = view.findViewById(R.id.txtTicker);
            this.txtPriceCurrent = view.findViewById(R.id.txtPriceCurrent);
            this.txtTarget = view.findViewById(R.id.txtPriceTarget);
            this.txtBookValue=view.findViewById(R.id.txtBookValue);
            this.txtOwe = view.findViewById(R.id.txtOwe);
            this.txtContent = view.findViewById(R.id.txtContent);
        }
    }
}
