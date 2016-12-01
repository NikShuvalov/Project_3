package shuvalov.nikita.restaurantroulette.UberResources;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
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
    public static final String TAG = "Serkan";
    public static final String RESULT_STRING = "shuvalov.nikita.restaurantroulette.UberResources.RESULT_STRING";
    public static final String MESSAGE_STRING = "shuvalov.nikita.restaurantroulette.UberResources.MESSAGE_STRING";
    public static final String RESULT_AVERAGE = "shuvalov.nikita.restaurantroulette.UberResources.RESULT_AVERAGE";
    public static final String MESSAGE_AVERAGE = "shuvalov.nikita.restaurantroulette.UberResources.MESSAGE_AVERAGE";
    private Context mContext;

    public UberAPI(Context context) {
        mContext = context;
    }

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

                //ToDo We still need to implement the receivers whereever we want to receive this info.

                LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(mContext);
                Intent intent = new Intent(RESULT_STRING);
                intent.putExtra(MESSAGE_STRING, estimate);
                broadcaster.sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<UberMainObject> call, Throwable t) {
                //
            }

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
                Integer average = ((getHigh+getLow)/2);

                //ToDo We still need to implement the receivers whereever we want to receive this info.

                LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(mContext);
                Intent intent = new Intent(RESULT_AVERAGE);
                intent.putExtra(MESSAGE_AVERAGE, average);
                broadcaster.sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<UberMainObject> call, Throwable t) {
                //
            }
            //Once logic is retrieved
        });
    }
}
