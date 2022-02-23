package com.pontianak.teknikpayment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.RecyclerView;

import com.pontianak.teknikpayment.Model.Berita;
import com.pontianak.teknikpayment.R;

import java.util.List;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.praktekholder> {
    public List<Berita> items;
    public Context ctx;
    public LayoutInflater lay;
    onSelect onselect;
   // Bantuan bantuan;

    public interface onSelect{
        public void onselectitem(Berita item);
    }

    public class praktekholder extends RecyclerView.ViewHolder {
        TextView txt_1;
        ImageView img1;
        //ImageView img_Model_slider;
        Guideline guideline13;

        public praktekholder(View itemView) {
            super(itemView);
            this.guideline13=itemView.findViewById(R.id.guideline13);
             this.img1=itemView.findViewById(R.id.img1);
            this.txt_1=itemView.findViewById(R.id.txt_1);


            //this.txt_pengurus_kebaktian=itemView.findViewById(R.id.txt_tempat_kunjungan);
            //this.img_Model_slider=itemView.findViewById(R.id.img_Model_slider);


        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public BeritaAdapter(List<Berita> items, Context ctx) {
        this.items = items;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
      //  this.bantuan = new Bantuan(ctx);
    }
    public BeritaAdapter(List<Berita> items, Context ctx,onSelect onselect) {
        this.items = items;
        this.ctx = ctx;
        this.onselect = onselect;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        //  this.bantuan = new Bantuan(ctx);
    }


    @NonNull
    @Override
    public praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = lay.inflate(R.layout.content_list_berita, parent, false);
        return new praktekholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull praktekholder holder, int position) {
        final Berita item = items.get(position);
        if(position==0){
            holder.guideline13.setGuidelinePercent(0.0f);
        }
        holder.txt_1.setText(item.title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent=new Intent(ctx,WebActivity.class);
                // intent.putExtra("url",myConfig.main_url+"/banner/History/"+praktek.getId_History());
                // ctx.startActivity(intent);
                onselect.onselectitem(items.get(position));
            }
        });


//





        // Glide.with(ctx).load(myConfig.root_url + "images/Model_slider/" + praktek.getGambar_Model_slider()).into(holder.img_Model_slider);
        //holder.txt_1.setText(praktek.teks);




        //holder.txt_nama_kebaktian.setText(praktek.getJudul_Model_slider());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent=new Intent(ctx,WebActivity.class);
               // intent.putExtra("url",myConfig.main_url+"/banner/Model_slider/"+praktek.getId_Model_slider());
               // ctx.startActivity(intent);
                onselect.onselectitem(item);
            }
        });



    }
}
