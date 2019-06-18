package com.example.roadexp_transporter.AddNewDriver;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.roadexp_transporter.LoginSingUp.LoginSessionManager;
import com.example.roadexp_transporter.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


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
import java.io.IOException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;

public class ADFrag2 extends Fragment {

    private String TAG = "ADFrag2";
    private View mRoot;

    private ProgressDialog mProgressDialog;
    private AddDriverPage mActivity;

    //private static final int PICK_IMAGE = 100;

//    private final int CAMERA_REQUEST = 1888;
//    private final int MY_CAMERA_PERMISSION_CODE = 100;
//    private final int GALLARY_REQUEST = 1001;
//
//    String mCurrentPhotoPath;

    private  final int MY_CAMERA_PERMISSION_CODE = 100;
    private  final int CAMERA_REQUEST = 1888;
    private String mImagePath = null;
    private Uri mFileUri;

    private String mMobile;

    public ADFrag2() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AddDriverPage) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot =  inflater.inflate(R.layout.adfrag2, container, false);
        mActivity.translateBoy(1);

        Bundle bundle = getArguments();

//        String name = bundle.getString("name");
//        String state = bundle.getString("state");
//        String city = bundle.getString("city");
//        String birthday = bundle.getString("birthday");
        mMobile = bundle.getString("mobile");
