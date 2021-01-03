package com.example.faketagram_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.faketagram_app.model.Users;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    Intent intent;

    Users userSelected = new Users();

    ImageView ivImageUserProfile;
    TextView txtNamesUserProfile, txtStatusUSerProfile;
    Button btnFollow;
    int buttonCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ivImageUserProfile = (ImageView) findViewById(R.id.ivImageUserProfile);
        txtNamesUserProfile = (TextView) findViewById(R.id.txtNamesUserProfile);
        txtStatusUSerProfile = (TextView) findViewById(R.id.txtStatusUserProfile);

        btnFollow = (Button) findViewById(R.id.btnFollowUserProfile);

        initializeUserSelected();
        initializeUserProfile();
        validateFollow();

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonCount == 0) {
                    unfollowUser();
                } else {
                    followUser();
                }
            }
        });
    }

    private void initializeUserSelected() {
        intent = getIntent();
        userSelected.setUser_id(intent.getIntExtra("userSelectedId", 0));
        userSelected.setName(intent.getStringExtra("userSelectedName"));
        userSelected.setLastname(intent.getStringExtra("userSelectedLastName"));
        userSelected.setUsername(intent.getStringExtra("userSelectedUsername"));
        userSelected.setEmail(intent.getStringExtra("userSelectedEmail"));
        userSelected.setPassword(intent.getStringExtra("userSelectedPassword"));
        userSelected.setStatus(intent.getStringExtra("userSelectedStatus"));
        userSelected.setCellphone(intent.getStringExtra("userSelectedCellphone"));
        userSelected.setImage_storage_path(intent.getStringExtra("userSelectedImage"));
    }

    private void initializeUserProfile() {
        if (userSelected.getImage_storage_path() != null) {
            Picasso.get().load(Constant.PROFILEIMAGE + userSelected.getImage_storage_path()).fit().into(ivImageUserProfile);
        }

        txtNamesUserProfile.setText(userSelected.getName() + " " + userSelected.getLastname());
        
        if (userSelected.getStatus() != null) {
            txtStatusUSerProfile.setText("'" + userSelected.getStatus() + "'");
        } else {
            txtStatusUSerProfile.setText("");
        }
    }

    private void followUser() {
        Call<ResponseBody> call = Constant.CONNECTION.followUser(Constant.AUTHTOKEN, userSelected.getUser_id());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Constant.Message(getApplicationContext(), "Followed");
                    initializeUnfollowButton();
                } else  {
                    Constant.Message(getApplicationContext(), "Error, try again: " + response.message());
                    Log.d("ERROR-UserProfileActivity-followUser-onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Message(getApplicationContext(), t.getMessage());
                Log.d("ERROR USER PROFILE ACTIVITY", t.getMessage());
            }
        });
    }

    private void unfollowUser() {
        Call<ResponseBody> call = Constant.CONNECTION.unfollowUser(Constant.AUTHTOKEN, userSelected.getUser_id());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Constant.Message(getApplicationContext(), "Unfollowed");
                } else  {
                    Constant.Message(getApplicationContext(), "Error, try again: " + response.message());
                    Log.d("ERROR-UserProfileActivity-unfollowUser-onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Message(getApplicationContext(), t.getMessage());
                Log.d("ERROR USER PROFILE ACTIVITY", t.getMessage());
            }
        });
        initializeFollowButton();
        buttonCount = 1;
    }

    private void validateFollow() {
        Call<List<FollowResponse>> call = Constant.CONNECTION.getFollowings(Constant.AUTHTOKEN);
        call.enqueue(new Callback<List<FollowResponse>>() {
            @Override
            public void onResponse(Call<List<FollowResponse>> call, Response<List<FollowResponse>> response) {
                if (response.isSuccessful()) {
                    for(FollowResponse followAux : response.body()){
                        if(followAux.getUser_followed_id() == userSelected.getUser_id()) {
                            initializeUnfollowButton();
                        }
                    }
                } else {
                    Constant.Message(getApplicationContext(), "Error, try again: " + response.message());
                    Log.d("ERROR-UserProfileActivity-validateFollow-onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<FollowResponse>> call, Throwable t) {
                Constant.Message(getApplicationContext(), t.getMessage());
                Log.d("ERROR USER PROFILE ACTIVITY", t.getMessage());
            }
        });
    }

    private void initializeFollowButton() {
        btnFollow.setText("Follow");
        buttonCount = 1;
    }

    private void initializeUnfollowButton() {
        btnFollow.setText("Unfollow");
        buttonCount = 0;
    }
}