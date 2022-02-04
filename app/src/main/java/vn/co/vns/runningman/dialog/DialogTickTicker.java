package vn.co.vns.runningman.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.VolleyAPI.TickAPI;
import vn.co.vns.runningman.interfaces.SimpeBaseCallback;
import vn.co.vns.runningman.object.Tick;

/**
 * Created by thanhnv on 7/3/17.
 */

public class DialogTickTicker {

    private final Context context;
    private Dialog dialog;
    private Button closeButton;
    private Button btnOK;
    private TextView tvMessageSendLikeeditPriceCurrent;
    private Tick tick;
    private EditText editTicker, editPriceCurrent, editPriceTarget, editOwe, editOrganization, editEPSYear, editEPSQuaterYear, editBookValue, editRoom, editContent;


    public DialogTickTicker(final Context context, String title, Tick tick) {
        this.context = context;
        this.tick = tick;
        dialog = new Dialog(this.context, R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle(title);
        dialog.setContentView(R.layout.dialog_tick_ticker);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        closeButton = dialog.findViewById(R.id.btnCancel);
        editTicker = dialog.findViewById(R.id.editTicker);
        editPriceCurrent = dialog.findViewById(R.id.editPriceCurrent);
        editPriceTarget = dialog.findViewById(R.id.editPriceTarget);
        editOwe = dialog.findViewById(R.id.editOwe);
        editOrganization = dialog.findViewById(R.id.editOrganization);
        editEPSYear = dialog.findViewById(R.id.editEPSYear);
        editEPSQuaterYear = dialog.findViewById(R.id.editQuaterYear);
        editBookValue = dialog.findViewById(R.id.editBookValue);
        editRoom = dialog.findViewById(R.id.editRoom);
        editContent = dialog.findViewById(R.id.editContent);
        setDialogView();
        btnOK = dialog.findViewById(R.id.btnOk);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTicker = dialog.findViewById(R.id.editTicker);
                Tick objTick = new Tick();
                objTick.setTicker(editTicker.getText().toString());
                objTick.setPriceCurrent(Float.parseFloat(String.valueOf(editPriceCurrent.getText())));
                if (!"".equalsIgnoreCase((editPriceTarget.getText().toString())))
                    objTick.setPriceTarget(Float.parseFloat(editPriceTarget.getText().toString()));
                objTick.setOptionTarget(Integer.parseInt("0"));
                objTick.setOwe(editOwe.getText().toString());
                objTick.setContent(editContent.getText().toString());
                objTick.setEPSYear(editEPSYear.getText().toString());
                objTick.setEPSQuaterYear(editEPSQuaterYear.getText().toString());
                objTick.setBookValue(editBookValue.getText().toString());
                objTick.setOrganization(editOrganization.getText().toString());
                objTick.setRoom(editRoom.getText().toString());
                TickAPI.getInstance().addTickTicker(objTick, new SimpeBaseCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(context, context.getResources().getString(R.string.common_content_delete), Toast.LENGTH_SHORT).show();
                        hide();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        hide();
                    }
                });
            }
        });
    }

    private void setDialogView() {
        editTicker.setText((null != tick) ? tick.getTicker() : "");
        editPriceCurrent.setText((null != tick) ? String.valueOf(tick.getPriceCurrent()) : "");
        editPriceTarget.setText((null != tick && null != tick.getPriceTarget()) ? String.valueOf(tick.getPriceTarget()) : "");
        editOwe.setText((null != tick && null != tick.getOwe()) ? tick.getOwe() : "");
        editOrganization.setText((null != tick && null != tick.getOrganization()) ? tick.getOrganization() : "");
        editEPSYear.setText((null != tick && null != tick.getEPSYear()) ? tick.getEPSYear() : "");
        editEPSQuaterYear.setText((null != tick && null != tick.getEPSQuaterYear()) ? tick.getEPSQuaterYear() : "");
        editBookValue.setText((null != tick && null != tick.getBookValue()) ? tick.getBookValue() : "");
        editRoom.setText((null != tick && null != tick.getRoom()) ? tick.getRoom() : "");
        editContent.setText((null != tick && null != tick.getContent()) ? tick.getContent() : "");

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
