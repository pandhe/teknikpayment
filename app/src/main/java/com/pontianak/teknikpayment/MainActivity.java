package com.pontianak.teknikpayment;

import android.content.Intent;

import androidx.annotation.Nullable;

import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pontianak.teknikpayment.Adapter.BelajarAdapter;
import com.pontianak.teknikpayment.Adapter.BeritaAdapter;
import com.pontianak.teknikpayment.Adapter.SholatAdapter;
import com.pontianak.teknikpayment.Adapter.SliderAdapter;
import com.pontianak.teknikpayment.Model.Belajar;
import com.pontianak.teknikpayment.Model.Berita;
import com.pontianak.teknikpayment.Model.JadwalSholat;
import com.pontianak.teknikpayment.Model.Model_slider;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends KuberlayarDilautan {
    ImageView img_profile;
    Service_Connector service_connector;
    Gson gson;
    List<JadwalSholat> arrayofslider = new ArrayList<>();
    List<Model_slider> arrayofslider2 = new ArrayList<>();
    List<Belajar> belajars = new ArrayList<>();
    RecyclerView recy_slider, recy_2, recy_3;
    SholatAdapter sliderAdapter;
    SliderAdapter slider2Adapter;
    BelajarAdapter belajarAdapter;
    SharedPrefManager sharedPrefManager;
    TextView txt_saldo;
    TextView txt_nama;
    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recy_berita;
    List<Berita> beritas = new ArrayList<>();
    BeritaAdapter beritaAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img_profile = findViewById(R.id.img_profile);
        service_connector = new Service_Connector();
        sharedPrefManager = new SharedPrefManager(this);
        gson = new GsonBuilder().create();
        recy_slider = findViewById(R.id.recy_slider);
        recy_2 = findViewById(R.id.recy_2);
        recy_3 = findViewById(R.id.recy_3);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);

        recy_slider.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recy_2.setLayoutManager(new GridLayoutManager(this, 3));
        recy_3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        sliderAdapter = new SholatAdapter(arrayofslider, this);
        slider2Adapter = new SliderAdapter(arrayofslider2, this);
        belajarAdapter = new BelajarAdapter(belajars, this);
        recy_slider.setAdapter(sliderAdapter);
        recy_2.setAdapter(slider2Adapter);
        recy_3.setAdapter(belajarAdapter);

        txt_nama = findViewById(R.id.txt1);
        TextView txt_status = findViewById(R.id.txt_3);
        //txt_nama.setText(sharedPrefManager.getSpString(sharedPrefManager.SP_NAMADOSEN));
        txt_nama.setText("Saldo");
        txt_status.setText(sharedPrefManager.getSpString(sharedPrefManager.SP_LEVEL));
        txt_saldo = findViewById(R.id.txt_2);
        ViewFlipper vf = findViewById(R.id.simpleViewFlipper);
        if (sharedPrefManager.getSpString(SharedPrefManager.SP_LEVEL).equals("UMUM")) {
            vf.setDisplayedChild(1);
        }

        recy_berita = findViewById(R.id.recy_berita);
        recy_berita.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        beritaAdapter = new BeritaAdapter(beritas, this, new BeritaAdapter.onSelect() {
            @Override
            public void onselectitem(Berita item) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.link));
                // Note the Chooser below. If no applications match,
                // Android displays a system message.So here there is no need for try-catch.
                startActivity(Intent.createChooser(intent, "Browse with"));

            }
        });
        recy_berita.setAdapter(beritaAdapter);


        //Log.i("ez","waw");
        //Log.i("ez",sharedPrefManager.getSpString(sharedPrefManager.SP_NAMA));


        Map<String, String> map = new HashMap();
        map.put("tipe", "jadwal sholat");


        //map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/get-retrieve-data.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try {
                    arrayofslider.clear();
                    JSONObject respon = new JSONObject(response);
                    JSONObject d = new JSONObject(respon.getString("data"));
                    JSONObject r = new JSONObject(d.getString("reponse"));
                    JSONObject d1 = new JSONObject(r.getString("data"));
                    JSONObject datas = new JSONObject(d1.getString("timings"));
                    JadwalSholat js = new JadwalSholat("Subuh", datas.getString("Fajr"), R.drawable.bg_malam);
                    arrayofslider.add(js);

                    js = new JadwalSholat("Dhuhr", datas.getString("Dhuhr"), R.drawable.icon_siang);
                    arrayofslider.add(js);
                    js = new JadwalSholat("Asr", datas.getString("Asr"), R.drawable.icon_siang);
                    arrayofslider.add(js);
                    js = new JadwalSholat("Maghrib", datas.getString("Maghrib"), R.drawable.bg_malam);
                    arrayofslider.add(js);
                    js = new JadwalSholat("Isha", datas.getString("Isha"), R.drawable.bg_malam);
                    arrayofslider.add(js);


                    sliderAdapter.notifyDataSetChanged();
                    checksession();
                    getberita();

                } catch (Throwable t) {
                    Log.i("ez", t.getMessage());
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
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        check_saldo();
                    }
                }
        );


    }

    @Override
    protected void onResume() {
        String fp = service_connector.server.ROOT_URL + "image/profile/" + sharedPrefManager.getSpString(sharedPrefManager.SP_PHOTO);
        if (sharedPrefManager.getSpString(SharedPrefManager.SP_LEVEL).equals("UMUM")) {
            fp = sharedPrefManager.getSpString(sharedPrefManager.SP_PHOTO);
        }


        Glide.with(this)
                .load(fp).apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.ic_default_profile)
                .into(img_profile);

        //txt_nama.setText(sharedPrefManager.getSpString(SharedPrefManager.SP_NAMADOSEN));
        txt_nama.setText("Saldo");
        check_saldo();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1003) {
            if (resultCode == RESULT_OK) {
                //f
                Log.i("ez", "finish");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


    public void check_saldo() {
        String firebase_token = FirebaseInstanceId.getInstance().getToken();
        Map<String, String> map = new HashMap();

        txt_saldo.setText("Memuat data ...");
        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        map.put("fcm", firebase_token);
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/check-wallet.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try {

                    JSONObject respon = new JSONObject(response);
                    if (respon.getBoolean("status")) {
                        swipeRefreshLayout.setRefreshing(false);
                        JSONObject MESSAGE = new JSONObject(respon.getString("message"));
                        txt_saldo.setText(bantuan.torupiah(bantuan.meisinteger(MESSAGE.getString("saldo"))));

                    } else {
                        txt_saldo.setText("Rp -");
                        request_token();
                    }
                    getmart();

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

    public void getmart() {
        Map<String, String> map = new HashMap();
        map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));

        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/product_merchant.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try {
                    arrayofslider2.clear();
                    JSONObject respon = new JSONObject(response);
                    JSONArray datas = new JSONArray(respon.getString("message"));
                    for (int i = 0; i < datas.length(); i++) {
                        //JSONObject dataa=new JSONObject(datas.get(i).toString());
                        Model_slider slder = gson.fromJson(datas.get(i).toString(), Model_slider.class);

                        arrayofslider2.add(slder);
                    }
                    // generateadapter();
                    slider2Adapter.notifyDataSetChanged();
                    //check_saldo();
                    getbelajar();
                } catch (Throwable t) {
                    Log.i("ez", t.getMessage());
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

    public void getbelajar() {

        Map<String, String> map = new HashMap();



        //map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/learning.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try {
                    belajars.clear();
                    JSONObject respon = new JSONObject(response);


                    JSONArray items = new JSONArray(respon.getString("data"));

                    for (int i = 0; i < items.length(); i++) {
                        //JSONObject dataa=new JSONObject(datas.get(i).toString());
                        Belajar belajar = gson.fromJson(items.get(i).toString(), Belajar.class);

                        belajars.add(belajar);
                    }


                    belajarAdapter.notifyDataSetChanged();


                } catch (Throwable t) {
                    Log.i("ez belajar", t.getMessage());
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

    // migrasi dari check token
    public void request_token() {
        if (sharedPrefManager.getSpString(SharedPrefManager.SP_PHONE).equals(" - ") || sharedPrefManager.getSpString(SharedPrefManager.SP_PHONE).equals("")) {
            newwarningdialog("Silahkan isi nomor telepon anda terlebih dahulu untuk menggunakan fitur payment");
        } else {
            Map<String, String> map = new HashMap();

            map.put("username", sharedPrefManager.getSpString(SharedPrefManager.SP_USERNAME));
            map.put("password", sharedPrefManager.getSpString(SharedPrefManager.SP_PASSWORD));

            map.put("apikey", "CkLsDh9B");
            map.put("nohp", sharedPrefManager.getSpString(SharedPrefManager.SP_PHONE));
            service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/generate-token.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
                @Override
                public void onResult() {

                }

                @Override
                public void onError(String message) {
                    //  showmessage("Server Error");
                    //   showprogress(false);
                }

                @Override
                public void onResponese(String response) {
                    try {
                        JSONObject respon = new JSONObject(response);
                        if (respon.getBoolean("status")) {
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_TOKEN, respon.getString("token"));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_EXPTOKEN, respon.getString("expired"));
                            //save_token();
                            check_saldo();


                        } else {
                            if (respon.getString("message").equals("username is not found")) {
                                create_wallet();
                            } else {
                                // showmessage("Token Salah");
                                // showprogress(false);
                            }

                        }

                    } catch (Exception e) {
                        // showmessage("Server Error");
                        //  showprogress(false);
                    }

                }

                @Override
                public void onNoConnection(String message) {
                    // showmessage("Tidak Ada Jaringan");
                    // showprogress(false);
                }

                @Override
                public void OnServerError(String message) {
                    //showmessage("Server Error");
                    // showprogress(false);
                }

                @Override
                public void OnTimeOut() {

                }
            });
        }


    }

    public void create_wallet() {
        Map<String, String> map = new HashMap();

        map.put("username", sharedPrefManager.getSpString(SharedPrefManager.SP_USERNAME));
        map.put("password", sharedPrefManager.getSpString(SharedPrefManager.SP_PASSWORD));
        map.put("apikey", "CkLsDh9B");
        map.put("nohp", sharedPrefManager.getSpString(SharedPrefManager.SP_PHONE));
        map.put("name", sharedPrefManager.getSpString(SharedPrefManager.SP_NAMA));
        service_connector.url_sendpostrequest(this, "https://payment.myumptk.com/api/v0/create-wallet.aspx", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }

            @Override
            public void onError(String message) {
                // showmessage("Server Error");
                // showprogress(false);
            }

            @Override
            public void onResponese(String response) {
                try {
                    JSONObject respon = new JSONObject(response);
                    if (respon.getBoolean("status")) {
                        request_token();
                        //save_token();


                    } else {
                        // showmessage("Token Salah");
                        // showprogress(false);
                    }

                } catch (Exception e) {
                    // showmessage("Server Error");
                    //  showprogress(false);
                }

            }

            @Override
            public void onNoConnection(String message) {
                //  showmessage("Tidak Ada Jaringan");
                //  showprogress(false);
            }

            @Override
            public void OnServerError(String message) {
                //  showmessage("Server Error");
                //  showprogress(false);
            }

            @Override
            public void OnTimeOut() {

            }
        });

    }

    public void save_token() {
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
        //  gohome(sharedPrefManager.getSpString(SharedPrefManager.SP_LEVEL));
    }

    public void getberita() {
        Map<String, String> map = new HashMap();
        //map.put("tipe", "berita");


        //map.put("token", sharedPrefManager.getSpString(SharedPrefManager.SP_TOKEN));
        service_connector.url_sendgetrequest(this, "https://untan.ac.id/wp-json/wp/v2/posts", new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try {
                    beritas.clear();

                    JSONArray items = new JSONArray(response);

                    int tot = items.length();
                    if(tot > 10){
                        tot = 10;
                    }

                    for (int i = 0; i < tot; i++) {
                        JSONObject dataa=new JSONObject(items.get(i).toString());
                        JSONObject datatitle= new JSONObject(dataa.getString("title"));
                        String titlebarita="";
                        titlebarita=datatitle.getString("rendered");

                        Berita berita = new Berita(titlebarita,dataa.getString("link"));

                        beritas.add(berita);
                    }


                    beritaAdapter.notifyDataSetChanged();
                    check_saldo();

                } catch (Throwable t) {
                    Log.i("ez", t.getMessage());
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

    public void checksession() {
        String firebase_token = FirebaseInstanceId.getInstance().getToken();
        Map<String, String> map = new HashMap();
        if (firebase_token == null) {
            firebase_token = FirebaseInstanceId.getInstance().getToken();

        } else {
            map.put("fcm_token", firebase_token);

        }

        service_connector.sendpostrequestwithsession_v3(this, sharedPrefManager.getSpCookie(), "service", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try {
                    JSONObject respon = new JSONObject(response);
                    sharedPrefManager.saveSPString(SharedPrefManager.SP_fitur, respon.getString("fitur"));
                    JSONArray JFITUR = new JSONArray(respon.getString("fitur"));
                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_IS_MOCK, JFITUR.getBoolean(1));

                    Log.i("ez MOCK", String.valueOf(JFITUR.getBoolean(1)));
                    check_saldo();

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

