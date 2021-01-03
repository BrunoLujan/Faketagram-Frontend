package com.example.faketagram_app;

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

public class ImageRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {

    List<Photographs> photographs;
    View.OnClickListener clickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImageImageListRow);
        }
    }

    public ImageRvAdapter(List<Photographs> users) {
        this.photographs = users;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_row, parent,
                false);

        ImageRvAdapter.ViewHolder vh = new ImageRvAdapter.ViewHolder(v);
        v.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageRvAdapter.ViewHolder vh = (ImageRvAdapter.ViewHolder) holder;

        if (photographs.get(position).getImage_storage_path() != null) {
            Picasso.get().load(Constant.PROFILEIMAGE + photographs.get(position).getImage_storage_path()).fit().into(vh.ivImage);
        }
    }

    @Override
    public int getItemCount() {
        return photographs.size();
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
