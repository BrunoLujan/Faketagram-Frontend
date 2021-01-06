package com.example.faketagram_app.ui.favourites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.faketagram_app.Constant;
import com.example.faketagram_app.FavouriteResponse;
import com.example.faketagram_app.FollowResponse;
import com.example.faketagram_app.ImageRvAdapter;
import com.example.faketagram_app.PhotographActivity;
import com.example.faketagram_app.R;
import com.example.faketagram_app.model.Photographs;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesFragment extends Fragment {

    View viewFragment;
    SwipeRefreshLayout srl;
    RecyclerView rv;

    List<Photographs> photographsList = new ArrayList<>();
    List<FavouriteResponse> favouriteResponsesList;
    ImageRvAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewFragment = inflater.inflate(R.layout.fragment_my_favourites, container, false);

        srl = viewFragment.findViewById(R.id.srlMyFavouritesFragment);
        rv = viewFragment.findViewById(R.id.rvMyFavouritesFragment);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                photographsList.clear();
                favouriteResponsesList.clear();
                getFavourites();
                srl.setRefreshing(false);
            }
        });

        getFavourites();

        return viewFragment;
    }

    public void getFavourites() {
        Call<List<FavouriteResponse>> call = Constant.CONNECTION.getFavourites(Constant.AUTHTOKEN);
        call.enqueue(new Callback<List<FavouriteResponse>>() {
            @Override
            public void onResponse(Call<List<FavouriteResponse>> call, Response<List<FavouriteResponse>> response) {
                if (response.isSuccessful()) {
                    favouriteResponsesList = response.body();
                    getPhotographs();
                } else {
                    Constant.Message(getContext(), "Error, try again: " + response.message());
                    Log.d("ERROR-FavouriteFragment-getFavourites-onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<FavouriteResponse>> call, Throwable t) {
                Constant.Message(getContext(), t.getMessage());
                Log.d("ERROR-FavouriteFragment-getFavourites-onFailure", t.getMessage());
            }
        });
    }

    public void getPhotographs() {
        for(FavouriteResponse favouriteAux : favouriteResponsesList){
            Call<Photographs> call = Constant.CONNECTION.getPhotographById(Constant.AUTHTOKEN, favouriteAux.getPhotograph_id());
            call.enqueue(new Callback<Photographs>() {
                @Override
                public void onResponse(Call<Photographs> call, Response<Photographs> response) {
                    if (response.isSuccessful()) {
                        photographsList.add(response.body());
                        buildRV();
                    } else {
                        Log.d("Error-FollowingFragment-getFollowings-onFailure: ", response.message());
                        Constant.Message(getContext(),"Error-FollowingFragment-getFollowings-onFailure: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Photographs> call, Throwable t) {
                    Log.d("Error-FavouriteFragment-getPhotographs-onFailure: ", t.getMessage());
                    Constant.Message(getContext(),"Error-FavouriteFragment-getPhotographs-onFailure: " + t.getMessage());
                }
            });
        }
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