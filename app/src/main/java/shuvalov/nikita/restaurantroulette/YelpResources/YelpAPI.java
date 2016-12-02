package shuvalov.nikita.restaurantroulette.YelpResources;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import shuvalov.nikita.restaurantroulette.Activities.DetailActivity;
import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.RecyclerViewAdapters.SearchActivityRecyclerAdapter;
import shuvalov.nikita.restaurantroulette.RestaurantSearchHelper;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.RestaurantsMainObject;

import static android.content.Context.NOTIFICATION_SERVICE;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_LAST_PUSHED_BUSINESS_NAME;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_PREFERENCES;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.ADDRESS_1;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.BUSINESS_ID;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.BUSINESS_NAME;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.BUSINESS_URL;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.CITY;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.DISTANCE;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.IMAGE_URL;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.IS_CLOSED;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.PHONE_NUMBER;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.RATING;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.REVIEW_COUNT;
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

    public YelpAPI(Context context) {
        mContext = context;
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
                Toast.makeText(mContext, "Size: "+mBusinessList.size(), Toast.LENGTH_SHORT).show();
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

    public void getRestaurantDeals() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YELP_SEARCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //ToDo CHANGE THE HARDCODED LAT LONG INFO!

        //double myLong = mLastLocation.getLongitude();
        //double myLat = mLastLocation.getLatitude();
        //Log.d(TAG, "Lat, Long: "+myLat+","+myLong);
        YelpNotificationService service = retrofit.create(YelpNotificationService.class);

        //ToDo CHANGE THE HARDCODED LAT LONG INFO!
        Call<RestaurantsMainObject> call = service.getRestaurantDeals("Bearer " + YELP_BEARER_TOKEN,
                OurAppConstants.GA_LATITUDE, OurAppConstants.GA_LONGITUDE, "restaurants", 500,
                "deals", "distance");

        call.enqueue(new Callback<RestaurantsMainObject>() {
            @Override
            public void onResponse(Call<RestaurantsMainObject> call, Response<RestaurantsMainObject> response) {
                String businessName = response.body().getBusinesses().get(0).getName();
                Double distance = response.body().getBusinesses().get(0).getDistance();
                long distanceRounded = Math.round(distance);
                Log.d(TAG, "onResponse: " + businessName);

                SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREFERENCES,
                        Context.MODE_PRIVATE);
                String lastPushedBusinessName =
                        sharedPreferences.getString(SHARED_PREF_LAST_PUSHED_BUSINESS_NAME, "default");

                if (businessName.equals(lastPushedBusinessName)) {
                    //Do nothing
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SHARED_PREF_LAST_PUSHED_BUSINESS_NAME, businessName);
                    editor.commit();
                    
                    Uri currentNotificationUri = RingtoneManager.getActualDefaultRingtoneUri(mContext,
                            RingtoneManager.TYPE_NOTIFICATION);

                    Intent intent = new Intent(mContext, DetailActivity.class);
                    //Passing all business object info
                    intent.putExtra(IMAGE_URL, response.body().getBusinesses().get(0).getImageUrl());
                    intent.putExtra(PHONE_NUMBER, response.body().getBusinesses().get(0).getPhone());
                    intent.putExtra(IS_CLOSED, response.body().getBusinesses().get(0).getIsClosed());
                    intent.putExtra(BUSINESS_URL, response.body().getBusinesses().get(0).getUrl());
                    intent.putExtra(BUSINESS_ID, response.body().getBusinesses().get(0).getId());
                    intent.putExtra(REVIEW_COUNT, response.body().getBusinesses().get(0).getReviewCount());
                    intent.putExtra(RATING, response.body().getBusinesses().get(0).getRating());
                    intent.putExtra(DISTANCE, response.body().getBusinesses().get(0).getDistance());
                    intent.putExtra(BUSINESS_NAME, response.body().getBusinesses().get(0).getName());
                    intent.putExtra(ADDRESS_1, response.body().getBusinesses().get(0).getLocation().getAddress1());
                    intent.putExtra(CITY, response.body().getBusinesses().get(0).getLocation().getCity());

                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                            (int) System.currentTimeMillis(), intent, 0);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
                    builder.setSmallIcon(R.drawable.ic_place_black_24dp);
                    builder.setSound(currentNotificationUri);
                    builder.setContentTitle("New food deal at " + businessName + "!");
                    builder.setContentText("It is " + distanceRounded + " meters to your location");
                    builder.setAutoCancel(true);
                    builder.setPriority(Notification.PRIORITY_MAX);
                    builder.setContentIntent(pendingIntent);

                    NotificationManager notificationManager =
                            (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(1, builder.build());
                }
            }

            @Override
            public void onFailure(Call<RestaurantsMainObject> call, Throwable t) {
                Log.d(TAG, "GET RESTAURANT DEALS FAILED");
            }
        });
    }
}


