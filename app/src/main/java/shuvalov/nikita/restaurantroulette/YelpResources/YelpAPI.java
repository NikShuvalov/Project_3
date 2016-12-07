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
import android.support.annotation.Nullable;
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
import shuvalov.nikita.restaurantroulette.Randomizer;

import shuvalov.nikita.restaurantroulette.RecyclerViewAdapters.DateNightRecyclerAdapter;

import shuvalov.nikita.restaurantroulette.RecyclerViewAdapters.RouletteActivityRecyclerAdapter;
import shuvalov.nikita.restaurantroulette.RecyclerViewAdapters.SearchActivityRecyclerAdapter;
import shuvalov.nikita.restaurantroulette.RestaurantSearchHelper;
import shuvalov.nikita.restaurantroulette.RouletteHelper;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.RestaurantsMainObject;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_LAST_PUSHED_BUSINESS_NAME;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LAT;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LOCATION;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LON;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_PREFERENCES;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.NOTIF_ADDRESS_1;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.NOTIF_BUSINESS_ID;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.NOTIF_BUSINESS_NAME;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.NOTIF_BUSINESS_URL;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.NOTIF_CITY;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.NOTIF_DISTANCE;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.NOTIF_IMAGE_URL;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.NOTIF_IS_CLOSED;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.NOTIF_LATITUTE;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.NOTIF_LONGITUDE;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.NOTIF_PHONE_NUMBER;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.NOTIF_PRICE;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.NOTIF_RATING;
import static shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants.NOTIF_REVIEW_COUNT;
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
    private List<Business> mBusinessList, mRandomPicksList;

    /**
     * Might not need to be an object but for now making it an object. Otherwise change methods to static
     *
     * @param context Needed for systemServices and such
     */
    public YelpAPI(Context context, Location lastUserLocation) {//ToDo:Housekeeping, remove mLastLocation because it does nothing.
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

    public void getRestaurants(String query, long price, int radius, final SearchActivityRecyclerAdapter adapter, final boolean isRandomized) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YELP_SEARCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SharedPreferences userSharedPref = mContext.getSharedPreferences(OurAppConstants.USER_PREFERENCES,MODE_PRIVATE);

        String priceQueryText = "";
        if (price == -1) {
            price = userSharedPref.getLong(OurAppConstants.SHARED_PREF_PRICING, 3);
        }

        switch ((int)price) {
            case 3:
                priceQueryText += "4,";
            case 2:
                priceQueryText+="3,";
            case 1:
                priceQueryText+="2,";
            case 0:
                priceQueryText+="1";
                break;
        }

        SharedPreferences locationPreferences = mContext.getSharedPreferences(OurAppConstants.USER_LAST_LOCATION,MODE_PRIVATE);
        double myLong = Double.parseDouble(locationPreferences.getString(OurAppConstants.USER_LAST_LON,"200"));
        double myLat = Double.parseDouble(locationPreferences.getString(OurAppConstants.USER_LAST_LAT,"200"));

        if (myLong ==200|| myLat == 200){
            Toast.makeText(mContext, "No location found in userPreferences", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "Lat, Long: "+myLat+","+myLong);
        Log.d(TAG, "Radius: "+radius*1000+", priceQueryText: "+priceQueryText);
        YelpSearchService service = retrofit.create(YelpSearchService.class);

        //ToDo: Replace "restaurants" with Constant and/or variable based on what the search category is.
        Call<RestaurantsMainObject> call = service.getRestaurants("Bearer " + YELP_BEARER_TOKEN, query, priceQueryText, "restaurants",
                40, myLat, myLong, radius*1000);//ToDo: Do we need to make a different MainObject for place of entertainment?

        call.enqueue(new Callback<RestaurantsMainObject>() {
            @Override
            public void onResponse(Call<RestaurantsMainObject> call, Response<RestaurantsMainObject> response) {
                mBusinessList = response.body().getBusinesses();
                RestaurantSearchHelper.getInstance().setmBusinessList(mBusinessList);
                if(isRandomized){
                    Randomizer randomizer = new Randomizer(mContext);
                    adapter.replaceList(randomizer.pickRandomFromList(mBusinessList));
                }else{
                    adapter.replaceList(mBusinessList);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<RestaurantsMainObject> call, Throwable t) {
                //Do nothing.
            }
        });

    }

    public Call<RestaurantsMainObject> getRestaurantsForRoulette(String query, int radius, final RouletteActivityRecyclerAdapter adapter) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YELP_SEARCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SharedPreferences userLocation= mContext.getSharedPreferences(OurAppConstants.USER_LAST_LOCATION, MODE_PRIVATE);
        double myLong = Double.parseDouble(userLocation.getString(OurAppConstants.USER_LAST_LON,"200"));
        double myLat = Double.parseDouble(userLocation.getString(OurAppConstants.USER_LAST_LAT,"200"));
        Log.d(TAG, "Lat, Long: "+myLat+","+myLong);
        YelpSearchService service = retrofit.create(YelpSearchService.class);

        long price = mContext.getSharedPreferences(OurAppConstants.USER_PREFERENCES,MODE_PRIVATE).getLong(OurAppConstants.SHARED_PREF_PRICING, 3);
        String priceQueryText = "";


        switch ((int)price) {
            case 3:
                priceQueryText += "4,";
            case 2:
                priceQueryText+="3,";
            case 1:
                priceQueryText+="2,";
            case 0:
                priceQueryText+="1";
                break;
        }


        //ToDo: Replace "restaurants" with Constant and/or variable based on what the search category is.
        Call<RestaurantsMainObject> call = service.getRestaurants("Bearer " + YELP_BEARER_TOKEN, query, priceQueryText, "restaurants",
                40, myLat, myLong, radius*1000);//ToDo: Do we need to make a different MainObject for place of entertainment?


        return call;
        /*call.enqueue(new Callback<RestaurantsMainObject>() {
            @Override
            public void onResponse(Call<RestaurantsMainObject> call, Response<RestaurantsMainObject> response) {
                mBusinessList = response.body().getBusinesses();
                Randomizer randomizer = new Randomizer(mContext);
                mRandomPicksList = randomizer.pickRandomFromList(mBusinessList);
                RouletteHelper.getInstance().setRandomList(mRandomPicksList);
                adapter.replaceList(mRandomPicksList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<RestaurantsMainObject> call, Throwable t) {
                //Do nothing.
            }
        });
        */
    }

    public void getRestaurantDeals() {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_LAST_LOCATION,
                Context.MODE_PRIVATE);

        String userLat = sharedPreferences.getString(USER_LAST_LAT, "last_user_lat");
        String userLon = sharedPreferences.getString(USER_LAST_LON, "last_user_lon");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YELP_SEARCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YelpNotificationService service = retrofit.create(YelpNotificationService.class);

        Call<RestaurantsMainObject> call = service.getRestaurantDeals("Bearer " + YELP_BEARER_TOKEN,
                Double.parseDouble(userLat), Double.parseDouble(userLon), "restaurants", 1000,
                "deals", "distance", "true");

        Log.d(TAG, "getRestaurantDeals: " + Double.parseDouble(userLat) + " / " + Double.parseDouble(userLon));

        call.enqueue(new Callback<RestaurantsMainObject>() {
            @Override
            public void onResponse(Call<RestaurantsMainObject> call, Response<RestaurantsMainObject> response) {
                if (response.body().getBusinesses().size() > 0 ) {
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
                        intent.putExtra(OurAppConstants.ORIGIN,OurAppConstants.NOTIFICATION_ORIGIN);
                        intent.putExtra(NOTIF_IMAGE_URL, response.body().getBusinesses().get(0).getImageUrl());
                        intent.putExtra(NOTIF_PHONE_NUMBER, response.body().getBusinesses().get(0).getPhone());
                        intent.putExtra(NOTIF_IS_CLOSED, response.body().getBusinesses().get(0).getIsClosed());
                        intent.putExtra(NOTIF_BUSINESS_URL, response.body().getBusinesses().get(0).getUrl());
                        intent.putExtra(NOTIF_BUSINESS_ID, response.body().getBusinesses().get(0).getId());
                        intent.putExtra(NOTIF_PRICE, response.body().getBusinesses().get(0).getPrice());
                        intent.putExtra(NOTIF_REVIEW_COUNT, response.body().getBusinesses().get(0).getReviewCount());
                        intent.putExtra(NOTIF_RATING, response.body().getBusinesses().get(0).getRating());
                        intent.putExtra(NOTIF_DISTANCE, response.body().getBusinesses().get(0).getDistance());
                        intent.putExtra(NOTIF_BUSINESS_NAME, response.body().getBusinesses().get(0).getName());
                        intent.putExtra(NOTIF_ADDRESS_1, response.body().getBusinesses().get(0).getLocation().getAddress1());
                        intent.putExtra(NOTIF_CITY, response.body().getBusinesses().get(0).getLocation().getCity());
                        intent.putExtra(NOTIF_LATITUTE, response.body().getBusinesses().get(0).getCoordinates().getLatitude());
                        intent.putExtra(NOTIF_LONGITUDE, response.body().getBusinesses().get(0).getCoordinates().getLongitude());

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
                } else {
                    Log.d(TAG, "onResponse: No restaurants with deals in 500m");
                }
            }

            @Override
            public void onFailure(Call<RestaurantsMainObject> call, Throwable t) {
                Log.d(TAG, "GET RESTAURANT DEALS FAILED");
            }
        });
    }

    //FixMe:If there's time, have the consequent searches take in coords instead of zip code.
    public void getGetBusinessByCategory(String category, String query, int radius, final SearchActivityRecyclerAdapter adapter, String zipCode) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YELP_SEARCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ZipCodeYelpSearchService service = retrofit.create(ZipCodeYelpSearchService.class);

        long price = mContext.getSharedPreferences(OurAppConstants.USER_PREFERENCES,MODE_PRIVATE).getLong(OurAppConstants.SHARED_PREF_PRICING, 3);
        String priceQueryText = "";
        int zipCodeAsInt= Integer.parseInt(zipCode);

        switch ((int)price) {
            case 3:
                priceQueryText +="4,";
            case 2:
                priceQueryText+="3,";
            case 1:
                priceQueryText+="2,";
            case 0:
                priceQueryText+="1";
                break;
        }
        Log.d(TAG, "getBusinessByCategory: "+category);
        Log.d(TAG, "Radius: "+radius*1000+", priceQueryText: "+priceQueryText);


        Call<RestaurantsMainObject> call = service.getBusinessByZip("Bearer " + YELP_BEARER_TOKEN, query, priceQueryText, category,
                40, zipCodeAsInt, radius*1000);

        call.enqueue(new Callback<RestaurantsMainObject>() {
            @Override
            public void onResponse(Call<RestaurantsMainObject> call, Response<RestaurantsMainObject> response) {
                mBusinessList = response.body().getBusinesses();
                if(mBusinessList.isEmpty()){
                    Toast.makeText(mContext, "Nothing nearby found", Toast.LENGTH_SHORT).show();
                }
                RestaurantSearchHelper.getInstance().setmBusinessList(mBusinessList);//ToDo:Make a separate singleton for dateNight searches if there's time.
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


