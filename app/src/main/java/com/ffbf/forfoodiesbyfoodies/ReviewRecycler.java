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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import butterknife.ButterKnife;


public class ReviewRecycler extends RecyclerView.Adapter<ReviewRecycler.ViewHolder> {

    private static Context context;
    private List<ReviewObject> reviews_list;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    ReviewObject reviewObject;
    User user;



    OnButtonClickListeners onButtonClickListeners;

    public ReviewRecycler(Context context, List<ReviewObject> reviews_list){
        this.context = context;
        this.reviews_list = reviews_list;
    }

    public void setOnButtonClickListeners(OnButtonClickListeners listener){
        this.onButtonClickListeners = listener;
    }

    @NonNull
    @Override
    public ReviewRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ReviewObject reviewObject = reviews_list.get(position);
        holder.userEmail.setText(reviewObject.getUserEmail());
        holder.reviewText.setText(reviewObject.getReviewText());
        holder.ratingBar.setRating(reviewObject.getRestaurantGivenStars());
    }
    @Override
    public int getItemCount() {
        return reviews_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView userEmail, reviewText;
        public RatingBar ratingBar;
        public ImageView thanksButton;
        public ViewHolder(@NonNull View view){
            super(view);
            userEmail = view.findViewById(R.id.review_row_user_email);
            reviewText = view.findViewById(R.id.review_row_review_text);
            ratingBar = view.findViewById(R.id.review_row_rating_bar);
            thanksButton = view.findViewById(R.id.review_row_like);
            ButterKnife.bind(this,view);
            userEmail.setOnClickListener(this);
            thanksButton.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.review_row_user_email:
                    onButtonClickListeners.onEmailClick(getAdapterPosition());
                    break;
                case R.id.review_row_like:
                    onButtonClickListeners.onVoteButtonClick(getAdapterPosition());
                    break;
            }
        }
    }
    public interface OnButtonClickListeners{
        void onEmailClick(int position);
        void onVoteButtonClick(int position);
    }
}