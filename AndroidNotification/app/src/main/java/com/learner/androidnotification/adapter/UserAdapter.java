package com.learner.androidnotification;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private List<User> userList = new ArrayList<>();
    private ItemClickListener itemClickListener;

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext().getApplicationContext())
                .inflate(R.layout.row_item_user, viewGroup, false);
        return new UserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder userHolder, int i) {
        userHolder.bindTo(userList.get(i));
    }

    public void setUserList(@NonNull List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView emailTextView;

        UserHolder(@NonNull View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.user_email_text_view);
            itemView.setOnClickListener(this);
        }

        void bindTo(User user) {
            emailTextView.setText(user.getEmail());
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.itemClick(userList.get(getAdapterPosition()));
        }
    }

    public interface ItemClickListener {
        void itemClick(User user);
    }
}
