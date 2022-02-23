package com.pontianak.teknikpayment.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pontianak.teknikpayment.Model.JadwalSholat;
import com.pontianak.teknikpayment.R;

import java.util.List;

public class SholatAdapter extends RecyclerView.Adapter<SholatAdapter.praktekholder> {
    public List<JadwalSholat> promo;
    public Context ctx;
    public LayoutInflater lay;
   // Bantuan bantuan;

    public class praktekholder extends RecyclerView.ViewHolder {
        TextView txt_1,txt_2;
        ImageView img2;
        //ImageView img_JadwalSholat;
        Guideline guideline13;

        public praktekholder(View itemView) {
            super(itemView);
            this.guideline13=itemView.findViewById(R.id.guideline13);
             this.img2=itemView.findViewById(R.id.img2);
             this.txt_1=itemView.findViewById(R.id.txt1);
             this.txt_2=itemView.findViewById(R.id.txt2);
            //this.txt_pengurus_kebaktian=itemView.findViewById(R.id.txt_tempat_kunjungan);
            //this.img_JadwalSholat=itemView.findViewById(R.id.img_JadwalSholat);


        }
    }

    @Override
    public int getItemCount() {
        return this.promo.size();
    }

    public SholatAdapter(List<JadwalSholat> promo, Context ctx) {
        this.promo = promo;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
      //  this.bantuan = new Bantuan(ctx);
    }

    @NonNull
    @Override
    public praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = lay.inflate(R.layout.content_list_jadwal_sholat, parent, false);
        return new praktekholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull praktekholder holder, int position) {
        final JadwalSholat praktek = promo.get(position);
        if(position==0){
           // holder.guideline13.setGuidelinePercent(0.10f);
        }



//        holder.txt_2.setText("");





        // Glide.with(ctx).load(myConfig.root_url + "images/JadwalSholat/" + praktek.getGambar_JadwalSholat()).into(holder.img_JadwalSholat);
        //holder.txt_1.setText(praktek.teks);
        //holder.img2.setImageResource(praktek.image_status);
        holder.txt_2.setText(praktek.jadwal);
        holder.txt_1.setText(praktek.nama);
        Log.i("ez",praktek.jadwal);


        //holder.txt_nama_kebaktian.setText(praktek.getJudul_JadwalSholat());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent=new Intent(ctx,WebActivity.class);
               // intent.putExtra("url",myConfig.main_url+"/banner/JadwalSholat/"+praktek.getId_JadwalSholat());
               // ctx.startActivity(intent);
            }
        });



    }
}
