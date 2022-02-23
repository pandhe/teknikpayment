package com.pontianak.teknikpayment;

import android.os.Bundle;
import android.widget.TextView;

import com.pontianak.teknikpayment.R;

public class MyLazisMuActivity extends KuberlayarDilautan {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lazis_mu);
        TextView txt_title=findViewById(R.id.txt_title);
        txt_title.setText("Lazismu");
    }
}