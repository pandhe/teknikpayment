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
import com.pontianak.teknikpayment.Adapter.OperatorAdapter;
import com.pontianak.teknikpayment.Adapter.PulsaOperatorAdapter;
import com.pontianak.teknikpayment.Model.Operator;
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

public class MyListrikActivity extends KuberlayarDilautan {
    RecyclerView recyclerView2;
    List<PulsaOperator> operators=new ArrayList<>();
    List<PulsaOperator> tokenpln=new ArrayList<>();
    List<PulsaOperator> vocherwifid=new ArrayList<>();
    List<Operator> operatordata=new ArrayList<>();
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    PulsaOperatorAdapter pulsaOperatorAdapter;
    OperatorAdapter operatordataadapter;
    PulsaOperatorAdapter tokenplnadapter;
    PulsaOperatorAdapter wifiidadapter;
    Gson gson;
    ProgressBar progress_1,progress_2,progress_3,progress_4;

    //pascabayar
    RecyclerView recy_pasca;
    List<PulsaOperator> operatorpasca=new ArrayList<>();
    PulsaOperatorAdapter operatorpascaadapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listrik);
        TextView txt_title=findViewById(R.id.txt_title);
        txt_title.setText("Listrik");
        ImageButton bt_back=findViewById(R.id.bt_back);
        //bt_back.setImageResource(R.drawable.ic_menu_voucher);
        service_connector=new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);

        //pasca

        recy_pasca=findViewById(R.id.recy_pasca);
        recy_pasca.setLayoutManager(new LinearLayoutManager(this));
        recy_pasca.setNestedScrollingEnabled(false);
        operatorpascaadapter=new PulsaOperatorAdapter(operatorpasca,this, new PulsaOperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_CODE_PRODUCT,operatorpasca.get(item).code);
                Intent intent=new Intent(MyListrikActivity.this, TransaksiActivity.class);
                intent.putExtra("id",operatorpasca.get(item).id);
                intent.putExtra("code",operatorpasca.get(item).code);
                intent.putExtra("image",operatorpasca.get(item).subcat_id);
                intent.putExtra("nama",operatorpasca.get(item).name);
                intent.putExtra("harga",String.valueOf(operatorpasca.get(item).price_user));
                intent.putExtra("tipepasca","cek");
                intent.putExtra("logo",String.valueOf(operatorpasca.get(item).subcategory.logo));
                sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_ACTION,"Listrik Pasca Bayar");
                startActivity(intent);

            }
        });
        recy_pasca.setAdapter(operatorpascaadapter);


        recyclerView2=findViewById(R.id.recy_pln);
        progress_1=findViewById(R.id.progress_1);
        progress_2=findViewById(R.id.progress_2);
        progress_3=findViewById(R.id.progress_3);
        progress_4=findViewById(R.id.progress_4);
        gson=new GsonBuilder().create();
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        pulsaOperatorAdapter=new PulsaOperatorAdapter(operators, this, new PulsaOperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                Intent intent=new Intent(MyListrikActivity.this, TransaksiActivity.class);
                intent.putExtra("id",operators.get(item).id);
                intent.putExtra("code",operators.get(item).code);
                intent.putExtra("image",operators.get(item).subcat_id);
                intent.putExtra("nama",operators.get(item).name);
                intent.putExtra("harga",String.valueOf(operators.get(item).price_user));
                intent.putExtra("tipe",operators.get(item).subcategory.category.tipe);
                sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_ACTION,"Token Listrik");
                startActivity(intent);
            }
        });
        operatordataadapter=new OperatorAdapter(operatordata,this, new OperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                Intent intent=new Intent(MyListrikActivity.this, PulsaOperatorActivity.class);
                intent.putExtra("title",operatordata.get(item).name);
                intent.putExtra("idd",String.valueOf(operatordata.get(item).id));
                intent.putExtra("logo",operatordata.get(item).logo);

                startActivity(intent);

            }
        });
        tokenplnadapter=new PulsaOperatorAdapter(tokenpln, this, new PulsaOperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                Intent intent=new Intent(MyListrikActivity.this, TransaksiActivity.class);
                intent.putExtra("id",tokenpln.get(item).id);
                intent.putExtra("code",tokenpln.get(item).code);
                intent.putExtra("image",tokenpln.get(item).subcat_id);
                intent.putExtra("nama",tokenpln.get(item).name);
                intent.putExtra("harga",String.valueOf(tokenpln.get(item).price_user));
                intent.putExtra("harga",String.valueOf(tokenpln.get(item).price_user));
                intent.putExtra("tipe",tokenpln.get(item).subcategory.category.tipe);
                intent.putExtra("logo",String.valueOf(tokenpln.get(item).subcategory.logo));

                startActivity(intent);
            }
        });

        wifiidadapter=new PulsaOperatorAdapter(vocherwifid, this, new PulsaOperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                Intent intent=new Intent(MyListrikActivity.this, TransaksiActivity.class);
                intent.putExtra("id",vocherwifid.get(item).id);
                intent.putExtra("code",vocherwifid.get(item).code);
                intent.putExtra("image",vocherwifid.get(item).subcat_id);
                intent.putExtra("nama",vocherwifid.get(item).name);
                intent.putExtra("harga",String.valueOf(vocherwifid.get(item).price_user));
                intent.putExtra("tipe",vocherwifid.get(item).subcategory.category.tipe);
                // intent.putExtra("harga",String.valueOf(tokenpln.get(item).price_user));

                startActivity(intent);
            }
        });
       recyclerView2.setAdapter(tokenplnadapter);

        recyclerView2.setNestedScrollingEnabled(false);


        loadpascabayar();







    }

    public void loadpascabayar(){
        Map<String,String> map=new HashMap();
        operatorpasca.clear();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/product.aspx/124", map, new Service_Connector.VolleyResponseListener_v3() {
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
                        loadtokenpln();



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
    public void loadtokenpln(){
        Map<String,String> map=new HashMap();
        tokenpln.clear();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/product.aspx/97", map, new Service_Connector.VolleyResponseListener_v3() {
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