//        String account = bundle.getString("account");
//        String password = bundle.getString("password");

        //Log.e("asd=",name+" "+state+" "+city+" "+birthday+" "+mMobile+" "+account+" "+password);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Please wait..");
        mProgressDialog.setCancelable(false);

        clickListener();
        return mRoot;
    }

    private void clickListener() {

        mRoot.findViewById(R.id.fard_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadProfilePicAndData();
//                replaceFragment(new ADFrag3(), "ad_frag_3");
            }
        });


        mRoot.findViewById(R.id.upload_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openDialog();
                openCamera();
            }
        });

        mRoot.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


    }

    private void openDialog() {

        final android.support.v7.app.AlertDialog alertDialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity(),android.
                    R.style.Theme_DeviceDefault_Light_Dialog_MinWidth).create();
        } else {
            alertDialog = new AlertDialog.Builder(getActivity()).create();
        }

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.alert_box_choose_pic,null);

        alertDialog.setView(view);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        view.findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openGallery();
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        alertDialog.show();
    }

    private void openCamera() {

        Log.e(TAG,"onCameraClick");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
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

                    ImageView imageView = mRoot.findViewById(R.id.upload_image);
                    imageView.setImageBitmap(bitmap);

                    Log.e(TAG, "Actual path : " + actualPath.toString());

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }

                uploadProfilePic();

            }
        }
    }


    private void uploadProfilePic() {

        Log.e(TAG,"called : uploadProfilePic");

        String URL = BASE_URL + "addDriver";

        Log.e(TAG,mMobile+" "+mFileUri);

        try {
            mProgressDialog.show();
            new MultipartUploadRequest(getActivity(),URL)

                    .addFileToUpload(String.valueOf(mFileUri), "prof_pic")
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
                           // Toast.makeText(getActivity(),serverResponse.getBodyAsString(),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            mProgressDialog.dismiss();

                            Log.e(TAG,serverResponse.getBodyAsString()+"");


                            try {
                               int code =  new JSONObject(serverResponse.getBodyAsString()).getInt("code");
                               if(code==1){
                                   Toast.makeText(getActivity(),"Profile pic uploaded",Toast.LENGTH_SHORT).show();
                                   replaceFragment(new ADFrag3(), "ad_frag_3");
                               }
                            } catch (JSONException e) {
                                e.printStackTrace();
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









    private void replaceFragment(Fragment fragment, String tag) {

        Bundle bundle = getArguments();

//        String name     = bundle.getString("name");
//        String state    = bundle.getString("state");
//        String city     = bundle.getString("city");
//        String birthday = bundle.getString("birthday");
//        String mobile   = bundle.getString("mobile");
//        String account  = bundle.getString("account");
//        String password = bundle.getString("password");


        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment,tag);
        //ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.addToBackStack(null);
        ft.commit();
    }













































//
////    private void uploadProfilePicAndData(){
////
////        mProgressDialog.show();
////        Log.e(TAG,"called : uploadProfilePicAndData");
////
////        String name     = getArguments().getString("name");
////        String state    = getArguments().getString("state");
////        String city     = getArguments().getString("city");
////        String birthday = getArguments().getString("birthday");
////        String mobile   = getArguments().getString("mobile");
////        String account  = getArguments().getString("account");
////        String password = getArguments().getString("password");
////
////        Log.e(TAG, name+" "+state+" " + city +" "+ birthday + " "+mobile + " "+account + " "+password );
////
////        final String URL = BASE_URL + "addDriver";
////
////
////        try {
////            new MultipartUploadRequest(getActivity(),URL)
////
////                    .addFileToUpload(mCurrentPhotoPath,"prof_pic")
////
////                    .addParameter("phn","1234564562")
////
////                    .setNotificationConfig(new UploadNotificationConfig())
////                    .setMaxRetries(2)
////                    .setDelegate(new UploadStatusDelegate() {
////                        @Override
////                        public void onProgress(Context context, UploadInfo uploadInfo) {}
////
////                        @Override
////                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
////                            mProgressDialog.dismiss();
////                            Log.e("TAG",serverResponse.getBodyAsString());
////                            if(exception!=null)
////                                Log.e("TAG",exception.toString());
////                            Toast.makeText(getActivity(),"Error uploading",Toast.LENGTH_SHORT).show();
////                        }
////
////                        @Override
////                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
////                            Log.e("TAG",serverResponse.getBodyAsString());
////                            mProgressDialog.dismiss();
////                            Toast.makeText(getActivity(),"Done",Toast.LENGTH_SHORT).show();
////                        }
////
////                        @Override
////                        public void onCancelled(Context context, UploadInfo uploadInfo) {
////                            mProgressDialog.dismiss();
////                        }
////                    }).startUpload();
////
////        } catch (Exception e) {
////            e.printStackTrace();
////            Log.e(TAG,e.toString());
////        }
////    }
//
//    private void openGallery() {
//        Intent gallery = new Intent(Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(gallery,GALLARY_REQUEST);
//    }
//
//
//
//    private void openCamera() {
//
//        Log.e(TAG,"onCameraClick");
//
//        if (checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.CAMERA},
//                    MY_CAMERA_PERMISSION_CODE);
//        }
//        else if (checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
//        }
//
//        else {
//
//            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//                File photoFile = null;
//                try {
//                    photoFile = createImageFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e(TAG,"file for image is not created");
//                }
//                if(photoFile!=null){
//                    Uri fileUri = FileProvider.getUriForFile(getActivity(),"com.example.roadexp_transporter.provider",photoFile);
//                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
//                }
//
//            }
//        }
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        ImageView imageView = mRoot.findViewById(R.id.upload_image);
//
//        switch (requestCode) {
//
////            case CAMERA_REQUEST:
////                if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
////
////                    ImageView imageView = mRoot.findViewById(R.id.camera);
////                    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
////                    compressImage(bitmap);
////                    Glide.with(getActivity())
////                            .load(mCurrentPhotoPath)
////                            .into(imageView);
////                }
//
//
//            case GALLARY_REQUEST:
//                if(requestCode == GALLARY_REQUEST && resultCode == RESULT_OK && data != null) {
//                    Uri imageUri = data.getData();
//
//                    CropImage.activity(imageUri)
//                            .setGuidelines(CropImageView.Guidelines.ON)
//                            .start(getActivity());
//                }
//
//                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                    if (resultCode == RESULT_OK) {
//                        Uri resultUri = result.getUri();
//                        try {
//                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),resultUri);
//                                Log.e(TAG,"bit="+bitmap);
//                                imageView.setImageBitmap(bitmap);
//
//                                String imageName = "1245pan"+".jpg";
//
////                        File file = new File(AddAccountDetails.this.getExternalCacheDir(),imageName);
////                        mFilePanUri = FileProvider.getUriForFile(AddAccountDetails.this,"com.example.mani.mekparkpartner.provider",file);
////                        Log.e(TAG,mFilePanUri.toString());
//
////                                mPanUri = getImageUri(mPanBitmap,imageName);
////                                mPanRealPath = getRealPathFromURI(mPanUri);
//
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                        Exception error = result.getError();
//                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//        }
//    }
//
//
//
//
//
//    private File createImageFile() throws IOException {
//
//        String imageName = "profile_pic"+".jpg";
//        File imageFile    = new File(getActivity().getExternalCacheDir(), imageName);
//
//        mCurrentPhotoPath = imageFile.getAbsolutePath();
//        Log.e(TAG,"currentPhotoPath = "+mCurrentPhotoPath);
//        Log.e(TAG,"imageName = "+imageName);
//        return imageFile;
//    }
//
//    private void compressImage(Bitmap bitmap) {
//
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 18, bos);
//
//        byte[] bitmapData = bos.toByteArray();
//
//        try {
//            //Compressed image is written in same previous image file
//            FileOutputStream fos = new FileOutputStream(mCurrentPhotoPath);
//            fos.write(bitmapData);
//            fos.flush();
//            fos.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG,"Exception while converting bitmap to file, "+e.toString());
//        }
//
//    }
}
