package com.pontianak.teknikpayment.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pontianak.teknikpayment.ItemMarketplaceActivity;
import com.pontianak.teknikpayment.Model.Model_slider;
import com.pontianak.teknikpayment.R;


import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.praktekholder> {
    public List<Model_slider> promo;
    public Context ctx;
    public LayoutInflater lay;
   // Bantuan bantuan;

    public class praktekholder extends RecyclerView.ViewHolder {
        TextView txt_1,txt_2;
        ImageView img_izin;
        //ImageView img_Model_slider;
        Guideline guideline13;
        Guideline guideline42;

        public praktekholder(View itemView) {
            super(itemView);
            this.guideline13=itemView.findViewById(R.id.guideline13);
            this.guideline42=itemView.findViewById(R.id.guideline42);
             this.img_izin=itemView.findViewById(R.id.img1);
             this.txt_1=itemView.findViewById(R.id.txt_1);
            this.txt_2=itemView.findViewById(R.id.txt_2);

            //this.txt_pengurus_kebaktian=itemView.findViewById(R.id.txt_tempat_kunjungan);
            //this.img_Model_slider=itemView.findViewById(R.id.img_Model_slider);


        }
    }

    @Override
    public int getItemCount() {
        return this.promo.size();
    }

    public SliderAdapter(List<Model_slider> promo, Context ctx) {
        this.promo = promo;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
      //  this.bantuan = new Bantuan(ctx);
    }

    @NonNull
    @Override
    public praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = lay.inflate(R.layout.content_list_promo, parent, false);
        return new praktekholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull praktekholder holder, int position) {
        final Model_slider praktek = promo.get(position);
        if(position==0){
            holder.guideline13.setGuidelinePercent(0.0f);
        }

        holder.txt_2.setText(praktek.nama);
        holder.txt_1.setText("Nama Item "+String.valueOf(position));



//        holder.txt_2.setText("");
        Log.i("ez",praktek.foto);





        // Glide.with(ctx).load(myConfig.root_url + "images/Model_slider/" + praktek.getGambar_Model_slider()).into(holder.img_Model_slider);
        //holder.txt_1.setText(praktek.teks);
        Glide.with(ctx)
                .load("https://payment.myumptk.com/"+praktek.foto)
                .into(holder.img_izin);

        //holder.txt_nama_kebaktian.setText(praktek.getJudul_Model_slider());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ctx, ItemMarketplaceActivity.class);
                intent.putExtra("nama",praktek.nama);
                intent.putExtra("foto","https://payment.myumptk.com/"+praktek.foto);
                ctx.startActivity(intent);
            }
        });



    }
}
