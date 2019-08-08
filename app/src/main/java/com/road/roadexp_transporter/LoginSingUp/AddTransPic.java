package com.road.roadexp_transporter.LoginSingUp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.road.roadexp_transporter.HomePage.AppHomePage;
import com.road.roadexp_transporter.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.hasPermissions;

public class AddTransPic extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();


    private  final int CAMERA_REQUEST = 1888;

    private ProgressDialog mProgressDialog;
    private String mMobile;

    //flag = 1(vehicle pic), 2(rc front pic) , 3(rc back pic), 4( insurance pic ) , 5(permit pic)
    private int flag = 0;
    private boolean mTransPicUpload = false;
    private boolean mAadharUploaded  = false;


    private final int PERMISSION_ALL = 1;

    private String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trans_pic);

        mMobile = getIntent().getStringExtra("mob");

        mProgressDialog = new ProgressDialog(AddTransPic.this);
        mProgressDialog.setMessage("Please wait..");
        mProgressDialog.setCancelable(false);

        clickListener();
    }

    private void clickListener() {

        findViewById(R.id.upload1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                openCamera();
            }
        });

        findViewById(R.id.upload2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 2;
                openCamera();
            }
        });


        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mTransPicUpload){
                    Toast.makeText(AddTransPic.this,"Transporter pic not uploaded",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!mAadharUploaded){
                    Toast.makeText(AddTransPic.this,"Aadhar pic not uploaded",Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });

    }

    private void openCamera() {

        Log.e(TAG,"onCameraClick");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        String[] PERMISSIONS = {
                Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        if(!hasPermissions(AddTransPic.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(AddTransPic.this, PERMISSIONS, PERMISSION_ALL);
            return;
        }


        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(AddTransPic.this.getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
                currentPhotoPath = photoFile.getAbsolutePath();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(AddTransPic.this, "com.road.roadexp_transporter.provider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent,CAMERA_REQUEST);
            }

        }
    }




    //On camera result
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        Log.e(TAG,"onActivityResult : called");

        if(requestCode != RESULT_CANCELED) {

            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                String compressedPath = compressImage(currentPhotoPath);
                uploadPic(compressedPath);

            }
        }
    }

    private void uploadPic(String imagePath){

        Log.e(TAG,"called : uploadPic");

        String URL = BASE_URL + "addTransPic";

        String imageParameter="";
        switch (flag){
            case 1 : imageParameter = "prof_trans_pic"; break;
            case 2 : imageParameter = "aadhar_trans_pic"; break;

            default: imageParameter = "";

        }


        try {
            mProgressDialog.show();
            new MultipartUploadRequest(AddTransPic.this,URL)

                    .addFileToUpload(imagePath, imageParameter)
                    .addParameter("mob",mMobile)

                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo){}

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            mProgressDialog.dismiss();
                            Toast.makeText(AddTransPic.this,"Error occured, please upload once again", Toast.LENGTH_SHORT).show();
                            ImageView imageView ;

                            switch (flag){
                                case 1 : imageView = findViewById(R.id.upload1); break;
                                case 2 : imageView = findViewById(R.id.upload2); break;


                                default: imageView = findViewById(R.id.upload1); break;
                            }

                            imageView.setImageDrawable(ContextCompat.getDrawable(AddTransPic.this,R.drawable.camera));
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            mProgressDialog.dismiss();

                            Log.e(TAG,serverResponse.getBodyAsString()+"");
                            try {
                                int code =  new JSONObject(serverResponse.getBodyAsString()).getInt("code");
                                if(code==1){
                                    String message = "";
                                    switch (flag){
                                        case 1 : message = "Transporter Pic uploaded"; mTransPicUpload = true; break;
                                        case 2 : message = "Aadhar uploaded"; mAadharUploaded = true;break;
                                        default: message = "";
                                    }
                                    Toast.makeText(AddTransPic.this,message,Toast.LENGTH_SHORT).show();

                                    if(mTransPicUpload && mAadharUploaded)
                                        goToNextPage();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG,e.toString());
                            }
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            mProgressDialog.dismiss();
                            Toast.makeText(AddTransPic.this,"Upload cancle",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .startUpload();

        } catch (Exception e) {
            mProgressDialog.dismiss();
            Log.e(TAG, e.toString());
            Toast.makeText(AddTransPic.this,"Error uploading",Toast.LENGTH_SHORT).show();
        }

    }

    private void goToNextPage() {

        Log.e(TAG,"called : goToNextPage");

        Intent i = new Intent(AddTransPic.this, AppHomePage.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(i);
        finishAffinity();


    }

    public String compressImage(String x) {

        String filePath = x;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {               imgRatio = maxHeight / actualHeight;                actualWidth = (int) (imgRatio * actualWidth);               actualHeight = (int) maxHeight;             } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);


            //iv1.setImageBitmap(bmp);

            ImageView imageView ;

            switch (flag){
                case 1 : imageView = findViewById(R.id.upload1); break;
                case 2 : imageView = findViewById(R.id.upload2); break;


                default: imageView = findViewById(R.id.upload1); break;

            }
            imageView.setImageBitmap(bmp);

        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        File f = null;
        try {
            f = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = f.getAbsolutePath();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = AddTransPic.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }
}
