package com.pontianak.teknikpayment;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.pontianak.teknikpayment.Model.History;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DetailHistoryActivity extends KuberlayarDilautan {
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    TextView txt2,txt3,txt4,txt5,txt6,txt7;
    History history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_transaksi);
        sharedPrefManager=new SharedPrefManager(this);
        service_connector=new Service_Connector();
        txt2=findViewById(R.id.txt2);
        txt3=findViewById(R.id.txt3);
        txt4=findViewById(R.id.txt4);
        txt5=findViewById(R.id.txt5);
        txt6=findViewById(R.id.txt6);
        txt7=findViewById(R.id.txt7);
        history=getIntent().getParcelableExtra("item");
        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent=new Intent(DetailHistoryActivity.this,CheckTransaksiActivity.class);
               /* try{
                    intent.putExtra("refid",history.refid);
                    intent.putExtra("tipe",history.jenis);
                   /// Log.i("ez reiff",refid.getString("REFID"));
                }
                catch (Throwable t){
                    intent.putExtra("refid"," ");
                    intent.putExtra("tipe"," ");
                }*/

               // startActivity(intent);
            }
        });

        txt2.setText("Status : "+history.status);
        txt2.setText("Referensi id : "+ history.refid);

        txt3.setText("Nominal Transaksi : "+bantuan.torupiah(history.nominal));
        txt7.setText("Waktu Transaksi : "+history.tgl);
        String subjek;

        //HONESTLY this JSON response is a joke ....A JOKE

        switch (history.jenis){
            case "PEMBELIAN":
                txt2.setText(history.keterangan);
                txt4.setText("Status : "+history.status);


                txt5.setText("Referensi ID : "+history.refid);

                break;
            case "KIRIM":
                if(history.keterangan==null || history.keterangan.equals("null")){
                    subjek=history.detail_dflash.toUpperCase();
                    getname("Penerima : ",subjek);
                }
                else{
                    subjek=history.keterangan.toUpperCase();
                    txt6.setText("Penerima : "+subjek);
                }
                txt5.setText("Biaya Transfer : "+bantuan.torupiah(history.biaya));

                break;
            case "TOPUP SALDO":
                txt4.setText("Status : "+history.status);
                try{
                    JSONObject MIDTRANS=new JSONObject(history.detail_dflash);
                    try{
                        JSONArray VA_NUMS=new JSONArray(MIDTRANS.getString("va_numbers"));
                        JSONObject VA_NUM=new JSONObject(VA_NUMS.get(0).toString());
                        txt4.setText(txt4.getText()+"\n\nNo Virtual Rek : "+VA_NUM.getString("va_number"));
                        txt4.setText(txt4.getText()+"\n\nBank : "+VA_NUM.getString("bank").toUpperCase());

                    }
                    catch (Throwable tt){

                    }

                    Iterator<?> keys = MIDTRANS.keys();
                    String utl="";

                    while( keys.hasNext() ) {
                        String key = (String) keys.next();
                        //showlog(key+" "+MIDTRANS.get(key));
                        //System.out.println("Key: " + key);
                       //System.out.println("Value: " + MIDTRANS.get(key));
                        switch (key){
                            case "va_numbers":
                            case "payment_amounts":
                            case "transaction_time":
                            case "transaction_id":
                            case "status_message":
                            case "status_code":
                            case "signature_key":
                                break;
                            default:
                                utl= utl+StringUtils.capitalize(key.replace("_"," "))+" : "+StringUtils.capitalize(MIDTRANS.get(key).toString().replace("_"," "))+"\n \n";
                                break;



                        }



                    }
                    txt7.setText(utl);
                    txt5.setText("Waktu Transaksi : "+history.tgl);
                    //txt5.setText("REKENING : "+VA_NUM.getString("bank").toUpperCase()+" - "+VA_NUM.getString("va_number"));


                }
                catch (Throwable t){

                }

                //holder.txt_1.setText("Topup Saldo MyUM ");
                txt6.setText("Biaya Topup : "+bantuan.torupiah(history.biaya));
                break;

            case "TERIMA":

                if(history.keterangan==null || history.keterangan.equals("null")){
                   subjek=history.detail_dflash.toUpperCase();
                    getname("Pengirim : ",subjek);
                }
                else{
                    subjek=history.keterangan.toUpperCase();
                    txt6.setText("Pengirim : "+subjek);
                }
                txt5.setText("Biaya Transfer : "+bantuan.torupiah(history.biaya));



                break;

            case "PENARIKAN":
                txt5.setText("REKENING : "+history.detail_dflash);
                txt6.setText("Biaya Transfer : "+bantuan.torupiah(history.biaya));
                break;
            default:
                //holder.txt_1.setText("Pembelian Merchan");
                break;
        }

        /*if(history.code_product==null) {
            history.code_product="-";
        }
        txt4.setText("Kode Produk : " + history.code_product);
        txt5.setText("Waktu Transaksi : "+bantuan.cariwaktu(history.created_at));
        if(history.data_bank==null){
            history.data_bank="-";
        }
        if(history.pengirim==null){
            history.pengirim="-";
        }*/







        konformasi_transaksi();


    }
    public void konformasi_transaksi(){
        //bt1.setEnabled(false);
        // bt1.setText("Tunggu Sebentar ...");
        // progressBar.setVisibility(View.VISIBLE);
        dialog_loading.show();
        Map<String,String> map=new HashMap();

        Intent intent=getIntent();



        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        map.put("refid",intent.getStringExtra("refid"));
        // map.put("destination",edt1.getText().toString());

        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/check-transaction.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
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
                        JSONObject datas=new JSONObject(respon.getString("transaction"));
                        JSONObject dflash=new JSONObject(datas.getString("dflash"));
                        txt3.setText("Referensi id : "+datas.getString("refid"));
                        txt6.setText("Jenis Transaksi : "+datas.getString("jenis"));
                        txt2.setText(dflash.getString("message"));
                        txt4.setText("Status transaksi : "+dflash.getString("status_text"));
                        txt5.setText("Harga : "+bantuan.torupiah(bantuan.meisinteger(dflash.getString("status_text"))));
                        txt7.setText("Waktu transaksi : "+bantuan.cariwaktu(dflash.getString("created_at")));




                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailHistoryActivity.this);
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

    private void getname(String penyerta, String username){
        Map<String,String> map=new HashMap();

        map.put("username", username);
        service_connector.sendpostrequestwithsession_v3(this, sharedPrefManager.getSpCookie(), "service/getname", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try{
                   JSONObject RESPON=new JSONObject(response);
                   txt6.setText(penyerta+RESPON.getString("data"));

                }
                catch (Throwable t){

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
