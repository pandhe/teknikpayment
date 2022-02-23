package com.pontianak.teknikpayment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pontianak.teknikpayment.Adapter.NotifikasiAdapter;
import com.pontianak.teknikpayment.Model.Notifikasi;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyNotifikasiActivity extends KuberlayarDilautan {

    RecyclerView recyclerView;
    List<Notifikasi> notifikasis=new ArrayList<>();
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    NotifikasiAdapter notifikasiAdapter;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        service_connector=new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);
        TextView txt_title=findViewById(R.id.txt_title);
        txt_title.setText("Notifikasi");
        recyclerView=findViewById(R.id.recy);
        gson=new GsonBuilder().create();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notifikasiAdapter=new NotifikasiAdapter(notifikasis, this, new NotifikasiAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {

                //Intent intent=new Intent(MyNotifikasiActivity.this, DetailHistoryActivity.class);
               // intent.putExtra("item",pulsaOperators.get(item));
//                intent.putExtra("code",pulsaOperators.get(item).code);
//                intent.putExtra("image",pulsaOperators.get(item).subcat_id);
//                intent.putExtra("nama",pulsaOperators.get(item).name);
//                intent.putExtra("harga",String.valueOf(pulsaOperators.get(item).price_user));
//
               // startActivity(intent);




            }
        });
        recyclerView.setAdapter(notifikasiAdapter);
        Intent intent=getIntent();
        loadopertator(intent.getStringExtra("idd"));







    }
    public void loadopertator(String idd){
        Map<String,String> map=new HashMap();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        map.put("pagination", "1");
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/data-notif.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
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

                        JSONArray datas=new JSONArray(respon.getString("data"));
                        for(int b=0;b<datas.length();b++){
                            JSONObject objNilai=new JSONObject(datas.get(b).toString());
                            Notifikasi notif=gson.fromJson(datas.get(b).toString(),Notifikasi.class);


                            notifikasis.add(notif);
                        }



                        notifikasiAdapter.notifyDataSetChanged();

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
