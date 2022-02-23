package com.pontianak.teknikpayment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ItemMarketplaceActivity extends AppCompatActivity {
    TextView text1;
    ImageView img1;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_marketplace);
        text1=findViewById(R.id.txt_nama_gerai);
        img1=findViewById(R.id.img_resto);
        intent=getIntent();
        text1.setText(intent.getStringExtra("nama"));
        Glide.with(this)
                .load(intent.getStringExtra("foto"))
                .into(img1);

    }
}