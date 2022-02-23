package com.pontianak.teknikpayment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pontianak.teknikpayment.Adapter.HistoriAdapter;
import com.pontianak.teknikpayment.Model.History;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiwayatActivity extends KuberlayarDilautan {

    RecyclerView recyclerView;
    List<History> pulsaOperators=new ArrayList<>();
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    HistoriAdapter historiAdapter;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        service_connector=new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);
        recyclerView=findViewById(R.id.recy);
        gson=new GsonBuilder().create();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
            historiAdapter=new HistoriAdapter(pulsaOperators, this, new HistoriAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {

                Intent intent=new Intent(RiwayatActivity.this, DetailHistoryActivity.class);
              intent.putExtra("item",pulsaOperators.get(item));
//                intent.putExtra("code",pulsaOperators.get(item).code);
//                intent.putExtra("image",pulsaOperators.get(item).subcat_id);
//                intent.putExtra("nama",pulsaOperators.get(item).name);
//                intent.putExtra("harga",String.valueOf(pulsaOperators.get(item).price_user));
//
                startActivity(intent);




            }
        });
        recyclerView.setAdapter(historiAdapter);
        Intent intent=getIntent();
        loadopertator(intent.getStringExtra("idd"));







    }
    public void loadopertator(String idd){
        Map<String,String> map=new HashMap();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/history-transaction.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject respon=new JSONObject(response);
                    if(respon.getBoolean("status")){

                        JSONArray datas=new JSONArray(respon.getString("message"));
                        for(int b=0;b<datas.length();b++){
                            JSONObject objNilai=new JSONObject(datas.get(b).toString());
                            //History nilai=gson.fromJson(datas.get(b).toString(),History.class);
                            History nilai;
                            switch (objNilai.getString("jenis")){
                                case "PEMBELIAN":
                                    nilai = new History(objNilai.getString("refid"), objNilai.getString("status"), objNilai.getString("tgl"), objNilai.getString("tgl_human"), objNilai.getString("jenis"), objNilai.getInt("nominal"), objNilai.getString("nominal_text"), objNilai.getInt("biaya"), objNilai.getString("biaya_text"), objNilai.getInt("saldo_saat_transaksi"), objNilai.getString("saldo_saat_transaksi_text"), objNilai.getString("keterangan"), objNilai.getString("detail_dflash"));

                                    break;
                                case "TOPUP SALDO":
                                    nilai = new History(objNilai.getString("refid"), objNilai.getString("status"), objNilai.getString("tgl"), objNilai.getString("tgl_human"), objNilai.getString("jenis"), objNilai.getInt("nominal"), objNilai.getString("nominal_text"), objNilai.getInt("biaya"), objNilai.getString("biaya_text"), objNilai.getInt("saldo_saat_transaksi"), objNilai.getString("saldo_saat_transaksi_text"), objNilai.getString("saldo_masuk_text"), objNilai.getString("detail_midtrans"));

                                    break;
                                case "KIRIM":
                                    nilai = new History(objNilai.getString("refid"), objNilai.getString("status"), objNilai.getString("tgl"), objNilai.getString("tgl_human"), objNilai.getString("jenis"), objNilai.getInt("nominal"), objNilai.getString("nominal_text"), objNilai.getInt("biaya"), objNilai.getString("biaya_text"), objNilai.getInt("saldo_saat_transaksi"), objNilai.getString("saldo_saat_transaksi_text"), objNilai.getString("penerima"), objNilai.getString("nik_penerima"));

                                    break;
                                case "TERIMA":
                                    nilai = new History(objNilai.getString("refid"), objNilai.getString("status"), objNilai.getString("tgl"), objNilai.getString("tgl_human"), objNilai.getString("jenis"), objNilai.getInt("nominal"), objNilai.getString("nominal_text"), objNilai.getInt("biaya"), objNilai.getString("biaya_text"), objNilai.getInt("saldo_saat_transaksi"), objNilai.getString("saldo_saat_transaksi_text"), objNilai.getString("pengirim"), objNilai.getString("nik_pengirim"));

                                    break;
                                case "PENARIKAN":
                                    nilai = new History(objNilai.getString("refid"), objNilai.getString("status"), objNilai.getString("tgl"), objNilai.getString("tgl_human"), objNilai.getString("jenis"), objNilai.getInt("nominal"), objNilai.getString("nominal_text"), objNilai.getInt("biaya"), objNilai.getString("biaya_text"), objNilai.getInt("saldo_saat_transaksi"), objNilai.getString("saldo_saat_transaksi_text"), objNilai.getString("bank"), objNilai.getString("nomor_rekening")+" - "+objNilai.getString("atas_nama"));

                                    break;
                                    default:
                                        nilai = new History(objNilai.getString("refid"), objNilai.getString("status"), objNilai.getString("tgl"), objNilai.getString("tgl_human"), objNilai.getString("jenis"), objNilai.getInt("nominal"), objNilai.getString("nominal_text"), objNilai.getInt("biaya"), objNilai.getString("biaya_text"), objNilai.getInt("saldo_saat_transaksi"), objNilai.getString("saldo_saat_transaksi_text"), objNilai.getString("penerima"), objNilai.getString("nik_pengirim"));

                                        break;

                            }

                            pulsaOperators.add(nilai);
                        }



                        historiAdapter.notifyDataSetChanged();

                    }
                    else{

                    }

                }
                catch (Exception e){
                    Log.i("ez",e.getMessage());

                }

            }

            @Override
            public void onNoConnection(String message) {

            }

            @Override
            public void OnServerError(String message) {

            }

            @Override
            public void OnTimeOut() {

            }
        });
    }
}
