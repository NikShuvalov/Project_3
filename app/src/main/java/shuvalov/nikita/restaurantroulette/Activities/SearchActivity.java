package shuvalov.nikita.restaurantroulette.Activities;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.RecyclerViewAdapters.SearchActivityRecyclerAdapter;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpAPI;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText mQueryEntry;
    private Spinner mRatingSpinner, mPricingSpinner, mRadiusSpinner, mLocationSpinner;
    private Button mSearch, mRandom;
    private RecyclerView mRecyclerView;
    private SearchActivityRecyclerAdapter mAdapter;
    private List<Business> mBusinessList;

    private ArrayAdapter<CharSequence> mRatingAdapter, mPricingAdapter, mRadiusAdapter, mLocationAdapter;

    private String mPrice, mRating, mRadius = "show all";//ToDo: Change value into constant
    private String mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findViews();
        addAdaptersToSpinners();
//        setUpRecyclerView();
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
//        mAdapter = new SearchActivityRecyclerAdapter() //ToDo:Add a list to go in here.
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }
    public void setOnClickListeners(){
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ToDo: Use values that are put into the spinners to perform a search.
                //ToDo: Move options and parameters off-screen
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
                Location mockLocation = new Location(LOCATION_SERVICE);
                mockLocation.setLongitude(OurAppConstants.GA_LONGITUDE);
                mockLocation.setLatitude(OurAppConstants.GA_LATITUDE);
                YelpAPI yelpApi = new YelpAPI(view.getContext(),mockLocation);
                yelpApi.getRestaurants(mQueryEntry.getText().toString(),Integer.parseInt(mRadius));
            }
        });
        mRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter().equals(mPricingAdapter)){
            mPrice = (String) adapterView.getItemAtPosition(i);
            Toast.makeText(this, "Price value changed to "+mPrice, Toast.LENGTH_SHORT).show();
        }else if (adapterView.getAdapter().equals(mRadiusAdapter)){
            mRadius = (String) adapterView.getItemAtPosition(i);
            Toast.makeText(this, "Radius value changed to "+mRadius, Toast.LENGTH_SHORT).show();
        }else if (adapterView.getAdapter().equals(mRatingAdapter)) {
            mRating = (String) adapterView.getItemAtPosition(i);
            Toast.makeText(this, "Rating value changed to " + mRating, Toast.LENGTH_SHORT).show();
        }else if(adapterView.getAdapter().equals(mLocationAdapter)) {
            mLocation = (String) adapterView.getItemAtPosition(i);
            Toast.makeText(this, "Location being used: "+ mLocation, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "SOMETHING WENT WRONG!", Toast.LENGTH_SHORT).show();
            Log.d("Search Activity", "Adapter not identified");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(this, "What this do?", Toast.LENGTH_SHORT).show();
    }
}
