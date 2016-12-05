package shuvalov.nikita.restaurantroulette.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import shuvalov.nikita.restaurantroulette.R;



public class MainActivity extends AppCompatActivity {
    CardView mBasicSearch, mDateNight, mRoulette;

    //ToDo:Remove debug Button once we're done with it
    Button mDebugButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setClickListener();

        //ToDo:Remove function and call once we're done with it
        setDebug();
    }

    public void findViews(){
        mBasicSearch = (CardView) findViewById(R.id.search_card_holder);
        mDateNight = (CardView)findViewById(R.id.date_night_card_holder);
        mRoulette = (CardView)findViewById(R.id.roulette_card_holder);
        mDebugButton = (Button)findViewById(R.id.debug_button);
    }

    public void setDebug(){
        mDebugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent debugIntent = new Intent(view.getContext(),DebugActivity.class);
                startActivity(debugIntent);
            }
        });
    }

    public View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            Intent intent;
            switch (id) {
                case R.id.search_card_holder:
                    intent = new Intent(view.getContext(), SearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.date_night_card_holder:
                    break;
                case R.id.roulette_card_holder:
                    break;
            }

        }
    };

    public void setClickListener () {
        mBasicSearch.setOnClickListener(mListener);
        mDateNight.setOnClickListener(mListener);
        mRoulette.setOnClickListener(mListener);
    }
}
