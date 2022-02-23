package com.pontianak.teknikpayment;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONObject;

public class ProfileActivity extends KuberlayarDilautan {
    Service_Connector service_connector;
    SharedPrefManager sharedPrefManager;
    TextView txt_saldo;
    ImageView img_profile;
    private FirebaseAuth mAuth;
    Group group2;
    TextView txt9,txt11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        service_connector=new Service_Connector();
        sharedPrefManager=new SharedPrefManager(this);
        img_profile=findViewById(R.id.img_profile);
        TextView txt_nama=findViewById(R.id.txt1);
        TextView txt_status=findViewById(R.id.txt_3);
        TextView txt_12=findViewById(R.id.txt12);
        txt9=findViewById(R.id.txt9);
        txt11=findViewById(R.id.txt11);
        txt_saldo=findViewById(R.id.txt_2);
        txt_nama.setText(sharedPrefManager.getSpString(sharedPrefManager.SP_NAMADOSEN));
        txt_status.setText(sharedPrefManager.getSpString(sharedPrefManager.SP_LEVEL));
        txt_12.setText("Detail Akun "+sharedPrefManager.getSpString(sharedPrefManager.SP_PHONE));
        txt_saldo=findViewById(R.id.txt_2);
        txt9.setText(bantuan.torupiah(0));
        txt11.setText(bantuan.torupiah(0));
        txt_saldo.setText(bantuan.torupiah(1000000));


        mAuth = FirebaseAuth.getInstance();
        group2=findViewById(R.id.group2);
        check_saldo();

    }
    @Override
    protected void onResume() {
        String fp=service_connector.server.ROOT_URL+"image/profile/"+sharedPrefManager.getSpString(sharedPrefManager.SP_PHOTO);
        if(sharedPrefManager.getSpString(SharedPrefManager.SP_LEVEL).equals("UMUM")){
            fp=sharedPrefManager.getSpString(sharedPrefManager.SP_PHOTO);
            group2.setVisibility(View.GONE);
        }


        Glide.with(this)
                .load(fp).apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.ic_default_profile)
                .into(img_profile);
        super.onResume();
    }
    public void photo_profil(){

    }
    public void check_saldo(){

        checkSaldo(new CheckSaldoListener() {
            @Override
            public void onLoading() {
                txt_saldo.setText("Memuat data ...");

            }

            @Override
            public void onResult(JSONObject result) {
                try{

                    if(result.getBoolean("status")){
                        JSONObject MESSAGE=new JSONObject(result.getString("message"));
                        txt_saldo.setText(bantuan.torupiah(bantuan.meisinteger(MESSAGE.getString("saldo"))));
                        txt9.setText(bantuan.torupiah(bantuan.meisinteger(MESSAGE.getString("pemasukan"))));
                        txt11.setText(bantuan.torupiah(bantuan.meisinteger(MESSAGE.getString("pengeluaran"))));

                    }
                    else{
                        txt_saldo.setText("Rp -");
                        // request_token();
                    }
                    //getmart();

                }
                catch (Exception e){

                }
            }

            @Override
            public void onError(String message) {

            }
        });

    }
    public void logout(View v){
        mAuth.getInstance().signOut();

        dialog_loading.show();
        service_connector.sendgetrequestwithsession_v3(this, sharedPrefManager.getSpCookie(), "service/logout", new Service_Connector.VolleyResponseListener_v3() {
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
                    //JSONArray datas=new JSONArray(respon.getString("status"));
                    sharedPrefManager.saveSPString(SharedPrefManager.SP_COOKIE,"-");
                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_ISLOGIN,false);
                    sharedPrefManager.saveSPString(SharedPrefManager.SP_USERNAME,"-");
                    sharedPrefManager.saveSPString(SharedPrefManager.SP_PASSWORD,"-");
                    sharedPrefManager.saveSPString(SharedPrefManager.SP_TOKEN,"-");
                    sharedPrefManager.saveSPString(SharedPrefManager.SP_EXPTOKEN,"-");
                    //sharedPrefManager.saveSPString(SharedPrefManager.SP,"-");
                    mAuth.signOut();


                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();
                    GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(ProfileActivity.this, gso);

                    mGoogleSignInClient.signOut().addOnCompleteListener(ProfileActivity.this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent=new Intent();
                                    intent.putExtra("action","3");
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }
                            });





                    //showpopup();
                }
                catch (Throwable t){
                    Log.i("ez",t.getMessage());
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
