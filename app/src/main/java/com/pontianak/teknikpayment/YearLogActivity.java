package com.pontianak.teknikpayment;

import android.app.ProgressDialog;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.pontianak.teknikpayment.Service.Server;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class YearLogActivity extends AppCompatActivity {
    String idDosen;
    TextView tvPeriode, tvTotalWaktu;
    ProgressDialog mProgressDialog;
    TextView[] tvTabelPeriode = new TextView[12];
    TextView[] tvTabelWaktu = new TextView[12];
    BarChart chart;
    String[] dataPeriode, dataWaktu, dataMonth, dataWaktuMenit;
    int finalYear = 0;
    Service_Connector service_connector;
    SharedPrefManager sharedPrefManager;
    //TextView tvTabelPeriode1,tvTabelPeriode2,tvTabelPeriode3,tvTabelPeriode4,tvTabelPeriode5,tvTabelPeriode6,tvTabelPeriode7,tvTabelPeriode8,tvTabelPeriode9,tvTabelPeriode10,tvTabelPeriode11,tvTabelPeriode12, tvTabelWaktu1,tvTabelWaktu2,tvTabelWaktu3,tvTabelWaktu4,tvTabelWaktu5,tvTabelWaktu6,tvTabelWaktu7,tvTabelWaktu8,tvTabelWaktu9,tvTabelWaktu10,tvTabelWaktu11,tvTabelWaktu12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        service_connector=new Service_Connector();
        sharedPrefManager=new SharedPrefManager(this);
        idDosen = sharedPrefManager.getSpString(SharedPrefManager.SP_ID);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_log);
        initTextViewTabel();

        settingChart();
        tvPeriode = findViewById(R.id.fieldYear);
        tvTotalWaktu = findViewById(R.id.waktuTotal);





        Calendar calendar = Calendar.getInstance();
        final int thisYear = calendar.get(Calendar.YEAR);
        finalYear = thisYear;

        tvPeriode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialogMonthPicker();
            }
        });

        String thisYearString = String.valueOf(thisYear);
        tvPeriode.setText(thisYearString);
        getStatisticData();

    }

    public void showDialogMonthPicker(){
        int tahun = Integer.valueOf(tvPeriode.getText().toString());
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(YearLogActivity.this, new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                // Log.d(TAG, "selectedMonth : " + selectedMonth + " selectedYear : " + selectedYear);
                selectedMonth += 1;
                tvPeriode.setText(String.valueOf(selectedYear));
                getStatisticData();
            }
        }, tahun, 1);

        builder.setActivatedYear(tahun)
                .setMinYear(1990)
                .setMaxYear(finalYear)
                .setMinMonth(Calendar.FEBRUARY)
                .setTitle("Pilih Tahun")
                .showYearOnly()
                //.setMonthRange(Calendar.FEBRUARY, Calendar.NOVEMBER)
                // .setMaxMonth(Calendar.OCTOBER)
                // .setYearRange(1890, 1890)
                // .setMonthAndYearRange(Calendar.FEBRUARY, Calendar.OCTOBER, 1890, 1890)
                //.showMonthOnly()
                // .showYearOnly()
                .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                    @Override
                    public void onMonthChanged(int selectedMonth) {
                        // Log.d(TAG, "Selected month : " + selectedMonth);
                        // Toast.makeText(MainActivity.this, " Selected month : " + selectedMonth, Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                    @Override
                    public void onYearChanged(int selectedYear) {
                        //Log.d(TAG, "Selected year : " + selectedYear);
                        // Toast.makeText(MainActivity.this, " Selected year : " + selectedYear, Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .show();

    }

    public void settingChart(){
        chart = findViewById(R.id.chartYear);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setScaleEnabled(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawLabels(false);

        final String[] mMonths = new String[]{
                "Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des"
        };

        ValueFormatter xAxisFormatter = new IndexAxisValueFormatter(chart);
        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(12);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                int idx = (int) value;
                try {
                    return mMonths[idx];
                }catch(Exception e){
                    return "";
                }
            }
        });


        Legend l = chart.getLegend();
        l.setEnabled(false);


    }

    private void setData() {
        BarDataSet set1;
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < dataPeriode.length; i++){

            values.add(new BarEntry(i, (Float.valueOf(dataWaktu[i]))));
        }


        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);

            chart.getData().notifyDataChanged();


        } else {
            set1 = new BarDataSet(values, "");

            set1.setDrawIcons(false);

//            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            /*int startColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor = ContextCompat.getColor(this, android.R.color.holo_blue_bright);
            set1.setGradientColor(startColor, endColor);*/

            int startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark);

            //List<GradientColor> gradientColors = new ArrayList<>();
            //gradientColors.add(new GradientColor(startColor2, startColor2));
