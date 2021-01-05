package com.example.faketagram_app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faketagram_app.Constant;
import com.example.faketagram_app.FeedRvAdapter;
import com.example.faketagram_app.ImageRvAdapter;
import com.example.faketagram_app.PhotographActivity;
import com.example.faketagram_app.R;
import com.example.faketagram_app.UserRvAdapter;
import com.example.faketagram_app.model.Photographs;
import com.example.faketagram_app.model.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    View viewFragment;
    RecyclerView rv;
    FeedRvAdapter adapter;

    List<Photographs> photographsList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewFragment = inflater.inflate(R.layout.fragment_home, container, false);
        rv = viewFragment.findViewById(R.id.rvHomeFragment);

        getFeedPhotographs();

        return viewFragment;
    }

    public void getFeedPhotographs() {
        Call<List<Photographs>> call = Constant.CONNECTION.getFeedPhotographs(Constant.AUTHTOKEN);
        call.enqueue(new Callback<List<Photographs>>() {
            @Override
            public void onResponse(Call<List<Photographs>> call, Response<List<Photographs>> response) {
                if (response.isSuccessful()) {
                    photographsList = response.body();
                    buildRV();
                } else {
                    Log.d("Error-HomeFragment-getFeedPhotographs-onFailure: ", response.message());
                    Constant.Message(getContext(),"Error-HomeFragment-getFeedPhotographs-onFailure");
                }
            }

            @Override
            public void onFailure(Call<List<Photographs>> call, Throwable t) {
                Log.d("Error-HomeFragment-getFeedPhotographs-onFailure: ", t.getMessage());
                Constant.Message(getContext(),"Error-HomeFragment-getFeedPhotographs-onFailure");
            }
        });
    }

    public void buildRV() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        Intent intent = new Intent(getActivity(), PhotographActivity.class);

        adapter = new FeedRvAdapter(photographsList);
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