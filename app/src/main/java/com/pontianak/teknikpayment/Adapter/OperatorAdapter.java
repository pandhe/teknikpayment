package com.pontianak.teknikpayment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.pontianak.teknikpayment.Model.Operator;
import com.pontianak.teknikpayment.R;

import java.util.List;

public class OperatorAdapter extends RecyclerView.Adapter<OperatorAdapter.praktekholder> {
    public List<Operator> promo;
    public Context ctx;
    public LayoutInflater lay;
    String bobot="100";
    onSelect onselect;
    public int laymode;


    public class praktekholder extends RecyclerView.ViewHolder {
        //TextView txt_1,txt_2,txt_3;
        TextView txt_1;
        ImageView img_operator;
        //Guideline guideline13;

        public praktekholder(View itemView) {
            super(itemView);
            //this.guideline13=itemView.findViewById(R.id.guideline27);

            //this.txt_pengurus_kebaktian=itemView.findViewById(R.id.txt_tempat_kunjungan);

            this.img_operator=itemView.findViewById(R.id.img_operator);
            this.txt_1=itemView.findViewById(R.id.txt3);


        }
    }
    public interface onSelect{
        public void onselectitem(int item);
    }

    @Override
    public int getItemCount() {
        return this.promo.size();
    }

    public OperatorAdapter(List<Operator> promo, Context ctx) {
        this.promo = promo;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        this.laymode=0;

    }
    public OperatorAdapter(List<Operator> promo, Context ctx, onSelect onselect) {
        this.promo = promo;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        this.onselect=onselect;

    }
    public OperatorAdapter(List<Operator> promo, Context ctx,int laymode, onSelect onselect) {
        this.promo = promo;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        this.onselect=onselect;
        this.laymode=laymode;

    }

    @NonNull
    @Override
    public praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview;
        rootview=lay.inflate(R.layout.content_list_operator_pulsa, parent, false);
        if(this.laymode==1){
            rootview=lay.inflate(R.layout.content_list_operator_pulsa_linear, parent, false);
        }
        return new praktekholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull praktekholder holder, @SuppressLint("RecyclerView") final int position) {
        final Operator praktek = promo.get(position);
        int logoop;
        holder.txt_1.setText(praktek.name);
        /*switch (praktek.id){
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
            case 67:
                logoop=R.drawable.logo_wifiid;
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

        Glide.with(ctx)
                .load(praktek.logo).placeholder(R.drawable.ic_logountan).fitCenter()
                .into(holder.img_operator);

        //int resID = ctx.getResources().getIdentifier(praktek., "drawable", ctx.getPackageName());

       // holder.img_operator.setImageResource(logoop);






        // Glide.with(ctx).load(myConfig.root_url + "images/Operator/" + praktek.getGambar_Operator()).into(holder.img_Operator);



        //holder.txt_nama_kebaktian.setText(praktek.getJudul_Operator());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent=new Intent(ctx,WebActivity.class);
               // intent.putExtra("url",myConfig.main_url+"/banner/Operator/"+praktek.getId_Operator());
               // ctx.startActivity(intent);
                onselect.onselectitem(position);
            }
        });



    }
}
