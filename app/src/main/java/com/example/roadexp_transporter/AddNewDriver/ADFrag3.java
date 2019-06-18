package com.example.roadexp_transporter.AddNewDriver;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.roadexp_transporter.CommonForAll.MySingleton;
import com.example.roadexp_transporter.LoginSingUp.LoginSessionManager;
import com.example.roadexp_transporter.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;
import static com.example.roadexp_transporter.LoginSingUp.LoginSessionManager.TRANS_ID;

public class ADFrag3 extends Fragment {

    private String TAG = "AVFrag3";
    private View mRoot;

    private AddDriverPage mActivity;

    private  final int MY_CAMERA_PERMISSION_CODE = 100;
    private  final int CAMERA_REQUEST = 1888;
    private String mImagePath = null;
    private Uri mFileUri;

    private ProgressDialog mProgressDialog;
    private String mMobile;

    //flag = 1(dl front pic), 2(dl_back_pic) , 3(aadhar pic)
    private int flag = 0;
    private boolean mDlFrontUploaded = false;
    private boolean mDlBackUploaded = false;
    private boolean mAadharUploaded = false;


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

        String name = bundle.getString("name");
        String state = bundle.getString("state");
        String city = bundle.getString("city");
        String birthday = bundle.getString("birthday");
        mMobile = bundle.getString("mobile");
        String account = bundle.getString("account");
        String password = bundle.getString("password");

        Log.e("asd=",name+" "+state+" "+city+" "+birthday+" "+mMobile+" "+account+" "+password);

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
                File file = new File(getActivity().getExternalCacheDir(), (System.currentTimeMillis()) + ".jpg");
                mFileUri = FileProvider.getUriForFile(getActivity(),"com.example.roadexp_transporter.provider",file);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }


    }

    //On camera result
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        Log.e(TAG,"onActivityResult : called");

        if(requestCode != RESULT_CANCELED) {

            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

                Uri selectedImage = mFileUri;
                String path = mFileUri.getPath();
                getActivity().getContentResolver().notifyChange(selectedImage, null);
                ContentResolver cr = getActivity().getContentResolver();

                Bitmap bitmap;
                File actualPath;

                try {
                    bitmap = MediaStore.Images.Media
                            .getBitmap(cr, selectedImage);

                    actualPath = new File(path);
                    mImagePath = actualPath.getAbsolutePath();

                    ImageView imageView ;

                    switch (flag){
                        case 1 : imageView = mRoot.findViewById(R.id.upload1); break;
                        case 2 : imageView = mRoot.findViewById(R.id.upload2); break;
                        case 3 : imageView = mRoot.findViewById(R.id.upload3); break;

                        default: imageView = mRoot.findViewById(R.id.upload1); break;

                    }
                    imageView.setImageBitmap(bitmap);

                    Log.e(TAG, "Actual path : " + actualPath.toString());

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }

                uploadPic();

            }
        }
    }

    // When image need to be send
    private void uploadPic(){

        Log.e(TAG,"called : uploadProfilePic");

        String URL = BASE_URL + "addDriverInfo";

        String imageParameter="";

        switch (flag){
            case 1 : imageParameter = "dl_pic_front"; break;
            case 2 : imageParameter = "dl_pic_back"; break;
            case 3 : imageParameter = "aadhar_front_pic"; break;

            default: imageParameter = "";

        }

        Log.e(TAG,mMobile+" "+mFileUri);

        try {
            mProgressDialog.show();
            new MultipartUploadRequest(getActivity(),URL)

                    .addFileToUpload(String.valueOf(mFileUri), imageParameter)
                    .addParameter("phn",mMobile)

                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo){}

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            mProgressDialog.dismiss();
                            Log.e("asd1",uploadInfo.toString());
                            if(serverResponse!=null)
                                Log.e("asd",serverResponse.getBodyAsString());
                            if(exception!=null)
                                Log.e("asd",exception.toString());
                            //Log.e(TAG,serverResponse.getBodyAsString());
                            //Toast.makeText(getActivity(),serverResponse.getBodyAsString(),Toast.LENGTH_SHORT).show();
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
                                        case 1 : message = "dl front pic uploaded;"; mDlFrontUploaded =true; break;
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

        String URL = BASE_URL + "addDriver";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                Log.e(TAG,response);

                //response ko handle kar lena

                //getActivity().finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Log.e(TAG,error.toString());
                Toast.makeText(getActivity(),"Other detail upload failed",Toast.LENGTH_SHORT).show();
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
                String account  = bundle.getString("account");
                String password = bundle.getString("password");

                String transId = new LoginSessionManager(getActivity()).getTransporterDetailsFromPref().get(TRANS_ID);

                parms.put("tid",transId);
                parms.put("d_name",name);
                parms.put("phn",phone);
                parms.put("city",city);
                parms.put("state",state);
                parms.put("birthday",birthday);
                parms.put("password",password);
                parms.put("bankAccd",account);

                return parms;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( RETRY_SECONDS, NO_OF_RETRY,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }




}
