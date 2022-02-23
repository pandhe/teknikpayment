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
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferPulsaActivity extends KuberlayarDilautan {
    RecyclerView recyclerView;
    List<Operator> operators=new ArrayList<>();
    List<Operator> operatordata=new ArrayList<>();
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    OperatorAdapter operatorAdapter,operatordataadapter;
    Gson gson;
    ProgressBar progress_1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_pulsa);
        TextView txt_title=findViewById(R.id.txt_title);
        txt_title.setText("Transfer Pulsa");
        ImageButton bt_back=findViewById(R.id.bt_back);
        bt_back.setImageResource(R.drawable.ic_menu_pulsa);

        service_connector=new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);
        recyclerView=findViewById(R.id.recy_operator);

        gson=new GsonBuilder().create();
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        recyclerView.setNestedScrollingEnabled(false);

        progress_1=findViewById(R.id.progress_1);

        operatorAdapter=new OperatorAdapter(operators,this, new OperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                Intent intent=new Intent(TransferPulsaActivity.this, PulsaOperatorActivity.class);
                intent.putExtra("title",operators.get(item).name);
                intent.putExtra("idd",String.valueOf(operators.get(item).id));
                startActivity(intent);

            }
        });
        operatordataadapter=new OperatorAdapter(operatordata,this, new OperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                Intent intent=new Intent(TransferPulsaActivity.this, PulsaOperatorActivity.class);
                intent.putExtra("title",operatordata.get(item).name);
                intent.putExtra("idd",String.valueOf(operatordata.get(item).id));
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(operatorAdapter);

        loadopertator();







    }
    public void loadopertator(){
        Map<String,String> map=new HashMap();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/subcategory.aspx/10", map, new Service_Connector.VolleyResponseListener_v3() {
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
