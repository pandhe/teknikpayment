package com.pontianak.teknikpayment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MintaSaldoActivity extends KuberlayarDilautan {
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    ImageView img1;
    TextInputEditText edt3,edt2;
    TextView txt_title4;
    Button bt1;
    Intent intent;
    String currentpay="";
    private String current = "";
    int currpointpay=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transfer_deposit);

        service_connector=new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);
        txt_title4=findViewById(R.id.txt_title4);
        TextView txt_title=findViewById(R.id.txt_title);
        txt_title.setText("Minta Deposit");
        TextView txt_title2=findViewById(R.id.txt_title2);
        txt_title2.setText("Detail Akun "+sharedPrefManager.getSpString(sharedPrefManager.SP_PHONE));
        edt3=findViewById(R.id.edt_3);
        edt2=findViewById(R.id.edt_2);
        bantuan.formatRupiah.setMaximumFractionDigits(0);
        //edt3.setText(sharedPrefManager.getSpString(SharedPrefManager.SP_PHONE));
        edt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals(current)){
                    edt3.removeTextChangedListener(this);

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
                    edt3.setText(formatted);
                    edt3.setSelection(formatted.length());
                    //currpointpay=bantuan.meisinteger(txt_point.getText().toString().replaceAll("[Rp,R,.]", ""));

                    edt3.addTextChangedListener(this);
                }
            }
        });
        check_saldo();

    }

    public void lanjut(View v){
        if(edt2.getText().toString().length()>5) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Anda yakin ingin meminta deposit Bernilai " + edt3.getText().toString() + " menuju " + edt2.getText().toString() + " ?")
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
        else{
            edt2.setError("Minimal harus berisi 5 karakter");
        }



    }
    public void check_saldo(){
        checkSaldo(new CheckSaldoListener() {
            @Override
            public void onLoading() {
                txt_title4.setText("-");
            }

            @Override
            public void onResult(JSONObject result) {
                try{

                    JSONObject MESSAGE=new JSONObject(result.getString("message"));
                    txt_title4.setText(bantuan.torupiah(bantuan.meisinteger(MESSAGE.getString("saldo"))));


                }
                catch (Exception e){
                    txt_title4.setText("Rp -");

                }
            }

            @Override
            public void onError(String message) {

            }
        });

    }

    public void konformasi_transaksi(){
        Map<String,String> map=new HashMap();
        dialog_loading.show();

        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        map.put("tujuan", edt2.getText().toString());
        map.put("nominal",edt3.getText().toString().replaceAll("[Rp,R,.]", ""));

        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/request-saldo.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(MintaSaldoActivity.this);
                        builder.setMessage(respon.getString("message"))
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // FIRE ZE MISSILES!
                                        finish();

                                    }
                                })
                        ;
                        // Create the AlertDialog object and return it
                        builder.create();
                        builder.show();






                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MintaSaldoActivity.this);
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

    public void openscanner(View view){
        Intent intent=new Intent(this,QrCodeActivity.class);
        startActivityForResult(intent,2001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2000:
                setResult(RESULT_OK);
                finish();
                break;
            case 2001:
                if(resultCode==RESULT_OK) {
                    edt2.setText(data.getStringExtra("rawvalue"));
                }
                break;

        }
    }
}
