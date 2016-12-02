package shuvalov.nikita.restaurantroulette.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.UberResources.UberAPI;
import shuvalov.nikita.restaurantroulette.UberResources.UberAPIConstants;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Location;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int REQUEST_CODE_LOCATION = 1;

    public TextView mBusinessName, mPrice,
            mPhoneNumber, mAddress, mOpenOrClosed;
    public ImageView mBusinessImage, mShare, mPhoneButton,
            mFirstStar, mSecondStar, mThirdStar, mFourthStar, mFifthStar;
    public Business mBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Reference to Views
        mBusinessName = (TextView) findViewById(R.id.business_name);
        mPrice = (TextView) findViewById(R.id.pricing);
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

        // TODO: Create Logic for Review Stars

        // Picasso method to get images for each business
        Picasso.with(this)
                .load("http://cdn2-www.dogtime.com/assets/uploads/gallery/30-impossibly-cute-puppies/impossibly-cute-puppy-8.jpg") // TODO: ADD URL
                .into(mBusinessImage);

        // TODO: add bindDataToView() method

        // Phone Button OnClickListener to Open Dialer Intent
        mPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: change 0123456789 to business.getPhone()
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0123456789"));
                startActivity(intent);
            }
        });

        // TODO: add OnClickListener for Share ImageView

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
//                mPrice.setText(estimate);
//            }
//        });
    }

    public void bindDataToView(Business business) {
        mBusinessName.setText(business.getName());
        mPrice.setText(business.getPrice());
        mPhoneNumber.setText(business.getPhone());

        Location location = business.getLocation();
        mAddress.setText(location.getAddress1() + " " + location.getCity());
        mOpenOrClosed.setText((business.getIsClosed()) ? "Closed" : "Open");
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
