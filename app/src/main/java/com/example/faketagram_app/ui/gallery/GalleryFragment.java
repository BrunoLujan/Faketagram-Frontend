package com.example.faketagram_app.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.faketagram_app.R;
import com.example.faketagram_app.UploadPhoto;

public class GalleryFragment extends Fragment {

    View vista;
    Button btnAddPhoto;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vista=inflater.inflate(R.layout.fragment_my_profile,container,false);
        btnAddPhoto= (Button) vista.findViewById(R.id.btnAddPhoto);

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),UploadPhoto.class);
                getActivity().startActivity(intent);
            }
        });
        return vista;
    }

}