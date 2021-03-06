package shuvalov.nikita.restaurantroulette.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import shuvalov.nikita.restaurantroulette.GoogleResources.GoogleAPI;
import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.RecyclerViewAdapters.SearchActivityRecyclerAdapter;
import shuvalov.nikita.restaurantroulette.RestaurantSearchHelper;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpAPI;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private EditText mQueryEntry;
    private Spinner mPricingSpinner, mRadiusSpinner;
    private Button mSearch, mRandom;
    private RecyclerView mRecyclerView;
    private SearchActivityRecyclerAdapter mAdapter;
    private List<Business> mBusinessList;
    private CardView mBasicCardHolder;
    private boolean mOptionsVisible;
    private ImageView mCloseView;
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences mSharedPreferences;
    private GoogleAPI mGoogleApi;
    private int mOrientation;
    private InputMethodManager mInputMethodManager;
    private ImageView mYelpLogo;

    private ArrayAdapter<CharSequence> mRatingAdapter, mPricingAdapter, mRadiusAdapter, mLocationAdapter;

    private String mRating, mRadius = "show all";//ToDo: Change value into constant
    private String mLocation;
    private long mPrice;

    //ToDo: Add a way to close options to see full list without having to search
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search for Restaurants");

        mGoogleApi = new GoogleAPI();

        mBusinessList = RestaurantSearchHelper.getInstance().getSearchResults();
        checkOrientation();

        mOptionsVisible = true;
        mSharedPreferences = getSharedPreferences(OurAppConstants.USER_PREFERENCES, MODE_PRIVATE);

        mGoogleApiClient = mGoogleApi.callGoogleLocApi(this);

        findViews();
        mBasicCardHolder.setVisibility(View.VISIBLE);

        addAdaptersToSpinners();
        setUpRecyclerView();
        setOnClickListeners();
    }
    public void findViews(){
        mQueryEntry = (EditText)findViewById(R.id.query_entry);
        mRadiusSpinner =(Spinner)findViewById(R.id.radius_spinner);
        mPricingSpinner = (Spinner)findViewById(R.id.price_spinner);
        mSearch = (Button)findViewById(R.id.basic_search);
        mRecyclerView = (RecyclerView)findViewById(R.id.search_recyclerview);
        mRandom = (Button)findViewById(R.id.random_search);
        mBasicCardHolder =(CardView)findViewById(R.id.basic_search_card);
        mCloseView =(ImageView)findViewById(R.id.close_search);
        mYelpLogo = (ImageView)findViewById(R.id.yelp_logo);
    }

    public void addAdaptersToSpinners(){
        mPricingAdapter = ArrayAdapter.createFromResource(this, R.array.price_array,android.R.layout.simple_spinner_item);
        mRadiusAdapter = ArrayAdapter.createFromResource(this, R.array.radius_array,android.R.layout.simple_spinner_item);

        mPricingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRadiusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mPricingSpinner.setAdapter(mPricingAdapter);
        mRadiusSpinner.setAdapter(mRadiusAdapter);

        mPricingSpinner.setSelection(1);
        mRadiusSpinner.setSelection(4);


        mPricingSpinner.setOnItemSelectedListener(this);
        mRadiusSpinner.setOnItemSelectedListener(this);

    }

    public void setUpRecyclerView(){
        RecyclerView.LayoutManager layoutManager;
        if(mOrientation==0){
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        }else{
            layoutManager = new GridLayoutManager(this,2);
        }
        mAdapter = new SearchActivityRecyclerAdapter(mBusinessList);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }
    public void setOnClickListeners(){
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);

                if(mOptionsVisible){
                    animateOptionsOffScreen();
                    mOptionsVisible=false;
                }

                YelpAPI yelpApi = new YelpAPI(view.getContext());
                String query = mQueryEntry.getText().toString();
                mQueryEntry.setText("");
                yelpApi.getRestaurants(query, mPrice,Integer.parseInt(mRadius),mAdapter, false);
            }
        });
        //ToDo:Combine mSearch and mRandom OnclickListener
        mRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
                if(mOptionsVisible){
                    animateOptionsOffScreen();
                    mOptionsVisible=false;
                }


                YelpAPI yelpApi = new YelpAPI(view.getContext());
                String query = mQueryEntry.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences(OurAppConstants.USER_PREFERENCES, MODE_PRIVATE);
                int radius = (int)sharedPreferences.getLong(OurAppConstants.SHARED_PREF_RADIUS,-1)+1;
                if (radius==0){
                    radius=3;
                }
                yelpApi.getRestaurants(query,-1,radius,mAdapter, true);
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
                if (mQueryEntry.getText().toString().isEmpty()) {
                    if(mOptionsVisible){
                        animateOptionsOffScreen();
                        mOptionsVisible=false;
                        if(mInputMethodManager.isActive()){
                            mInputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
                        }
                    }
                } else {
                    mQueryEntry.setText("");
                }
            }
        });
        mYelpLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent yelpIntent =new Intent(Intent.ACTION_VIEW);
                yelpIntent.setData(Uri.parse("https://www.yelp.com/"));
                startActivity(yelpIntent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter().equals(mPricingAdapter)){
            mPrice = i;
        }else if (adapterView.getAdapter().equals(mRadiusAdapter)){
            mRadius = (String) adapterView.getItemAtPosition(i);
        }
        else{
            Toast.makeText(this, "SOMETHING WENT WRONG!", Toast.LENGTH_SHORT).show();
            Log.d("Search Activity", "Adapter not identified");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(this, "What this do?", Toast.LENGTH_SHORT).show();
    }

    public void animateOptionsOffScreen(){
        Animation basicSearchAnimation;
        if (mOrientation == 0) {
            basicSearchAnimation= AnimationUtils.loadAnimation(this,R.anim.basic_search_out);
        }else{
            basicSearchAnimation = AnimationUtils.loadAnimation(this,R.anim.basic_landscape_out);
        }
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

//        Animation randomSearchAnimation;
//        if(mOrientation==0){
//            randomSearchAnimation = AnimationUtils.loadAnimation(this, R.anim.random_out);
//        } else{
//            randomSearchAnimation = AnimationUtils.loadAnimation(this, R.anim.basic_landscape_out);
//        }
//        randomSearchAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                mRandom.clearAnimation();
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        mRandom.setAnimation(randomSearchAnimation);
//        mRandom.setVisibility(View.GONE);

    }

    public void animationOptionsOntoScreen(){
        Animation basicSearchAnimation;

        if(mOrientation==0){
            basicSearchAnimation= AnimationUtils.loadAnimation(this,R.anim.basic_search_in);
        }else{
            basicSearchAnimation = AnimationUtils.loadAnimation(this, R.anim.basic_landscape_in);
        }
        mBasicCardHolder.setAnimation(basicSearchAnimation);
        mBasicCardHolder.setVisibility(View.VISIBLE);
//
//        Animation randomSearchAnimation;
//
//        if(mOrientation ==0){
//            randomSearchAnimation = AnimationUtils.loadAnimation(this, R.anim.random_in);
//        }else{
//            randomSearchAnimation = AnimationUtils.loadAnimation(this, R.anim.basic_landscape_in);
//        }
//        mRandom.setAnimation(randomSearchAnimation);
//        mRandom.setVisibility(View.VISIBLE);

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
    public void onConnected(@Nullable Bundle bundle) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(OurAppConstants.USER_LAST_LAT,mGoogleApi.getUserLat(mGoogleApiClient));
        editor.putString(OurAppConstants.USER_LAST_LON, mGoogleApi.getUserLon(mGoogleApiClient));
        if(editor.commit()){
            Log.d("Search Activity", "Location successfully saved");
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void checkOrientation(){
        WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display mDisplay = mWindowManager.getDefaultDisplay();
        mOrientation=mDisplay.getRotation();

        Log.d("ORIENTATION_TEST", "getOrientation(): " + mOrientation);
    }
    public void connectToYelp(){//Lazy implementation

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
