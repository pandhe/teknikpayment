package com.pontianak.teknikpayment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class KuberlayarDilautan extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;
    public int PICTURE_RESULT = 1;
    public Bantuan bantuan=new Bantuan();
    public AlertDialog.Builder builder_loading;
    public AlertDialog dialog_loading;
    View mydialog ;
    LayoutInflater gabung2;
    Boolean paused=false;
    Boolean showlog=true;
    private FirebaseAuth mAuth;
    Service_Connector service_connector;


    FirebaseUser currentUser;

    public interface CheckSaldoListener{
        void onLoading();
        void onResult(JSONObject result);
        void onError(String message);

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            try {
                JSONObject jsonObject = new JSONObject(intent.getStringExtra("datas"));
                Log.d("receiver", "Got message: " + intent.getStringExtra("datas"));
                if(!paused) {
                    switch (sharedPrefManager.getSpString(SharedPrefManager.SP_LAST_CODE_PRODUCT)){
                        case "CPLN":


                        case "CHALO":


                        case "CXL":


                        case "CMATRIX":


                        case "CTRI":

                        case "CSMART":
                            newdialog(message,jsonObject);
                            break;
                        default:
                            newwarningdialog(message);
                            break;

                    }

                }
            }
            catch (Throwable t){

            }





        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        paused=true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        paused=false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gabung2 =getLayoutInflater();
        sharedPrefManager=new SharedPrefManager(this);
        mydialog = gabung2.inflate(R.layout.include_popup_loading, null);
        builder_loading = new AlertDialog.Builder(this);
        builder_loading.setView(mydialog);
        dialog_loading=builder_loading.create();
        dialog_loading.setCancelable(false);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("PUSH_NOTIFICATION"));

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        service_connector=new Service_Connector();



    }

    String namaactivitynye=this.getClass().getSimpleName();
    private ProgressDialog mProgressDialog;
    public void mypulsa(View view){
        Intent intee=new Intent(this, BeliPulsaActivity.class);
        intee.putExtra("aksi","Pulsa");
        startActivity(intee);
    }
    public void myvoucher(View view){
        Intent intee=new Intent(this, MyVoucherActivity.class);
        intee.putExtra("aksi","myVoucher");
        startActivity(intee);
    }

    public void mylistrik(View view){
        Intent intee=new Intent(this, MyListrikActivity.class);
        intee.putExtra("aksi","myListrik");
        startActivity(intee);
    }

    public void checklistrik(View view){

    }

    public void mypdam(View view){
        Intent intee=new Intent(this, MyPdamActivity.class);
        intee.putExtra("aksi","myPdam");
        startActivity(intee);
    }

    public void myupkp(View view){
        Intent intee=new Intent(this, MyUpkpActivity.class);
        intee.putExtra("aksi","myUPKP");
        startActivity(intee);
    }


    public void mytvinternet(View view){
        Intent intee=new Intent(this, MyTvInternetActivity.class);
        intee.putExtra("aksi","Tv Internet");
        startActivity(intee);
    }


    public void mytagihan(View view){

        Intent intee=new Intent(this, TagihanActivity.class);
        intee.putExtra("aksi","Tagihan");
        startActivity(intee);
    }
    public void myMenu(View view){
        Intent intee=new Intent(this, FiturLinkajaActivity.class);
        intee.putExtra("aksi","Transfer");
        startActivity(intee);
    }
    public void myDonasi(View view){
        Intent intee=new Intent(this, FiturLinkajaActivity.class);
        intee.putExtra("aksi","My Tix");
        startActivity(intee);
    }
    public void myLazisMU(View view){

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lazismu.org/"));
        // Note the Chooser below. If no applications match,
        // Android displays a system message.So here there is no need for try-catch.
        startActivity(Intent.createChooser(intent, "Browse with"));

        /*Intent intee=new Intent(this, MyLazisMuActivity.class);
        intee.putExtra("aksi","My LazisMU");
        startActivity(intee);*/
    }
    public void myBayar(View view){
        Intent intee=new Intent(this, BayarActivity.class);
        intee.putExtra("aksi","Bayar");
        //startActivityForResult(intee,1003);
        startActivity(intee);
    }
    public void mylainnya(View view){
        Intent intee=new Intent(this, HomeDeposit.class);
        intee.putExtra("aksi","My Cash");
        startActivity(intee);
    }
    public void myDeposit(View view){
        Intent intee=new Intent(this, DepositAtivity.class);
        intee.putExtra("aksi","My Cash");
        startActivity(intee);
    }
    public void myWithdraw(View view){
        Intent intee=new Intent(this, WithdrawActivity.class);
        intee.putExtra("aksi","My Cash");
        startActivity(intee);
    }
    public void myprofil(View view){
        if(!namaactivitynye.equals("ProfileActivity")) {
            Intent intee = new Intent(this, ProfileActivity.class);
            intee.putExtra("aksi", "Lainnya");
            startActivityForResult(intee, 1003);
            //startActivity(intee);
        }
    }
    public void Ubahprofil(View view){
        if(!namaactivitynye.equals("UbahProfilActivity")) {
            Intent intee = new Intent(this, UbahProfilActivity.class);
            intee.putExtra("aksi", "Lainnya");
            startActivityForResult(intee, 1003);
            //startActivity(intee);
        }
    }
    public void myTransferPulsa(View view){
        Intent intee=new Intent(this, TransferPulsaActivity.class);
        intee.putExtra("aksi","Bayar");
        //startActivityForResult(intee,1003);
        startActivity(intee);
    }
    public void myTransferDepostit(View view){
        Intent intee=new Intent(this, TransferDepositActivity.class);
        intee.putExtra("aksi","Bayar");
        //startActivityForResult(intee,1003);
        startActivity(intee);
    }
    public void myMintaSaldo(View view){
        Intent intee=new Intent(this, MintaSaldoActivity.class);
        intee.putExtra("aksi","Minta");
        //startActivityForResult(intee,1003);
        startActivity(intee);
    }

    public void myNotification(View view){
        Intent intee=new Intent(this, MyNotifikasiActivity.class);
        intee.putExtra("aksi","Minta");
        //startActivityForResult(intee,1003);
        startActivity(intee);
    }


    public void myphotoprofil(View view){
        if(!namaactivitynye.equals("PhotoProfilActivity")) {
            Intent intee = new Intent(this, PhotoProfilActivity.class);
            intee.putExtra("aksi", "Lainnya");
           // startActivityForResult(intee, 1003);
            startActivity(intee);
        }
    }
    public void changepassword(View view){
        if(!namaactivitynye.equals("GantiPasswordActivity")) {
            Intent intee = new Intent(this, GantiPasswordActivity.class);
            intee.putExtra("aksi", "Lainnya");
            // startActivityForResult(intee, 1003);
            startActivity(intee);
        }
    }
    public void myqr(View view){
        Intent intee=new Intent(this, QrCodeActivity.class);
        intee.putExtra("aksi","Lainnya");
        startActivityForResult(intee,1003);
        //startActivity(intee);
    }
    public void myriwayat(View view){
        if(!namaactivitynye.equals("RiwayatActivity")) {
            Intent intee = new Intent(this, RiwayatActivity.class);
            intee.putExtra("aksi", "Riwayat");
            //startActivityForResult(intee,1003);
            startActivity(intee);
        }
    }
    public void myfavorit(View view){
        if(!namaactivitynye.equals("FavoritActivity")) {
            Intent intee = new Intent(this, FavoritActivity.class);
            intee.putExtra("aksi", "Favorit");
            //startActivityForResult(intee,1003);
            startActivity(intee);
        }
    }
    public void myselfqr(View view){
        Intent intee=new Intent(this, MyQrActivity.class);
        intee.putExtra("aksi","Favorit");
        //startActivityForResult(intee,1003);
        startActivity(intee);
    }
    public void gomain(View view){
        if(!namaactivitynye.equals("MainActivity")) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }
    public void goback(View view){
       this.finish();
    }
    public void lompatkeberanda(){
        if(!namaactivitynye.equals("MainActivity")) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }



    public void gochecktransaction(JSONObject refid){
        Intent intent=new Intent(this,CheckTransaksiActivity.class);
        try{
            intent.putExtra("refid",refid.getString("ref_id"));
            intent.putExtra("tipe",refid.getString("buyer_sku_code"));
            intent.putExtra("customer_no",refid.getString("customer_no"));
            intent.putExtra("customer_name",refid.getString("customer_name"));
            intent.putExtra("buyer_sku_code",refid.getString("buyer_sku_code"));
            intent.putExtra("selling_price",refid.getString("selling_price"));
            intent.putExtra("desc",refid.getString("desc"));

            Log.i("ez reiff",refid.getString("ref_id"));
        }
        catch (Throwable t){
            intent.putExtra("refid"," ");
            intent.putExtra("tipe"," ");
        }

        startActivity(intent);

    }

    public void paksaforceclose(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingin memaksa force close ?").setMessage("Fitur ini digunakan developer untuk memastika report crash dikirim dengan benar")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Crashlytics.getInstance().crash(); // Force a crash

                    }
                }).setNegativeButton("tidak jadi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                
            }
        })
        ;
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();

    }
    public void showProgressDialog(boolean showprogress,String teks) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        if(showprogress){
           mProgressDialog.setMessage(teks);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);


            mProgressDialog.show();
        }
        else{
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }

    }
    public void newdialog(String msg,JSONObject refid){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setPositiveButton("Lebih Detail", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        gochecktransaction(refid);


                    }
                }).setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
        ;
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }

    public void newwarningdialog(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       // gochecktransaction(refid);


                    }
                })
        ;
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }

    public void showlog(String message){
        Log.i("ez log",message);
    }

    public void checkSaldo(CheckSaldoListener checkSaldoListener){
        Map<String,String> map=new HashMap();
        checkSaldoListener.onLoading();
        map.put("token",sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/check-wallet.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
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
                    checkSaldoListener.onResult(respon);

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
