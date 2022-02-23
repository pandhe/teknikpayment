package com.pontianak.teknikpayment;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pontianak.teknikpayment.Adapter.PulsaOperatorAdapter;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TransaksiActivity extends KuberlayarDilautan {
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    PulsaOperatorAdapter pulsaOperatorAdapter;
    Gson gson;
    TextView txt1,txt2,txt3;
    ImageView img1;
    TextInputEditText edt1;
    Button bt1;
    Intent intent;
    Bantuan bantuan=new Bantuan();
    String hargaitem="0";
    ProgressBar progressBar;
    String tipeinput;
    TextView txt_title, txt_title2, txt_title4;
    String logo ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);
        service_connector=new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);
        txt1=findViewById(R.id.txt1);
        txt2=findViewById(R.id.txt2);
        txt3=findViewById(R.id.txt3);
        txt_title4=findViewById(R.id.txt_title4);
        bt1=findViewById(R.id.bt1);
        edt1=findViewById(R.id.edt1);
        img1=findViewById(R.id.img1);
        progressBar=findViewById(R.id.progressBar);
        intent=getIntent();

        txt1.setText(intent.getStringExtra("nama"));
        try{
            tipeinput=intent.getStringExtra("tipe");
            if(!tipeinput.equalsIgnoreCase("Nomor Tujuan")){
                edt1.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
            }
        }
        catch (Throwable t){
            tipeinput="Nomor Tujuan";
        }

        String label_input="";



       /* switch (tipeinput){
            case 0:
                edt1.setText(sharedPrefManager.getSpString(SharedPrefManager.SP_PHONE));
                break;
            case 1:
                label_input="Masukkan Email Anda";
                edt1.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
                break;
        }*/
        txt3.setText(tipeinput);
        edt1.setHint("Masukkan "+tipeinput);
        txt_title=findViewById(R.id.txt_title);
        txt_title.setText(sharedPrefManager.getSpString(SharedPrefManager.SP_LAST_ACTION));

        txt_title2=findViewById(R.id.txt_title2);
        txt_title2.setText("Detail Akun "+sharedPrefManager.getSpString(sharedPrefManager.SP_PHONE));

        try{
            hargaitem=bantuan.torupiah(bantuan.meisdouble(intent.getStringExtra("harga")));
        }
        catch (Throwable t){
            hargaitem="0";
        }



        if(bantuan.meisfloat(intent.getStringExtra("harga"))<1){
            txt2.setText("-");
        }
        else {
            txt2.setText(hargaitem);
        }

        //dialog_loading.show();


        int logoop;
        logo=intent.getStringExtra("logo");
        switch (intent.getIntExtra("image",0)){
            case 9:
                logoop=R.drawable.ic_logo_pln;
                break;
            case 1:
            case 2:
            case 54:
            case 70:
            case 75:
                logoop=R.drawable.logotelkomsel;
                break;
            case 15:
                logoop=R.drawable.ic_logo_digigame;
                break;
            case 16:
                logoop=R.drawable.ic_logo_gemschool;
                break;
            case 17:
                logoop=R.drawable.ic_logo_garena;
                break;
            case 18:
                logoop=R.drawable.ic_logo_lyto;
                break;
            case 19:
                logoop=R.drawable.ic_logo_megaxus;
                break;
            case 20:
                logoop=R.drawable.ic_logo_netmarble;
                break;
            case 21:
                logoop=R.drawable.ic_logo_onelife;
                break;
            case 22:
                logoop=R.drawable.ic_logo_playon;
                break;
            case 36:
            case 37:
                logoop=R.drawable.logo_axis;
                break;

            case 35:
            case 38:
            case 79:
                logoop=R.drawable.logoxl;
                break;
            case 42:
            case 43:
            case 59:
                logoop=R.drawable.ic_logo_indosat;

                break;
            case 55:
            case 56:
            case 72:
            case 77:
                logoop=R.drawable.ic_logo_smartphone;
                break;
            case 71:
            case 76:
                logoop=R.drawable.logo_matrix;
                break;
            case 67:
                logoop=R.drawable.logo_wifiid;
                break;
            case 62:
            case 61:
            case 73:
            case 78:
                logoop=R.drawable.logo3;
                break;
            case 64:
            case 74:
                logoop=R.drawable.logoxl;
                break;
            case 66:
                logoop=R.drawable.ic_logo_playstore;
                break;
            case 11:
                logoop=R.drawable.ic_logo_pubgm;
                break;
            case 12:
                logoop=R.drawable.ic_logo_ffm;
                break;
            case 13:
                logoop=R.drawable.ic_logo_ml;
                break;
            case 14:
                logoop=R.drawable.ic_logo_steam;
                break;
            default:
                logoop=R.drawable.logoumy;
                break;
        }

        //int resID = ctx.getResources().getIdentifier(praktek., "drawable", ctx.getPackageName());

        //holder.img_operator.setImageResource(logoop);

        //img1.setImageResource(logoop);
        Glide.with(this)
                .load(logo).placeholder(R.drawable.logoumy).fitCenter()
                .into(img1);


        gson=new GsonBuilder().create();
        check_saldo();
    }

    public void lanjut(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
        builder.setMessage("Anda yakin ingin menyelesaikan transaksi "+intent.getStringExtra("nama")+" Bernilai "+hargaitem+ " ?")
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
        //bt1.setEnabled(false);
       // bt1.setText("Tunggu Sebentar ...");
       // progressBar.setVisibility(View.VISIBLE);
        dialog_loading.show();
        Map<String,String> map=new HashMap();


        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        map.put("code_product",intent.getStringExtra("code"));
        map.put("destination",edt1.getText().toString());
        if(intent.getStringExtra("tipepasca")!=null){
            if(intent.getStringExtra("tipepasca").length()>1){
                map.put("tipe",intent.getStringExtra("tipepasca"));
                if(sharedPrefManager.getSpBoolean(SharedPrefManager.SP_IS_MOCK)){
                    map.put("test","1");
                    map.put("refid","123aaaa");
                }

            }
        }



        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/transaction.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {
               // bt1.setEnabled(true);
               // bt1.setText("Lanjutkan");
               // progressBar.setVisibility(View.GONE);
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
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_DESTINATION,edt1.getText().toString());
                        if(respon.getString("message").equals("Transaksi Sukses")){
                            if(intent.getStringExtra("tipepasca")!=null){
                                if(intent.getStringExtra("tipepasca").length()>1){
                                    JSONObject DT = new JSONObject(respon.getString("data"));
                                    gochecktransaction(DT);
                                }
                            }
                        }

                        else{
                            Intent intent=new Intent(TransaksiActivity.this, WebTransaksiActivity.class);
                            AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);

                            builder.setMessage(respon.getString("message"))
                                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();

                                        }
                                    })
                            ;
                            // Create the AlertDialog object and return it
                            builder.create();
                            builder.show();

                        }
                        //JSONArray datas=new JSONArray(respon.getString("message"));


                            //intent.putExtra("url",String.valueOf(pulsaOperators.get(item).price_user));

                          //  startActivity(intent);






                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
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




}
