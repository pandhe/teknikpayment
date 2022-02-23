package com.pontianak.teknikpayment;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pontianak.teknikpayment.R;

public class BayarActivity extends KuberlayarDilautan {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar);
        TextView txt_title=findViewById(R.id.txt_title);
        txt_title.setText("MyTransfer");
        ImageButton bt_back=findViewById(R.id.bt_back);
        bt_back.setImageResource(R.drawable.ic_menu_bayar);

    }
}
