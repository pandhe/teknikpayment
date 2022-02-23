package com.pontianak.teknikpayment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pontianak.teknikpayment.Service.DataPart;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;
import com.pontianak.teknikpayment.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PhotoProfilActivity extends KuberlayarDilautan {
    Service_Connector service_connector;
    SharedPrefManager sharedPrefManager;

    Bitmap bitmap,bitmap2;
    Uri imageUri, file;
    ImageView img_1;
    private int PICTURE_RESULT = 1;
    public int READ_REQUEST_CODE = 42;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_profil);
        service_connector=new Service_Connector();
        sharedPrefManager=new SharedPrefManager(this);
        img_1=findViewById(R.id.img_1);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        String fp=service_connector.server.ROOT_URL+"image/profile/"+sharedPrefManager.getSpString(sharedPrefManager.SP_PHOTO);
        if(sharedPrefManager.getSpString(SharedPrefManager.SP_LEVEL).equals("UMUM")){
            fp=sharedPrefManager.getSpString(sharedPrefManager.SP_PHOTO);
        }


        Glide.with(this)
                .load(fp).apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.ic_default_profile)
                .into(img_1);
    }
    private static File getOutputMediaFile(){

//        File mediaStorageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),"MyUMPTK");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Log.i("foto_path1 :", "Scanned " + mediaStorageDir.getPath());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(getOutputMediaFile().getAbsolutePath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    public void takephoto(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        startActivityForResult(intent, PICTURE_RESULT);
    }
    public void performFileSearch(View view) {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }
    public byte[] getByteImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] bb;
        if(bmp.getHeight() <= 400 && bmp.getWidth() <= 400){
            bb = baos.toByteArray();

        }
        else{
            int targetWidth = 400; // your arbitrary fixed limit
            int targetHeight = (int) (bitmap.getHeight() * targetWidth / (double) bitmap.getWidth()); // casts to avoid truncating
            bitmap = Bitmap.createScaledBitmap(bitmap, 400, targetHeight, false);

            ByteArrayOutputStream baos2=new  ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos2);
            bb = baos.toByteArray();


        }

        return bb;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICTURE_RESULT && resultCode == Activity.RESULT_OK) {
            try {
                galleryAddPic();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);
                if (bitmap.getWidth() > bitmap.getHeight()) {
                    Matrix mat = new Matrix();
                    mat.postRotate(getCameraPhotoOrientation(file.getPath()));
                    bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
                } else {
                    bitmap2 = bitmap;
                }

                Glide.with(this).load(bitmap2).into(img_1);

  //             Glide.with(this).load(bitmap2).into(img_1);
              //  getLocation(file.getPath(), 0.0, 0.0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                //Log.i(TAG, "Uri: " + uri.toString());
                // String src=getPath(uri);
                //copytodir(src,uri);

                //showImage(uri);
                file = uri;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);
                    //Log.i("ezzuri",uri.getPath());

                    if (bitmap.getWidth() > bitmap.getHeight()) {
                        Matrix mat = new Matrix();
                        mat.postRotate(getCameraPhotoOrientation(file.getPath()));
                        bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
                    } else {
                        bitmap2 = bitmap;
                    }
                    Glide.with(this).load(bitmap2).into(img_1);

                }
                catch (IOException ie){

                }

            }
        }

    }
    public int getCameraPhotoOrientation(String imagePath){
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("RotateImage", "Exif orientation: " + "errorr");
        }
        return rotate;
    }
    public void upload_foto(View view){
        if(bitmap2!=null){
        showProgressDialog(true,"Mengunggah Gambar...");


        Map<String,String> map=new HashMap();


        Map<String, DataPart> dalam=new HashMap<>();
        dalam.put("gambar",new DataPart("kesempataann.jpg",getByteImage(bitmap2),"image/jpeg"));

        service_connector.sendmultipartsessionpost(this,sharedPrefManager.getSpCookie(), "service/uploadfotoprofil", map,dalam,new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }
            @Override
            public void onError(String message) {
                //hideDialog();
                Log.i("koordinat post :", "error");
                showProgressDialog(false,"");
                Toast.makeText(getApplicationContext(),"Error 1, "+message+" !!!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponese(String response) {
              //  hideDialog();

                try{
                    JSONObject respon=new JSONObject(response);
                    if(respon.getString("status").equals("1")){
                        Log.i("koordinat post :", "true");
                        Toast.makeText(getApplicationContext(), "Foto profil berhasil ditambahkan ", Toast.LENGTH_LONG).show();
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_PHOTO,respon.getString("data"));


                    }
                    showProgressDialog(false,"");


                    // Intent intent = new Intent(AddGedungActivity.this, MainActivity.class);
                   // startActivity(intent);
                }
                catch (Throwable t){
                    showProgressDialog(false,"");

                    Toast.makeText(getApplicationContext(), "Error, terjadi sesuatu !!! ", Toast.LENGTH_LONG).show();
                    Log.i("ez",t.getMessage());
                    Log.i("koordinat post :", "error2");
                }

            }

            @Override
            public void onNoConnection(String message) {
               // hideDialog();
                showProgressDialog(false,"");
                Toast.makeText(getApplicationContext(),"Error, No Internet Connection !!!", Toast.LENGTH_LONG).show();
                Log.i("koordinat post :", "error3");
            }

            @Override
            public void OnServerError(String message) {
               // hideDialog();
                showProgressDialog(false,"");
                Toast.makeText(getApplicationContext(),"Error 2, "+message+" !!!", Toast.LENGTH_LONG).show();
                Log.i("koordinat post :", "error4");

            }

            @Override
            public void OnTimeOut() {
             //   hideDialog();
                showProgressDialog(false,"");
                Toast.makeText(getApplicationContext(),"Error, Request Timeout !!!", Toast.LENGTH_LONG).show();
                Log.i("koordinat post :", "error5");
            }
        });}
    }
}
