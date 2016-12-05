package shuvalov.nikita.restaurantroulette.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import shuvalov.nikita.restaurantroulette.GoogleResources.GoogleAPI;
import shuvalov.nikita.restaurantroulette.GoogleResources.GoogleAPIConstants;
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

        //Location call:
        GoogleAPI googleAPI = new GoogleAPI();
        mGoogleApiClient = googleAPI.callGoogleLocApi(this);

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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            float userLat = new GoogleAPI().getUserLat(mGoogleApiClient);
            float userLon = new GoogleAPI().getUserLon(mGoogleApiClient);

            SharedPreferences sharedPreferences = getSharedPreferences(USER_LAST_LOCATION,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(USER_LAST_LAT, userLat);
            editor.putFloat(USER_LAST_LON, userLon);
            editor.commit();

            Log.d(GoogleAPIConstants.TAG, "onConnected: " + userLat + " / " + userLon);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_GLOC_API: {
                //Ignore this correction; we only run this if/when we get the permission; so it can never be a problem.
                float userLat = new GoogleAPI().getUserLat(mGoogleApiClient);
                float userLon = new GoogleAPI().getUserLon(mGoogleApiClient);

                SharedPreferences sharedPreferences = getSharedPreferences(USER_LAST_LOCATION,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(USER_LAST_LAT, userLat);
                editor.putFloat(USER_LAST_LON, userLon);
                editor.commit();

                Log.d(GoogleAPIConstants.TAG, "onConnected: " + userLat + " / " + userLon);
                break;
            }
        }
    }

    public View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
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
}
