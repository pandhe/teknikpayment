package com.pontianak.teknikpayment;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.zxing.WriterException;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MyQrActivity extends KuberlayarDilautan {
    SharedPrefManager sharedPrefManager;
    Service_Connector service_connector;
    TextView txt_saldo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr);
        service_connector=new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);
        ImageView img_profil=findViewById(R.id.imgprofil);
        ImageView img_qr=findViewById(R.id.img_qr);
        TextView txt_nama=findViewById(R.id.txt_nama);
        txt_saldo=findViewById(R.id.txt_saldo);
        TextView txt1,txt2,txt3;
        txt1=findViewById(R.id.txt1);
        txt2=findViewById(R.id.txt2);
        txt3=findViewById(R.id.txt3);
        txt2.setText("NIP / NIM : "+sharedPrefManager.getSpString(SharedPrefManager.SP_USERNAME));
        txt1.setText(sharedPrefManager.getSpString(SharedPrefManager.SP_LEVEL));
        txt3.setText("No Telp : "+sharedPrefManager.getSpString(SharedPrefManager.SP_PHONE));
        txt_nama.setText(sharedPrefManager.getSpString(SharedPrefManager.SP_NAMADOSEN));
        String fp=service_connector.server.ROOT_URL+"image/profile/"+sharedPrefManager.getSpString(sharedPrefManager.SP_PHOTO);
        if(sharedPrefManager.getSpString(SharedPrefManager.SP_LEVEL).equals("UMUM")){
            fp=sharedPrefManager.getSpString(sharedPrefManager.SP_PHOTO);
           // group2.setVisibility(View.GONE);
        }


        Glide.with(this)
                .load(fp).apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.ic_default_profile)
                .into(img_profil);
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        Bitmap bitmap;
        QRGEncoder qrgEncoder = new QRGEncoder(sharedPrefManager.getSpString(SharedPrefManager.SP_USERNAME), null, QRGContents.Type.TEXT, smallerDimension);
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.encodeAsBitmap();
            // Setting Bitmap to ImageView
            img_qr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v("ez", e.toString());
        }
        check_saldo();

    }
    public void check_saldo(){
        Map<String,String> map=new HashMap();

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
                    if(respon.getBoolean("status")){
                        JSONObject MESSAGE=new JSONObject(respon.getString("message"));
                        txt_saldo.setText(bantuan.torupiah(bantuan.meisinteger(MESSAGE.getString("saldo"))));

                    }
                    else{
                       // request_token();
                    }
                    //getmart();

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
