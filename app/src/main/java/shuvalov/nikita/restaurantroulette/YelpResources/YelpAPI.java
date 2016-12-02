package shuvalov.nikita.restaurantroulette.YelpResources;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import shuvalov.nikita.restaurantroulette.RecyclerViewAdapters.SearchActivityRecyclerAdapter;
import shuvalov.nikita.restaurantroulette.RestaurantSearchHelper;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.RestaurantsMainObject;

import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.YELP_APP_SECRET;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.YELP_BEARER_TOKEN;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.YELP_CLIENT_ID;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.YELP_GRANT_TYPE;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.YELP_SEARCH_BASE_URL;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.YELP_TOKEN_BASE_URL;

/**
 * Created by NikitaShuvalov on 11/30/16.
 */

public class YelpAPI {
    public static final String TAG = "YelpAPI_class";
    private Location mLastLocation;
    private Context mContext;
    private List<Business> mBusinessList;

    /**
     * Might not need to be an object but for now making it an object. Otherwise change methods to static
     *
     * @param context Needed for systemServices and such
     */
    public YelpAPI(Context context, Location lastUserLocation) {
        mContext = context;
        mLastLocation = lastUserLocation;
    }

    /**
     * I think this only needs to be called when our BearerToken is expired or compromised
     */
    public void getBearerToken() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(YELP_TOKEN_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            YelpBearerTokenService service = retrofit.create(YelpBearerTokenService.class);
            Call<YelpBearerTokenObject> call = service.getBearerToken(YELP_GRANT_TYPE, YELP_CLIENT_ID,
                    YELP_APP_SECRET);

            call.enqueue(new Callback<YelpBearerTokenObject>() {
                @Override
                public void onResponse(Call<YelpBearerTokenObject> call, Response<YelpBearerTokenObject> response) {
                    //ToDo: If this is getting called there's a good chance that the BearerToken has gone bad.
                    // How are we saving a new bearerToken if we encounter this?

                    String bearerToken = response.body().getAccessToken();
                    Log.d(TAG, "onResponse: " + bearerToken);
                }

                @Override
                public void onFailure(Call<YelpBearerTokenObject> call, Throwable t) {
                    //Show error message.
                }
            });
        }
    }

    public void getRestaurants(String query, int radius, final SearchActivityRecyclerAdapter adapter) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YELP_SEARCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        double myLong = mLastLocation.getLongitude();
        double myLat = mLastLocation.getLatitude();
        Log.d(TAG, "Lat, Long: "+myLat+","+myLong);
        YelpSearchService service = retrofit.create(YelpSearchService.class);

        //ToDo: Replace "restaurants" with Constant and/or variable based on what the search category is.
        Call<RestaurantsMainObject> call = service.getRestaurants("Bearer " + YELP_BEARER_TOKEN, query, "restaurants",
                40, myLat, myLong, radius*1000);//ToDo: Do we need to make a different MainObject for place of entertainment?

        call.enqueue(new Callback<RestaurantsMainObject>() {
            @Override
            public void onResponse(Call<RestaurantsMainObject> call, Response<RestaurantsMainObject> response) {
                mBusinessList = response.body().getBusinesses();
                RestaurantSearchHelper.getInstance().setmBusinessList(mBusinessList);
                adapter.replaceList(mBusinessList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<RestaurantsMainObject> call, Throwable t) {
                //Do nothing.
            }
        });

    }
}


