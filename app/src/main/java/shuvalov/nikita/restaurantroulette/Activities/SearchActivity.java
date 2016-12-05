package shuvalov.nikita.restaurantroulette.Activities;

import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.RecyclerViewAdapters.SearchActivityRecyclerAdapter;
import shuvalov.nikita.restaurantroulette.RestaurantSearchHelper;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpAPI;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText mQueryEntry;
    private Spinner mRatingSpinner, mPricingSpinner, mRadiusSpinner, mLocationSpinner;
    private Button mSearch, mRandom;
    private RecyclerView mRecyclerView;
    private SearchActivityRecyclerAdapter mAdapter;
    private List<Business> mBusinessList;
    private CardView mBasicCardHolder;
    private boolean mOptionsVisible;
    private ImageView mCloseView;

    private ArrayList<Spinner> mSpinnersInCardView;

    private ArrayAdapter<CharSequence> mRatingAdapter, mPricingAdapter, mRadiusAdapter, mLocationAdapter;

    private String mPrice, mRating, mRadius = "show all";//ToDo: Change value into constant
    private String mLocation;


    //ToDo: Add a way to close options to see full list without having to search
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mBusinessList = RestaurantSearchHelper.getInstance().getSearchResults();
        mOptionsVisible = true;
        mSpinnersInCardView = new ArrayList<>();


        findViews();
        mBasicCardHolder.setVisibility(View.VISIBLE);
        mRandom.setVisibility(View.VISIBLE);
        for(Spinner spinner:mSpinnersInCardView) {
            spinner.setEnabled(true);
        }
        addAdaptersToSpinners();
        setUpRecyclerView();
        setOnClickListeners();
    }
    public void findViews(){
        mQueryEntry = (EditText)findViewById(R.id.query_entry);
        mRatingSpinner =(Spinner)findViewById(R.id.rating_spinner);
        mRadiusSpinner =(Spinner)findViewById(R.id.radius_spinner);
        mPricingSpinner = (Spinner)findViewById(R.id.price_spinner);
        mSearch = (Button)findViewById(R.id.basic_search);
        mRecyclerView = (RecyclerView)findViewById(R.id.search_recyclerview);
        mRandom = (Button)findViewById(R.id.random_search);
        mLocationSpinner = (Spinner)findViewById(R.id.location_spinner);
        mBasicCardHolder =(CardView)findViewById(R.id.basic_search_card);
        mCloseView =(ImageView)findViewById(R.id.close_search);

        mSpinnersInCardView.add(mRatingSpinner);
        mSpinnersInCardView.add(mRadiusSpinner);
        mSpinnersInCardView.add(mRatingSpinner);
        mSpinnersInCardView.add(mPricingSpinner);
    }

    public void addAdaptersToSpinners(){
        mRatingAdapter = ArrayAdapter.createFromResource(this, R.array.rating_array,android.R.layout.simple_spinner_item);
        mPricingAdapter = ArrayAdapter.createFromResource(this, R.array.price_array,android.R.layout.simple_spinner_item);
        mRadiusAdapter = ArrayAdapter.createFromResource(this, R.array.radius_array,android.R.layout.simple_spinner_item);
        mLocationAdapter = ArrayAdapter.createFromResource(this, R.array.location_array, android.R.layout.simple_spinner_item);

        mRatingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPricingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRadiusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLocationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mRatingSpinner.setAdapter(mRatingAdapter);
        mPricingSpinner.setAdapter(mPricingAdapter);
        mRadiusSpinner.setAdapter(mRadiusAdapter);
        mLocationSpinner.setAdapter(mLocationAdapter);

        mRatingSpinner.setOnItemSelectedListener(this);
        mPricingSpinner.setOnItemSelectedListener(this);
        mRadiusSpinner.setOnItemSelectedListener(this);
        mLocationSpinner.setOnItemSelectedListener(this);

    }

    public void setUpRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        mAdapter = new SearchActivityRecyclerAdapter(mBusinessList);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }
    public void setOnClickListeners(){
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
                mQueryEntry.setText("");
                if(mOptionsVisible){
                    animateOptionsOffScreen();
                    mOptionsVisible=false;
                }

                Location mockLocation = new Location(LOCATION_SERVICE);
                mockLocation.setLongitude(OurAppConstants.GA_LONGITUDE);
                mockLocation.setLatitude(OurAppConstants.GA_LATITUDE);
                YelpAPI yelpApi = new YelpAPI(view.getContext(), mockLocation);
                String query = mQueryEntry.getText().toString();
                yelpApi.getRestaurants(query,Integer.parseInt(mRadius),mAdapter, false);
            }
        });
        //ToDo:Combine mSearch and mRandom OnclickListener
        mRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
                if(mOptionsVisible){
                    animateOptionsOffScreen();
                    mOptionsVisible=false;
                }

                Location mockLocation = new Location(LOCATION_SERVICE);
                mockLocation.setLongitude(OurAppConstants.GA_LONGITUDE);
                mockLocation.setLatitude(OurAppConstants.GA_LATITUDE);
                YelpAPI yelpApi = new YelpAPI(view.getContext(), mockLocation);
                String query = mQueryEntry.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences(OurAppConstants.USER_PREFERENCES, MODE_PRIVATE);
                int radius = (int)sharedPreferences.getLong(OurAppConstants.SHARED_PREF_RADIUS,-1);
                if (radius==-1){
                    radius=3;
                }
                yelpApi.getRestaurants(query,radius,mAdapter, true);
            }
        });

        mQueryEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mOptionsVisible){
                    mOptionsVisible=true;
                    animationOptionsOntoScreen();
                }
            }
        });
        mCloseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQueryEntry.setText("");

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter().equals(mPricingAdapter)){
            mPrice = (String) adapterView.getItemAtPosition(i);
        }else if (adapterView.getAdapter().equals(mRadiusAdapter)){
            mRadius = (String) adapterView.getItemAtPosition(i);
        }else if (adapterView.getAdapter().equals(mRatingAdapter)) {
            mRating = (String) adapterView.getItemAtPosition(i);
        }else if(adapterView.getAdapter().equals(mLocationAdapter)) {
            mLocation = (String) adapterView.getItemAtPosition(i);
        }else{
            Toast.makeText(this, "SOMETHING WENT WRONG!", Toast.LENGTH_SHORT).show();
            Log.d("Search Activity", "Adapter not identified");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(this, "What this do?", Toast.LENGTH_SHORT).show();
    }

    public void animateOptionsOffScreen(){
        Animation basicSearchAnimation = AnimationUtils.loadAnimation(this,R.anim.basic_search_out);
        basicSearchAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBasicCardHolder.clearAnimation();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBasicCardHolder.setAnimation(basicSearchAnimation);
        mBasicCardHolder.setVisibility(View.GONE);

        Animation randomSearchAnimation = AnimationUtils.loadAnimation(this, R.anim.random_out);
        randomSearchAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mRandom.clearAnimation();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mRandom.setAnimation(randomSearchAnimation);
        mRandom.setVisibility(View.GONE);

    }

    public void animationOptionsOntoScreen(){
        Animation basicSearchAnimation = AnimationUtils.loadAnimation(this,R.anim.basic_search_in);

        mBasicCardHolder.setAnimation(basicSearchAnimation);
        for(Spinner spinner:mSpinnersInCardView){
            spinner.setEnabled(true);
        }
        mBasicCardHolder.setVisibility(View.VISIBLE);

        Animation randomSearchAnimation = AnimationUtils.loadAnimation(this, R.anim.random_in);
        mRandom.setAnimation(randomSearchAnimation);
        mRandom.setVisibility(View.VISIBLE);

    }

}
