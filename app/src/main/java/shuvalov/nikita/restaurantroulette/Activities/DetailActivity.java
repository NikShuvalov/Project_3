package shuvalov.nikita.restaurantroulette.Activities;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import shuvalov.nikita.restaurantroulette.GoogleResources.GoogleAPI;
import shuvalov.nikita.restaurantroulette.GoogleResources.GoogleAPIConstants;
import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.PicassoImageManager;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.RestaurantSearchHelper;
import shuvalov.nikita.restaurantroulette.UberResources.UberAPI;
import shuvalov.nikita.restaurantroulette.UberResources.UberAPIConstants;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Location;

import static shuvalov.nikita.restaurantroulette.Activities.UserSettingsActivity.verifyLocationPermissions;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LAT;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LOCATION;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_LAST_LON;

public class DetailActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    public TextView mBusinessName, mPricing, mUberEstimate,
            mPhoneNumber, mAddress, mOpenOrClosed;
    public ImageView mBusinessImage, mShare, mPhoneButton, mMapFrame,
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

        // Review Stars
        mFirstStar = (ImageView) findViewById(R.id.first_star);
        mSecondStar = (ImageView) findViewById(R.id.second_star);
        mThirdStar = (ImageView) findViewById(R.id.third_star);
        mFourthStar = (ImageView) findViewById(R.id.fourth_star);
        mFifthStar = (ImageView) findViewById(R.id.fifth_star);
        
        // Gets Instance of the Business
        mBusinessPosition = getIntent().getIntExtra(OurAppConstants.BUSINESS_POSITION_INTENT_KEY, -1);
        mBusiness = RestaurantSearchHelper.getInstance().getBusinessAtPosition(mBusinessPosition);
        bindDataToView(mBusiness);

        // Phone Button OnClickListener to Open Dialer Intent
        mPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mBusiness.getPhone()));
                startActivity(intent);
            }
        });

        // Share Button OnClickListener to Share Activity
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, ShareActivity.class);
                intent.putExtra(OurAppConstants.BUSINESS_POSITION_INTENT_KEY, mBusinessPosition);
                startActivity(intent);
            }
        });

        // Google Map OnClickListener to Map Activity
        mMapFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, MapsActivity.class);
                intent.putExtra(OurAppConstants.BUSINESS_POSITION_INTENT_KEY, mBusinessPosition);
                startActivity(intent);
            }
        });
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

            double businessLat = mBusiness.getCoordinates().getLatitude();
            float businessLatFloat = (float) businessLat;

            double businessLon = mBusiness.getCoordinates().getLongitude();
            float businessLonFloat = (float) businessLon;

            UberAPI uberAPI = new UberAPI(this);
            // TODO: Change Start Lat, Lon to User Location

            uberAPI.getEstimateAsString(userLat, userLon,
                    businessLatFloat, businessLonFloat, UberAPIConstants.UBER_SERVER_ID);
            uberAPI.setUberApiResultListener(new UberAPI.UberApiResultListener() {
                @Override
                public void onUberEstimateReady(String estimate) {
                    mUberEstimate.setText(estimate);
                }
            });

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
}
