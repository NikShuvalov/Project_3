package shuvalov.nikita.restaurantroulette.RecyclerViewAdapters;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import shuvalov.nikita.restaurantroulette.Activities.MapsActivity;
import shuvalov.nikita.restaurantroulette.Activities.RouletteActivity;
import shuvalov.nikita.restaurantroulette.GoogleResources.GoogleAPI;
import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.RouletteHelper;
import shuvalov.nikita.restaurantroulette.UberResources.UberAPI;
import shuvalov.nikita.restaurantroulette.UberResources.UberAPIConstants;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Coordinates;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Location;

import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LAT;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LOCATION;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LON;

/**
 * Created by justinwells on 12/4/16.
 */

public class RouletteActivityRecyclerAdapter extends RecyclerView.Adapter<RouletteResultViewHolder> {
    List<Business> mBusinessList;
    String mLat, mLon;

    public RouletteActivityRecyclerAdapter(List<Business> businessList, String userLat, String userLon) {
        mBusinessList = businessList;
        mLat = userLat;
        mLon = userLon;
    }

    @Override
    public RouletteResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_roulette_item, null);
        return new RouletteResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RouletteResultViewHolder holder, final int position) {
        holder.bindDataToView(mBusinessList.get(position), mLat, mLon);
    }

    @Override
    public int getItemCount() {
        return mBusinessList.size();
    }
    public void replaceList(List<Business> newSearchResult){
        mBusinessList.clear();
        mBusinessList.addAll(newSearchResult);
    }

    public void clearList () {
        mBusinessList.clear();
    }
}

class RouletteResultViewHolder extends RecyclerView.ViewHolder{
    private CardView mRouletteCard;
    private TextView  mUberEstimate, mCost, mAddress;
    private ImageView mStar1, mStar2, mStar3, mStar4, mStar5;


    public RouletteResultViewHolder(View itemView) {
        super(itemView);
        mRouletteCard = (CardView) itemView.findViewById(R.id.mystery_restaurant);
        mUberEstimate = (TextView) itemView.findViewById(R.id.roulette_uber_estimate);
        mCost = (TextView) itemView.findViewById(R.id.roulette_price);
        mStar1 = (ImageView) itemView.findViewById(R.id.first_star_roulette);
        mStar2 = (ImageView) itemView.findViewById(R.id.second_star_roulette);
        mStar3 = (ImageView) itemView.findViewById(R.id.third_star_roulette);
        mStar4 = (ImageView) itemView.findViewById(R.id.fourth_star_roulette);
        mStar5 = (ImageView) itemView.findViewById(R.id.fifth_star_roulette);
        mAddress = (TextView) itemView.findViewById(R.id.roulette_address);

    }

    public void bindDataToView(final Business business, String userLat, String userLon){
        final Context context = mRouletteCard.getContext();

        mUberEstimate.setText("Estimated Uber Cost: Loading...");
        final Vibrator vibrator = (Vibrator) mUberEstimate.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        final String uberEstimate = "Estimated Uber Cost: ";
        mCost.setText(business.getPrice());

        mAddress.setText(business.getLocation().getAddress1());
        setStars(business.getRating());


        double lat = business.getCoordinates().getLatitude();
        double lon = business.getCoordinates().getLongitude();
        float bizLat = (float) lat;
        float bizLon = (float) lon;
        UberAPI uberAPI = new UberAPI(context);
        float userLatitude = Float.parseFloat(userLat);
        float userLongitude = Float.parseFloat(userLon);

        uberAPI.getEstimateAsString(userLatitude, userLongitude,
                bizLat, bizLon, UberAPIConstants.UBER_SERVER_ID);
        uberAPI.setUberApiResultListener(new UberAPI.UberApiResultListener() {
            @Override
            public void onUberEstimateReady(String estimate) {
                mUberEstimate.setText(uberEstimate + " " + estimate);
            }
        });



        mRouletteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(20);
                Toast.makeText(context, business.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mRouletteCard.getContext(), MapsActivity.class);
                int position = RouletteHelper.getInstance().getPositionofBusiness(business);
                intent.putExtra(OurAppConstants.BUSINESS_POSITION_INTENT_KEY, position);
                intent.putExtra("origin", "roulette");
                view.getContext().startActivity(intent);
            }
        });
    }

    public void setStars(double rating){
        int stars = (int) rating * 2;

        switch (stars) {
            case 2 :
                mStar1.setImageResource(R.drawable.one_star_rating);
                mStar2.setImageResource(R.drawable.blank_star_rating);
                mStar3.setImageResource(R.drawable.blank_star_rating);
                mStar4.setImageResource(R.drawable.blank_star_rating);
                mStar5.setImageResource(R.drawable.blank_star_rating);
                break;
            case 3 :
                mStar1.setImageResource(R.drawable.one_star_rating);
                mStar2.setImageResource(R.drawable.one_half_star_rating);
                mStar3.setImageResource(R.drawable.blank_star_rating);
                mStar4.setImageResource(R.drawable.blank_star_rating);
                mStar5.setImageResource(R.drawable.blank_star_rating);
                break;
            case 4 :
                mStar1.setImageResource(R.drawable.two_star_rating);
                mStar2.setImageResource(R.drawable.two_star_rating);
                mStar3.setImageResource(R.drawable.blank_star_rating);
                mStar4.setImageResource(R.drawable.blank_star_rating);
                mStar5.setImageResource(R.drawable.blank_star_rating);
                break;
            case 5 :
                mStar1.setImageResource(R.drawable.two_star_rating);
                mStar2.setImageResource(R.drawable.two_star_rating);
                mStar3.setImageResource(R.drawable.two_half_star_rating);
                mStar4.setImageResource(R.drawable.blank_star_rating);
                mStar5.setImageResource(R.drawable.blank_star_rating);
                break;
            case 6 :
                mStar1.setImageResource(R.drawable.three_star_rating);
                mStar2.setImageResource(R.drawable.three_star_rating);
                mStar3.setImageResource(R.drawable.three_star_rating);
                mStar4.setImageResource(R.drawable.blank_star_rating);
                mStar5.setImageResource(R.drawable.blank_star_rating);
                break;
            case 7 :
                mStar1.setImageResource(R.drawable.three_star_rating);
                mStar2.setImageResource(R.drawable.three_star_rating);
                mStar3.setImageResource(R.drawable.three_star_rating);
                mStar4.setImageResource(R.drawable.three_half_star_rating);
                mStar5.setImageResource(R.drawable.blank_star_rating);
                break;
            case 8 :
                mStar1.setImageResource(R.drawable.four_star_rating);
                mStar2.setImageResource(R.drawable.four_star_rating);
                mStar3.setImageResource(R.drawable.four_star_rating);
                mStar4.setImageResource(R.drawable.four_star_rating);
                mStar5.setImageResource(R.drawable.blank_star_rating);
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