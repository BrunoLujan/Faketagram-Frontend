package com.example.faketagram_app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faketagram_app.model.Comments;
import com.example.faketagram_app.model.Users;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Comments> commentsList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvComment;
        TextView tvPublishDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImageCommentsListRow);
            tvComment = itemView.findViewById(R.id.tvCommentCommentsListRow);
            tvPublishDate = itemView.findViewById(R.id.tvPublishDateCommentsListRow);
        }
    }

    public CommentRvAdapter(List<Comments> comments) { this.commentsList = comments; }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_list_row, parent,
                false);

        CommentRvAdapter.ViewHolder vh = new CommentRvAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CommentRvAdapter.ViewHolder vh = (CommentRvAdapter.ViewHolder) holder;

        getUserImageProfile(vh, commentsList.get(position).getUser_id());

        vh.tvComment.setText(commentsList.get(position).getComment());
        vh.tvPublishDate.setText(commentsList.get(position).getPublish_date());

    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    private void getUserImageProfile(ViewHolder vh, int userId) {
        Call<Users> call = Constant.CONNECTION.getUserById(Constant.AUTHTOKEN, userId);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    if (response.body().getImage_storage_path() != null) {
                        Picasso.get().load(Constant.PROFILEIMAGE + response.body().getImage_storage_path()).fit().into(vh.ivImage);
                    }
                    Log.d("SI", response.message());
                } else {
                    Log.d("ERROR-CommentRvAdapter-getComments-onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.d("ERROR-CommentRvAdapter-getComments-onFailure", t.getMessage());
            }
        });
    }
}
