package shuvalov.nikita.restaurantroulette.GoogleResources;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by NikitaShuvalov on 11/30/16.
 */

public class GoogleAPI {
    GoogleApiClient mGoogleApiClient;

    /**
     * Since GoogleLoc API needs to be implemented in an Activity; here are the steps:
     *
     * 1) Call callGoogleLocApi() from onCreate of an Activity. Assign the return to a
     * GoogleApiClient object member variable.
     *
     * 2) Implement GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
     * to the activity.
     *
     * 3) Call getUserLat and getUsetLon at onConnected method; passing in the GoogleApiClient member
     * variable you created at step 1.
     *
     * 4) at onStart and onStop of Activity; call connect() and disconnect() on the GoogleApiClient member
     * variable you created at step 1.
     *
     */

    public GoogleApiClient callGoogleLocApi(Context context) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) context)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) context)
                    .addApi(LocationServices.API)
                    .build();
        }
        return mGoogleApiClient;
    }

    public String getUserLat(GoogleApiClient googleApiClient) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        Double userLatDouble = location.getLatitude();
        Log.d("Actual_Double_Value", "getUserLat: " + userLatDouble);
        String userLatString = userLatDouble.toString();
        return userLatString;
    }

    public String getUserLon(GoogleApiClient googleApiClient) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        Double userLonDouble = location.getLongitude();
        Log.d("Actual_Double_Value", "getUserLat: " + userLonDouble);
        String UserLonString = userLonDouble.toString();
        return UserLonString;
    }
}
