package com.sc.coffeeprince.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sc.coffeeprince.R;

import butterknife.BindView;

/**
 * Created by fopa on 2017-11-07.
 */

public class CustomDialog extends Dialog implements View.OnClickListener {
    Activity activity;
    CustomDialogListener listener;

    @BindView(R.id.txtAlertTitle)
    TextView txtAlertTitle;

    @BindView(R.id.txtAlertMessage)
    TextView txtAlertMessage;

    @BindView(R.id.btnAlert)
    Button btnAlert;

    public CustomDialog (Activity activity,CustomDialogListener listener){
        super(activity);
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_alert_dialog);

        btnAlert.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        listener.btnAlertClicked(getTxtAlertTitle());
    }
    public void setTxtAlertTitle(String txt){
        this.txtAlertTitle.setText(txt);
    }
    public String getTxtAlertTitle(){
        return this.txtAlertTitle.getText().toString();
    }
    public void setTxtAlertMessage(String message){
        txtAlertMessage.setText(message);
    }

    public interface CustomDialogListener{
        void btnAlertClicked(String title);
    }
}
