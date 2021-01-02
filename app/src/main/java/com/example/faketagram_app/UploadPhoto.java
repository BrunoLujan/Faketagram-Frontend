package com.example.faketagram_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.faketagram_app.ui.profile.ProfileFragment;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadPhoto extends AppCompatActivity {

    ImageView ivImageUploadPhoto;
    Button btnSelectFileUploadPhoto, btnUploadPhoto;

    Uri imageFileURI = null;

    int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        ivImageUploadPhoto = (ImageView) findViewById(R.id.ivImageUploadPhoto);
        btnSelectFileUploadPhoto = (Button) findViewById(R.id.btnSelectFileUploadPhoto);
        btnUploadPhoto = (Button) findViewById(R.id.btnUploadPhoto);

        btnSelectFileUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    showSelectedImage();
                } else {
                    requestStoragePermission();
                }
            }
        });

        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });
    }

    public void showSelectedImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/jpeg");
        startActivityForResult(intent.createChooser(intent, "Select an application"),10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imageFileURI = data.getData();
            ivImageUploadPhoto.setImageURI(imageFileURI);
        }
    }

    public void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(UploadPhoto.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constant.Message(getApplicationContext(), "Permission GRANTED");
            } else {
                Constant.Message(getApplicationContext(), "Permission DENIED");
            }
        }
    }

    private void uploadPhoto () {
        if (imageFileURI != null){
            File imageFile = new File(getRealPathFromURI(imageFileURI));
            RequestBody requestBody = RequestBody.create(MediaType.parse("images/jpeg"), imageFile);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("image_storage_path", imageFile.getName(), requestBody);

            Call<ResponseBody> call = Constant.CONNECTION.uploadFeedPhoto(Constant.AUTHTOKEN, filePart);
            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        Intent intent = new Intent(UploadPhoto.this, MainActivity.class);
                        Constant.Message(getApplicationContext(), "Successfully uploaded photo");
                        startActivity(intent);
                    } else {
                        Constant.Message(getApplicationContext(), "Error, try again: " + response.message());
                        Log.d("ERROR-UploadPhoto-uploadPhoto-onResponse", response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Constant.Message(getApplicationContext(), t.getMessage());
                    Log.d("ERROR UPLOAD PHOTO PROFILE", t.getMessage());
                }
            });
        } else {
            Constant.Message(getApplicationContext(), "Select a file");
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String result;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}