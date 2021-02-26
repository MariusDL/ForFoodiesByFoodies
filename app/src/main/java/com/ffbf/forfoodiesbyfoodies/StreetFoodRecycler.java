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

public class StreetFoodRecycler extends RecyclerView.Adapter<StreetFoodRecycler.ViewHolder>{

    private static Context context;
    private List<StreetFoodObject> streetFoodList;
    private static StreetFoodRecycler.ViewHolder.ClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView street_food_image;
        public RatingBar street_food_rating;
        public TextView street_food_name;
        public TextView street_food_location;

        public ViewHolder(@NonNull View view, Context ctx ){
            super(view);
            context = ctx;
            street_food_name = view.findViewById(R.id.restaurant_row_name);
            street_food_location = view.findViewById(R.id.restaurant_row_location);
            street_food_rating = view.findViewById(R.id.restaurant_row_rating);
            street_food_image = view.findViewById(R.id.restaurant_row_image);
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
    public StreetFoodRecycler(Context context, List<StreetFoodObject> streetFoodList){
        this.context = context;
        this.streetFoodList = streetFoodList;
    }
    public void setOnItemClickListener(ViewHolder.ClickListener clickListener) {
        StreetFoodRecycler.clickListener = clickListener;
    }
    @NonNull
    @Override
    public StreetFoodRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_row,parent,false);
        return new ViewHolder(view, context);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        StreetFoodObject streetFood = streetFoodList.get(position);
        String imageUrl = null;

        holder.street_food_name.setText(streetFood.getSf_name());
        holder.street_food_location.setText(streetFood.getSf_location());

        if(streetFood.getRating_number()>0){
            holder.street_food_rating.setRating(streetFood.getTotal_rating()/streetFood.getRating_number());
        } else {
            holder.street_food_rating.setRating(0);
        }

        imageUrl = streetFood.getImg();

        if(!imageUrl.isEmpty()){
            Picasso.get().load(imageUrl).into(holder.street_food_image);
        } else {
            holder.street_food_image.setImageResource(R.drawable.add_location_image);
        }
    }

    @Override
    public int getItemCount() {
        return streetFoodList.size();
    }
}
