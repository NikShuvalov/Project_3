package shuvalov.nikita.restaurantroulette.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import shuvalov.nikita.restaurantroulette.GoogleResources.GoogleAPI;
import shuvalov.nikita.restaurantroulette.GoogleResources.GoogleAPIConstants;
import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.R;

import static shuvalov.nikita.restaurantroulette.Activities.UserSettingsActivity.verifyLocationPermissions;
import static shuvalov.nikita.restaurantroulette.GoogleResources.GoogleAPIConstants.REQUEST_CODE_LOCATION_GLOC_API;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LAT;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LOCATION;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LON;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    CardView mBasicSearch, mDateNight, mRoulette;
    private GoogleApiClient mGoogleApiClient;

    //ToDo:Remove debug Button once we're done with it
    Button mDebugButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setClickListener();


        //ToDo:Remove function and call once we're done with it
        setDebug();

        if (isLocationServiceEnabled()) {
            //Location call:
            GoogleAPI googleAPI = new GoogleAPI();
            mGoogleApiClient = googleAPI.callGoogleLocApi(this);
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services are Off")
                    .setMessage("Location services are needed for this app. Please tap 'Allow' and enable location services.")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            AlertDialog dialogNotification = builder.create();
            dialogNotification.show();
        }
    }

    public void findViews(){
        mBasicSearch = (CardView) findViewById(R.id.search_card_holder);
        mDateNight = (CardView)findViewById(R.id.date_night_card_holder);
        mRoulette = (CardView)findViewById(R.id.roulette_card_holder);
        mDebugButton = (Button)findViewById(R.id.debug_button);
    }

    public void setDebug(){
        mDebugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent debugIntent = new Intent(view.getContext(),DebugActivity.class);
                startActivity(debugIntent);
            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                String userLat = new GoogleAPI().getUserLat(mGoogleApiClient);
                String userLon = new GoogleAPI().getUserLon(mGoogleApiClient);

                SharedPreferences sharedPreferences = getSharedPreferences(USER_LAST_LOCATION,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(USER_LAST_LAT, userLat);
                editor.putString(USER_LAST_LON, userLon);
                editor.commit();

                Log.d("String_value", "onConnected: " + userLat);
                Log.d("String_value", "onConnected: " + userLon);
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
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient !=null) {
            mGoogleApiClient.disconnect();
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

    public View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(OurAppConstants.VIBRATION_TIME);
            int id = view.getId();
            Intent intent;
            switch (id) {
                case R.id.search_card_holder:
                    intent = new Intent(view.getContext(), SearchActivity.class);
                    break;
                case R.id.date_night_card_holder:
                    intent = new Intent(view.getContext(), DateNightActivity.class);
                    break;
                case R.id.roulette_card_holder:
                    intent = new Intent(view.getContext(), RouletteActivity.class);
                    break;
                default:
                    intent = null;
                    break;
            }
            startActivity(intent);
        }
    };

    public void setClickListener () {
        mBasicSearch.setOnClickListener(mListener);
        mDateNight.setOnClickListener(mListener);
        mRoulette.setOnClickListener(mListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, UserSettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isLocationServiceEnabled(){
        LocationManager locationManager = null;
        boolean gps_enabled= false,network_enabled = false;

        if(locationManager ==null)
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try{
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception ex){
            //do nothing...
        }

        try{
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){
            //do nothing...
        }

        return gps_enabled || network_enabled;

    }

}
