package shuvalov.nikita.restaurantroulette.YelpResources;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.RestaurantsMainObject;

/**
 * Created by Serkan on 23/11/16.
 */

public interface YelpSearchService {
    @GET("search")
    Call<RestaurantsMainObject> getRestaurants(@Header("Authorization") String bearerToken,
                                               @Query("term") String term,
                                               @Query("categories") String categories,
                                               @Query("limit") int limit,
                                               @Query("latitude") Double lat,
                                               @Query("longitude") Double lon,
                                               @Query("radius") int radius);
}
