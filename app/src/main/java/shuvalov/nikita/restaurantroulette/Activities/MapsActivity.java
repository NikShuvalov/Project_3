package shuvalov.nikita.restaurantroulette.Activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.RestaurantSearchHelper;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public Business mBusiness;
    public int mBusinessPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Gets Instance of the Business
        mBusinessPosition = getIntent().getIntExtra(OurAppConstants.BUSINESS_POSITION_INTENT_KEY, -1);
        mBusiness = RestaurantSearchHelper.getInstance().getBusinessAtPosition(mBusinessPosition);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // TODO: Change GA LAT and LON to user location
        // User Location (temporarily using GA Lat and Lng)
        LatLng userLocation = new LatLng(OurAppConstants.GA_LATITUDE, OurAppConstants.GA_LONGITUDE);
        mMap.addMarker(new MarkerOptions()
                .position(userLocation)
                .title("General Assembly"));

        // Business Location
        LatLng businessCoordinates = new LatLng(mBusiness.getCoordinates().getLatitude(),
                mBusiness.getCoordinates().getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(businessCoordinates)
                .title(mBusiness.getName()));

        // Changes Zoom based on the distance between GA Location and Business
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(userLocation);
        builder.include(businessCoordinates);
        LatLngBounds bounds = builder.build();

        CameraUpdate latLngBounds = CameraUpdateFactory.newLatLngBounds(bounds, 150);
        mMap.animateCamera(latLngBounds);
    }
}
