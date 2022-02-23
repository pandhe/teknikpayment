package com.pontianak.teknikpayment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.NetworkResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends KuberlayarDilautan {
    SharedPrefManager sharedPrefManager;
    ProgressDialog pDialog;
    Button btnLogin,btnLogin1,bt_daftar,btnLogin2,btnLoginv;
    EditText etUsername, etPassword,etDpassword,etUlangpassword,etVeriv,edt_1,edt_2,edt_3;
    Intent intent;
    //declarasikan dulu
    Service_Connector service_connector;
    String usermane="";
    String password="";
    String level;
    ProgressBar progressBar;
    TextView text_warning;
    //ConstraintLayout cons;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private boolean phone=false;
    private  boolean verif=false;
    private boolean tokenmode=false;
    private ViewFlipper vf;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //delcarasikan
        service_connector=new Service_Connector();
        text_warning=findViewById(R.id.txt_warning);
        progressBar=findViewById(R.id.progressBar);
        vf=findViewById(R.id.simpleViewFlipper);
        //cons=findViewById(R.id.cons);


        sharedPrefManager = new SharedPrefManager(this);
        btnLogin = findViewById(R.id.bt_login);
        //btnLogin1 = findViewById(R.id.btnLogin1);
        //bt_daftar = findViewById(R.id.bt_daftar);
        //btnLogin2 = findViewById(R.id.btnLogin2);
        //btnLoginv = findViewById(R.id.btnLoginv);
        etUsername = (EditText) findViewById(R.id.edt_email);
        etPassword = (EditText) findViewById(R.id.edt_password);
        edt_1=findViewById(R.id.edt_1);
        edt_2=findViewById(R.id.edt_2);
        edt_3=findViewById(R.id.edt_3);
       // etDpassword=findViewById(R.id.etDPassword);
        //etUlangpassword=findViewById(R.id.etUlangPassword);
        //etVeriv=findViewById(R.id.etVeriv);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA }, 1000);

        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                usermane=etUsername.getText().toString();
                password=etPassword.getText().toString();
                login(false);
            }
        });

        checklogin();
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
    public void checklogin(){
        checkfasession();

    }
    public void checkfasession(){
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            gohome(sharedPrefManager.getSpString(SharedPrefManager.SP_LEVEL));


        }
        else{
            checksession();
        }

    }
    public void checksession(){
        String firebase_token= FirebaseInstanceId.getInstance().getToken();
        Map<String,String> map=new HashMap();
        if(firebase_token==null){
            firebase_token= FirebaseInstanceId.getInstance().getToken();

        }
        else{
            map.put("fcm_token",firebase_token);

        }

        service_connector.sendpostrequestwithsession_v3(this, sharedPrefManager.getSpCookie(), "service",map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {
                showmessage("");
                showprogress(false);
            }
            @Override
            public void onError(String message) {
                showmessage(message);
                showprogress(false);
            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject respon=new JSONObject(response);
                    if(respon.getString("login").equals("1")){
                        usermane=sharedPrefManager.getSpString(SharedPrefManager.SP_USERNAME);
                        password=sharedPrefManager.getSpString(SharedPrefManager.SP_PASSWORD);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_fitur,respon.getString("fitur"));
                        JSONArray JFITUR = new JSONArray(respon.getString("fitur"));
                        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_IS_MOCK,JFITUR.getBoolean(1));

                        Log.i("ez MOCK",String.valueOf(JFITUR.getBoolean(1)));



                        if(tokenmode) {
                            request_token();
                        }
                        else{
                            gohome(sharedPrefManager.getSpString(SharedPrefManager.SP_LEVEL));
                        }

                    }
                    else{
                        //letakkan kondisi jika username / password salah
                        if(sharedPrefManager.getSpBoolean(sharedPrefManager.SP_ISLOGIN)) {
                            login(true);

                        }
                        else{
                            vf.setDisplayedChild(1);
                            //vf.setVisibility(View.VISIBLE);
                        }
                    }

                }
                catch (Exception e){
                    showmessage(e.getMessage());
                    showprogress(false);
                }

            }

            @Override
            public void onNoConnection(String message) {
                showmessage("Gagal terhubung ke server, hidupkan ulang aplikasi");
                showprogress(false);
            }

            @Override
            public void OnServerError(String message) {
                showmessage("Gagal terhubung ke server, hidupkan ulang aplikasi");
                showprogress(false);
            }

            @Override
            public void OnTimeOut() {
                showmessage("Gagal terhubung ke server, hidupkan ulang aplikasi");
                showprogress(false);
            }
        });
    }

    public void request_token(){
        Map<String,String> map=new HashMap();

            map.put("username", usermane);
            map.put("password", password);

        map.put("apikey", "CkLsDh9B");
        map.put("nohp",sharedPrefManager.getSpString(SharedPrefManager.SP_PHONE));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/generate-token.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }
            @Override
            public void onError(String message) {
                showmessage("Server Error");
                showprogress(false);
            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject respon=new JSONObject(response);
                    if(respon.getBoolean("status")){
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_TOKEN,respon.getString("token"));
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_EXPTOKEN,respon.getString("expired"));
                        save_token();


                    }
                    else{
                        if(respon.getString("message").equals("username is not found")){
                            create_wallet();
                        }
                        else{
                            showmessage("Token Salah");
                            showprogress(false);
                        }

                    }

                }
                catch (Exception e){
                    showmessage("Server Error");
                    showprogress(false);
                }

            }

            @Override
            public void onNoConnection(String message) {
                showmessage("Tidak Ada Jaringan");
                showprogress(false);
            }

            @Override
            public void OnServerError(String message) {
                showmessage("Server Error");
                showprogress(false);
            }

            @Override
            public void OnTimeOut() {

            }
        });

    }

    public void create_wallet(){
        Map<String,String> map=new HashMap();

        map.put("username", usermane);
        map.put("password", password);

        map.put("apikey", "CkLsDh9B");
        map.put("nohp",sharedPrefManager.getSpString(SharedPrefManager.SP_PHONE));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/create-wallet.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }
            @Override
            public void onError(String message) {
                showmessage("Server Error");
                showprogress(false);
            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject respon=new JSONObject(response);
                    if(respon.getBoolean("status")){
                        request_token();
                        save_token();


                    }
                    else{
                        showmessage("Token Salah");
                        showprogress(false);
                    }

                }
                catch (Exception e){
                    showmessage("Server Error");
                    showprogress(false);
                }

            }

            @Override
            public void onNoConnection(String message) {
                showmessage("Tidak Ada Jaringan");
                showprogress(false);
            }

            @Override
            public void OnServerError(String message) {
                showmessage("Server Error");
                showprogress(false);
            }

            @Override
            public void OnTimeOut() {

            }
        });

    }
    public void save_token(){
       /* Map<String,String> map=new HashMap();
        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        map.put("token_expired", sharedPrefManager.getSpString(SharedPrefManager.SP_EXPTOKEN));

        service_connector.sendpostrequestwithsession_v3(this, sharedPrefManager.getSpCookie(), "service/add_token", map, new Service_Connector.VolleyResponseListener_v3() {
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
                    if(respon.getString("status").equals("1")){
                        gohome(sharedPrefManager.getSpString(SharedPrefManager.SP_LEVEL));



                    }
                    else{
                        showmessage("Token Salah");
                        showprogress(false);
                    }

                }
                catch (Exception e){
                    showmessage("Server Error");
                    showprogress(false);
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
        });*/
        gohome(sharedPrefManager.getSpString(SharedPrefManager.SP_LEVEL));
    }

    public void login(boolean autologin){
        String firebase_token= FirebaseInstanceId.getInstance().getToken();
        Map<String,String> map=new HashMap();
        if(firebase_token==null){
            map.put("fcm_token","-");

        }
        else{
            map.put("fcm_token",firebase_token);

        }

        if(autologin){
            map.put("username", sharedPrefManager.getSpString(SharedPrefManager.SP_USERNAME));
            map.put("password", sharedPrefManager.getSpString(SharedPrefManager.SP_PASSWORD));
        }
        else {
            map.put("username", usermane);
            map.put("password", password);
        }
        if(phone){
            map.put("phone", "1");
        }
        else{
            map.put("phone", "0");
        }
        showprogress(true);
        showmessage("");
        //untuk send post session request
        service_connector.sendpostsessionrequest(LoginActivity.this, "service/login", map, new Service_Connector.VolleyResponseCookieListener_v2() {
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
                    if(respon.getString("status").equals("1")){

                        JSONObject datalogin=new JSONObject(respon.getString("data"));
                        level=datalogin.getString("level");

                        //nomor telefon telah diverifikasi
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_PHONE,datalogin.getString("no_hp"));
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMADOSEN,datalogin.getString("nama_lengkap"));
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA,datalogin.getString("nama_lengkap"));

                        sharedPrefManager.saveSPString(SharedPrefManager.SP_PHOTO,datalogin.getString("photo"));

                        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID, datalogin.getString("id"));

                        sharedPrefManager.saveSPString(SharedPrefManager.SP_LEVEL,level);

                        if(!autologin) {
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_USERNAME, usermane);
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_PASSWORD, password);
                        }
                        if(tokenmode) {
                            if(respon.getBoolean("is_register")){
                                vf.setDisplayedChild(2);

                            }
                            else{
                                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_ISLOGIN,true);

                                request_token();
                            }

                        }
                        else{

                            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_ISLOGIN,true);

                            gohome(sharedPrefManager.getSpString(SharedPrefManager.SP_LEVEL));
                        }






                    }
                    else{
                        //letakkan kondisi jika username / password salah
                        showprogress(false);
                        //bt_daftar.setVisibility(View.VISIBLE);
                        showmessage(respon.getString("message"));
                    }

                }
                catch (Exception e){
                    Log.i("ez",e.getMessage());

                }

            }

            @Override
            public void onCookie(NetworkResponse nr) {
                sharedPrefManager.saveSPString(SharedPrefManager.SP_COOKIE, nr.headers.get("Set-Cookie"));

            }

            @Override
            public void onNoConnection(String message) {
                showmessage("Koneksi Gagal");
                showprogress(false);


            }

            @Override
            public void OnServerError(String message) {
                showmessage("Server Error");
                showprogress(false);

            }

            @Override
            public void OnTimeOut() {
                showmessage("Waktu Sambung Habis");
                showprogress(false);

            }
        });
    }





    public void verif(){
        String phoneNumber = "+16505554567";
        String smsCode = "123456";

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

// Configure faking the auto-retrieval with the whitelisted numbers.
        //firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode);
        String fusername="";
        String substr=usermane.substring(0,2);
        String substr2=usermane.substring(1);
        if(substr.equals("08")){
            fusername="+62"+substr2;
        }
        else{
            fusername=usermane;
        }

        Log.i("ez","send verification to "+fusername);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                fusername, 30L /*timeout*/, TimeUnit.SECONDS,
                this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // Save the verification id somewhere
                        // ...

                        // The corresponding whitelisted code above should be used to complete sign-in.
                        mVerificationId=verificationId;
                        showprogress(false);
                        bt_daftar.setVisibility(View.GONE);
                       // vf.setDisplayedChild(3);
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // Sign in with the credential
                        // ...
                        phone=true;
                        verif=true;
                        login(true);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // ...
                        showmessage("Terjadi kesalahan saat memeriksa "+usermane);
                        showprogress(false);
                        Log.i("ez",e.getMessage());
                    }

                });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ez", "signInWithCredential:success");

                           // FirebaseUser user = task.getResult().getUser();
                            phone=true;
                            verif=true;
                            login(true);

                            // [START_EXCLUDE]
                            //updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("ez", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                etVeriv.setError("Kode tidak sama");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            //updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }


    public void gohome(String lv){
       switch (lv){
            case "MAHASISWA":


               intent = new Intent(LoginActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
                break;
           case "UMUM":


               intent = new Intent(LoginActivity.this, MainActivity.class);
               finish();
               startActivity(intent);
               break;

            case "DOSEN":
               intent = new Intent(LoginActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
                break;

           case "STAFF":
               intent = new Intent(LoginActivity.this, MainActivity.class);
               finish();
               startActivity(intent);
               break;
           default:

               break;

        }



    }
    public void setverif(){
        service_connector.sendgetrequestwithsession_v3(this, sharedPrefManager.getSpCookie(), "setverif", new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {

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
    public void ganti_profil_awal(View view){
        showProgressDialog(true,"Mengganti biodata...");
        Map<String,String> map=new HashMap();
        map.put("no_hp",edt_1.getText().toString());
        map.put("password",sharedPrefManager.getSpString(SharedPrefManager.SP_PASSWORD));
        map.put("new_password",edt_2.getText().toString());
        map.put("confirmpassword",edt_3.getText().toString());

        service_connector.sendpostrequestwithsession_v3(this, sharedPrefManager.getSpCookie(), "service/change_profil_awal",map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }
            @Override
            public void onError(String message) {
                showProgressDialog(false,"");

            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject respon=new JSONObject(response);
                    if(respon.getString("status").equals("1")){
                        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_ISLOGIN,true);

                        showProgressDialog(false,"");
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_PASSWORD,edt_2.getText().toString());
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_PHONE,edt_1.getText().toString());

                        vf.setDisplayedChild(1);
                        request_token();


                    }
                    else{

                    }

                }
                catch (Exception e){

                }

            }

            @Override
            public void onNoConnection(String message) {
                showProgressDialog(false,"");

            }

            @Override
            public void OnServerError(String message) {
                showProgressDialog(false,"");

            }

            @Override
            public void OnTimeOut() {
                showProgressDialog(false,"");

            }
        });
    }
    public void signInGoogle(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("ezz", "Google sign in failed", e);
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("ez", "firebaseAuthWithGoogle:" + acct.getId());
        //group2.setVisibility(View.VISIBLE);
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ez", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            getuser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("ez", "signInWithCredential:failure", task.getException());
                            //group2.setVisibility(View.INVISIBLE);
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            // updateUI();
                        }

                        // [START_EXCLUDE]
                        // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void getuser(final FirebaseUser user){
        //group2.setVisibility(View.VISIBLE);
        showprogress(true);
        showmessage("");
        user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {

                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    Map<String,String> mymap=new HashMap<>();
                    if(user.getPhoneNumber()!=null)
                        mymap.put("no_telp",user.getPhoneNumber());

                    mymap.put("id",user.getUid());
                    mymap.put("email",user.getEmail());

                    mymap.put("idtoken",idToken);

                    Log.i("ez","loginuseerrr"+user.getUid());


                    service_connector.sendpostrequest(LoginActivity.this, "service/login_fcm", mymap, new Service_Connector.VolleyResponseListener_v3() {
                        @Override
                        public void onResult() {
                            showprogress(false);
                            showmessage("");

                        }

                        @Override
                        public void onError(String message) {
                            Log.i("ez",message);
                           // group2.setVisibility(View.INVISIBLE);
                            showprogress(false);
                            showmessage("Login dengan google gagal, coba dengan akun lain");

                        }

                        @Override
                        public void onResponese(String response) {
                            try{
                                JSONObject jsonObject=new JSONObject(response);
                                if(jsonObject.getString("status").equals("1")){
                                    JSONObject datalogin=new JSONObject(jsonObject.getString("data"));

                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_PHONE,datalogin.getString("no_hp"));
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMADOSEN,datalogin.getString("nama_lengkap"));
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA,datalogin.getString("nama_lengkap"));

                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_USERNAME, user.getEmail());
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_PASSWORD, user.getUid());


                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_PHOTO,user.getPhotoUrl().toString());

                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ID, jsonObject.getString("datalogin"));

                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_LEVEL,"UMUM");
                                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_ISLOGIN,true);

                                    gohome(sharedPrefManager.getSpString(SharedPrefManager.SP_LEVEL));


                                    if(tokenmode) {


                                    }
                                    else{


                                    }
                                }
                                else {
                                    //group2.setVisibility(View.INVISIBLE);
                                }
                            }
                            catch (Throwable t){
                                showlog(t.getMessage());

                            }
                        }

                        @Override
                        public void onNoConnection(String message) {
                           // group2.setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void OnServerError(String message) {
                            Log.i("ez",message);
                           // group2.setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void OnTimeOut() {
                           // group2.setVisibility(View.INVISIBLE);

                        }
                    });

                }
                else{
                    Log.i("ez",task.getException().getMessage());
                    showprogress(false);
                    showmessage("Login dengan google gagal, coba dengan akun lain");
                }
            }
        });



    }
}
