package com.example.faketagram_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faketagram_app.model.Users;

import java.util.List;

public class UserRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Users> users;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameUserListRow);
            tvUsername = itemView.findViewById(R.id.tvUsernameListRow);
        }
    }


    public UserRvAdapter(List<Users> users) {
        this.users = users;
    }


    @NonNull
    @Override
    public UserRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_row, parent,
                false);

        UserRvAdapter.ViewHolder vh = new UserRvAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserRvAdapter.ViewHolder vh = (UserRvAdapter.ViewHolder) holder;
        vh.tvName.setText(users.get(position).getName() + " " + users.get(position).getLastname());
        vh.tvUsername.setText("@" + users.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
