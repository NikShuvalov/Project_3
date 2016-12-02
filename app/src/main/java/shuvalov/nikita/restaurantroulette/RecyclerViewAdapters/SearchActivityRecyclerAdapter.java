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
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Category;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Location;

/**
 * Created by NikitaShuvalov on 12/1/16.
 */

/**
 * Created by NikitaShuvalov on 11/24/16.
 */

public class SearchActivityRecyclerAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {
    List<Business> mBusinessList;

    public SearchActivityRecyclerAdapter(List<Business> businessList) {
        mBusinessList = businessList;
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_search_form, null);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchResultViewHolder holder, final int position) {
        holder.bindDataToView(mBusinessList.get(position));
        holder.mHolderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.mNameView.getContext(), DetailActivity.class);
                intent.putExtra(OurAppConstants.BUSINESS_POSITION_INTENT_KEY,position);
                holder.mHolderLayout.getContext().startActivity(intent);
            }
        });

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

class SearchResultViewHolder extends RecyclerView.ViewHolder{
    TextView mNameView, mDescView, mPrice, mRating;
    ImageView mPicture;
    RelativeLayout mHolderLayout;

    public SearchResultViewHolder(View itemView) {
        super(itemView);
        mNameView = (TextView)itemView.findViewById(R.id.business_name_view);
        mDescView = (TextView)itemView.findViewById(R.id.description_text_view);
        mPrice = (TextView)itemView.findViewById(R.id.price_view);
        mRating = (TextView)itemView.findViewById(R.id.rating_view);
        mHolderLayout = (RelativeLayout)itemView.findViewById(R.id.holder_layout);
        mPicture = (ImageView)itemView.findViewById(R.id.picture);
    }

    public void bindDataToView(Business business){
        mNameView.setText(business.getName());
        String categoriesText = "";
        for (Category category: business.getCategories()){
            categoriesText+=(category.getTitle()+", ");
        }
        Location location = business.getLocation();
        String fullAddress = location.getAddress1()+" "+location.getCity();
        mDescView.setText(fullAddress+"\n"+business.getPhone()+"\n"+categoriesText);
        mPrice.setText(business.getPrice());
        mRating.setText(String.valueOf(business.getRating())); //FixMe: Change this to the Yelp star rating instead of numbers. & in the xml.
        //ToDo:Set image by using picasso with image.url
    }
}