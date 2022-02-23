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

public class MyPdamActivity extends AppCompatActivity {

    List<Operator> operators=new ArrayList<>();

    //pascabayar
    RecyclerView recy_pasca;
    List<PulsaOperator> operatorpasca=new ArrayList<>();
    PulsaOperatorAdapter operatorpascaadapter;

    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    OperatorAdapter operatorAdapter;

    Gson gson;
    ProgressBar progress_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pdam);
        TextView txt_title=findViewById(R.id.txt_title);
        txt_title.setText("PDAM");
        ImageButton bt_back=findViewById(R.id.bt_back);
        bt_back.setImageResource(R.drawable.ic_menu_tagihan);
        service_connector=new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);

        progress_1=findViewById(R.id.progress_1);
        //recyclerView1=findViewById(R.id.recy_operator_paket);
        gson=new GsonBuilder().create();

        //recyclerView1.setLayoutManager(new GridLayoutManager(this,3));
        operatorAdapter=new OperatorAdapter(operators,this,1, new OperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                Intent intent=new Intent(MyPdamActivity.this, PulsaOperatorActivity.class);
                intent.putExtra("title",operators.get(item).name);
                intent.putExtra("idd",String.valueOf(operators.get(item).id));
                intent.putExtra("logo",String.valueOf(operators.get(item).logo));
                sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_ACTION,"PDAM Pasca Bayar");
                startActivity(intent);

            }
        });

        //pasca

        recy_pasca=findViewById(R.id.recy_pasca);
        recy_pasca.setLayoutManager(new LinearLayoutManager(this));
        recy_pasca.setNestedScrollingEnabled(false);
        operatorpascaadapter=new PulsaOperatorAdapter(operatorpasca,this, new PulsaOperatorAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_CODE_PRODUCT,operatorpasca.get(item).code);
                Intent intent=new Intent(MyPdamActivity.this, TransaksiActivity.class);
                intent.putExtra("id",operatorpasca.get(item).id);
                intent.putExtra("code",operatorpasca.get(item).code);
                intent.putExtra("image",operatorpasca.get(item).subcat_id);
                intent.putExtra("nama",operatorpasca.get(item).name);
                intent.putExtra("harga",String.valueOf(operatorpasca.get(item).price_user));
                intent.putExtra("tipepasca","cek");
                intent.putExtra("logo",String.valueOf(operatorpasca.get(item).subcategory.logo));

                startActivity(intent);

            }
        });
        recy_pasca.setAdapter(operatorpascaadapter);
        //recyclerView1.setAdapter(operatordataadapter);

        //operatordata.add(new PulsaOperator(312,74,"PDAM Pontianak","PDAM1"));
        //operatordata.add(new PulsaOperator(312,74,"PDAM Singkawang","PDAM2"));
        //operatordataadapter.notifyDataSetChanged();
        progress_1.setVisibility(View.GONE);
         loadpascabayar();
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
        Intent intent=new Intent(MyPdamActivity.this, PulsaOperatorActivity.class);
        intent.putExtra("title",operator.name);
        intent.putExtra("idd",operator.id);
        startActivity(intent);
    }
}
