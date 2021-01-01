package com.example.faketagram_app;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.faketagram_app.model.Users;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {

    ImageView ivImageEditProfile;
    EditText txtStatusEditProfile;
    Button btnSelectFileEditProfile, btnSaveChangesEditProfile;

    Uri imageFileURI = null;

    int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ivImageEditProfile = (ImageView) findViewById(R.id.ivImageEditProfile);

        if(Constant.LOGGEDUSER.getImage_storage_path() != null ){
            Picasso.get().load(Constant.PROFILEIMAGE + Constant.LOGGEDUSER.getImage_storage_path()).fit().into(ivImageEditProfile);
        }

        txtStatusEditProfile = (EditText) findViewById(R.id.txtStatusEditProfile);
        btnSelectFileEditProfile = (Button) findViewById(R.id.btnSelectFileEditProfile);
        btnSaveChangesEditProfile = (Button) findViewById(R.id.btnSaveChangesEditProfile);

        if (Constant.LOGGEDUSER.getStatus() != null) {
            txtStatusEditProfile.setText(Constant.LOGGEDUSER.getStatus());
        } else {
            txtStatusEditProfile.setText("Click here and insert text");
        }

        btnSelectFileEditProfile.setOnClickListener(new View.OnClickListener() {
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

        btnSaveChangesEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        Constant.Message(getApplicationContext(), "You must login again to apply changes");
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
            ivImageEditProfile.setImageURI(imageFileURI);
        }
    }

    public void saveChanges() {
        uploadProfilePhoto();

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

    public void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(EditProfile.this,
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

    public void uploadProfilePhoto(){
        if (imageFileURI != null){
            File imageFile = new File(getRealPathFromURI(imageFileURI));
            RequestBody requestBody = RequestBody.create(MediaType.parse("images/jpeg"), imageFile);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("image_storage_path", imageFile.getName(), requestBody);

            Call<ResponseBody> call = Constant.CONNECTION.uploadProfilePhoto(Constant.AUTHTOKEN,filePart);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        updateStatus();
                    } else {
                        Constant.Message(getApplicationContext(), "Error, try again: " + response.message());
                        Log.d("ERROR-EditProfile-uploadProfilePhoto-onResponse", response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Constant.Message(getApplicationContext(), t.getMessage());
                    Log.d("ERROR EDIT PROFILE", t.getMessage());
                }
            });
        } else {
            updateStatus();
        }
    }

    public void updateStatus() {
        StatusRequest status = new StatusRequest();
        status.setStatus(txtStatusEditProfile.getText().toString());
        Call<ResponseBody> call = Constant.CONNECTION.updateStatus(Constant.AUTHTOKEN, status);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Constant.Message(getApplicationContext(), "New changes has been upload");
                    logout();
                } else {
                    Constant.Message(getApplicationContext(), "Error, try again: " + response.message());
                    Log.d("ERROR-EditProfile-updateStatus-onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Message(getApplicationContext(), t.getMessage());
                Log.d("ERROR EDIT PROFILE", t.getMessage());
            }
        });
    }

    public void logout() {
        Call<ResponseBody> call = Constant.CONNECTION.logout(Constant.AUTHTOKEN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Intent intent = new Intent(EditProfile.this, SignIn.class);
                    Constant.AUTHTOKEN = null;
                    Constant.LOGGEDUSER = null;
                    startActivity(intent);
                } else {
                    Constant.Message(getApplicationContext(), "Error, try again: " + response.message());
                    Log.d("ERROR-EditProfile-logout", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Message(getApplicationContext(), t.getMessage());
                Log.d("ERROR EDIT PROFILE", t.getMessage());
            }
        });
    }
}

