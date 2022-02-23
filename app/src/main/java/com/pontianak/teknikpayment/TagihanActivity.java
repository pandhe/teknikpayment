package com.pontianak.teknikpayment;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
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

public class TagihanActivity extends AppCompatActivity {
    RecyclerView recyclerView,recyclerView1;
    List<Operator> operators=new ArrayList<>();
    List<PulsaOperator> operatordata=new ArrayList<>();
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    OperatorAdapter operatorAdapter;
    PulsaOperatorAdapter operatordataadapter;
    Gson gson;
    ProgressBar progress_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagihan);
        TextView txt_title=findViewById(R.id.txt_title);
        txt_title.setText("Tagihan");
        ImageButton bt_back=findViewById(R.id.bt_back);

        service_connector=new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);
        recyclerView=findViewById(R.id.recyclerView);
        progress_1=findViewById(R.id.progress_1);
        //recyclerView1=findViewById(R.id.recy_operator_paket);
        gson=new GsonBuilder().create();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView1.setLayoutManager(new GridLayoutManager(this,3));
        operatorAdapter=new OperatorAdapter(operators,this,1, new OperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                Intent intent=new Intent(TagihanActivity.this, PulsaOperatorActivity.class);
                intent.putExtra("title",operators.get(item).name);
                intent.putExtra("idd",String.valueOf(operators.get(item).id));
                startActivity(intent);

            }
        });
        operatordataadapter=new PulsaOperatorAdapter(operatordata,this, new PulsaOperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_CODE_PRODUCT,operatordata.get(item).code);
                Intent intent=new Intent(TagihanActivity.this, TransaksiActivity.class);
                intent.putExtra("id",operatordata.get(item).id);
                intent.putExtra("code",operatordata.get(item).code);
                intent.putExtra("image",operatordata.get(item).subcat_id);
                intent.putExtra("nama",operatordata.get(item).name);
                intent.putExtra("harga",String.valueOf(operatordata.get(item).price_user));
                intent.putExtra("tipepasca","cek");
                intent.putExtra("logo",String.valueOf(operatordata.get(item).subcategory.logo));
                sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_ACTION,"Tagihan Pasca Bayar");
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(operatordataadapter);
        //recyclerView1.setAdapter(operatordataadapter);
        //operatordata.add(new PulsaOperator(308,70,"Cek Halo","CHALO"));
        //operatordata.add(new PulsaOperator(309,71,"Cek Matrix","CMATRIX"));
        //operatordata.add(new PulsaOperator(310,72,"Cek Smart","CSMART"));
        //operatordata.add(new PulsaOperator(311,73,"Cek Tri","CTRI"));
        //operatordata.add(new PulsaOperator(312,74,"Cek XL","CXL"));
        //operatordata.add(new PulsaOperator(312,74,"Cek BPJS","CXL"));
        operatordataadapter.notifyDataSetChanged();
        progress_1.setVisibility(View.GONE);
               // loadopertator();
        loadpascabayar("104");
        loadpascabayar("106");
    }

    public void loadpascabayar(String id){
        Map<String,String> map=new HashMap();
        //operatordata.clear();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/product.aspx/"+id, map, new Service_Connector.VolleyResponseListener_v3() {
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

    public void loadopertator(){
        Map<String,String> map=new HashMap();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/subcategory.aspx/20", map, new Service_Connector.VolleyResponseListener_v3() {
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
                        //operators.add(new Operator())
                        if(datas.length()>1) {

                            for (int b = 0; b < datas.length(); b++) {
                                //JSONObject objnilai=new JSONObject(datas.get(b).toString());
                                Operator nilai = gson.fromJson(datas.get(b).toString(), Operator.class);

                                operators.add(nilai);
                            }


                            operatorAdapter.notifyDataSetChanged();
                            //loadopertator_data();
                        }
                        else{
                            Operator nilai = gson.fromJson(datas.get(0).toString(), Operator.class);
                            langsungsaja(nilai);
                        }

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

    public void cektagihan(View view){
        Intent intent=new Intent(this,CekdanBayarTransaksi.class);
        intent.putExtra("code","CPLN");

        startActivity(intent);

    }

    private void langsungsaja(Operator operator){
        Intent intent=new Intent(TagihanActivity.this, PulsaOperatorActivity.class);
        intent.putExtra("title",operator.name);
        intent.putExtra("idd",operator.id);
        startActivity(intent);
    }
}
