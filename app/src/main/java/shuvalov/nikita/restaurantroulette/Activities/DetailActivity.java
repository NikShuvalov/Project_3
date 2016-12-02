package shuvalov.nikita.restaurantroulette.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.RestaurantSearchHelper;
import shuvalov.nikita.restaurantroulette.UberResources.UberAPI;
import shuvalov.nikita.restaurantroulette.UberResources.UberAPIConstants;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Location;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int REQUEST_CODE_LOCATION = 1;

    public TextView mBusinessName, mPricing, mUberEstimate,
            mPhoneNumber, mAddress, mOpenOrClosed;
    public ImageView mBusinessImage, mShare, mPhoneButton,
            mFirstStar, mSecondStar, mThirdStar, mFourthStar, mFifthStar;
    public Business mBusiness;
    public int mBusinessPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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

        // Map Setup
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button button = (Button) findViewById(R.id.map_activity_intent);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        // Temporarily Commented out
//        UberAPI uberAPI = new UberAPI(this);
//        uberAPI.getEstimateAsString(40.73873873873874f, -73.97987613997012f, 40.5945945945946f, -73.9387914156729f, UberAPIConstants.UBER_SERVER_ID);
//        uberAPI.setUberApiResultListener(new UberAPI.UberApiResultListener() {
//            @Override
//            public void onUberEstimateReady(String estimate) {
//                mUberEstimate.setText(estimate);
//            }
//        });
    }

    public void bindDataToView(Business business) {
        mBusinessName.setText(business.getName());
        mPricing.setText(business.getPrice());
        mPhoneNumber.setText(business.getPhone());

        Location location = business.getLocation();
        mAddress.setText(location.getAddress1() + " " + location.getCity());
        mOpenOrClosed.setText((business.getIsClosed()) ? "Closed" : "Open");

        reviewStars();

        // Picasso method to get images for each business
        Picasso.with(this)
                .load(mBusiness.getImageUrl())
                .into(mBusinessImage);
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
    public void onMapReady(GoogleMap googleMap) {
        LatLng ourLocation = new LatLng(40.73873873873874, -73.97987613997012);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE_LOCATION);

        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ourLocation, 13));

            googleMap.addMarker(new MarkerOptions()
                    .title("General Assembly")
                    .snippet("Where We Study ADI")
                    .position(ourLocation));
        }
    }
}
