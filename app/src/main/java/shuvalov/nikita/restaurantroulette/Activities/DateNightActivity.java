package shuvalov.nikita.restaurantroulette.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import shuvalov.nikita.restaurantroulette.DateNightHelper;
import shuvalov.nikita.restaurantroulette.GoogleResources.GoogleAPI;
import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.RecyclerViewAdapters.DateNightRecyclerAdapter;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpAPI;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;

public class DateNightActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Button mFinalize;
    private RecyclerView mRecyclerView;
    private DateNightRecyclerAdapter mAdapter;
    private List<Business> mDateItinerary;
    private GoogleAPI mGoogleAPI;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_night);

        mDateItinerary = DateNightHelper.getInstance().getDateItinerary();
        mGoogleAPI = new GoogleAPI();
        mGoogleApiClient = mGoogleAPI.callGoogleLocApi(this);

        buttonLogic();
        recyclerLogic();
    }

    public void buttonLogic(){
        mFinalize = (Button)findViewById(R.id.finalize);
        mFinalize.setVisibility(View.GONE);//Starts off as invisible until there's something to finalize.
        mFinalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Finish up here, make an activity to go to MapActivity with markers on each location.
            }
        });
    }
    public void recyclerLogic(){
        mRecyclerView = (RecyclerView)findViewById(R.id.date_itinerary_recycler);
        mAdapter = new DateNightRecyclerAdapter(mDateItinerary);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        String userLat = mGoogleAPI.getUserLat(mGoogleApiClient);
        String userLong = mGoogleAPI.getUserLon(mGoogleApiClient);
        SharedPreferences userLocation = getSharedPreferences(OurAppConstants.USER_LAST_LOCATION, MODE_PRIVATE);
        userLocation.edit()
                .putString(OurAppConstants.USER_LAST_LAT,userLat)
                .putString(OurAppConstants.USER_LAST_LON,userLong)
                .commit();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

}
