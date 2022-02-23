package com.pontianak.teknikpayment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GantiPasswordActivity extends KuberlayarDilautan {
    Service_Connector service_connector;
    SharedPrefManager sharedPrefManager;
    Button btnLogin,btnLogin1,bt_daftar,btnLogin2,btnLoginv;
    TextInputEditText etUsername, etPassword,etDpassword,etUlangpassword,etVeriv;
    ProgressBar progressBar;
    TextView text_warning;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);
        TextView txt_title=findViewById(R.id.txt_title);
        txt_title.setText("Ganti Password");
        service_connector=new Service_Connector();
        sharedPrefManager=new SharedPrefManager(this);
        btnLogin = findViewById(R.id.bt_login);
        text_warning=findViewById(R.id.txt_warning);
        progressBar=findViewById(R.id.progressBar);

        etUsername = findViewById(R.id.edt_email);
        etPassword = findViewById(R.id.edt_password);
        etUlangpassword = findViewById(R.id.edt_password2);
    }
    public void showprogress(boolean prog){
        if(prog)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }
    public void showmessage(String message){
        if(message.equals("")){
            text_warning.setVisibility(View.GONE);
        }
        else{
            text_warning.setText(message);
            text_warning.setVisibility(View.VISIBLE);
        }


    }
    public void gantipassword(View view){
        if(etUlangpassword.getText().toString().equals(etPassword.getText().toString())){
            checklogin();
        }
        else{
           showmessage("Konfirmasi Password Baru Salah");
        }

    }
    public void checklogin(){
        showprogress(true);
        Map<String,String> map=new HashMap();
        map.put("password",etUsername.getText().toString());
        map.put("new_password",etPassword.getText().toString());
        map.put("confirmpassword",etUlangpassword.getText().toString());

        service_connector.sendpostrequestwithsession_v3(this, sharedPrefManager.getSpCookie(), "service/change_password",map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }
            @Override
            public void onError(String message) {
                showprogress(false);

            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject respon=new JSONObject(response);
                    if(respon.getString("status").equals("1")){
                        showprogress(false);
                        showmessage("Password Berhasil Diganti");
                        finish();

                    }
                    else{

                    }

                }
                catch (Exception e){

                }

            }

            @Override
            public void onNoConnection(String message) {
                showprogress(false);

            }

            @Override
            public void OnServerError(String message) {
                showprogress(false);

            }

            @Override
            public void OnTimeOut() {
                showprogress(false);

            }
        });
    }

}
