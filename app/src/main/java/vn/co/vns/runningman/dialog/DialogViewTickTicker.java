package vn.co.vns.runningman.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.object.Tick;

/**
 * Created by thanhnv on 7/3/17.
 */

public class DialogViewTickTicker {

    private final Context context;
    private Dialog dialog;
    private Button closeButton;
    private Button  btnOK;
    private TextView tvMessageSendLike;
    private TextView txtValueTicker,txtValuePriceCurrent,txtValuePriceTarget,txtValueOwe,txtValueOrganization,txtValueEPSYear,txtValueEPSQuaterYear,txtValueBookValue,txtValueRoom,txtValueContent;


    public DialogViewTickTicker(final Context context, String title, Tick tick) {
        this.context = context;
        dialog = new Dialog(this.context,R.style.DialogSlideAnim);
        dialog.setTitle(title);
        dialog.setContentView(R.layout.dialog_view_tick_ticker);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        closeButton = dialog.findViewById(R.id.btnClose);
        txtValueTicker= dialog.findViewById(R.id.txtValueTicker);
        txtValuePriceTarget= dialog.findViewById(R.id.txtValuePriceTarget);
        txtValueOwe= dialog.findViewById(R.id.txtValueOwe);
        txtValueOrganization= dialog.findViewById(R.id.txtValueOrganization);
        txtValueEPSYear= dialog.findViewById(R.id.txtValueEPSYear);
        txtValueEPSQuaterYear= dialog.findViewById(R.id.txtValueQuaterYear);
        txtValueBookValue= dialog.findViewById(R.id.txtValueBookValue);
        txtValueRoom= dialog.findViewById(R.id.txtValueRoom);
        txtValueContent= dialog.findViewById(R.id.txtValueContent);
        txtValuePriceCurrent=dialog.findViewById(R.id.txtPriceCurrent);

        txtValueTicker.setText(tick.getTicker());
        txtValuePriceTarget.setText(String.valueOf(tick.getPriceTarget()));
        txtValueBookValue.setText(tick.getBookValue());
        txtValueOwe.setText(tick.getOwe());
        txtValueOrganization.setText(tick.getOrganization());
        txtValueEPSYear.setText(tick.getEPSYear());
        txtValueEPSQuaterYear.setText(tick.getEPSQuaterYear());
        txtValueRoom.setText(tick.getRoom());
        txtValueContent.setText(tick.getContent());
        txtValuePriceCurrent.setText(String.valueOf(tick.getPriceCurrent()));

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

    }

    public void show() {
        if (dialog != null)
            dialog.show();
    }

    public void hide() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
