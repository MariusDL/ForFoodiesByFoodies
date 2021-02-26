package com.ffbf.forfoodiesbyfoodies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class UserRecycler extends RecyclerView.Adapter<UserRecycler.ViewHolder>{


    private static Context context;
    private List<User> userList;
    private static ViewHolder.ClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textView;
        public ImageView imageView;

        public ViewHolder(@NonNull View view, Context ctx ) {
            super(view);
            context = ctx;

            textView = view.findViewById(R.id.user_row_name);
            imageView = view.findViewById(R.id.user_row_image);

            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(),view);
        }
        public interface ClickListener{
            void onItemClick(int position, View v);
        }
    }
    public UserRecycler(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    public void setOnItemClickListener(ViewHolder.ClickListener clickListener){
        UserRecycler.clickListener = clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row,parent,false);
        return new ViewHolder(view, context);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = userList.get(position);
        String imageUrl = null;

        holder.textView.setText(user.getFirst_name()+" " + user.getLast_name());
        imageUrl = user.getProfile_image();
        if(!imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.default_profile_photo);
        }
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }

}
