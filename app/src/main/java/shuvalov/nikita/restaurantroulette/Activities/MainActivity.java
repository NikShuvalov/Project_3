package shuvalov.nikita.restaurantroulette.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.UberResources.UberAPI;
import shuvalov.nikita.restaurantroulette.UberResources.UberAPIConstants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UberAPI uberAPI = new UberAPI();

        uberAPI.getEstimateAsString(37.625732f, -122.377807f, 37.785114f, -122.406677f,
                UberAPIConstants.UBER_SERVER_ID);

        uberAPI.getEstimateAsIntAverage(37.625732f, -122.377807f, 37.785114f, -122.406677f,
                UberAPIConstants.UBER_SERVER_ID);
    }
}
