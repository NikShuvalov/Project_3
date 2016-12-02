package shuvalov.nikita.restaurantroulette.UberResources;

import android.content.Context;
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
    private UberApiResultListener mListener;
    private UberApiAverageResultListener mAverageListener;

    public UberAPI(Context context) {
        mListener = null;
        mAverageListener = null;
        mContext = context;
    }

    public interface UberApiResultListener {
        void onUberEstimateReady(String estimate);
    }

    public void setUberApiResultListener(UberApiResultListener listener) {
        mListener = listener;
    }

    public interface UberApiAverageResultListener {
        void onUberAverageEstimateReady(Integer averageEstimate);
    }

    public void setUberApiAverageResultListener(UberApiAverageResultListener listener) {
        mAverageListener = listener;
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

                if (mListener != null) {
                    mListener.onUberEstimateReady(estimate);
                }
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

                if (mAverageListener != null) {
                    mAverageListener.onUberAverageEstimateReady(average);
                }
            }

            @Override
            public void onFailure(Call<UberMainObject> call, Throwable t) {
                //
            }
            //Once logic is retrieved
        });
    }
}
