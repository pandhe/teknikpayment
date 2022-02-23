package com.pontianak.teknikpayment;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class MyTvInternetActivity extends KuberlayarDilautan {
    RecyclerView recyclerView2,recyclerView3;
    List<PulsaOperator> tokenpln=new ArrayList<>();
    List<PulsaOperator> vocherwifid=new ArrayList<>();
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    PulsaOperatorAdapter tokenplnadapter;
    PulsaOperatorAdapter wifiidadapter;
    Gson gson;
    ProgressBar progress_1,progress_4;

    //pascabayar
    RecyclerView recy_pasca;
    List<PulsaOperator> operatorpasca=new ArrayList<>();
    PulsaOperatorAdapter operatorpascaadapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_internet);
        TextView txt_title=findViewById(R.id.txt_title);
        txt_title.setText("Tv & Internet");
        ImageButton bt_back=findViewById(R.id.bt_back);
        //bt_back.setImageResource(R.drawable.ic_menu_voucher);
        service_connector=new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);

        recyclerView2=findViewById(R.id.recy_orange);
        recyclerView3=findViewById(R.id.recy_operator_wifiid);
        progress_1=findViewById(R.id.progress_1);
        progress_4=findViewById(R.id.progress_4);
        gson=new GsonBuilder().create();
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));

        recy_pasca=findViewById(R.id.recy_pasca);
        recy_pasca.setLayoutManager(new LinearLayoutManager(this));
        recy_pasca.setNestedScrollingEnabled(false);
        operatorpascaadapter=new PulsaOperatorAdapter(operatorpasca,this, new PulsaOperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_CODE_PRODUCT,operatorpasca.get(item).code);
                Intent intent=new Intent(MyTvInternetActivity.this, TransaksiActivity.class);
                intent.putExtra("id",operatorpasca.get(item).id);
                intent.putExtra("code",operatorpasca.get(item).code);
                intent.putExtra("image",operatorpasca.get(item).subcat_id);
                intent.putExtra("nama",operatorpasca.get(item).name);
                intent.putExtra("harga",String.valueOf(operatorpasca.get(item).price_user));
                intent.putExtra("tipepasca","cek");
                intent.putExtra("logo",String.valueOf(operatorpasca.get(item).subcategory.logo));
                sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_ACTION,"Internet Pasca Bayar");
                startActivity(intent);

            }
        });
        recy_pasca.setAdapter(operatorpascaadapter);


        tokenplnadapter=new PulsaOperatorAdapter(tokenpln, this, new PulsaOperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                Intent intent=new Intent(MyTvInternetActivity.this, TransaksiActivity.class);
                intent.putExtra("id",tokenpln.get(item).id);
                intent.putExtra("code",tokenpln.get(item).code);
                intent.putExtra("image",tokenpln.get(item).subcat_id);
                intent.putExtra("nama",tokenpln.get(item).name);
                intent.putExtra("harga",String.valueOf(tokenpln.get(item).price_user));
                intent.putExtra("harga",String.valueOf(tokenpln.get(item).price_user));
                intent.putExtra("tipe",tokenpln.get(item).subcategory.category.tipe);
                intent.putExtra("logo",String.valueOf(tokenpln.get(item).subcategory.logo));
                    sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_ACTION,"TV");

                startActivity(intent);
            }
        });

        wifiidadapter=new PulsaOperatorAdapter(vocherwifid, this, new PulsaOperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                Intent intent=new Intent(MyTvInternetActivity.this, TransaksiActivity.class);
                intent.putExtra("id",vocherwifid.get(item).id);
                intent.putExtra("code",vocherwifid.get(item).code);
                intent.putExtra("image",vocherwifid.get(item).subcat_id);
                intent.putExtra("nama",vocherwifid.get(item).name);
                intent.putExtra("harga",String.valueOf(vocherwifid.get(item).price_user));
                intent.putExtra("tipe",vocherwifid.get(item).subcategory.category.tipe);
                // intent.putExtra("harga",String.valueOf(tokenpln.get(item).price_user));
                intent.putExtra("logo",String.valueOf(vocherwifid.get(item).subcategory.logo));
                sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_ACTION,"Internet");
                startActivity(intent);
            }
        });
        recyclerView2.setAdapter(tokenplnadapter);

        recyclerView2.setNestedScrollingEnabled(false);

        recyclerView3.setAdapter(wifiidadapter);

        recyclerView3.setNestedScrollingEnabled(false);

        loadtokenpln();







    }
    public void loadtokenpln(){
        Map<String,String> map=new HashMap();
        tokenpln.clear();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/product.aspx/98", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {
                progress_1.setVisibility(View.GONE);
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
                            //JSONObject objnilai=new JSONObject(datas.get(b).toString());
                            PulsaOperator nilai=gson.fromJson(datas.get(b).toString(),PulsaOperator.class);



                            tokenpln.add(nilai);
                        }



                        tokenplnadapter.notifyDataSetChanged();
                        //loadopertator_data();
                        loadwifiid();


                    }
                    else{

                    }

                }
                catch (Exception e){

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

    public void loadwifiid(){
        Map<String,String> map=new HashMap();
        vocherwifid.clear();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/product.aspx/126", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {
                progress_4.setVisibility(View.GONE);
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
                            //JSONObject objnilai=new JSONObject(datas.get(b).toString());
                            PulsaOperator nilai=gson.fromJson(datas.get(b).toString(),PulsaOperator.class);


                            vocherwifid.add(nilai);
                        }



                        wifiidadapter.notifyDataSetChanged();
                        //loadopertator_data();
                        loadpascabayar();

                    }
                    else{

                    }

                }
                catch (Exception e){

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

    public void loadpascabayar(){
        Map<String,String> map=new HashMap();
        operatorpasca.clear();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/product.aspx/105", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {
                progress_1.setVisibility(View.GONE);
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
                            //JSONObject objnilai=new JSONObject(datas.get(b).toString());
                            PulsaOperator nilai=gson.fromJson(datas.get(b).toString(),PulsaOperator.class);



                            operatorpasca.add(nilai);
                        }



                        operatorpascaadapter.notifyDataSetChanged();




                    }
                    else{

                    }

                }
                catch (Exception e){

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

