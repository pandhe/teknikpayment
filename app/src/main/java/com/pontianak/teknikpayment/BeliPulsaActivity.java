package com.pontianak.teknikpayment;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pontianak.teknikpayment.Adapter.OperatorAdapter;
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

public class BeliPulsaActivity extends KuberlayarDilautan {
    RecyclerView recyclerView,recyclerView1,recy_operator_pasca;
    List<Operator> operators=new ArrayList<>();
    List<Operator> operatordata=new ArrayList<>();
    List<Operator> operatorpasca=new ArrayList<>();
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    OperatorAdapter operatorAdapter,operatordataadapter,operatorpascaadapter;
    Gson gson;
    ProgressBar progress_1,progress_2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beli_pulsa);
        TextView txt_title=findViewById(R.id.txt_title);
        txt_title.setText("Pulsa");
        ImageButton bt_back=findViewById(R.id.bt_back);
        //bt_back.setImageResource(R.drawable.img_pulsa);

        service_connector=new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);
        recyclerView=findViewById(R.id.recy_operator);
        recyclerView1=findViewById(R.id.recy_operator_paket);
        gson=new GsonBuilder().create();
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView1.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView1.setNestedScrollingEnabled(false);
        recy_operator_pasca=findViewById(R.id.recy_operator_pasca);
        progress_1=findViewById(R.id.progress_1);
        progress_2=findViewById(R.id.progress_2);
        operatorAdapter=new OperatorAdapter(operators,this, new OperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                Intent intent=new Intent(BeliPulsaActivity.this, PulsaOperatorActivity.class);
                intent.putExtra("title",operators.get(item).name);
                intent.putExtra("idd",String.valueOf(operators.get(item).id));
                intent.putExtra("logo",String.valueOf(operators.get(item).logo));
                sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_ACTION,"Pulsa");

                startActivity(intent);

            }
        });
        operatordataadapter=new OperatorAdapter(operatordata,this, new OperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                Intent intent=new Intent(BeliPulsaActivity.this, PulsaOperatorActivity.class);
                intent.putExtra("title",operatordata.get(item).name);
                intent.putExtra("idd",String.valueOf(operatordata.get(item).id));
                intent.putExtra("logo",String.valueOf(operatordata.get(item).logo));
                sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_ACTION,"Data");
                startActivity(intent);


            }
        });
        recyclerView.setAdapter(operatorAdapter);
        recyclerView1.setAdapter(operatordataadapter);
        loadopertator();







    }
    public void loadopertator(){
        Map<String,String> map=new HashMap();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/subcategory.aspx/2", map, new Service_Connector.VolleyResponseListener_v3() {
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
                            Operator nilai=gson.fromJson(datas.get(b).toString(),Operator.class);

                            operators.add(nilai);
                        }



                        operatorAdapter.notifyDataSetChanged();
                        loadopertator_data();

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

    public void loadopertator_data(){
        Map<String,String> map=new HashMap();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/subcategory.aspx/3", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {
                progress_2.setVisibility(View.GONE);
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
                            Operator nilai=gson.fromJson(datas.get(b).toString(),Operator.class);

                            operatordata.add(nilai);
                        }



                        operatordataadapter.notifyDataSetChanged();

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
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/product.aspx/103", map, new Service_Connector.VolleyResponseListener_v3() {
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



                            //operatorpasca.add(nilai);
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
