package shuvalov.nikita.restaurantroulette.YelpResources;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

/**
 * Created by Serkan on 02/12/16.
 */

//IGNORE THAT THANG!
public class YelpJobService extends JobService {
    public static final String TAG = "Serkan";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob: " + jobParameters.getExtras().get("Type"));
        YelpAPI yelpAPI = new YelpAPI(getApplicationContext());
        yelpAPI.getRestaurantDeals();
        jobFinished(jobParameters, false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }


}
