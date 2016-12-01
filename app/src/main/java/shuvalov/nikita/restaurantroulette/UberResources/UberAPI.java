package shuvalov.nikita.restaurantroulette.UberResources;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import shuvalov.nikita.restaurantroulette.UberResources.UberObjects.UberMainObject;

import static shuvalov.nikita.restaurantroulette.UberResources.UberAPIConstants.UBER_REQUEST_URL;


/**
 * Created by NikitaShuvalov on 11/30/16.
 */

public class UberAPI {

    public void getEstimateAsString(Float start_Lat, Float start_Lon, Float end_Lat, Float end_Lon,
                                      String server_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UBER_REQUEST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UberService service = retrofit.create(UberService.class);
        Call<UberMainObject> call = service.getEstimate(start_Lat, start_Lon, end_Lat, end_Lon, server_id);

        call.enqueue(new Callback<UberMainObject>() {
            @Override
            public void onResponse(Call<UberMainObject> call, Response<UberMainObject> response) {
                String estimate = response.body().getPrices().get(1).getEstimate();
                Log.d("UBER", "onResponse: " + estimate);

                //TO COMMUNICATE THIS VALUE BACK TO AN ACTIVITY WE NEED A BROADCAST MANAGER SET.

            }

            @Override
            public void onFailure(Call<UberMainObject> call, Throwable t) {
                //
            }
            //Once logic is retrieved

        });
    }


    public void getEstimateAsIntAverage(Float start_Lat, Float start_Lon, Float end_Lat, Float end_Lon,
                                      String server_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UBER_REQUEST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UberService service = retrofit.create(UberService.class);
        Call<UberMainObject> call = service.getEstimate(start_Lat, start_Lon, end_Lat, end_Lon, server_id);

        call.enqueue(new Callback<UberMainObject>() {
            @Override
            public void onResponse(Call<UberMainObject> call, Response<UberMainObject> response) {
                Integer getHigh = response.body().getPrices().get(1).getHighEstimate();
                Integer getLow = response.body().getPrices().get(1).getLowEstimate();
                Log.d("UBER", "onResponse: AVERAGE $" + ((getHigh+getLow)/2));
            }

            @Override
            public void onFailure(Call<UberMainObject> call, Throwable t) {
                //
            }
            //Once logic is retrieved

        });
    }
}
