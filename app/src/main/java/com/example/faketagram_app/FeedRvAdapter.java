package com.example.faketagram_app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faketagram_app.model.Photographs;
import com.example.faketagram_app.model.Users;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {

    List<Photographs> photographsList;
    View.OnClickListener clickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserProfileImage, ivImage;
        TextView tvNamesAndUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserProfileImage = itemView.findViewById(R.id.ivUserProfileImageFeedListRow);
            ivImage = itemView.findViewById(R.id.ivImageFeedListRow);
            tvNamesAndUsername = itemView.findViewById(R.id.tvNamesAndUsernameFeedListRow);
        }
    }

    public FeedRvAdapter(List<Photographs> photographs) {
        this.photographsList = photographs;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list_row, parent,
                false);

        FeedRvAdapter.ViewHolder vh = new FeedRvAdapter.ViewHolder(v);
        v.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FeedRvAdapter.ViewHolder vh = (FeedRvAdapter.ViewHolder) holder;

        getUserById(vh, photographsList.get(position).getUser_id());

        if (photographsList.get(position).getImage_storage_path() != null) {
            Picasso.get().load(Constant.PROFILEIMAGE + photographsList.get(position).getImage_storage_path()).fit().into(vh.ivImage);
        }

    }

    public void getUserById(ViewHolder vh, int userId) {
        Call<Users> call = Constant.CONNECTION.getUserById(Constant.AUTHTOKEN, userId);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    if (response.body().getImage_storage_path() != null) {
                        Picasso.get().load(Constant.PROFILEIMAGE + response.body().getImage_storage_path()).fit().into(vh.ivUserProfileImage);
                    }
                    vh.tvNamesAndUsername.setText(response.body().getName() + " " +
                            response.body().getLastname() + " - @" + response.body().getUsername());
                } else {
                    Log.d("ERROR-FeedRvAdapter-getUserById-onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.d("ERROR-FeedRvAdapter-getUserById-onFailure", t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return photographsList.size();
    }

    @Override
    public void onClick(View v) {
        if(clickListener != null) {
            clickListener.onClick(v);
        }
    }

    public void setOnClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
