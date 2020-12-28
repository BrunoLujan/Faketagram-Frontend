package com.example.faketagram_app.ui.profile;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faketagram_app.Constant;
import com.example.faketagram_app.EditProfile;
import com.example.faketagram_app.R;
import com.example.faketagram_app.UploadPhoto;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    View viewFragment;

    ImageView ivImageMyProfile;
    TextView txtNamesProfileFragment, txtStatusProfileFragment;
    Button btnAddPhoto, btnEditProfile;

    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        viewFragment=inflater.inflate(R.layout.fragment_my_profile,container,false);

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

        setUserOnProfile();
        return viewFragment;
    }

    public void setUserOnProfile() {
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
}