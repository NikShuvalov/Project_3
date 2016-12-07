package shuvalov.nikita.restaurantroulette.YelpResources;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.RestaurantsMainObject;

/**
 * Created by NikitaShuvalov on 12/6/16.
 */

public interface ZipCodeYelpSearchService {

        @GET("search")
        Call<RestaurantsMainObject> getBusinessByZip(@Header("Authorization") String bearerToken,
                                                   @Query("term") String term,
                                                   @Query("price") String costs,
                                                   @Query("categories") String categories,
                                                   @Query("limit") int limit,
                                                   @Query("location")int zipCode,
                                                   @Query("radius") int radius);

}
