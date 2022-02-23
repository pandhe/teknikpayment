package com.pontianak.teknikpayment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pontianak.teknikpayment.Model.Belajar;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.R;

import java.util.List;

public class BelajarAdapter extends RecyclerView.Adapter<BelajarAdapter.praktekholder> {
    public List<Belajar> items;
    public Context ctx;
    public LayoutInflater lay;
    public Service_Connector service_connector;
   // Bantuan bantuan;

    public class praktekholder extends RecyclerView.ViewHolder {
        TextView txt_1,txt_2,txt_3;
        ImageView img1,img2;
        //ImageView img_Model_slider;
        Guideline guideline13;

        public praktekholder(View itemView) {
            super(itemView);
            this.guideline13=itemView.findViewById(R.id.guideline13);
             this.img1=itemView.findViewById(R.id.img1);
            this.img2=itemView.findViewById(R.id.img2);
             this.txt_1=itemView.findViewById(R.id.txt_1);
            this.txt_2=itemView.findViewById(R.id.txt_2);
            this.txt_3=itemView.findViewById(R.id.txt_3);

            //this.txt_pengurus_kebaktian=itemView.findViewById(R.id.txt_tempat_kunjungan);
            //this.img_Model_slider=itemView.findViewById(R.id.img_Model_slider);


        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public BelajarAdapter(List<Belajar> items, Context ctx) {
        this.items = items;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        this.service_connector = new Service_Connector();
      //  this.bantuan = new Bantuan(ctx);
    }

    @NonNull
    @Override
    public praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = lay.inflate(R.layout.content_list_belajar, parent, false);
        return new praktekholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull praktekholder holder, int position) {
        final Belajar item = items.get(position);
        if(position==0){
            holder.guideline13.setGuidelinePercent(0.0f);
        }
        String des = "View : "+String.valueOf(item.view);
        if(des.length() > 25){
            des=item.deskripsi.substring(0,20);
        }
        holder.txt_2.setText(des);
        holder.txt_1.setText(item.nama);
        holder.txt_3.setText(item.author);



//        holder.txt_2.setText("");
        Log.i("ez",String.valueOf(item.thumbnail));





        // Glide.with(ctx).load(myConfig.root_url + "images/Model_slider/" + praktek.getGambar_Model_slider()).into(holder.img_Model_slider);
        //holder.txt_1.setText(praktek.teks);
        if(item.thumbnail == null){
            Glide.with(ctx)
                    .load(service_connector.url + "image/belajar/php.jpg" )
                    .into(holder.img1);

        }else{
            Glide.with(ctx)
                    .load(item.thumbnail)
                    .into(holder.img1);
        }


        Glide.with(ctx)
                .load(item.author_picture).apply(RequestOptions.circleCropTransform())
                .into(holder.img2);

        //holder.txt_nama_kebaktian.setText(praktek.getJudul_Model_slider());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent=new Intent(ctx,WebActivity.class);
               // intent.putExtra("url",myConfig.main_url+"/banner/Model_slider/"+praktek.getId_Model_slider());
               // ctx.startActivity(intent);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.link));
                // Note the Chooser below. If no applications match,
                // Android displays a system message.So here there is no need for try-catch.
                ctx.startActivity(Intent.createChooser(intent, "Browse with"));
            }
        });



    }
}
