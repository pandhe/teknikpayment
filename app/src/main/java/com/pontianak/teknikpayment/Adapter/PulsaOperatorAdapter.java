package com.pontianak.teknikpayment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pontianak.teknikpayment.Bantuan;
import com.pontianak.teknikpayment.Model.PulsaOperator;
import com.pontianak.teknikpayment.R;

import java.util.List;

public class PulsaOperatorAdapter extends RecyclerView.Adapter<PulsaOperatorAdapter.praktekholder> {
    public List<PulsaOperator> promo;
    public Context ctx;
    public LayoutInflater lay;
    String bobot="100";
    String logo = "";
    onSelect onselect;
    Bantuan bantuan =new Bantuan();

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
        return this.promo.size();
    }

    public PulsaOperatorAdapter(List<PulsaOperator> promo, Context ctx) {
        this.promo = promo;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);

    }
    public PulsaOperatorAdapter(List<PulsaOperator> promo, Context ctx,String logo, onSelect onselect) {
        this.promo = promo;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        this.onselect=onselect;
        this.logo=logo;

    }

    public PulsaOperatorAdapter(List<PulsaOperator> promo, Context ctx, onSelect onselect) {
        this.promo = promo;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        this.onselect=onselect;

    }

    @NonNull
    @Override
    public praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = lay.inflate(R.layout.content_list_pulsa, parent, false);
        return new praktekholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull praktekholder holder, @SuppressLint("RecyclerView") final int position) {
        final PulsaOperator praktek = promo.get(position);

/*

        //int resID = ctx.getResources().getIdentifier(praktek., "drawable", ctx.getPackageName());
        int logoop;
        switch (praktek.subcat_id){
            case 9:
                logoop=R.drawable.ic_logo_pln;
                break;

            case 1:
            case 2:
            case 54:
            case 70:
            case 75:
                logoop=R.drawable.logotelkomsel;
                break;
            case 15:
                logoop=R.drawable.ic_logo_digigame;
                break;
            case 16:
                logoop=R.drawable.ic_logo_gemschool;
                break;
            case 17:
                logoop=R.drawable.ic_logo_garena;
                break;
            case 18:
                logoop=R.drawable.ic_logo_lyto;
                break;
            case 19:
                logoop=R.drawable.ic_logo_megaxus;
                break;
            case 20:
                logoop=R.drawable.ic_logo_netmarble;
                break;
            case 21:
                logoop=R.drawable.ic_logo_onelife;
                break;
            case 22:
                logoop=R.drawable.ic_logo_playon;
                break;
            case 36:
            case 37:
                logoop=R.drawable.logo_axis;
                break;

            case 35:
            case 38:
            case 79:
                logoop=R.drawable.logoxl;
                break;
            case 42:
            case 43:
            case 59:
                logoop=R.drawable.ic_logo_indosat;

                break;
            case 55:
            case 56:
            case 72:
            case 77:
                logoop=R.drawable.ic_logo_smartphone;
                break;
            case 71:
            case 76:
                logoop=R.drawable.logo_matrix;
                break;
            case 62:
            case 61:
            case 73:
            case 78:
                logoop=R.drawable.logo3;
                break;
            case 64:
            case 74:
                logoop=R.drawable.logoxl;
                break;
            case 66:
                logoop=R.drawable.ic_logo_playstore;
                break;
            case 67:
                logoop=R.drawable.logo_wifiid;
                break;
            case 11:
                logoop=R.drawable.ic_logo_pubgm;
                break;
            case 12:
                logoop=R.drawable.ic_logo_ffm;
                break;
            case 13:
                logoop=R.drawable.ic_logo_ml;
                break;
            case 14:
                logoop=R.drawable.ic_logo_steam;
                break;
            default:
                logoop=R.drawable.logoumy;
                break;
        }*/

        //int resID = ctx.getResources().getIdentifier(praktek., "drawable", ctx.getPackageName());

        //holder.img_operator.setImageResource(logoop);
        if(logo == null){
            logo ="";
        }

        if(logo.length()>=1) {
            Glide.with(ctx)
                    .load(logo).placeholder(R.drawable.logoumy)
                    .into(holder.img_1);
        }
        else{
            if(praktek.subcategory !=null) {
                if (praktek.subcategory.logo != null) {
                    Log.i("adapter", "p" + praktek.subcategory.logo);
                    Glide.with(ctx)
                            .load(praktek.subcategory.logo).placeholder(R.drawable.logoumy)
                            .into(holder.img_1);
                }
            }

        }

        //holder.img_1.setImageResource(logoop);
        holder.txt_1.setText(praktek.name);
        float a=praktek.price_user/1000;
        int multiplier=(int) a+1;




        if(praktek.price_user==0){
            holder.txt_2.setText("");
            holder.txt_2.setVisibility(View.GONE);
        }
        else{
           // holder.txt_2.setText(bantuan.torupiah(multiplier*1000));
            holder.txt_2.setText(bantuan.torupiah(praktek.price_user));
        }






        // Glide.with(ctx).load(myConfig.root_url + "images/PulsaOperator/" + praktek.getGambar_PulsaOperator()).into(holder.img_PulsaOperator);




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent=new Intent(ctx,WebActivity.class);
               // intent.putExtra("url",myConfig.main_url+"/banner/PulsaOperator/"+praktek.getId_PulsaOperator());
               // ctx.startActivity(intent);
                onselect.onselectitem(position);
            }
        });



    }
}
