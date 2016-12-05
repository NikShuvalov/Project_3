package shuvalov.nikita.restaurantroulette.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.RecyclerViewAdapters.RouletteActivityRecyclerAdapter;
import shuvalov.nikita.restaurantroulette.RestaurantSearchHelper;
import shuvalov.nikita.restaurantroulette.RouletteHelper;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpAPI;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;

import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_NUM_OF_RESULTS;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_PRICING;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_RADIUS;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_RATING;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_PREFERENCES;

public class RouletteActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RouletteActivityRecyclerAdapter mAdapter;
    private FloatingActionButton mRouletteButton, mSettingsButton;
    private List<Business> mRouletteList;
    private EditText mRouletteQuery;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette);

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
        mSettingsButton = (FloatingActionButton) findViewById(R.id.settings_button);

    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.roulette_button :
                    final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);

                    SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES,
                            Context.MODE_PRIVATE);

                    long radiusSavedPosition = sharedPreferences.getLong(SHARED_PREF_RADIUS, -1);

                    Location mockLocation = new Location(LOCATION_SERVICE);
                    mockLocation.setLongitude(OurAppConstants.GA_LONGITUDE);
                    mockLocation.setLatitude(OurAppConstants.GA_LATITUDE);

                    YelpAPI yelpAPI = new YelpAPI(RouletteActivity.this, mockLocation);

                    String query = mRouletteQuery.getText().toString();

                    if (query == null || query.equals("")) {
                        mRouletteQuery.setError("Please enter search criteria");
                    } else {
                        yelpAPI.getRestaurantsForRoulette(query, (int) radiusSavedPosition,mAdapter);
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



}
