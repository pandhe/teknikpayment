package com.pontianak.teknikpayment;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pontianak.teknikpayment.R;

public class FiturLinkajaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitur_linkaja);
        Intent intent=getIntent();
        TextView textView=findViewById(R.id.txt_fitur);
        textView.setText(intent.getStringExtra("aksi"));

    }
}
