package shuvalov.nikita.restaurantroulette.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import shuvalov.nikita.restaurantroulette.R;

public class UserSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        //Spinner for RATINGS
        Spinner spinnerRATING = (Spinner) findViewById(R.id.spinner_rating);
        ArrayAdapter<CharSequence> adapterRating = ArrayAdapter.createFromResource(this,
                R.array.rating_array, android.R.layout.simple_spinner_item);
        adapterRating.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRATING.setAdapter(adapterRating);

        //Spinner for PRICE
        Spinner spinnerPrice = (Spinner) findViewById(R.id.spinner_pricing);
        ArrayAdapter<CharSequence> adapterPrice = ArrayAdapter.createFromResource(this,
                R.array.price_array, android.R.layout.simple_spinner_item);
        adapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrice.setAdapter(adapterPrice);

        //Spinner for Radius
        Spinner spinnerRadius = (Spinner) findViewById(R.id.spinner_radius);
        ArrayAdapter<CharSequence> adapterRadius = ArrayAdapter.createFromResource(this,
                R.array.radius_array, android.R.layout.simple_spinner_item);
        adapterRadius.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRadius.setAdapter(adapterRadius);

        //Spinner for Radius
        Spinner spinnerResult = (Spinner) findViewById(R.id.spinner_result);
        ArrayAdapter<CharSequence> adapterResult = ArrayAdapter.createFromResource(this,
                R.array.result_array, android.R.layout.simple_spinner_item);
        adapterResult.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerResult.setAdapter(adapterResult);


    }


}
