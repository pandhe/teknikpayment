package com.pontianak.teknikpayment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pontianak.teknikpayment.Bantuan;
import com.pontianak.teknikpayment.Model.Notifikasi;
import com.pontianak.teknikpayment.R;

import java.util.List;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.praktekholder> {
    public List<Notifikasi> items;
    public Context ctx;
    public LayoutInflater lay;
    String bobot="100";
    onSelect onselect;
    Bantuan bantuan=new Bantuan();

    public class praktekholder extends RecyclerView.ViewHolder {
        TextView txt_1,txt_2,txt_3;
        ImageView img_1;
        //Guideline guideline13;

        public praktekholder(View itemView) {
            super(itemView);
            //this.guideline13=itemView.findViewById(R.id.guideline27);
            this.txt_1=itemView.findViewById(R.id.txt_label);
            this.txt_2=itemView.findViewById(R.id.txt_harga);

            //this.txt_pengurus_kebaktian=itemView.findViewById(R.id.txt_tempat_kunjungan);

            this.img_1=itemView.findViewById(R.id.img_1);


        }
    }
    public interface onSelect{
        public void onselectitem(int item);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public NotifikasiAdapter(List<Notifikasi> items, Context ctx) {
        this.items = items;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);


    }
    public NotifikasiAdapter(List<Notifikasi> items, Context ctx, onSelect onselect) {
        this.items = items;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        this.onselect=onselect;

    }

    @NonNull
    @Override
    public praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = lay.inflate(R.layout.content_list_notifikasi, parent, false);
        return new praktekholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull praktekholder holder, final int position) {
        final Notifikasi notif = items.get(position);



        //int resID = ctx.getResources().getIdentifier(praktek., "drawable", ctx.getPackageName());
        int logoop;
//        switch (praktek.subcat_id){
//            case 1:
//            case 2:
//                logoop=R.drawable.logotelkomsel;
//                break;
//            case 9:
//                logoop=R.drawable.ic_logo_pln;
//                break;
//            case 15:
//                logoop=R.drawable.ic_logo_digigame;
//                break;
//            case 16:
//                logoop=R.drawable.ic_logo_gemschool;
//                break;
//            case 17:
//                logoop=R.drawable.ic_logo_garena;
//                break;
//            case 18:
//                logoop=R.drawable.ic_logo_lyto;
//                break;
//            case 19:
//                logoop=R.drawable.ic_logo_megaxus;
//                break;
//            case 20:
//                logoop=R.drawable.ic_logo_netmarble;
//                break;
//            case 21:
//                logoop=R.drawable.ic_logo_onelife;
//                break;
//            case 22:
//                logoop=R.drawable.ic_logo_playon;
//                break;
//
//            case 35:
//            case 37:
//                logoop=R.drawable.logoxl;
//                break;
//            case 42:
//            case 43:
//                logoop=R.drawable.ic_logo_indosat;
//
//                break;
//            case 55:
//            case 56:
//                logoop=R.drawable.ic_logo_smartphone;
//                break;
//            case 62:
//            case 61:
//                logoop=R.drawable.logo3;
//                break;
//            case 64:
//                logoop=R.drawable.logoxl;
//                break;
//            case 66:
//                logoop=R.drawable.ic_logo_playstore;
//                break;
//            case 11:
//                logoop=R.drawable.ic_logo_pubgm;
//                break;
//            case 12:
//                logoop=R.drawable.ic_logo_ffm;
//                break;
//            case 13:
//                logoop=R.drawable.ic_logo_ml;
//                break;
//            case 14:
//                logoop=R.drawable.ic_logo_steam;
//                break;
//            default:
//                logoop=R.drawable.logoumy;
//                break;
//        }

        //int resID = ctx.getResources().getIdentifier(praktek., "drawable", ctx.getPackageName());

        //holder.img_operator.setImageResource(logoop);

        holder.txt_1.setText(notif.message);

        //holder.txt_1.setText(praktek.jenis+" "+praktek.code_product);
        logoop=R.drawable.logokapuas;

        holder.img_1.setImageResource(logoop);


        //holder.txt_2.setText(bantuan.torupiah(praktek.nominal));






        // Glide.with(ctx).load(myConfig.root_url + "images/History/" + praktek.getGambar_History()).into(holder.img_History);



        //holder.txt_nama_kebaktian.setText(praktek.getJudul_History());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent=new Intent(ctx,WebActivity.class);
               // intent.putExtra("url",myConfig.main_url+"/banner/History/"+praktek.getId_History());
               // ctx.startActivity(intent);
                onselect.onselectitem(position);
            }
        });



    }
}
