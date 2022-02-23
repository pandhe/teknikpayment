package com.pontianak.teknikpayment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pontianak.teknikpayment.Adapter.BankAdapter;
import com.pontianak.teknikpayment.Model.Bank;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithdrawActivity extends KuberlayarDilautan {
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    ImageView img1;
    EditText edt1;
    Button bt1;
    Intent intent;
    String currentpay="";
    private String current = "";
    int currpointpay=0;
    RecyclerView recyclerView;
    List<Bank> bankList=new ArrayList<>();
    BankAdapter bankAdapter;
    Gson gson;
    String currentbank="";
    String idcurrentbank="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        service_connector=new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);
        edt1=findViewById(R.id.edt1);
        recyclerView=findViewById(R.id.recyclerView);
        gson=new GsonBuilder().create();
        bantuan.formatRupiah.setMaximumFractionDigits(0);
        bankAdapter=new BankAdapter(bankList, this, new BankAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {
                Bank bank=bankList.get(item);
                currentbank=bank.bank_name+" a.n "+bank.bank_placeholder;
                idcurrentbank=String.valueOf(bank.id);
                lanjut2();

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bankAdapter);

        //edt1.setText(sharedPrefManager.getSpString(SharedPrefManager.SP_PHONE));
        edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals(current)){
                    edt1.removeTextChangedListener(this);

                    String cleanString = editable.toString().replaceAll("[Rp,R,.]", "");



                    String formatted="";
                    if(cleanString.length()>1) {
                        double parsed = Double.parseDouble(cleanString);
                        Log.i("ez",cleanString);
                        formatted = bantuan.formatRupiah.format((parsed));
                    }
                    else{
                        formatted =cleanString;

                    }


                    currentpay = formatted;
                    edt1.setText(formatted);
                    edt1.setSelection(formatted.length());
                    //currpointpay=bantuan.meisinteger(txt_point.getText().toString().replaceAll("[Rp,R,.]", ""));

                    edt1.addTextChangedListener(this);
                }
            }
        });
        loadpilihbank();

    }

    public void loadpilihbank(){
        dialog_loading.show();
        Map<String,String> map=new HashMap();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        //map.put("nominal",edt1.getText().toString().replaceAll("[Rp,R,.]", ""));

        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/bank-list.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
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
                        bankList.clear();

                        JSONArray datas=new JSONArray(respon.getString("message"));
                        for(int i=0;i<datas.length();i++){
                            Bank bank=gson.fromJson(datas.get(i).toString(),Bank.class);
                            bankList.add(bank);
                        }
                        bankAdapter.notifyDataSetChanged();


                        //Intent intent=new Intent(WithdrawActivity.this, WebTransaksiActivity.class);
                       // intent.putExtra("url",respon.getString("message"));

                       // startActivityForResult(intent,2000);






                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(WithdrawActivity.this);
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

    public void lanjut(View v){
        Intent intent=new Intent(this,TambahAkunBankActivity.class);
        startActivityForResult(intent,1003);




    }
    public void lanjut2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda yakin ingin melakukan penarikan Bernilai "+edt1.getText().toString()+ " menuju akun bank "+currentbank+" ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        konformasi_transaksi();
                    }
                })
                .setNegativeButton("Batalkan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();



    }
    public void konformasi_transaksi(){
        Map<String,String> map=new HashMap();
        dialog_loading.show();


        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        map.put("nominal",edt1.getText().toString().replaceAll("[Rp,R,.]", ""));
        map.put("bank_id",idcurrentbank);

        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/wihdrawal.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {
                dialog_loading.dismiss();
            }
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject respon=new JSONObject(response);
                    if(respon.getBoolean("status")){

                        AlertDialog.Builder builder = new AlertDialog.Builder(WithdrawActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(WithdrawActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1003){
            if(resultCode==RESULT_OK){
                loadpilihbank();
            }
        }
    }
}
