package shuvalov.nikita.restaurantroulette.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import shuvalov.nikita.restaurantroulette.GoogleResources.GoogleAPI;
import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.PicassoImageManager;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.RestaurantSearchHelper;
import shuvalov.nikita.restaurantroulette.UberResources.UberAPI;
import shuvalov.nikita.restaurantroulette.UberResources.UberAPIConstants;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Coordinates;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Location;

import static shuvalov.nikita.restaurantroulette.Activities.UserSettingsActivity.verifyLocationPermissions;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LAT;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LOCATION;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LON;

public class DetailActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "DetailActivity";

    public TextView mBusinessName, mPricing, mUberEstimate,
            mPhoneNumber, mAddress, mOpenOrClosed;
    public ImageView mBusinessImage, mShare, mPhoneButton, mMapFrame, mYelpLogo,
            mFirstStar, mSecondStar, mThirdStar, mFourthStar, mFifthStar;
    public Business mBusiness;
    public int mBusinessPosition;
    public GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        GoogleAPI googleAPI = new GoogleAPI();
        mGoogleApiClient = googleAPI.callGoogleLocApi(this);

        // Reference to Views
        mBusinessName = (TextView) findViewById(R.id.business_name);
        mPricing = (TextView) findViewById(R.id.pricing);
        mUberEstimate = (TextView) findViewById(R.id.uber_estimate);
        mPhoneNumber = (TextView) findViewById(R.id.phone_number);
        mAddress = (TextView) findViewById(R.id.address);
        mOpenOrClosed = (TextView) findViewById(R.id.open_or_closed);

        mBusinessImage = (ImageView) findViewById(R.id.business_image);
        mShare = (ImageView) findViewById(R.id.share_image_view);
        mPhoneButton = (ImageView) findViewById(R.id.call_image_view);
        mMapFrame = (ImageView) findViewById(R.id.map_imageview);
        mYelpLogo = (ImageView) findViewById(R.id.detail_yelp_logo);

        // Review Stars
        mFirstStar = (ImageView) findViewById(R.id.first_star);
        mSecondStar = (ImageView) findViewById(R.id.second_star);
        mThirdStar = (ImageView) findViewById(R.id.third_star);
        mFourthStar = (ImageView) findViewById(R.id.fourth_star);
        mFifthStar = (ImageView) findViewById(R.id.fifth_star);

        String origin = getIntent().getStringExtra(OurAppConstants.ORIGIN);
        if(origin.equals(OurAppConstants.NOTIFICATION_ORIGIN)){
            Log.d(TAG, "onCreate: Arrived from notification" );
            //For Business Obj
            String image_url = getIntent().getStringExtra(YelpAPIConstants.NOTIF_IMAGE_URL);
            String phone_number = getIntent().getStringExtra(YelpAPIConstants.NOTIF_PHONE_NUMBER);
            boolean is_closed = getIntent().getBooleanExtra(YelpAPIConstants.NOTIF_IS_CLOSED, false);
            String business_url = getIntent().getStringExtra(YelpAPIConstants.NOTIF_BUSINESS_URL);
            String business_id = getIntent().getStringExtra(YelpAPIConstants.NOTIF_BUSINESS_ID);
            String price = getIntent().getStringExtra(YelpAPIConstants.NOTIF_PRICE);
            int review_count = getIntent().getIntExtra(YelpAPIConstants.NOTIF_REVIEW_COUNT, 0);
            double rating = getIntent().getDoubleExtra(YelpAPIConstants.NOTIF_RATING, 0);
            double distance = getIntent().getDoubleExtra(YelpAPIConstants.NOTIF_DISTANCE, 0);
            String name = getIntent().getStringExtra(YelpAPIConstants.NOTIF_BUSINESS_NAME);
            //For Location obj
            String address_1 = getIntent().getStringExtra(YelpAPIConstants.NOTIF_ADDRESS_1);
            String city = getIntent().getStringExtra(YelpAPIConstants.NOTIF_CITY);
            //For Coordinates
            double lat = getIntent().getDoubleExtra(YelpAPIConstants.NOTIF_LATITUTE, 0);
            double lon = getIntent().getDoubleExtra(YelpAPIConstants.NOTIF_LONGITUDE, 0);

            Location location = new Location(address_1, null, null, city, null, null, null);
            Coordinates coordinates = new Coordinates(lat, lon);
            mBusiness = new Business(null, coordinates, distance, business_id, image_url, is_closed,
                    location, name, phone_number, price, rating, review_count, business_url);
            RestaurantSearchHelper.getInstance().addBusinessToList(mBusiness);


        }else if(origin.equals(OurAppConstants.SEARCH_ORIGIN)){
            // Gets Instance of the Business
            mBusinessPosition = getIntent().getIntExtra(OurAppConstants.BUSINESS_POSITION_INTENT_KEY, -1);
            mBusiness = RestaurantSearchHelper.getInstance().getBusinessAtPosition(mBusinessPosition);
        }

        bindDataToView(mBusiness);

        // Phone Button OnClickListener to Open Dialer Intent
        mPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + mBusiness.getPhone()));
                startActivity(callIntent);
            }
        });

        // Share Button OnClickListener to Share Activity
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(DetailActivity.this, ShareActivity.class);
                shareIntent.putExtra(OurAppConstants.BUSINESS_POSITION_INTENT_KEY, mBusinessPosition);
                startActivity(shareIntent);
            }
        });

        // Google Map OnClickListener to Map Activity
        mMapFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(DetailActivity.this, MapsActivity.class);
                mapIntent.putExtra(OurAppConstants.BUSINESS_POSITION_INTENT_KEY, mBusinessPosition);
                mapIntent.putExtra("origin", "detail");
                startActivity(mapIntent);
            }
        });

        // Yelp Logo OnClickListener to Yelp
        mYelpLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent yelpIntent = new Intent(Intent.ACTION_VIEW);
                yelpIntent.setData(Uri.parse(mBusiness.getUrl()));
                startActivity(yelpIntent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mBusiness.getName());
    }

    public void bindDataToView(Business business) {
        mBusinessName.setText(business.getName());
        mPricing.setText(business.getPrice());
        mPhoneNumber.setText(business.getPhone());

        Location location = business.getLocation();
        mAddress.setText(location.getAddress1() + ", " + location.getCity());
        mOpenOrClosed.setText((business.getIsClosed()) ? "Closed" : "Open");

        mapSetup();
        reviewStars();

        // Picasso method to get images for each business
        Picasso.with(this)
                .load(mBusiness.getImageUrl())
                .into(mBusinessImage);
    }

    public void mapSetup(){
        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        int display_width = size.x;

        String baseUrl = "https://maps.googleapis.com/maps/api/staticmap";
        String businessCoordinates = String.valueOf(mBusiness.getCoordinates().getLatitude() + ","
                + String.valueOf(mBusiness.getCoordinates().getLongitude()));
        String zoomText = "18";
        String sizeText = String.valueOf(display_width) + "x" + "300";
        String key = "AIzaSyA6Q8c-2DtOC2WKvOx3vFU7gBV_ZnwL-VU";

        String searchText = baseUrl + "?" + "center=" + businessCoordinates +
                "&zoom=" + zoomText +
                "&size=" + sizeText +
                "&key=" + key +
                "&markers=" + businessCoordinates;

        PicassoImageManager picassoImageManager = new PicassoImageManager(this, mMapFrame);
        picassoImageManager.setImageFromUrl(searchText);
    }

    public void reviewStars(){
        if (mBusiness.getRating() == 1) {
            mFirstStar.setImageResource(R.drawable.one_star_rating);
        } else if (mBusiness.getRating() == 1.5) {
            mFirstStar.setImageResource(R.drawable.one_star_rating);
            mSecondStar.setImageResource(R.drawable.one_half_star_rating);
        } else if (mBusiness.getRating() == 2) {
            mFirstStar.setImageResource(R.drawable.two_star_rating);
            mSecondStar.setImageResource(R.drawable.two_star_rating);
        } else if (mBusiness.getRating() == 2.5) {
            mFirstStar.setImageResource(R.drawable.two_star_rating);
            mSecondStar.setImageResource(R.drawable.two_star_rating);
            mThirdStar.setImageResource(R.drawable.two_half_star_rating);
        } else if (mBusiness.getRating() == 3) {
            mFirstStar.setImageResource(R.drawable.three_star_rating);
            mSecondStar.setImageResource(R.drawable.three_star_rating);
            mThirdStar.setImageResource(R.drawable.three_star_rating);
        } else if (mBusiness.getRating() == 3.5) {
            mFirstStar.setImageResource(R.drawable.three_star_rating);
            mSecondStar.setImageResource(R.drawable.three_star_rating);
            mThirdStar.setImageResource(R.drawable.three_star_rating);
            mFourthStar.setImageResource(R.drawable.three_half_star_rating);
        } else if (mBusiness.getRating() == 4) {
            mFirstStar.setImageResource(R.drawable.four_star_rating);
            mSecondStar.setImageResource(R.drawable.four_star_rating);
            mThirdStar.setImageResource(R.drawable.four_star_rating);
            mFourthStar.setImageResource(R.drawable.four_star_rating);
        } else if (mBusiness.getRating() == 4.5) {
            mFirstStar.setImageResource(R.drawable.four_star_rating);
            mSecondStar.setImageResource(R.drawable.four_star_rating);
            mThirdStar.setImageResource(R.drawable.four_star_rating);
            mFourthStar.setImageResource(R.drawable.four_star_rating);
            mFifthStar.setImageResource(R.drawable.four_half_star_rating);
        } else if (mBusiness.getRating() == 5) {
            mFirstStar.setImageResource(R.drawable.five_star_rating);
            mSecondStar.setImageResource(R.drawable.five_star_rating);
            mThirdStar.setImageResource(R.drawable.five_star_rating);
            mFourthStar.setImageResource(R.drawable.five_star_rating);
            mFifthStar.setImageResource(R.drawable.five_star_rating);
        }
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

            // Uber Estimate
            double businessLat = mBusiness.getCoordinates().getLatitude();
            float businessLatFloat = (float) businessLat;

            double businessLon = mBusiness.getCoordinates().getLongitude();
            float businessLonFloat = (float) businessLon;

            UberAPI uberAPI = new UberAPI(this);
            uberAPI.getEstimateAsString(Float.parseFloat(userLat), Float.parseFloat(userLon),
                    businessLatFloat, businessLonFloat, UberAPIConstants.UBER_SERVER_ID);
            uberAPI.setUberApiResultListener(new UberAPI.UberApiResultListener() {
                @Override
                public void onUberEstimateReady(String estimate) {
                    mUberEstimate.setText(estimate);
                }
            });

            Log.d(TAG, "onConnected: " + userLat + " / " + userLon);
        } else {
            verifyLocationPermissions(this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionFailed: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
