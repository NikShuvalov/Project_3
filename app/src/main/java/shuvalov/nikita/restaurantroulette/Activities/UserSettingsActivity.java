package shuvalov.nikita.restaurantroulette.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import shuvalov.nikita.restaurantroulette.R;

public class UserSettingsActivity extends AppCompatActivity {
    boolean isFirstTimeRating = true;
    boolean isFirstTimePrice = true;
    boolean isFirstTimeRadius = true;
    boolean isFirstTimeSearchResult = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        SharedPreferences sharedPreferences = getSharedPreferences("user_preferences",
                Context.MODE_PRIVATE);
        final long ratingSavedPosition = sharedPreferences.getLong("rating", -1);
        final long priceSavedPosition = sharedPreferences.getLong("pricing", -1);
        final long radiusSavedPosition = sharedPreferences.getLong("radius", -1);
        final long searchResultsSavedPosition = sharedPreferences.getLong("search_results", -1);

        Log.d("Serkan", "onCreate: " + ratingSavedPosition);

        //Spinner for RATINGS
        final Spinner spinnerRating = (Spinner) findViewById(R.id.spinner_rating);
        ArrayAdapter<CharSequence> adapterRating = ArrayAdapter.createFromResource(this,
                R.array.rating_array, android.R.layout.simple_spinner_item);
        adapterRating.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (ratingSavedPosition != -1) {
            spinnerRating.post(new Runnable() {
                @Override
                public void run() {
                    spinnerRating.setSelection((int) ratingSavedPosition);
                }
            });
        }
        spinnerRating.setAdapter(adapterRating);

        spinnerRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                long position = adapterView.getItemIdAtPosition(i);
                if (isFirstTimeRating) {
                    isFirstTimeRating = false;
                } else {
                    SharedPreferences sharedPreferences =  getSharedPreferences("user_preferences",
                                    Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("rating", position);
                    editor.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

        //Spinner for PRICE
        final Spinner spinnerPrice = (Spinner) findViewById(R.id.spinner_pricing);
        ArrayAdapter<CharSequence> adapterPrice = ArrayAdapter.createFromResource(this,
                R.array.price_array, android.R.layout.simple_spinner_item);
        adapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (priceSavedPosition != -1) {
            spinnerPrice.post(new Runnable() {
                @Override
                public void run() {
                    spinnerPrice.setSelection((int) priceSavedPosition);
                }
            });
        }
        spinnerPrice.setAdapter(adapterPrice);

        spinnerPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Long position = adapterView.getItemIdAtPosition(i);
                if (isFirstTimePrice) {
                    isFirstTimePrice = false;
                } else {
                    SharedPreferences sharedPreferences =  getSharedPreferences("user_preferences",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("pricing", position);
                    editor.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

        //Spinner for Radius
        final Spinner spinnerRadius = (Spinner) findViewById(R.id.spinner_radius);
        ArrayAdapter<CharSequence> adapterRadius = ArrayAdapter.createFromResource(this,
                R.array.radius_array, android.R.layout.simple_spinner_item);
        adapterRadius.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (radiusSavedPosition != -1) {
            spinnerRadius.post(new Runnable() {
                @Override
                public void run() {
                    spinnerRadius.setSelection((int) radiusSavedPosition);
                }
            });
        }
        spinnerRadius.setAdapter(adapterRadius);

        spinnerRadius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Long position = adapterView.getItemIdAtPosition(i);
                if (isFirstTimeRadius) {
                    isFirstTimeRadius = false;
                } else {
                    SharedPreferences sharedPreferences =  getSharedPreferences("user_preferences",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("radius", position);
                    editor.commit();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

        //Spinner for Radius
        final Spinner spinnerResult = (Spinner) findViewById(R.id.spinner_result);
        ArrayAdapter<CharSequence> adapterResult = ArrayAdapter.createFromResource(this,
                R.array.result_array, android.R.layout.simple_spinner_item);
        adapterResult.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (searchResultsSavedPosition != -1) {
            spinnerResult.post(new Runnable() {
                @Override
                public void run() {
                    spinnerResult.setSelection((int) searchResultsSavedPosition);
                }
            });
        }
        spinnerResult.setAdapter(adapterResult);

        spinnerResult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Long position = adapterView.getItemIdAtPosition(i);
                if (isFirstTimeSearchResult) {
                    isFirstTimeSearchResult = false;
                } else {
                    SharedPreferences sharedPreferences =  getSharedPreferences("user_preferences",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("search_results", position);
                    editor.commit();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

    }


}
