package shuvalov.nikita.restaurantroulette.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;

import android.os.AsyncTask;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shuvalov.nikita.restaurantroulette.GoogleResources.GoogleAPI;
import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.Randomizer;
import shuvalov.nikita.restaurantroulette.RecyclerViewAdapters.RouletteActivityRecyclerAdapter;
import shuvalov.nikita.restaurantroulette.RouletteHelper;
import shuvalov.nikita.restaurantroulette.YelpCallObject;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpAPI;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.RestaurantsMainObject;


import static shuvalov.nikita.restaurantroulette.OurAppConstants.DEFAULT_MAX_COST;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.DEFAULT_MAX_DISTANCE;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.DEFAULT_MIN_RATING;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.DEFAULT_NUM_RESULTS;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_NUM_OF_RESULTS;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_PRICING;

import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_RADIUS;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_RATING;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LAT;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LOCATION;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LON;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_PREFERENCES;

public class RouletteActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private RecyclerView mRecyclerView;
    private RouletteActivityRecyclerAdapter mAdapter;
    private FloatingActionButton mRouletteButton;
    private ImageView mSettingsButton;
    private List<Business> mRouletteList;
    private EditText mRouletteQuery;
    private Location mLocation;
    private String mLat, mLon;
    private ProgressBar mProgressBar;
    private Dialog loadingDialog;
    public GoogleApiClient mGoogleApiClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Restaurant Roulette");

        GoogleAPI googleAPI = new GoogleAPI();
        mGoogleApiClient = googleAPI.callGoogleLocApi(RouletteActivity.this);



        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);

        mRouletteList = RouletteHelper.getInstance().getRandomList();

        setViews();
        setUpRecyclerView();

        mRouletteButton.setOnClickListener(mListener);
        mSettingsButton.setOnClickListener(mListener);
    }

    public void setViews () {
        mRecyclerView = (RecyclerView) findViewById(R.id.roulette_recycler_view);
        mRouletteButton = (FloatingActionButton) findViewById(R.id.roulette_button);
        mRouletteQuery = (EditText) findViewById(R.id.roulette_query);
        mSettingsButton = (ImageView) findViewById(R.id.settings_button);
        mProgressBar = (ProgressBar) findViewById(R.id.roulette_progress_bar);
        mProgressBar.setVisibility(View.GONE);

    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.roulette_button :

                    if (useDefaults()) {
                        setArgumentsToDefault();
                    }

                    final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);

                    SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES,
                            Context.MODE_PRIVATE);

                    long radiusSavedPosition = sharedPreferences.getLong(SHARED_PREF_RADIUS, -1);

                    Log.d("Roulette Activity", "onClick: "+radiusSavedPosition);


                    YelpAPI yelpAPI = new YelpAPI(RouletteActivity.this, mLocation);

                    String query = mRouletteQuery.getText().toString();

                    if (query == null || query.equals("")) {
                        mRouletteQuery.setError("Please enter search criteria");
                    } else {

                        mAdapter.clearList();
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.VISIBLE);
                        YelpCallObject object = new YelpCallObject(query,(int) radiusSavedPosition,
                                mAdapter, yelpAPI, RouletteActivity.this);
                        yelpAPI.getRestaurantsForRoulette(query,(int) radiusSavedPosition, mAdapter)
                                .enqueue(new Callback<RestaurantsMainObject>() {
                                    @Override
                                    public void onResponse(Call<RestaurantsMainObject> call, Response<RestaurantsMainObject> response) {
                                        Randomizer randomizer = new Randomizer(RouletteActivity.this);
                                        List<Business> mRandomPicksList = randomizer.pickRandomFromList(response.body().getBusinesses());
                                        RouletteHelper.getInstance().setRandomList(mRandomPicksList);
                                        mAdapter.replaceList(mRandomPicksList);
                                        mAdapter.notifyDataSetChanged();
                                        mProgressBar.setVisibility(View.GONE);
                                        mRecyclerView.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onFailure(Call<RestaurantsMainObject> call, Throwable t) {

                                    }
                                });
                        /*
                        AsyncTask<YelpCallObject, Void, Void> randomTask = new AsyncTask<YelpCallObject, Void, Void>() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                mRouletteButton.setOnClickListener(null);
                                mRecyclerView.setVisibility(View.GONE);
                                mProgressBar.setVisibility(View.VISIBLE);
                                mProgressBar.setIndeterminate(true);


                            }

                            @Override
                            protected Void doInBackground(YelpCallObject... yelpCallObjects) {
                                String query = yelpCallObjects[0].getmQuery();
                                int radius = yelpCallObjects[0].getmRadius();
                                RouletteActivityRecyclerAdapter adapter = yelpCallObjects[0].getmAdapter();
                                yelpCallObjects[0].getmYelpAPI().getRestaurantsForRoulette(query, radius ,adapter);
                                return null;
                            }


                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                mProgressBar.setIndeterminate(false);
                                mProgressBar.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mRouletteButton.setOnClickListener(mListener);

                            }
                        };

                        randomTask.execute(object);
                            */
                    }
                    break;


                case R.id.settings_button:
                    Intent intent = new Intent(RouletteActivity.this, UserSettingsActivity.class);
                    startActivity(intent);

            }

        }
    };

    public void setUpRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        mAdapter = new RouletteActivityRecyclerAdapter(mRouletteList);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLat = new GoogleAPI().getUserLat(mGoogleApiClient);
        mLon= new GoogleAPI().getUserLon(mGoogleApiClient);


        mLocation = new Location(LOCATION_SERVICE);
        mLocation.setLongitude(Double.parseDouble(mLon));
        mLocation.setLatitude(Double.parseDouble(mLat));

        SharedPreferences sharedPreferences = getSharedPreferences(USER_LAST_LOCATION,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_LAST_LAT, mLat);
        editor.putString(USER_LAST_LON, mLon);
        editor.commit();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "lol failure", Toast.LENGTH_SHORT).show();
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


    public boolean useDefaults () {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES,
                Context.MODE_PRIVATE);
        long numRandoms = sharedPreferences.getLong(OurAppConstants.SHARED_PREF_NUM_OF_RESULTS, -1);
        long maxPrice = sharedPreferences.getLong(OurAppConstants.SHARED_PREF_PRICING,-1);
        long rating = sharedPreferences.getLong(OurAppConstants.SHARED_PREF_RATING,  -1);
        long radius = sharedPreferences.getLong(OurAppConstants.SHARED_PREF_RADIUS, -1);

        if (numRandoms != -1 && maxPrice != -1 && rating != -1 && radius != -1) {
            return false;
        }

        return true;
    }

    public void setArgumentsToDefault () {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(SHARED_PREF_NUM_OF_RESULTS, DEFAULT_NUM_RESULTS);
        editor.putLong(SHARED_PREF_RADIUS, DEFAULT_MAX_DISTANCE);
        editor.putLong(SHARED_PREF_RATING, DEFAULT_MIN_RATING);
        editor.putLong(SHARED_PREF_PRICING, DEFAULT_MAX_COST);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}




