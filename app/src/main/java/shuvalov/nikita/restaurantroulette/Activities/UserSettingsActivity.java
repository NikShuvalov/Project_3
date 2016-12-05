package shuvalov.nikita.restaurantroulette.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import shuvalov.nikita.restaurantroulette.GoogleResources.GoogleAPI;
import shuvalov.nikita.restaurantroulette.GoogleResources.GoogleAPIConstants;
import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpJobService;

import static shuvalov.nikita.restaurantroulette.GoogleResources.GoogleAPIConstants.PERMISSION_LOCATION;
import static shuvalov.nikita.restaurantroulette.GoogleResources.GoogleAPIConstants.REQUEST_CODE_LOCATION_GLOC_API;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_NOTIFICATION_CHECKED;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_NUM_OF_RESULTS;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_PRICING;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_RADIUS;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_RATING;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LAT;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LOCATION;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LON;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_PREFERENCES;

public class UserSettingsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    boolean isFirstTimeRating = true;
    boolean isFirstTimePrice = true;
    boolean isFirstTimeRadius = true;
    boolean isFirstTimeSearchResult = true;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);


        //Location call:
        GoogleAPI googleAPI = new GoogleAPI();
        mGoogleApiClient = googleAPI.callGoogleLocApi(this);


        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES,
                Context.MODE_PRIVATE);
        final long ratingSavedPosition = sharedPreferences.getLong(SHARED_PREF_RATING, -1);
        final long priceSavedPosition = sharedPreferences.getLong(SHARED_PREF_PRICING, -1);
        final long radiusSavedPosition = sharedPreferences.getLong(SHARED_PREF_RADIUS, -1);
        final long searchResultsSavedPosition = sharedPreferences.getLong(SHARED_PREF_NUM_OF_RESULTS, -1);
        boolean isDealsEnabled = sharedPreferences.getBoolean(SHARED_PREF_NOTIFICATION_CHECKED, false);

        Log.d("Serkan", "onCreate: " + ratingSavedPosition);

        //Spinner for RATINGS
        final Spinner spinnerRating = (Spinner) findViewById(R.id.spinner_rating);
        ArrayAdapter<CharSequence> adapterRating = ArrayAdapter.createFromResource(this,
                R.array.rating_array, android.R.layout.simple_spinner_item);
        adapterRating.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (ratingSavedPosition != -1) {
            spinnerRating.post(new Runnable() {
                @Override
                public void run() {
                    spinnerRating.setSelection((int) ratingSavedPosition);
                }
            });
        }
        spinnerRating.setAdapter(adapterRating);

        spinnerRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                long position = adapterView.getItemIdAtPosition(i);
                if (isFirstTimeRating) {
                    isFirstTimeRating = false;
                } else {
                    SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
                                    Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(SHARED_PREF_RATING, position);
                    editor.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

        //Spinner for PRICE
        final Spinner spinnerPrice = (Spinner) findViewById(R.id.spinner_pricing);
        ArrayAdapter<CharSequence> adapterPrice = ArrayAdapter.createFromResource(this,
                R.array.price_array, android.R.layout.simple_spinner_item);
        adapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (priceSavedPosition != -1) {
            spinnerPrice.post(new Runnable() {
                @Override
                public void run() {
                    spinnerPrice.setSelection((int) priceSavedPosition);
                }
            });
        }
        spinnerPrice.setAdapter(adapterPrice);

        spinnerPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Long position = adapterView.getItemIdAtPosition(i);
                if (isFirstTimePrice) {
                    isFirstTimePrice = false;
                } else {
                    SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(SHARED_PREF_PRICING, position);
                    editor.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

        //Spinner for Radius
        final Spinner spinnerRadius = (Spinner) findViewById(R.id.spinner_radius);
        ArrayAdapter<CharSequence> adapterRadius = ArrayAdapter.createFromResource(this,
                R.array.radius_array, android.R.layout.simple_spinner_item);
        adapterRadius.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (radiusSavedPosition != -1) {
            spinnerRadius.post(new Runnable() {
                @Override
                public void run() {
                    spinnerRadius.setSelection((int) radiusSavedPosition);
                }
            });
        }
        spinnerRadius.setAdapter(adapterRadius);

        spinnerRadius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Long position = adapterView.getItemIdAtPosition(i);
                if (isFirstTimeRadius) {
                    isFirstTimeRadius = false;
                } else {
                    SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(SHARED_PREF_RADIUS, position);
                    editor.commit();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

        //Spinner for Result
        final Spinner spinnerResult = (Spinner) findViewById(R.id.spinner_result);
        ArrayAdapter<CharSequence> adapterResult = ArrayAdapter.createFromResource(this,
                R.array.result_array, android.R.layout.simple_spinner_item);
        adapterResult.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (searchResultsSavedPosition != -1) {
            spinnerResult.post(new Runnable() {
                @Override
                public void run() {
                    spinnerResult.setSelection((int) searchResultsSavedPosition);
                }
            });
        }
        spinnerResult.setAdapter(adapterResult);

        spinnerResult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Long position = adapterView.getItemIdAtPosition(i);
                if (isFirstTimeSearchResult) {
                    isFirstTimeSearchResult = false;
                } else {
                    SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(SHARED_PREF_NUM_OF_RESULTS, position);
                    editor.commit();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

//        Button buttonHome = (Button) findViewById(R.id.button_home);
//        Button buttonWork = (Button) findViewById(R.id.button_work);
//
//        buttonHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
//                        Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                //ToDo: Change the 1f, 2f with actual location data from Google Location API.
//
//                editor.putFloat(SHARED_PREF_HOME_LAT, 1f);
//                editor.putFloat(SHARED_PREF_HOME_LON, 2f);
//                editor.commit();
//            }
//        });
//
//        buttonWork.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
//                        Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                //ToDo: Change the 1f, 2f with actual location data from Google Location API.
//
//                editor.putFloat(SHARED_PREF_WORK_LAT, 1f);
//                editor.putFloat(SHARED_PREF_WORK_LON, 2f);
//                editor.commit();
//            }
//        });

        final CheckBox checkBox = (CheckBox) findViewById(R.id.deals_checkbox);

        if (isDealsEnabled) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    Toast.makeText(UserSettingsActivity.this, "Create service", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SHARED_PREF_NOTIFICATION_CHECKED, true);
                    editor.commit();

                    //Create Jobscheduler Service
                    //IGNORE ERRORS: THEY ARE BECAUSE OF OUR MIN SDK.
                    PersistableBundle periodicPersistableBundle = new PersistableBundle();
                    periodicPersistableBundle.putString("Type","Periodic Yelp API CHECK");

                    JobInfo periodicJobInfo = new JobInfo.Builder(OurAppConstants.PERIODIC_JOB_ID,
                            new ComponentName(UserSettingsActivity.this, YelpJobService.class))
                            .setExtras(periodicPersistableBundle)
                            .setPeriodic(10000) //RUN IT EVERY 10 secs
                            .build();

                    JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                    jobScheduler.schedule(periodicJobInfo);

                } else {
                    Toast.makeText(UserSettingsActivity.this, "Kill service", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SHARED_PREF_NOTIFICATION_CHECKED, false);
                    editor.commit();

                    //Kill Jobscheduler Service
                    JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                    jobScheduler.cancelAll();
                }
            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            String userLat = new GoogleAPI().getUserLat(mGoogleApiClient);
            String userLon = new GoogleAPI().getUserLon(mGoogleApiClient);

            Log.d("GOOGLEAPI", "onConnected: " + userLat);
            Log.d("GOOGLEAPI", "onConnected: " + userLon);

            SharedPreferences sharedPreferences = getSharedPreferences(USER_LAST_LOCATION,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USER_LAST_LAT, userLat);
            editor.putString(USER_LAST_LON, userLon);
            editor.commit();

            Log.d(GoogleAPIConstants.TAG, "onConnected: " + userLat + " / " + userLon);

            //Below code is to enable
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(10000);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, locationRequest, this);
        } else {
            verifyLocationPermissions(this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(GoogleAPIConstants.TAG, "onConnectionFailed: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(GoogleAPIConstants.TAG, "onConnectionFailed: " + connectionResult);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    public static void verifyLocationPermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSION_LOCATION,
                    REQUEST_CODE_LOCATION_GLOC_API
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_GLOC_API: {
                //Ignore this correction; we only run this if/when we get the permission; so it can never be a problem.
                String userLat = new GoogleAPI().getUserLat(mGoogleApiClient);
                String userLon = new GoogleAPI().getUserLon(mGoogleApiClient);

                SharedPreferences sharedPreferences = getSharedPreferences(USER_LAST_LOCATION,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(USER_LAST_LAT, userLat);
                editor.putString(USER_LAST_LON, userLon);
                editor.commit();

                Log.d(GoogleAPIConstants.TAG, "onConnected: " + userLat + " / " + userLon);
                break;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        String userNewLat = new GoogleAPI().getUpdatedUserLat(location);
        String userNewLon = new GoogleAPI().getUpdatedUserLon(location);

        Log.d("LOCATION_CHANGED", "onLocationChanged: " + userNewLat);
        Log.d("LOCATION_CHANGED", "onLocationChanged: " + userNewLon);

        SharedPreferences sharedPreferences = getSharedPreferences(USER_LAST_LOCATION,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_LAST_LAT, userNewLat);
        editor.putString(USER_LAST_LON, userNewLon);
        editor.commit();
    }
}
