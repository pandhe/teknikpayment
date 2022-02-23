package com.pontianak.teknikpayment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pontianak.teknikpayment.Bantuan;
import com.pontianak.teknikpayment.Model.DetailTagihan;
import com.pontianak.teknikpayment.R;

import java.util.List;

public class DetaillTagihanAdapter extends RecyclerView.Adapter<DetaillTagihanAdapter.praktekholder> {
    public List<DetailTagihan> items;
    public Context ctx;
    public LayoutInflater lay;
    onSelect onselect;
    Bantuan bantuan =new Bantuan();

    public interface onSelect{
        public void onselectitem(DetailTagihan item);
    }

    public class praktekholder extends RecyclerView.ViewHolder {
        TextView txt_1,txt_7,txt_8,txt_9,txt_10;
        //ImageView img_Model_slider;


        public praktekholder(View itemView) {
            super(itemView);

            this.txt_1=itemView.findViewById(R.id.txt_1);
            this.txt_7=itemView.findViewById(R.id.txt_7);
            this.txt_8=itemView.findViewById(R.id.txt_8);
            this.txt_9=itemView.findViewById(R.id.txt_9);
            this.txt_10=itemView.findViewById(R.id.txt_10);

            //this.txt_pengurus_kebaktian=itemView.findViewById(R.id.txt_tempat_kunjungan);
            //this.img_Model_slider=itemView.findViewById(R.id.img_Model_slider);


        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public DetaillTagihanAdapter(List<DetailTagihan> items, Context ctx) {
        this.items = items;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
      //  this.bantuan = new Bantuan(ctx);
    }
    public DetaillTagihanAdapter(List<DetailTagihan> items, Context ctx, onSelect onselect) {
        this.items = items;
        this.ctx = ctx;
        this.onselect = onselect;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        //  this.bantuan = new Bantuan(ctx);
    }


    @NonNull
    @Override
    public praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = lay.inflate(R.layout.content_list_detailtagihan, parent, false);
        return new praktekholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull praktekholder holder, int position) {
        final DetailTagihan item = items.get(position);
        if(position==0){
         //   holder.guideline13.setGuidelinePercent(0.0f);
        }
        holder.txt_1.setText("Periode "+item.periode);
        holder.txt_7.setText(bantuan.torupiah(bantuan.meisinteger(item.nilai_tagihan)));
        holder.txt_8.setText(bantuan.torupiah(bantuan.meisinteger(item.admin)));
        holder.txt_9.setText(bantuan.torupiah(bantuan.meisinteger(item.denda)));
        holder.txt_10.setText(bantuan.torupiah(bantuan.meisinteger(item.nilai_tagihan)+bantuan.meisinteger(item.admin)+bantuan.meisinteger(item.denda)));



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
