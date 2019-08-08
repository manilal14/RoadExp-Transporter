package com.road.roadexp_transporter.AddNewDriver;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.road.roadexp_transporter.CommonForAll.MySingleton;
import com.road.roadexp_transporter.DriverPackage.Unverified.UnverifiedDriver;
import com.road.roadexp_transporter.LoginSingUp.LoginSessionManager;
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
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;
import static com.road.roadexp_transporter.LoginSingUp.LoginSessionManager.TRANS_ID;

public class ADFrag3 extends Fragment {

    private String TAG = "AVFrag3";
    private View mRoot;

    private AddDriverPage mActivity;

    private  final int MY_CAMERA_PERMISSION_CODE = 100;
    private  final int CAMERA_REQUEST = 1888;


    private ProgressDialog mProgressDialog;
    private String mMobile;

    //flag = 1(dl front pic), 2(dl_back_pic) , 3(aadhar pic)
    private int flag = 0;
    private boolean mDlFrontUploaded = false;
    private boolean mDlBackUploaded = false;
    private boolean mAadharUploaded = false;

    private String currentPhotoPath;


    public ADFrag3() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (AddDriverPage) getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.adfrag3, container, false);
        mActivity.translateBoy(2);


        Bundle bundle = getArguments();

        mMobile = bundle.getString("mobile");

//
//        Log.e("asd=",name+" "+state+" "+city+" "+birthday+" "+mMobile+" "+account+" "+password);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Please wait..");
        mProgressDialog.setCancelable(false);

        clickListener();
        return mRoot;
    }

    private void clickListener() {

        mRoot.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().onBackPressed();

            }
        });

        mRoot.findViewById(R.id.all_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mDlFrontUploaded){
                    Toast.makeText(getActivity(),"dl front pic not uploaded",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!mDlBackUploaded){
                    Toast.makeText(getActivity(),"dl back pic not uploaded",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!mAadharUploaded){
                    Toast.makeText(getActivity(),"Aadhar not uploaded",Toast.LENGTH_SHORT).show();
                    return;
                }

                sendOtherDetail();


            }
        });

        mRoot.findViewById(R.id.upload1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                openCamera();
            }
        });

        mRoot.findViewById(R.id.upload2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 2;
                openCamera();
            }
        });

        mRoot.findViewById(R.id.upload3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 3;
                openCamera();
            }
        });


    }

    private void openCamera() {

        Log.e(TAG,"onCameraClick");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE);
        }
        else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        else {

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {

                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    currentPhotoPath = photoFile.getAbsolutePath();
                } catch (IOException ex) {

                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.road.roadexp_transporter.provider", photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent,CAMERA_REQUEST);
                }
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

        Log.e(TAG,"called : uploadProfilePic");

        String URL = BASE_URL + "addDriver";

        String imageParameter="";

        switch (flag){
            case 1 : imageParameter = "dl_pic_front"; break;
            case 2 : imageParameter = "dl_pic_back"; break;
            case 3 : imageParameter = "aadhar_front_pic"; break;

            default: imageParameter = "";

        }


        try {
            mProgressDialog.show();
            new MultipartUploadRequest(getActivity(),URL)

                    .addFileToUpload(imagePath, imageParameter)
                    .addParameter("phn",mMobile)

                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo){}

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            mProgressDialog.dismiss();
                            Toast.makeText(getActivity(),"Error occured, please upload once again", Toast.LENGTH_SHORT).show();
                            ImageView imageView ;

                            switch (flag){
                                case 1 : imageView = mRoot.findViewById(R.id.upload1); break;
                                case 2 : imageView = mRoot.findViewById(R.id.upload2); break;
                                case 3 : imageView = mRoot.findViewById(R.id.upload3); break;
                                default: imageView = mRoot.findViewById(R.id.upload1); break;

                            }
                            imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.camera));
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
                                        case 1 : message = "dl front pic uploaded"; mDlFrontUploaded =true; break;
                                        case 2 : message = "dl back pic uploaded"; mDlBackUploaded = true;break;
                                        case 3 : message = "aadhar uploaded"; mAadharUploaded = true; break;

                                        default: message = "";

                                    }
                                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG,e.toString());
                            }
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            mProgressDialog.dismiss();
                            Toast.makeText(getActivity(),"Upload cancle",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .startUpload();

        } catch (Exception e) {
            mProgressDialog.dismiss();
            Log.e(TAG, e.toString());
            Toast.makeText(getActivity(),"Error uploading",Toast.LENGTH_SHORT).show();
        }

    }

    private void sendOtherDetail() {

        Log.e(TAG, "called : sendOtherDetail");
        mProgressDialog.show();

        String URL = BASE_URL + "addDriverInfo";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                Log.e(TAG,response);

                try {
                    int code = new JSONObject(response).getInt("code");
                    if(code!=1){
                        Toast.makeText(getActivity(),"Code is not 1",Toast.LENGTH_SHORT).show();

                        ImageView imageView ;

                        switch (flag){
                            case 1 : imageView = mRoot.findViewById(R.id.upload1); break;
                            case 2 : imageView = mRoot.findViewById(R.id.upload2); break;
                            case 3 : imageView = mRoot.findViewById(R.id.upload3); break;
                            default: imageView = mRoot.findViewById(R.id.upload1); break;
                        }
                        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.camera));
                        return;
                    }

                    Toast.makeText(getActivity(),"Driver added successfully",Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(new Intent(getActivity(), UnverifiedDriver.class));
                    i.putExtra("currentViewPagerItem",1);
                    startActivity(i);

                    getActivity().finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Log.e(TAG,error.toString());
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> parms = new HashMap<>();

                Bundle bundle = getArguments();
                String name     = bundle.getString("name");
                String state    = bundle.getString("state");
                String city     = bundle.getString("city");
                String phone    = bundle.getString("mobile");
                String birthday = bundle.getString("birthday");
                String lice_exp = bundle.getString("license_exp");
                String account  = bundle.getString("account");

                String ifse  = bundle.getString("ifse");
                String holderName = bundle.getString("holder_name");

                String password = bundle.getString("password");

                String transId = new LoginSessionManager(getActivity()).getTransporterDetailsFromPref().get(TRANS_ID);

                parms.put("tid",transId);
                parms.put("d_name",name);
                parms.put("phn",phone);
                parms.put("city",city);
                parms.put("state",state);
                parms.put("birthday",birthday);
                parms.put("lice_exp",lice_exp);
                parms.put("password",password);
                parms.put("bankAccd",account);

                parms.put("ifse",ifse);
                parms.put("acc_name",holderName);

                return parms;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( RETRY_SECONDS, NO_OF_RETRY,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
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

            ImageView imageView ;

            switch (flag){
                case 1 : imageView = mRoot.findViewById(R.id.upload1); break;
                case 2 : imageView = mRoot.findViewById(R.id.upload2); break;
                case 3 : imageView = mRoot.findViewById(R.id.upload3); break;

                default: imageView = mRoot.findViewById(R.id.upload1); break;

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
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }




}
