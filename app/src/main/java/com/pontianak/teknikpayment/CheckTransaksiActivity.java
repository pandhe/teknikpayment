package com.pontianak.teknikpayment;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pontianak.teknikpayment.Adapter.DetaillTagihanAdapter;
import com.pontianak.teknikpayment.Model.DetailTagihan;
import com.pontianak.teknikpayment.Model.History;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckTransaksiActivity extends KuberlayarDilautan {
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    TextView txt2,txt3,txt4,txt5,txt6,txt7;
    Intent intent;
    Button bt_1;
    RecyclerView recy_detail_tagihan;
    List<DetailTagihan> detailTagihans = new ArrayList<>();
    DetaillTagihanAdapter detaillTagihanAdapter;
    Gson gson;
    TextView txt_title, txt_title2, txt_title4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_transaksi);
        txt_title=findViewById(R.id.txt_title);
        txt_title2=findViewById(R.id.txt_title2);
       txt_title4=findViewById(R.id.txt_title4);

        sharedPrefManager=new SharedPrefManager(this);
        service_connector=new Service_Connector();
        txt2=findViewById(R.id.txt2);
        txt3=findViewById(R.id.txt3);
        txt4=findViewById(R.id.txt4);
        txt5=findViewById(R.id.txt5);
        txt6=findViewById(R.id.txt6);
        txt7=findViewById(R.id.txt7);
        bt_1=findViewById(R.id.bt_1);
        gson=new GsonBuilder().create();
        txt_title.setText(sharedPrefManager.getSpString(SharedPrefManager.SP_LAST_ACTION));
        txt_title2.setText("Detail Akun "+sharedPrefManager.getSpString(sharedPrefManager.SP_PHONE));

        recy_detail_tagihan = findViewById(R.id.recy_detail_tagihan);
        detaillTagihanAdapter = new DetaillTagihanAdapter(detailTagihans, this, new DetaillTagihanAdapter.onSelect() {
            @Override
            public void onselectitem(DetailTagihan item) {

            }
        });
        recy_detail_tagihan.setLayoutManager(new LinearLayoutManager(this));
        recy_detail_tagihan.setNestedScrollingEnabled(false);
        recy_detail_tagihan.setAdapter(detaillTagihanAdapter);
        intent=getIntent();
        //bt_1.setVisibility(View.GONE);

        //this static logic caused by unpredictable JSON respon
        showlog(sharedPrefManager.getSpString(SharedPrefManager.SP_LAST_CODE_PRODUCT));
        switch (sharedPrefManager.getSpString(SharedPrefManager.SP_LAST_CODE_PRODUCT)){
            case "CPLN":


            case "CHALO":


            case "CXL":


            case "CMATRIX":


            case "CTRI":

            case "CSMART":
               // bt_1.setVisibility(View.VISIBLE);
                break;
            default:
              //
                //
                //bt_1.setVisibility(View.GONE);
                break;

        }





        //konformasi_transaksi();

                txt2.setText("Nama : "+intent.getStringExtra("customer_name"));

                txt3.setText("Nominal : "+intent.getStringExtra("selling_price"));
                txt3.setText("Nominal : "+intent.getStringExtra("selling_price"));


                txt5.setText("Referensi ID : "+intent.getStringExtra("refid"));


                txt5.setText("Buyer SKU : "+intent.getStringExtra("buyer_sku_code"));
                txt6.setText("Nomor Pelanggan : "+intent.getStringExtra("customer_no"));

        try{
            JSONObject descs=new JSONObject(intent.getStringExtra("desc"));
            JSONArray detail=new JSONArray(descs.getString("detail"));
            for(int b=0;b<detail.length();b++) {
             DetailTagihan detailTagihan=gson.fromJson(detail.get(b).toString(), DetailTagihan.class);
             detailTagihans.add(detailTagihan);
            }
            detaillTagihanAdapter.notifyDataSetChanged();




        }
        catch (Exception e){
            Log.i("ez",e.getMessage());

        }
        check_saldo();


    }
    public void konformasi_transaksi(){
        //bt1.setEnabled(false);
        // bt1.setText("Tunggu Sebentar ...");
        // progressBar.setVisibility(View.VISIBLE);
        dialog_loading.show();
        Map<String,String> map=new HashMap();





        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        map.put("refid",intent.getStringExtra("refid"));
        if(intent.getStringExtra("tipe")!=null) {
            map.put("tipe", intent.getStringExtra("tipe"));
        }

       // map.put("destination",edt1.getText().toString());
        Log.i("ez tokeeeee",intent.getStringExtra("refid") + " ghgh "+sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));

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
                        JSONObject objNilai=new JSONObject(respon.getString("data"));
                        txt6.setText("Jenis Transaksi : "+datas.getString("jenis"));
                        txt4.setText("Status transaksi : "+datas.getString("status_text"));
                        JSONObject dflash;
                        History history;

                        switch (objNilai.getString("jenis")){
                            case "PEMBELIAN":
                                history = new History(objNilai.getString("refid"), objNilai.getString("status"), objNilai.getString("tgl"), objNilai.getString("tgl_human"), objNilai.getString("jenis"), objNilai.getInt("nominal"), objNilai.getString("nominal_text"), objNilai.getInt("biaya"), objNilai.getString("biaya_text"), objNilai.getInt("saldo_saat_transaksi"), objNilai.getString("saldo_saat_transaksi_text"), objNilai.getString("keterangan"), objNilai.getString("detail_dflash"));

                                break;
                            case "TOPUP SALDO":
                                history = new History(objNilai.getString("refid"), objNilai.getString("status"), objNilai.getString("tgl"), objNilai.getString("tgl_human"), objNilai.getString("jenis"), objNilai.getInt("nominal"), objNilai.getString("nominal_text"), objNilai.getInt("biaya"), objNilai.getString("biaya_text"), objNilai.getInt("saldo_saat_transaksi"), objNilai.getString("saldo_saat_transaksi_text"), objNilai.getString("saldo_masuk_text"), objNilai.getString("detail_midtrans"));

                                break;
                            case "KIRIM":
                                history = new History(objNilai.getString("refid"), objNilai.getString("status"), objNilai.getString("tgl"), objNilai.getString("tgl_human"), objNilai.getString("jenis"), objNilai.getInt("nominal"), objNilai.getString("nominal_text"), objNilai.getInt("biaya"), objNilai.getString("biaya_text"), objNilai.getInt("saldo_saat_transaksi"), objNilai.getString("saldo_saat_transaksi_text"), objNilai.getString("penerima"), objNilai.getString("nik_penerima"));

                                break;
                            case "TERIMA":
                                history = new History(objNilai.getString("refid"), objNilai.getString("status"), objNilai.getString("tgl"), objNilai.getString("tgl_human"), objNilai.getString("jenis"), objNilai.getInt("nominal"), objNilai.getString("nominal_text"), objNilai.getInt("biaya"), objNilai.getString("biaya_text"), objNilai.getInt("saldo_saat_transaksi"), objNilai.getString("saldo_saat_transaksi_text"), objNilai.getString("pengirim"), objNilai.getString("nik_pengirim"));

                                break;
                            case "PENARIKAN":
                                history = new History(objNilai.getString("refid"), objNilai.getString("status"), objNilai.getString("tgl"), objNilai.getString("tgl_human"), objNilai.getString("jenis"), objNilai.getInt("nominal"), objNilai.getString("nominal_text"), objNilai.getInt("biaya"), objNilai.getString("biaya_text"), objNilai.getInt("saldo_saat_transaksi"), objNilai.getString("saldo_saat_transaksi_text"), objNilai.getString("bank"), objNilai.getString("nomor_rekening")+" - "+objNilai.getString("atas_nama"));

                                break;
                            default:
                                history = new History(objNilai.getString("refid"), objNilai.getString("status"), objNilai.getString("tgl"), objNilai.getString("tgl_human"), objNilai.getString("jenis"), objNilai.getInt("nominal"), objNilai.getString("nominal_text"), objNilai.getInt("biaya"), objNilai.getString("biaya_text"), objNilai.getInt("saldo_saat_transaksi"), objNilai.getString("saldo_saat_transaksi_text"), objNilai.getString("penerima"), objNilai.getString("nik_pengirim"));

                                break;

                        }
                        String subjek;

                        switch (datas.getString("jenis")){
                            case "PEMBELIAN":
                                history = new History(objNilai.getString("refid"), objNilai.getString("status"), objNilai.getString("tgl"), objNilai.getString("tgl_human"), objNilai.getString("jenis"), objNilai.getInt("nominal"), objNilai.getString("nominal_text"), objNilai.getInt("biaya"), objNilai.getString("biaya_text"), objNilai.getInt("saldo_saat_transaksi"), objNilai.getString("saldo_saat_transaksi_text"), objNilai.getString("keterangan"), objNilai.getString("detail_dflash"));

                                txt2.setText(history.keterangan);

                                dflash=new JSONObject(history.detail_dflash);
                                if(dflash.getString("status_text").equalsIgnoreCase("Gagal")){
                                  //  bt_1.setVisibility(View.GONE);

                                }
                                txt3.setText("Nominal : "+bantuan.torupiah(history.nominal));


                                txt5.setText("Referensi ID : "+history.refid);

                                break;
                            case "KIRIM":
                                if(history.keterangan==null || history.keterangan.equals("null")){
                                    subjek=history.detail_dflash.toUpperCase();
                                }
                                else{
                                    subjek=history.keterangan.toUpperCase();
                                }
                                txt5.setText("Biaya Transfer : "+bantuan.torupiah(history.biaya));
                                txt6.setText("Penerima : "+subjek);
                                break;
                            case "TOPUP SALDO":
                                try{
                                    JSONObject MIDTRANS=new JSONObject(history.detail_dflash);
                                    JSONArray VA_NUMS=new JSONArray(MIDTRANS.getString("va_numbers"));
                                    JSONObject VA_NUM=new JSONObject(VA_NUMS.get(0).toString());
                                    txt5.setText("REKENING : "+VA_NUM.getString("bank").toUpperCase()+" - "+VA_NUM.getString("va_number"));


                                }
                                catch (Throwable t){

                                }

                                //holder.txt_1.setText("Topup Saldo MyUM ");
                                txt6.setText("Biaya Topup : "+bantuan.torupiah(history.biaya));
                                break;

                            case "TERIMA":

                                if(history.keterangan==null || history.keterangan.equals("null")){
                                    subjek=history.detail_dflash.toUpperCase();
                                }
                                else{
                                    subjek=history.keterangan.toUpperCase();
                                }
                                txt5.setText("Biaya Transfer : "+bantuan.torupiah(history.biaya));

                                txt6.setText("Pengirim : "+subjek);
                                break;

                            case "PENARIKAN":
                                txt5.setText("REKENING : "+history.detail_dflash);
                                txt6.setText("Biaya Transfer : "+bantuan.torupiah(history.biaya));
                                break;
                            default:
                                //holder.txt_1.setText("Pembelian Merchan");
                                break;
                        }

                        txt7.setText("Waktu : "+history.tgl);











                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckTransaksiActivity.this);
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
                    Log.i("ez",e.getMessage());

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

    public void lanjutkan_transaksi(View view){
        String CPRODUK;
        CPRODUK=sharedPrefManager.getSpString(SharedPrefManager.SP_LAST_CODE_PRODUCT);
        //bt1.setEnabled(false);
        // bt1.setText("Tunggu Sebentar ...");
        // progressBar.setVisibility(View.VISIBLE);
        dialog_loading.show();
        Map<String,String> map=new HashMap();


        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        map.put("code_product",CPRODUK);
        map.put("refid",intent.getStringExtra("refid"));
        map.put("destination",sharedPrefManager.getSpString(SharedPrefManager.SP_LAST_DESTINATION));
        //map.put("test","1");
        map.put("tipe","bayar");
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
                sharedPrefManager.saveSPString(SharedPrefManager.SP_LAST_CODE_PRODUCT,CPRODUK);
                try{
                    JSONObject respon=new JSONObject(response);
                    if(respon.getBoolean("status")){

                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckTransaksiActivity.this);
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

                        //intent.putExtra("url",String.valueOf(pulsaOperators.get(item).price_user));

                        //  startActivity(intent);






                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckTransaksiActivity.this);
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
