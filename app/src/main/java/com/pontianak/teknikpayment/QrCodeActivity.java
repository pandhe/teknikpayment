package com.pontianak.teknikpayment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.VisionHelper.BarcodeGraphic;
import com.pontianak.teknikpayment.VisionHelper.CameraSource;
import com.pontianak.teknikpayment.VisionHelper.CameraSourcePreview;
import com.pontianak.teknikpayment.VisionHelper.FrameMetadata;
import com.pontianak.teknikpayment.VisionHelper.GraphicOverlay;
import com.pontianak.teknikpayment.VisionHelper.VisionProcessorBase;
import com.pontianak.teknikpayment.R;

import java.io.IOException;
import java.util.List;

public class QrCodeActivity extends AppCompatActivity {
    TextView barcodeInfo;
    SurfaceView cameraView;
    CameraSource cameraSource;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private String selectedModel = "Barcode Detection";
    private Service_Connector service_connector;
    Intent intent;
    int curr_camera=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_qr_code);
        service_connector=new Service_Connector();
        intent=getIntent();

        preview = (CameraSourcePreview) findViewById(R.id.firePreview);
        if (preview == null) {
            Log.d("ez", "Preview is null");
        }
        graphicOverlay = (GraphicOverlay) findViewById(R.id.fireFaceOverlay);
        if (graphicOverlay == null) {
            Log.d("ez", "graphicOverlay is null");
        }

        createCameraSource(selectedModel);
        startCameraSource();








    }
    private void createCameraSource(String model) {
        // If there's no existing cameraSource, create one.
       // if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay,curr_camera);
        //}



         cameraSource.setMachineLearningFrameProcessor(new BarcodeScanningProcessor());


    }
    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d("ez", "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d("ez", "resume: graphOverlay is null");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e("ez", "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }
    public class BarcodeScanningProcessor extends VisionProcessorBase<List<FirebaseVisionBarcode>> {

        private static final String TAG = "BarcodeScanProc";

        private final FirebaseVisionBarcodeDetector detector;
        FirebaseVisionBarcodeDetectorOptions options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(
                                FirebaseVisionBarcode.FORMAT_QR_CODE)
                        .build();

        public BarcodeScanningProcessor() {
            // Note that if you know which format of barcode your app is dealing with, detection will be
            // faster to specify the supported barcode formats one by one, e.g.
            // new FirebaseVisionBarcodeDetectorOptions.Builder()
            //     .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
            //     .build();
            detector = FirebaseVision.getInstance().getVisionBarcodeDetector();

        }

        @Override
        public void stop() {
            try {
                detector.close();
            } catch (IOException e) {
                Log.e(TAG, "Exception thrown while trying to close Barcode Detector: " + e);
            }
        }

        @Override
        protected Task<List<FirebaseVisionBarcode>> detectInImage(FirebaseVisionImage image) {
            return detector.detectInImage(image);
        }

        @Override
        protected void onSuccess(
                @NonNull List<FirebaseVisionBarcode> barcodes,
                @NonNull FrameMetadata frameMetadata,
                @NonNull GraphicOverlay graphicOverlay) {
            graphicOverlay.clear();
            for (int i = 0; i < barcodes.size(); ++i) {
                final FirebaseVisionBarcode barcode = barcodes.get(i);
                cameraSource.release();
                Log.i("ez",barcode.getRawValue());
                try {
                    detector.close();
                } catch (IOException e) {
                    Log.e(TAG, "Exception thrown while trying to close Barcode Detector: " + e);
                }

                Intent resint=new Intent();
                resint.putExtra("rawvalue",barcode.getRawValue());

                setResult(RESULT_OK,resint);
                finish();




                BarcodeGraphic barcodeGraphic = new BarcodeGraphic(graphicOverlay, barcode);
                graphicOverlay.add(barcodeGraphic);
            }
        }

        @Override
        protected void onFailure(@NonNull Exception e) {
            Log.e(TAG, "Barcode detection failed " + e);
        }
    }
    private void mulaiagik(String mes){
        cameraSource.release();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(QrCodeActivity.this);

        builder1.setMessage(mes);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Oke",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        createCameraSource(selectedModel);
                        startCameraSource();
                    }
                });



        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void switchcamera(View v){
        if(curr_camera==0){
            curr_camera=1;
        }
        else{
            curr_camera=0;
        }
        cameraSource.release();

        createCameraSource(selectedModel);
        startCameraSource();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        cameraSource.release();
        finish();
    }

}
