        package com.pontianak.teknikpayment;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class MyVoucherActivity extends KuberlayarDilautan {
    RecyclerView recyclerView,recyclerView1;
    List<PulsaOperator> operators=new ArrayList<>();


    List<Operator> operatordata=new ArrayList<>();
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    PulsaOperatorAdapter pulsaOperatorAdapter;
    OperatorAdapter operatordataadapter;

    PulsaOperatorAdapter wifiidadapter;
    Gson gson;
    ProgressBar progress_2,progress_3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_voucher);
        TextView txt_title=findViewById(R.id.txt_title);
        txt_title.setText("Voucher");
        ImageButton bt_back=findViewById(R.id.bt_back);
        //bt_back.setImageResource(R.drawable.ic_menu_voucher);
        service_connector=new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);
        recyclerView=findViewById(R.id.recy_operator);
        recyclerView1=findViewById(R.id.recy_operator_paket);



        progress_2=findViewById(R.id.progress_2);
        progress_3=findViewById(R.id.progress_3);


        gson=new GsonBuilder().create();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setLayoutManager(new GridLayoutManager(this,2));


        pulsaOperatorAdapter=new PulsaOperatorAdapter(operators, this, new PulsaOperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                Intent intent=new Intent(MyVoucherActivity.this, TransaksiActivity.class);
                intent.putExtra("id",operators.get(item).id);
                intent.putExtra("code",operators.get(item).code);
                intent.putExtra("image",operators.get(item).subcat_id);
                intent.putExtra("nama",operators.get(item).name);
                intent.putExtra("harga",String.valueOf(operators.get(item).price_user));
                intent.putExtra("tipe",operators.get(item).subcategory.category.tipe);
                sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_ACTION,"Voucher");
                startActivity(intent);
            }
        });
        operatordataadapter=new OperatorAdapter(operatordata,this, new OperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                Intent intent=new Intent(MyVoucherActivity.this, PulsaOperatorActivity.class);
                intent.putExtra("title",operatordata.get(item).name);
                intent.putExtra("idd",String.valueOf(operatordata.get(item).id));
                intent.putExtra("logo",operatordata.get(item).logo);
                sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_ACTION,"Voucher");
                startActivity(intent);

            }
        });



        recyclerView.setAdapter(pulsaOperatorAdapter);

        recyclerView1.setAdapter(operatordataadapter);



        recyclerView1.setNestedScrollingEnabled(false);
        recyclerView.setNestedScrollingEnabled(false);

        loadopertator();






    }

    public void loadopertator(){
        Map<String,String> map=new HashMap();
        operators.clear();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/product.aspx/127", map, new Service_Connector.VolleyResponseListener_v3() {
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
                            PulsaOperator nilai=gson.fromJson(datas.get(b).toString(),PulsaOperator.class);


                            operators.add(nilai);
                        }



                        pulsaOperatorAdapter.notifyDataSetChanged();
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
        operatordata.clear();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/subcategory.aspx/8", map, new Service_Connector.VolleyResponseListener_v3() {
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
                            //JSONObject objnilai=new JSONObject(datas.get(b).toString());
                            Operator nilai=gson.fromJson(datas.get(b).toString(),Operator.class);

                            operatordata.add(nilai);
                        }



                        operatordataadapter.notifyDataSetChanged();
                        // loadtokenpln();
                        progress_3.setVisibility(View.GONE);


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

