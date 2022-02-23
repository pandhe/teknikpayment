package com.pontianak.teknikpayment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pontianak.teknikpayment.Adapter.PulsaOperatorAdapter;
import com.pontianak.teknikpayment.Model.PulsaOperator;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PulsaOperatorActivity extends KuberlayarDilautan {
    RecyclerView recyclerView;
    List<PulsaOperator> pulsaOperators=new ArrayList<>();
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    PulsaOperatorAdapter pulsaOperatorAdapter;
    Gson gson;
    SwipeRefreshLayout swiperefresh;
    public String logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulsa_operator);
        service_connector=new Service_Connector();
        TextView txt_title=findViewById(R.id.txt_title);
        Intent intent=getIntent();

        txt_title.setText(intent.getStringExtra("title"));
        logo=intent.getStringExtra("logo");
        ImageButton bt_back=findViewById(R.id.bt_back);
        //bt_back.setImageResource(R.drawable.ic_menu_pulsa);
        swiperefresh=findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadopertator(intent.getStringExtra("idd"));
            }
        });
        sharedPrefManager = new SharedPrefManager(this);
        recyclerView=findViewById(R.id.recy);
        gson=new GsonBuilder().create();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pulsaOperatorAdapter=new PulsaOperatorAdapter(pulsaOperators, this, logo, new PulsaOperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {

                Intent intent=new Intent(PulsaOperatorActivity.this, TransaksiActivity.class);
                try{
                    showlog(pulsaOperators.get(item).subcategory.toString());
                 // JSONObject subcat=new JSONObject(pulsaOperators.get(item).subcategory);
                 // JSONObject CATEGORY=new JSONObject(subcat.getString("category"));
                   intent.putExtra("tipe",pulsaOperators.get(item).subcategory.category.tipe);
                    intent.putExtra("logo",String.valueOf(logo));

                    //intent.putExtra("aksi","Pulsa ");

                    // intent.putExtra("tipe",pulsaOperators.get(item).subcategory.category.tipe);

                }
                catch (Throwable t){
                    showlog(t.getMessage());

                }


                        intent.putExtra("id",pulsaOperators.get(item).id);
                        intent.putExtra("code",pulsaOperators.get(item).code);
                        intent.putExtra("image",pulsaOperators.get(item).subcat_id);
                        intent.putExtra("nama",pulsaOperators.get(item).name);
                        intent.putExtra("harga",String.valueOf(pulsaOperators.get(item).price_user));


                        startActivity(intent);




            }
        });
        recyclerView.setAdapter(pulsaOperatorAdapter);

        loadopertator(intent.getStringExtra("idd"));
        swiperefresh.setRefreshing(true);







    }
    public void loadopertator(String idd){
        Map<String,String> map=new HashMap();

        pulsaOperators.clear();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/product.aspx/"+idd, map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }
            @Override
            public void onError(String message) {
                swiperefresh.setRefreshing(false);
            }

            @Override
            public void onResponese(String response) {
                swiperefresh.setRefreshing(false);
                try{
                    JSONObject respon=new JSONObject(response);
                    if(respon.getBoolean("status")){

                        JSONArray datas=new JSONArray(respon.getString("message"));
                        if(datas.length()>1){
                        for(int b=0;b<datas.length();b++){
                            //JSONObject objnilai=new JSONObject(datas.get(b).toString());
                            PulsaOperator nilai=gson.fromJson(datas.get(b).toString(),PulsaOperator.class);

                            pulsaOperators.add(nilai);
                        }
                    }
                    else{
                        PulsaOperator nilai = gson.fromJson(datas.get(0).toString(), PulsaOperator.class);
                        langsungsaja(nilai);
                    }



                        pulsaOperatorAdapter.notifyDataSetChanged();

                    }
                    else{

                    }

                }
                catch (Exception e){
                    swiperefresh.setRefreshing(false);
                    showlog(e.getMessage());


                }

            }

            @Override
            public void onNoConnection(String message) {
                swiperefresh.setRefreshing(false);
            }

            @Override
            public void OnServerError(String message) {
                swiperefresh.setRefreshing(false);
            }

            @Override
            public void OnTimeOut() {
                swiperefresh.setRefreshing(false);
            }
        });
    }
    private void langsungsaja(PulsaOperator operator){
        Intent intent=new Intent(PulsaOperatorActivity.this, TransaksiActivity.class);
        intent.putExtra("id",operator.id);
        intent.putExtra("code",operator.code);
        intent.putExtra("image",operator.subcat_id);
        intent.putExtra("nama",operator.name);
        intent.putExtra("harga",operator.price_user);

        startActivity(intent);
        this.finish();
    }
}