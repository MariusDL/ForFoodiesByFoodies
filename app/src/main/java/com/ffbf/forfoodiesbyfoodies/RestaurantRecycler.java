package com.ffbf.forfoodiesbyfoodies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantRecycler extends RecyclerView.Adapter<RestaurantRecycler.ViewHolder>{
    private static Context context;
    private List<RestaurantObject> restaurantList;
    private static RestaurantRecycler.ViewHolder.ClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView restaurantName, restaurantLocation;
        public ImageView imageView;
        public RatingBar ratingBar;

        public ViewHolder(@NonNull View view, Context ctx ){
            super(view);
            context = ctx;
            restaurantName = view.findViewById(R.id.restaurant_row_name);
            restaurantLocation = view.findViewById(R.id.restaurant_row_location);
            ratingBar = view.findViewById(R.id.restaurant_row_rating);
            imageView = view.findViewById(R.id.restaurant_row_image);
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
    public RestaurantRecycler(Context context, List<RestaurantObject> restaurantList){
        this.context = context;
        this.restaurantList = restaurantList;
    }

    public void setOnItemClickListener(ViewHolder.ClickListener clickListener) {
        RestaurantRecycler.clickListener = clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_row,parent,false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RestaurantObject restaurant = restaurantList.get(position);
        String imageUrl = null;
        holder.restaurantName.setText( restaurant.getR_name());
        holder.restaurantLocation.setText(restaurant.getR_location());
        if(restaurant.getRating_number()>0) {
            holder.ratingBar.setRating(restaurant.getTotal_rating() / restaurant.getRating_number());
        } else {
            holder.ratingBar.setRating(0);
        }
        imageUrl = restaurant.getImg();
        if(!imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.add_location_image);
        }
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}