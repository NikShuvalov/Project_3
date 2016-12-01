package shuvalov.nikita.restaurantroulette.UberResources;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import shuvalov.nikita.restaurantroulette.UberResources.UberObjects.UberMainObject;

/**
 * Created by Serkan on 30/11/16.
 */

public interface UberService {
    @GET("price")
    Call<UberMainObject> getEstimate(@Query("start_latitude") Float start_latitude,
                                     @Query("start_longitude") Float start_longitude,
                                     @Query("end_latitude") Float end_latitude,
                                     @Query("end_longitude") Float end_longitude,
                                     @Query("server_token") String server_id);
}
