package com.pontianak.teknikpayment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pontianak.teknikpayment.Model.Bank_corporate;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class  TambahAkunBankActivity extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    EditText edt_1,edt_3;
    TextView edt_2;
    ArrayList<Bank_corporate> arrayofmeta = new ArrayList<>();
    AlertDialog.Builder builder;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_akun_bank);
        service_connector=new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);
        edt_1=findViewById(R.id.edt_1);
        edt_2=findViewById(R.id.edt_2);
        edt_3=findViewById(R.id.edt_3);
        builder = new AlertDialog.Builder(TambahAkunBankActivity.this);
        gson=new GsonBuilder().create();

        load_bank_corporate();

        edt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.create();
                builder.show();
            }
        });



    }
    public void confirm_tambahbank(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda yakin ingin menambahkan akun bank ini ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        tambah_bank();
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
    private void tambah_bank(){
        Map<String,String> map=new HashMap();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        map.put("bank_name",edt_2.getText().toString());
        map.put("bank_account",edt_3.getText().toString());
        map.put("bank_placeholder",edt_1.getText().toString());

        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/add-bank.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
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

                      setResult(RESULT_OK);
                      finish();

                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(TambahAkunBankActivity.this);
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
    private void load_bank_corporate(){
        Map<String,String> map=new HashMap();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));


        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/bank.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
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
                        AlertDialog dialog;
                        builder.setTitle("Pilih Bank");
                        JSONArray message=new JSONArray(respon.getString("message"));
                        for(int a=0;a<message.length();a++){
                            Bank_corporate bank_corporate=gson.fromJson(message.get(a).toString(),Bank_corporate.class);
                            arrayofmeta.add(bank_corporate);

                        }

                        final String[] arr = new String[arrayofmeta.size()];
                        for (int i = 0; i < arrayofmeta.size(); i++) {
                            arr[i] = arrayofmeta.get(i).bank_name;


                        }

                        builder.setItems(arr, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                edt_2.setText(arrayofmeta.get(which).bank_name);


                            }
                        });
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user clicked OK
                            }
                        });

                        edt_2.setHint("Ketuk untuk memilih bank");



// create and show the alert dialog


                    }
                    else{
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(TambahAkunBankActivity.this);
                        builder2.setMessage(respon.getString("message"))
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // FIRE ZE MISSILES!

                                    }
                                })
                        ;
                        // Create the AlertDialog object and return it
                        builder2.create();
                        builder2.show();
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
