package shuvalov.nikita.restaurantroulette.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import shuvalov.nikita.restaurantroulette.R;

public class SearchActivity extends AppCompatActivity {
    EditText mQueryEntry;
    Spinner mRatingSpinner, mPricingSpinner, mRadiusSpinner;
    Button mSearch;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findViews();

        //ToDo: Populate spinner items using String ArrayList
        ArrayAdapter<CharSequence> ratingAdapter = ArrayAdapter.createFromResource(this,,android.R.layout.simple_spinner_item);

//                ArrayAdapter<CharSequence> pricingAdapter = ArrayAdapter.createFromResource(this,,android.R.layout.simple_spinner_item);
//                ArrayAdapter<CharSequence> radiusAdapter = ArrayAdapter.createFromResource(this,,,android.R.layout.simple_spinner_item);






    }
    public void findViews(){
        mQueryEntry = (EditText)findViewById(R.id.query_entry);
        mRatingSpinner =(Spinner)findViewById(R.id.rating_spinner);
        mRadiusSpinner =(Spinner)findViewById(R.id.radius_spinner);
        mPricingSpinner = (Spinner)findViewById(R.id.price_spinner);
        mSearch = (Button)findViewById(R.id.basic_search);
        mRecyclerView = (RecyclerView)findViewById(R.id.search_recyclerview);
    }
}
