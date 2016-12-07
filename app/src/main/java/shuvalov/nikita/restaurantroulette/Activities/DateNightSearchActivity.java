package shuvalov.nikita.restaurantroulette.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.RecyclerViewAdapters.SearchActivityRecyclerAdapter;
import shuvalov.nikita.restaurantroulette.RestaurantSearchHelper;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpAPI;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;

public class DateNightSearchActivity extends AppCompatActivity{
    private RecyclerView mRecyclerView;
    private SearchActivityRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_night_search);

        setUp();
        doYelpSearch();
    }

    public void setUp(){
        mRecyclerView = (RecyclerView)findViewById(R.id.search_recyclerview);
        RestaurantSearchHelper.getInstance().clearSearchList();//Clears list if there was anything from other search results.
        List<Business> businesses = RestaurantSearchHelper.getInstance().getSearchResults();
        mAdapter = new SearchActivityRecyclerAdapter(businesses);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Results");
    }
    public void doYelpSearch(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String zip = bundle.getString(OurAppConstants.SEARCH_ZIP_VALUE);
        String query = bundle.getString(OurAppConstants.SEARCH_QUERY);
        String category = bundle.getString(OurAppConstants.SEARCH_CATEGORY);
        YelpAPI yelpAPI = new YelpAPI(this);
        SharedPreferences sharedPreferences = getSharedPreferences(OurAppConstants.USER_PREFERENCES,MODE_PRIVATE);
        int radius = (int)sharedPreferences.getLong(OurAppConstants.SHARED_PREF_RADIUS,5)+1;
        yelpAPI.getGetBusinessByCategory(category,query,radius,mAdapter,zip);
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
