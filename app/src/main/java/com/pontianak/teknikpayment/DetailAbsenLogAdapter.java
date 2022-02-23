package com.pontianak.teknikpayment;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pontianak.teknikpayment.R;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class DetailAbsenLogAdapter extends RecyclerView.Adapter<DetailAbsenLogAdapter.ViewHolder> {

    private List<DetailAbsen> mData;
    private LayoutInflater mInflater;
    private Context ctx;

    // data is passed into the constructor
    DetailAbsenLogAdapter(Context context, List<DetailAbsen> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.ctx = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.logabsen_content, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DetailAbsen detailAbsen = mData.get(position);
        String waktuTrack = detailAbsen.getWaktuTrack();
        String statusTrack = detailAbsen.getStatusTrack();

        int backgroundStatus = R.drawable.background_masuk_rounded;
        String textStatus = "Status : Didalam lingkungan universitas";

        switch(statusTrack){
            case "-" :
                backgroundStatus = R.drawable.background_gray_rounded;
                textStatus = "Status : Tidak terdata";
                break;
            case "LUAR" :
                backgroundStatus = R.drawable.background_red_rounded;
                textStatus = "Status : Diluar lingkungan universitas";
                break;
            case "BREAK" :
                textStatus = "Status : Istirahat (break)";
                break;
        }

        holder.tvTanggal.setText(waktuTrack);
        holder.tvDetail.setText(textStatus);
        holder.tvTunjangan.setVisibility(View.GONE);
        holder.tvStatus.setBackground(ContextCompat.getDrawable(ctx,backgroundStatus));
        holder.tvStatus.setText(statusTrack);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvStatus;
        private final TextView tvTanggal;
        private final TextView tvDetail;
        private final TextView tvTunjangan;
        private final LinearLayout layLog;

        ViewHolder(View itemView) {
            super(itemView);
            tvStatus = (TextView) itemView.findViewById(R.id.status);
            tvTanggal = (TextView) itemView.findViewById(R.id.tanggal);
            tvDetail = (TextView) itemView.findViewById(R.id.detailLog);
            tvTunjangan = (TextView) itemView.findViewById(R.id.tunjangan);
            layLog = (LinearLayout) itemView.findViewById(R.id.layLog);
        }


    }

}