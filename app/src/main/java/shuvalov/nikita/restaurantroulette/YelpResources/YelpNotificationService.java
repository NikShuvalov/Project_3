package shuvalov.nikita.restaurantroulette.YelpResources;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.RestaurantsMainObject;

/**
 * Created by Serkan on 02/12/16.
 */

public interface YelpNotificationService {
    @GET("search")
    Call<RestaurantsMainObject> getRestaurantDeals(@Header("Authorization") String bearerToken,
                                                   @Query("latitude") Double lat,
                                                   @Query("longitude") Double lon,
                                                   @Query("categories") String categories,
                                                   @Query("radius") int radius,
                                                   @Query("attributes") String attributes,
                                                   @Query("sort_by") String sort_by,
                                                   @Query("open_now") String open_now);
}