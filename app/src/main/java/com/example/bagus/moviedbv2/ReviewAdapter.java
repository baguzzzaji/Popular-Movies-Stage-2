package com.example.bagus.moviedbv2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private List<Review> reviews;
    private Context context;
    private int rowLayout;

    public ReviewAdapter(List<Review> reviews, Context context, int rowLayout) {
        this.reviews = reviews;
        this.context = context;
        this.rowLayout = rowLayout;
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        TextView reviewText;
        TextView reviewAuthor;

        public ReviewHolder(View itemView) {
            super(itemView);
            reviewText = (TextView) itemView.findViewById(R.id.review_text);
            reviewAuthor = (TextView) itemView.findViewById(R.id.review_author);
        }
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout, parent, false);

        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.reviewText.setText(review.getReviewContent());
        holder.reviewAuthor.setText(review.getReviewAuthor());
    }

    @Override
    public int getItemCount() {
        return (reviews == null ? 0 : reviews.size());
    }
}
