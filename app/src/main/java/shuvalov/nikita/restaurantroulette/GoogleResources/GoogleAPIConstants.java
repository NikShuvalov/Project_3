package shuvalov.nikita.restaurantroulette.GoogleResources;

import android.Manifest;

/**
 * Created by Serkan on 04/12/16.
 */

public class GoogleAPIConstants {
    public static final String TAG = "GoogleAPI";
    public static final String[] PERMISSION_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    public static final int REQUEST_CODE_LOCATION_GLOC_API = 1;
}
