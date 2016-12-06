package shuvalov.nikita.restaurantroulette;

import android.content.Context;

import shuvalov.nikita.restaurantroulette.RecyclerViewAdapters.RouletteActivityRecyclerAdapter;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpAPI;

/**
 * Created by justinwells on 12/6/16.
 */

/**
 * using this to pass necessary data to async task in roulette activity
 */

public class YelpCallObject {
    private YelpAPI mYelpAPI;

    public YelpAPI getmYelpAPI() {
        return mYelpAPI;
    }

    private String mQuery;
    private int mRadius;
    private RouletteActivityRecyclerAdapter mAdapter;
    private Context mContext;

    public YelpCallObject(String mQuery, int mRadius, RouletteActivityRecyclerAdapter mAdapter
            ,YelpAPI yelpAPI, Context mContext) {
        this.mQuery = mQuery;
        this.mRadius = mRadius;
        this.mAdapter = mAdapter;
        this.mContext = mContext;
        mYelpAPI = yelpAPI;
    }

    public String getmQuery() {
        return mQuery;
    }

    public int getmRadius() {
        return mRadius;
    }

    public RouletteActivityRecyclerAdapter getmAdapter() {
        return mAdapter;
    }

    public Context getmContext() {
        return mContext;
    }
}
