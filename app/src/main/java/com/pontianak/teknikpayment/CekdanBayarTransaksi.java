package com.pontianak.teknikpayment;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CekdanBayarTransaksi extends KuberlayarDilautan {
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    EditText edt1;
    Button bt1;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cekdan_bayar_transaksi);
        service_connector=new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);
        edt1=findViewById(R.id.edt1);
        intent=getIntent();
    }
    public void lanjut(View v){

                        konformasi_transaksi();

    }

    public void konformasi_transaksi(){
        Map<String,String> map=new HashMap();
        dialog_loading.show();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        map.put("code_product",intent.getStringExtra("code"));
        map.put("destination",edt1.getText().toString());

        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/transaction.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {
                dialog_loading.hide();

            }
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject respon=new JSONObject(response);
                    if(respon.getBoolean("status")){

                        //JSONArray datas=new JSONArray(respon.getString("message"));

                        //Intent intent=new Intent(CekdanBayarTransaksi.this, WebTransaksiActivity.class);
                        //intent.putExtra("url",respon.getString("message"));

                       // startActivity(intent);
                        AlertDialog.Builder builder = new AlertDialog.Builder(CekdanBayarTransaksi.this);
                        builder.setMessage(respon.getString("message"))
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // FIRE ZE MISSILES!

                                    }
                                })
                        ;
                        // Create the AlertDialog object and return it
                        builder.create();
                        builder.show();






                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(CekdanBayarTransaksi.this);
                        builder.setMessage(respon.getString("message"))
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // FIRE ZE MISSILES!

                                    }
                                })
                        ;
                        // Create the AlertDialog object and return it
                        builder.create();
                        builder.show();
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
