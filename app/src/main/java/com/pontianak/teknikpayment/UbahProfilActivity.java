package com.pontianak.teknikpayment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UbahProfilActivity extends KuberlayarDilautan {
    SharedPrefManager sharedPrefManager;
    ProgressDialog pDialog;
    EditText edt_1, edt_2;
    Service_Connector service_connector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profil);
        service_connector = new Service_Connector();
        TextView txt_title=findViewById(R.id.txt_title);
        txt_title.setText("Edit Profil");


        sharedPrefManager = new SharedPrefManager(this);

        edt_1 = findViewById(R.id.edt_1);
        edt_2 = findViewById(R.id.edt_2);
        edt_1.setText(sharedPrefManager.getSpString(sharedPrefManager.SP_PHONE));
        edt_2.setText(sharedPrefManager.getSpString(SharedPrefManager.SP_NAMADOSEN));


    }

    public void confirm_ganti_profil(View view) {
        boolean isvalid = true;
        if (edt_1.getText().toString().length() < 5) {
            edt_1.setError("Isian ini setidaknya harus berisi 10 karakter");
            isvalid = false;
        }
        if (edt_2.getText().toString().length() < 5) {
            edt_2.setError("Isian ini setidaknya harus berisi 3 karakter");
            isvalid = false;
        }
        if (isvalid) {
            ganti_profil_awal();
        }
    }

    public void ganti_profil_awal() {
        dialog_loading.show();

        if(sharedPrefManager.getSpString(SharedPrefManager.SP_LEVEL).equals("UMUM")){
            currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        String idToken = task.getResult().getToken();
                        prosesgantiprofil(idToken);

                    }
                }
            });
        }
        else{
            prosesgantiprofil("-");

        }





    }

    private void prosesgantiprofil(String idtoken){
        Map<String, String> map = new HashMap();
        map.put("nama_lengkap", edt_2.getText().toString());
        map.put("no_hp", edt_1.getText().toString());
        map.put("idtoken", idtoken);
        service_connector.sendpostrequestwithsession_v3(this, sharedPrefManager.getSpCookie(), "service/change_profil", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {
                dialog_loading.hide();
            }

            @Override
            public void onError(String message) {


            }

            @Override
            public void onResponese(String response) {
                try {
                    JSONObject respon = new JSONObject(response);
                    if (respon.getString("status").equals("1")) {

                        sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMADOSEN, edt_2.getText().toString());
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_PHONE, edt_1.getText().toString());
                        AlertDialog.Builder builder = new AlertDialog.Builder(UbahProfilActivity.this);
                        builder.setMessage("Perubahan profil berhasil terkirim")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();

                                    }
                                })
                        ;
                        // Create the AlertDialog object and return it
                        builder.create();
                        builder.show();


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UbahProfilActivity.this);
                        builder.setMessage("Tidak ada perubahan profil yang dapat dilakukan")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog_loading.hide();


                                    }
                                })
                        ;
                        // Create the AlertDialog object and return it
                        builder.create();
                        builder.show();
                    }

                } catch (Exception e) {

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
