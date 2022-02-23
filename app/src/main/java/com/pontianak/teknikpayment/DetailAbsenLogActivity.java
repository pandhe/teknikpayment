package com.pontianak.teknikpayment;

import android.app.ProgressDialog;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.pontianak.teknikpayment.Service.Server;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DetailAbsenLogActivity extends AppCompatActivity {
    TextView tvMasuk, tvPulang, tvJumlah, tvBreak, tvTanggal;
    String idUser;
    DetailAbsenLogAdapter detailAbsenLogAdapter;
    RecyclerView rvLog;
    Service_Connector service_connector;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_log);

        TextView txt_title=findViewById(R.id.txt_title);
        txt_title.setText("DETAIL LOG");
        ImageButton bt_back=findViewById(R.id.bt_back);
        bt_back.setImageResource(R.drawable.ic_menu_absen);



        tvMasuk = findViewById(R.id.fieldMasuk);
        tvPulang = findViewById(R.id.fieldPulang);
        tvBreak = findViewById(R.id.fieldBreak);
        tvJumlah = findViewById(R.id.fieldJumlah);
        tvTanggal = findViewById(R.id.fieldTanggal);

        String waktuMasuk = getIntent().getStringExtra("waktuMasuk");
        String waktuPulang = getIntent().getStringExtra("waktuPulang");
        String waktuBreak = getIntent().getStringExtra("waktuBreak");
        String tanggal = getIntent().getStringExtra("tanggal");
        String totalWaktu = getIntent().getStringExtra("banyakWaktu");

        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        idUser = sharedPrefManager.getSpString(SharedPrefManager.SP_ID);

        if(totalWaktu == null){
            totalWaktu = "-";
        }

        if(waktuMasuk == null || waktuPulang == null || waktuBreak == null || tanggal == null || idUser == null){
            Toast.makeText(this, "Terjadi kesalahan teknis !!!", Toast.LENGTH_LONG).show();
            finish();
        }

        if(waktuBreak.equals("TIDAK BREAK OUT-TIDAK BREAK IN")){
            waktuBreak = "TIDAK ADA ISTIRAHAT";
        }

        tvTanggal.setText(tanggal);
        tvMasuk.setText(waktuMasuk);
        tvBreak.setText(waktuBreak);
        tvPulang.setText(waktuPulang);
        tvJumlah.setText(totalWaktu);

        service_connector = new Service_Connector();

        List<DetailAbsen> listDetailLog = new ArrayList<DetailAbsen>();

        rvLog = findViewById(R.id.revLog);
        detailAbsenLogAdapter = new DetailAbsenLogAdapter(DetailAbsenLogActivity.this, listDetailLog);
        RecyclerView.LayoutManager recyce = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvLog.setLayoutManager(recyce);
        rvLog.setAdapter(detailAbsenLogAdapter);

        getDetailLog(tanggal);
    }


    public void getDetailLog(String tanggal){
        showProgressDialog();
        tanggal = tanggal.replace("(HARI INI)","");

        String urlLog = Server.urlServer + "/getDetailAbsen.php?id="+idUser+"&tanggal="+tanggal;
        HashMap<String, String> params = new HashMap<String, String>();

        service_connector.url_sendpostrequest(this, urlLog, params, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }

            @Override
            public void onError(String message) {
                Log.d("VolleyError", String.valueOf(message));
                hideProgressDialog();
                Toast.makeText(DetailAbsenLogActivity.this, "Akses Ke Server Gagal", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponese(String response) {
                List<DetailAbsen> listDetailLog = new ArrayList<DetailAbsen>();
                try{
                    JSONObject responseObject = new JSONObject(response);

                    JSONArray jsonarray = new JSONArray(responseObject.getString("data"));
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String waktu = jsonobject.getString("waktu");
                        String status = jsonobject.getString("status");

                        DetailAbsen detailAbsen = new DetailAbsen();
                        detailAbsen.setWaktuTrack(waktu);
                        detailAbsen.setStatusTrack(status);

                        listDetailLog.add(detailAbsen);
                    }

                    DetailAbsenLogAdapter detailAbsenLogAdapter = new DetailAbsenLogAdapter(DetailAbsenLogActivity.this,listDetailLog);
                    rvLog.setAdapter(detailAbsenLogAdapter);
                    detailAbsenLogAdapter.notifyDataSetChanged();

                }catch(Exception e){
                    Log.d("jsonAbsenLog",String.valueOf(e));
                }finally {
                    hideProgressDialog();
                }
            }

            @Override
            public void onNoConnection(String message) {
                hideProgressDialog();
            }

            @Override
            public void OnServerError(String message) {
                hideProgressDialog();
            }

            @Override
            public void OnTimeOut() {
                hideProgressDialog();
            }
        });

    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Tunggu Sebentar!!!");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
