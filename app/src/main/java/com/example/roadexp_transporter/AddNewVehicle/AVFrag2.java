package com.example.roadexp_transporter.AddNewVehicle;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.roadexp_transporter.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import static android.app.Activity.RESULT_CANCELED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.hasPermissions;

public class AVFrag2 extends Fragment {

    private String TAG = "AVFrag2";
    private View mRoot;

    private AddVehicleHomePage mActivity;

    private  final int MY_CAMERA_PERMISSION_CODE = 100;
    private  final int CAMERA_REQUEST = 1888;
    private String mImagePath = null;
    private Uri mFileUri;

    private ProgressDialog mProgressDialog;
    private String mVehicleNum;

    //flag = 1(vehicle pic), 2(rc front pic) , 3(rc back pic)
    private int flag = 0;
    private boolean mVehiclePicUpload = false;
    private boolean mRcFrontUploaded  = false;
    private boolean mRcBackUploaded   = false;

    private final int PERMISSION_ALL = 1;

    public AVFrag2() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AddVehicleHomePage) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot =  inflater.inflate(R.layout.avfrag2, container, false);
        mActivity.translateTruck(1);

        mVehicleNum = getArguments().getString("vehicle_num");
        Log.e(TAG,mVehicleNum);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Please wait..");
        mProgressDialog.setCancelable(false);

        clickListener();

        return mRoot;
    }

    private void clickListener() {

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

        mRoot.findViewById(R.id.fard_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mVehiclePicUpload){
                    Toast.makeText(getActivity(),"Vehicle pic not uploaded",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!mRcFrontUploaded){
                    Toast.makeText(getActivity(),"Rc front pic not uploaded",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!mRcBackUploaded){
                    Toast.makeText(getActivity(),"Rc back pic not uploaded",Toast.LENGTH_SHORT).show();
                    return;
                }

                goToNextPage();

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
        if(!hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
            return;
        }


        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File file = new File(getActivity().getExternalCacheDir(), (System.currentTimeMillis()) + ".jpg");
                mFileUri = FileProvider.getUriForFile(getActivity(),"com.example.roadexp_transporter.provider",file);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
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
                    compressImage(bitmap);

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

    private void uploadPic(){

        Log.e(TAG,"called : uploadProfilePic");

        String URL = BASE_URL + "addVehicles";

        String imageParameter="";
        switch (flag){
            case 1 : imageParameter = "pic_v"; break;
            case 2 : imageParameter = "pic_rc_front"; break;
            case 3 : imageParameter = "pic_rc_back"; break;

            default: imageParameter = "";

        }


        try {
            mProgressDialog.show();
            new MultipartUploadRequest(getActivity(),URL)

                    .addFileToUpload(String.valueOf(mFileUri), imageParameter)
                    .addParameter("vehicle_num",mVehicleNum)
                    .addParameter("u_flag","0")

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
                                        case 1 : message = "Vehicle Pic uploded"; mVehiclePicUpload = true; break;
                                        case 2 : message = "Rc front pic uploaded"; mRcFrontUploaded = true;break;
                                        case 3 : message = "Rc back pic uploaded"; mRcBackUploaded   = true; break;
                                        default: message = "";
                                    }
                                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

                                    if(mVehiclePicUpload && mRcFrontUploaded && mRcBackUploaded)
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

    private void goToNextPage() {

        Bundle bundle = getArguments();

        AVFrag3 frag = new AVFrag3();
        frag.setArguments(bundle);

        replaceFragment(frag, "av_frag_3");
    }


    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment,tag);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void compressImage(Bitmap bitmap) {
        Log.e(TAG,"called : complressImage");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 18, bos);

        byte[] bitmapData = bos.toByteArray();

        try {
            //Compressed image is written in same previous image file
            FileOutputStream fos = new FileOutputStream(mImagePath);
            fos.write(bitmapData);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"Exception while converting bitmap to file, "+e.toString());
        }

    }



}
