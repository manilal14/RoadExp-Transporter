package com.example.roadexp_transporter.AddNewDriver;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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


import com.bumptech.glide.Glide;
import com.example.roadexp_transporter.R;



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

    private  final int CAMERA_REQUEST = 1888;
    private  final int MY_CAMERA_PERMISSION_CODE = 100;

    String mCurrentPhotoPath;

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

        mProgressDialog = new ProgressDialog(getActivity());

        clickListener();
        return mRoot;
    }

    private void clickListener() {

        mRoot.findViewById(R.id.fard_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadProfilePicAndData();

                replaceFragment(new ADFrag3(), "ad_frag_3");
            }
        });


        mRoot.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment,tag);
        //ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.addToBackStack(null);
        ft.commit();
    }


//    private void uploadProfilePicAndData(){
//
//        mProgressDialog.show();
//        Log.e(TAG,"called : uploadProfilePicAndData");
//
//        String name     = getArguments().getString("name");
//        String state    = getArguments().getString("state");
//        String city     = getArguments().getString("city");
//        String birthday = getArguments().getString("birthday");
//        String mobile   = getArguments().getString("mobile");
//        String account  = getArguments().getString("account");
//        String password = getArguments().getString("password");
//
//        Log.e(TAG, name+" "+state+" " + city +" "+ birthday + " "+mobile + " "+account + " "+password );
//
//        final String URL = BASE_URL + "addDriver";
//
//
//        try {
//            new MultipartUploadRequest(getActivity(),URL)
//
//                    .addFileToUpload(mCurrentPhotoPath,"prof_pic")
//
//                    .addParameter("phn","1234564562")
//
//                    .setNotificationConfig(new UploadNotificationConfig())
//                    .setMaxRetries(2)
//                    .setDelegate(new UploadStatusDelegate() {
//                        @Override
//                        public void onProgress(Context context, UploadInfo uploadInfo) {}
//
//                        @Override
//                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
//                            mProgressDialog.dismiss();
//                            Log.e("TAG",serverResponse.getBodyAsString());
//                            if(exception!=null)
//                                Log.e("TAG",exception.toString());
//                            Toast.makeText(getActivity(),"Error uploading",Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
//                            Log.e("TAG",serverResponse.getBodyAsString());
//                            mProgressDialog.dismiss();
//                            Toast.makeText(getActivity(),"Done",Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onCancelled(Context context, UploadInfo uploadInfo) {
//                            mProgressDialog.dismiss();
//                        }
//                    }).startUpload();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG,e.toString());
//        }
//    }

//    private void openGallery() {
//        Intent gallery = new Intent(Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(gallery, PICK_IMAGE);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case CAMERA_REQUEST:
                if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

                    ImageView imageView = mRoot.findViewById(R.id.camera);
                    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                    compressImage(bitmap);
                    Glide.with(getActivity())
                            .load(mCurrentPhotoPath)
                            .into(imageView);


                }


//        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
//
//            Uri imageUri = data.getData();
//            Log.e(TAG,imageUri.toString());
//
//            ImageView imageView = mRoot.findViewById(R.id.folder);
//
//            imageView.setImageURI(imageUri);
//        }
        }
    }


    private void openCamera() {

        Log.e(TAG,"onCameraClick");

        if (checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE);
        }
        else if (checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG,"file for image is not created");
                }
                if(photoFile!=null){
                    Uri fileUri = FileProvider.getUriForFile(getActivity(),"com.example.roadexp_transporter.provider",photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

            }
        }
    }




    private File createImageFile() throws IOException {

        String imageName = "profile_pic"+".jpg";
        File imageFile    = new File(getActivity().getExternalCacheDir(), imageName);

        mCurrentPhotoPath = imageFile.getAbsolutePath();
        Log.e(TAG,"currentPhotoPath = "+mCurrentPhotoPath);
        Log.e(TAG,"imageName = "+imageName);
        return imageFile;
    }

    private void compressImage(Bitmap bitmap) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 18, bos);

        byte[] bitmapData = bos.toByteArray();

        try {
            //Compressed image is written in same previous image file
            FileOutputStream fos = new FileOutputStream(mCurrentPhotoPath);
            fos.write(bitmapData);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"Exception while converting bitmap to file, "+e.toString());
        }

    }
}
