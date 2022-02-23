package com.pontianak.teknikpayment;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

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

public class DepositAtivity extends KuberlayarDilautan {
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    ImageView img1;
    TextInputEditText edt1;
    Button bt1;
    Intent intent;
    String currentpay="";
    private String current = "";
    int currpointpay=0;
    TextView txt_title2,txt_title4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deposit_ativity);

        service_connector=new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);
        edt1=findViewById(R.id.edt1);
        bantuan.formatRupiah.setMaximumFractionDigits(0);
        TextView txt_title=findViewById(R.id.txt_title);
        txt_title4=findViewById(R.id.txt_title4);
        txt_title.setText("Deposit");
        txt_title2=findViewById(R.id.txt_title2);
        txt_title2.setText("Detail Akun "+sharedPrefManager.getSpString(sharedPrefManager.SP_PHONE));
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

                    String cleanString = editable.toString().replaceAll("[Rp,R,.,,]", "");
                     cleanString = cleanString.replaceAll(",00", "");



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
        check_saldo();

    }

    public void lanjut(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda yakin ingin melakukan deposit Bernilai "+edt1.getText().toString()+ " ?")
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

        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/deposit.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
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

                        Intent intent=new Intent(DepositAtivity.this, WebTransaksiActivity.class);
                        intent.putExtra("url",respon.getString("message"));

                        startActivityForResult(intent,2000);






                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(DepositAtivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2000:
                setResult(RESULT_OK);
                finish();
                break;

        }
    }
}
