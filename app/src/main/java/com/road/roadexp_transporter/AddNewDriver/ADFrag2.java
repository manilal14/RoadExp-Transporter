package com.road.roadexp_transporter.AddNewDriver;


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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;


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

import static android.app.Activity.RESULT_CANCELED;

import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.hasPermissions;

public class ADFrag2 extends Fragment {

    private String TAG = "ADFrag2";
    private View mRoot;

    private ProgressDialog mProgressDialog;
    private AddDriverPage mActivity;

    private final int PERMISSION_ALL = 1;
    private  final int CAMERA_REQUEST = 1888;

    private String mMobile;

    private String currentPhotoPath;

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
        mMobile = bundle.getString("mobile");

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



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.e("asd","called");
    }

    //On camera result
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        Log.e(TAG,"onActivityResult : called");

        Log.e(TAG,"onActivityResult : called");

        if(requestCode != RESULT_CANCELED) {

            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                String compressedPath = compressImage(currentPhotoPath);
                uploadProfilePic(compressedPath);

            }
        }

    }


    private void uploadProfilePic(String imagePath) {

        Log.e(TAG,"called : uploadProfilePic");

        String URL = BASE_URL + "addDriver";


        try {
            mProgressDialog.show();
            new MultipartUploadRequest(getActivity(),URL)

                    .addFileToUpload(imagePath, "prof_pic")
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
                            Toast.makeText(getActivity(),"Error occured, please upload once again", Toast.LENGTH_SHORT).show();
                            ImageView imageView = mRoot.findViewById(R.id.upload_image);
                            imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.camera));
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

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment,tag);
        ft.addToBackStack(null);
        ft.commit();
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
            bmp = BitmapFactory.decodeFile(filePath, options);

            ImageView imageView = mRoot.findViewById(R.id.upload_image);
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
