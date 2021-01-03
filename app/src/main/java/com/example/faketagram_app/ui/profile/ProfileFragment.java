package com.example.faketagram_app.ui.profile;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faketagram_app.Constant;
import com.example.faketagram_app.EditProfile;
import com.example.faketagram_app.ImageRvAdapter;
import com.example.faketagram_app.PhotographActivity;
import com.example.faketagram_app.R;
import com.example.faketagram_app.UploadPhoto;
import com.example.faketagram_app.UserProfileActivity;
import com.example.faketagram_app.UserRvAdapter;
import com.example.faketagram_app.model.Photographs;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    View viewFragment;
    RecyclerView rv;

    ImageView ivImageMyProfile;
    TextView txtNamesProfileFragment, txtStatusProfileFragment;
    Button btnAddPhoto, btnEditProfile;

    List<Photographs> photographsList;
    ImageRvAdapter adapter;

    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        viewFragment=inflater.inflate(R.layout.fragment_my_profile,container,false);

        rv = viewFragment.findViewById(R.id.rvMyProfileFragment);
        ivImageMyProfile = (ImageView) viewFragment.findViewById(R.id.ivImageMyProfile);
        txtNamesProfileFragment = (TextView) viewFragment.findViewById(R.id.txtNamesMyProfile);
        txtStatusProfileFragment = (TextView) viewFragment.findViewById(R.id.txtStatusMyProfile);

        btnAddPhoto= (Button) viewFragment.findViewById(R.id.btnAddPhoto);
        btnEditProfile= (Button) viewFragment.findViewById(R.id.btnEditProfile);

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),UploadPhoto.class);
                getActivity().startActivity(intent);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),EditProfile.class);
                getActivity().startActivity(intent);
            }
        });

        initializeMyProfile();
        initializeMyPhotos();
        return viewFragment;
    }

    private void initializeMyProfile() {
        if (Constant.LOGGEDUSER.getImage_storage_path() != null) {
            Picasso.get().load(Constant.PROFILEIMAGE + Constant.LOGGEDUSER.getImage_storage_path()).fit().into(ivImageMyProfile);
        }

        txtNamesProfileFragment.setText(Constant.LOGGEDUSER.getName() + " " + Constant.LOGGEDUSER.getLastname());

        if (Constant.LOGGEDUSER.getStatus() != null) {
            txtStatusProfileFragment.setText("'" + Constant.LOGGEDUSER.getStatus() + "'");
        } else {
            txtStatusProfileFragment.setText("");
        }
    }

    public void initializeMyPhotos() {
        Call<List<Photographs>> call = Constant.CONNECTION.getPhotographsByUserId(Constant.AUTHTOKEN, Constant.LOGGEDUSER.getUser_id());
        call.enqueue(new Callback<List<Photographs>>() {
            @Override
            public void onResponse(Call<List<Photographs>> call, Response<List<Photographs>> response) {
                if (response.isSuccessful()) {
                    photographsList = response.body();
                    buildRV();
                } else {
                    Constant.Message(getContext(),"Error-ProfileFragment-initializeMyPhotos-onResponse");
                }
            }

            @Override
            public void onFailure(Call<List<Photographs>> call, Throwable t) {
                Log.d("Error-ProfileFragment-initializeMyPhotos-onFailure: ", t.getMessage());
                Constant.Message(getContext(),"Error-ProfileFragment-initializeMyPhotos-onFailure");
            }
        });
    }

    private void buildRV() {
        GridLayoutManager glm = new GridLayoutManager(getContext(), 3);
        rv.setLayoutManager(glm);

        Intent intent = new Intent(getActivity(), PhotographActivity.class);
        adapter = new ImageRvAdapter(photographsList);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("photographSelectedId", photographsList.get
                        (rv.getChildAdapterPosition(v)).getPhotograph_id());
                intent.putExtra("photographSelectedPublishDate", photographsList.get
                        (rv.getChildAdapterPosition(v)).getPublish_date());
                intent.putExtra("photographSelectedImageStoragePath", photographsList.get
                        (rv.getChildAdapterPosition(v)).getImage_storage_path());
                intent.putExtra("photographUserId", photographsList.get
                        (rv.getChildAdapterPosition(v)).getUser_id());
                startActivity(intent);
            }
        });
        rv.setAdapter(adapter);
    }
}