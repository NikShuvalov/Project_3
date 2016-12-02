package shuvalov.nikita.restaurantroulette.Activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpAPI;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpJobService;

import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_NOTIFICATION_CHECKED;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_NUM_OF_RESULTS;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_PRICING;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_RADIUS;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.SHARED_PREF_RATING;
import static shuvalov.nikita.restaurantroulette.OurAppConstants.USER_PREFERENCES;

public class UserSettingsActivity extends AppCompatActivity {
    boolean isFirstTimeRating = true;
    boolean isFirstTimePrice = true;
    boolean isFirstTimeRadius = true;
    boolean isFirstTimeSearchResult = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES,
                Context.MODE_PRIVATE);
        final long ratingSavedPosition = sharedPreferences.getLong(SHARED_PREF_RATING, -1);
        final long priceSavedPosition = sharedPreferences.getLong(SHARED_PREF_PRICING, -1);
        final long radiusSavedPosition = sharedPreferences.getLong(SHARED_PREF_RADIUS, -1);
        final long searchResultsSavedPosition = sharedPreferences.getLong(SHARED_PREF_NUM_OF_RESULTS, -1);
        boolean isDealsEnabled = sharedPreferences.getBoolean(SHARED_PREF_NOTIFICATION_CHECKED, false);

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
                    SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
                                    Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(SHARED_PREF_RATING, position);
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
                    SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(SHARED_PREF_PRICING, position);
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
                    SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(SHARED_PREF_RADIUS, position);
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
                    SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(SHARED_PREF_NUM_OF_RESULTS, position);
                    editor.commit();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

        Button buttonHome = (Button) findViewById(R.id.button_home);
        Button buttonWork = (Button) findViewById(R.id.button_work);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //ToDo: Change the 1f, 2f with actual location data from Google Location API.

                editor.putFloat("lat_home", 1f);
                editor.putFloat("lon_home", 2f);
                editor.commit();
            }
        });

        buttonWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //ToDo: Change the 1f, 2f with actual location data from Google Location API.

                editor.putFloat("lat_work", 1f);
                editor.putFloat("lon_work", 2f);
                editor.commit();
            }
        });

        final CheckBox checkBox = (CheckBox) findViewById(R.id.deals_checkbox);

        if (isDealsEnabled) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    Toast.makeText(UserSettingsActivity.this, "Create service", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SHARED_PREF_NOTIFICATION_CHECKED, true);
                    editor.commit();

                    //Create Jobscheduler Service

                    PersistableBundle periodicPersistableBundle = new PersistableBundle();
                    periodicPersistableBundle.putString("Type","Periodic Yelp API CHECK");

                    JobInfo periodicJobInfo = new JobInfo.Builder(OurAppConstants.PERIODIC_JOB_ID,
                            new ComponentName(UserSettingsActivity.this, YelpJobService.class))
                            .setExtras(periodicPersistableBundle)
                            .setPeriodic(10000) //RUN IT EVERY 10 secs
                            .build();

                    JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                    jobScheduler.schedule(periodicJobInfo);

                } else {
                    Toast.makeText(UserSettingsActivity.this, "Kill service", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SHARED_PREF_NOTIFICATION_CHECKED, false);
                    editor.commit();

                    //Kill Jobscheduler Service
                    JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                    jobScheduler.cancelAll();
                }
            }
        });

    }
}
