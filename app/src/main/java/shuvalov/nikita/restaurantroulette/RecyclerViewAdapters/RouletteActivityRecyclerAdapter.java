package shuvalov.nikita.restaurantroulette.RecyclerViewAdapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import shuvalov.nikita.restaurantroulette.Activities.DetailActivity;
import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.PicassoImageManager;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.Randomizer;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Category;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Location;

/**
 * Created by justinwells on 12/4/16.
 */

public class RouletteActivityRecyclerAdapter extends RecyclerView.Adapter<RouletteResultViewHolder> {
    List<Business> mBusinessList;

    public RouletteActivityRecyclerAdapter(List<Business> businessList) {
        mBusinessList = businessList;
    }

    @Override
    public RouletteResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_roulette_item, null);
        return new RouletteResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RouletteResultViewHolder holder, final int position) {
        holder.bindDataToView(mBusinessList.get(position));



    }

    @Override
    public int getItemCount() {
        return mBusinessList.size();
    }
    public void replaceList(List<Business> newSearchResult){
        mBusinessList.clear();
        mBusinessList.addAll(newSearchResult);
    }
}

class RouletteResultViewHolder extends RecyclerView.ViewHolder{
    private TextView  mUberEstimate, mCost;
    private ImageView mStar1, mStar2, mStar3, mStar4, mStar5;


    public RouletteResultViewHolder(View itemView) {
        super(itemView);
        mUberEstimate = (TextView) itemView.findViewById(R.id.roulette_uber_estimate);
        mCost = (TextView) itemView.findViewById(R.id.roulette_price);
        mStar1 = (ImageView) itemView.findViewById(R.id.first_star_roulette);
        mStar2 = (ImageView) itemView.findViewById(R.id.second_star_roulette);
        mStar3 = (ImageView) itemView.findViewById(R.id.third_star_roulette);
        mStar4 = (ImageView) itemView.findViewById(R.id.fourth_star_roulette);
        mStar5 = (ImageView) itemView.findViewById(R.id.fifth_star_roulette);

    }

    public void bindDataToView(Business business){

        String uberEstimate = "Estimated Uber Cost: $13.50";
        mUberEstimate.setText(uberEstimate);
        mCost.setText(business.getPrice());
        setStars(business.getRating());
    }

    public void setStars(double rating){
        int stars = (int) rating * 2;

        switch (stars) {
            case 2 :
                mStar1.setImageResource(R.drawable.one_star_rating);
                break;
            case 3 :
                mStar1.setImageResource(R.drawable.one_star_rating);
                mStar2.setImageResource(R.drawable.one_half_star_rating);
                break;
            case 4 :
                mStar1.setImageResource(R.drawable.two_star_rating);
                mStar2.setImageResource(R.drawable.two_star_rating);
                break;
            case 5 :
                mStar1.setImageResource(R.drawable.two_star_rating);
                mStar2.setImageResource(R.drawable.two_star_rating);
                mStar3.setImageResource(R.drawable.two_half_star_rating);
                break;
            case 6 :
                mStar1.setImageResource(R.drawable.three_star_rating);
                mStar2.setImageResource(R.drawable.three_star_rating);
                mStar3.setImageResource(R.drawable.three_star_rating);
                break;
            case 7 :
                mStar1.setImageResource(R.drawable.three_star_rating);
                mStar2.setImageResource(R.drawable.three_star_rating);
                mStar3.setImageResource(R.drawable.three_star_rating);
                mStar4.setImageResource(R.drawable.three_half_star_rating);
                break;
            case 8 :
                mStar1.setImageResource(R.drawable.four_star_rating);
                mStar2.setImageResource(R.drawable.four_star_rating);
                mStar3.setImageResource(R.drawable.four_star_rating);
                mStar4.setImageResource(R.drawable.four_star_rating);
                break;
            case 9 :
                mStar1.setImageResource(R.drawable.four_star_rating);
                mStar2.setImageResource(R.drawable.four_star_rating);
                mStar3.setImageResource(R.drawable.four_star_rating);
                mStar4.setImageResource(R.drawable.four_star_rating);
                mStar5.setImageResource(R.drawable.four_half_star_rating);
                break;
            case 10 :
                mStar1.setImageResource(R.drawable.five_star_rating);
                mStar2.setImageResource(R.drawable.five_star_rating);
                mStar3.setImageResource(R.drawable.five_star_rating);
                mStar4.setImageResource(R.drawable.five_star_rating);
                mStar5.setImageResource(R.drawable.five_star_rating);
                break;


        }

    }
}