/**
 gradientColors.add(new GradientColor(startColor2, endColor2));
 gradientColors.add(new GradientColor(startColor3, endColor3));
 gradientColors.add(new GradientColor(startColor4, endColor4));
 gradientColors.add(new GradientColor(startColor5, endColor5));
 */
            //set1.setGradientColors(gradientColors);
            set1.setColor(ContextCompat.getColor(YearLogActivity.this,R.color.colorPrimary));
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //data.setValueTypeface(tfLight);
            data.setBarWidth(0.9f);

            chart.setData(data);
        }
        chart.invalidate();
    }
    public void initTextViewTabel(){
        /**
         tvTabelPeriode1 = findViewById(R.id.periode1);
         tvTabelPeriode2 = findViewById(R.id.periode2);
         tvTabelPeriode3 = findViewById(R.id.periode3);
         tvTabelPeriode4 = findViewById(R.id.periode4);
         tvTabelPeriode5 = findViewById(R.id.periode5);
         tvTabelPeriode6 = findViewById(R.id.periode6);
         tvTabelPeriode7 = findViewById(R.id.periode7);
         tvTabelPeriode8 = findViewById(R.id.periode8);
         tvTabelPeriode9 = findViewById(R.id.periode9);
         tvTabelPeriode10 = findViewById(R.id.periode10);
         tvTabelPeriode11 = findViewById(R.id.periode11);
         tvTabelPeriode12 = findViewById(R.id.periode12);

         tvTabelWaktu1 = findViewById(R.id.waktu1);
         tvTabelWaktu2 = findViewById(R.id.waktu2);
         tvTabelWaktu3 = findViewById(R.id.waktu3);
         tvTabelWaktu4 = findViewById(R.id.waktu4);
         tvTabelWaktu5 = findViewById(R.id.waktu5);
         tvTabelWaktu6 = findViewById(R.id.waktu6);
         tvTabelWaktu7 = findViewById(R.id.waktu7);
         tvTabelWaktu8 = findViewById(R.id.waktu8);
         tvTabelWaktu9 = findViewById(R.id.waktu9);
         tvTabelWaktu10 = findViewById(R.id.waktu10);
         tvTabelWaktu11 = findViewById(R.id.waktu11);
         tvTabelWaktu12 = findViewById(R.id.waktu12);
         **/

        tvTabelPeriode[0] = findViewById(R.id.periode1);
        tvTabelPeriode[1] = findViewById(R.id.periode2);
        tvTabelPeriode[2] = findViewById(R.id.periode3);
        tvTabelPeriode[3] = findViewById(R.id.periode4);
        tvTabelPeriode[4] = findViewById(R.id.periode5);
        tvTabelPeriode[5] = findViewById(R.id.periode6);
        tvTabelPeriode[6] = findViewById(R.id.periode7);
        tvTabelPeriode[7] = findViewById(R.id.periode8);
        tvTabelPeriode[8] = findViewById(R.id.periode9);
        tvTabelPeriode[9] = findViewById(R.id.periode10);
        tvTabelPeriode[10] = findViewById(R.id.periode11);
        tvTabelPeriode[11] = findViewById(R.id.periode12);

        tvTabelWaktu[0] = findViewById(R.id.waktu1);
        tvTabelWaktu[1] = findViewById(R.id.waktu2);
        tvTabelWaktu[2] = findViewById(R.id.waktu3);
        tvTabelWaktu[3] = findViewById(R.id.waktu4);
        tvTabelWaktu[4] = findViewById(R.id.waktu5);
        tvTabelWaktu[5] = findViewById(R.id.waktu6);
        tvTabelWaktu[6] = findViewById(R.id.waktu7);
        tvTabelWaktu[7] = findViewById(R.id.waktu8);
        tvTabelWaktu[8] = findViewById(R.id.waktu9);
        tvTabelWaktu[9] = findViewById(R.id.waktu10);
        tvTabelWaktu[10] = findViewById(R.id.waktu11);
        tvTabelWaktu[11] = findViewById(R.id.waktu12);
    }

    public void getStatisticData(){
        final String periode = tvPeriode.getText().toString();
        showProgressDialog();
        String urlLog = Server.urlServer + "/getYearlyStatistic.php";

        //int socketTimeout = 20000;//30 seconds - change to what you want
        //RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", idDosen);
        params.put("year",periode);

        service_connector.url_sendpostrequest(this, urlLog, params, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }
            @Override
            public void onError(String message) {
                Log.d("VolleyError", String.valueOf(message));
                hideProgressDialog();
                Toast.makeText(YearLogActivity.this, "Akses Ke Server Gagal", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponese(String response) {

                String status = "0";
                String totalJam = "0 jam";
                String totalMenit = "0 menit";
                dataPeriode = new String[12];
                dataWaktu = new String[12];
                dataMonth = new String[12];
                dataWaktuMenit = new String[12];

                try{
                    JSONObject responseObject = new JSONObject(response);
                    status= responseObject.getString("status");
                    totalJam = responseObject.getString("totalJam");
                    totalMenit = responseObject.getString("totalMenit");

                    JSONArray jsonarray = new JSONArray(responseObject.getString("data"));

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        dataPeriode[i] = jsonobject.getString("monthYearTextShort");
                        dataMonth[i] = jsonobject.getString("monthShort");
                        dataWaktu[i] = jsonobject.getString("jamMenit");
                        dataWaktuMenit[i] = jsonobject.getString("menit");
                    }

                }catch(Exception e){
                    Log.d("jsonAbsenLog",String.valueOf(e));
                }finally {
                    if(dataPeriode.length > 0) {
                        for (int i = 0; i < dataPeriode.length; i++) {
                            tvTabelPeriode[i].setText(dataPeriode[i]);
                            tvTabelWaktu[i].setText(dataWaktu[i] + " jam");
                        }
                    }
                    setData();
                    tvTotalWaktu.setText(totalJam +" jam");
                    hideProgressDialog();
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

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Tunggu Sebentar!!!");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
