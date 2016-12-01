package shuvalov.nikita.restaurantroulette.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import shuvalov.nikita.restaurantroulette.R;

public class DebugActivity extends AppCompatActivity {
    Button mProfile, mShare, mSearch, mDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);


        findViews();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if (view.equals(mProfile)) {
                    intent = new Intent(view.getContext(), UserSettingsActivity.class);
                } else if (view.equals(mShare)) {
                    intent = new Intent(view.getContext(), ShareActivity.class);
                } else if (view.equals(mSearch)) {
                    intent = new Intent(view.getContext(), SearchActivity.class);
                } else if (view.equals(mDetail)) {
                    intent = new Intent(view.getContext(), DetailActivity.class);
                }
                if (intent != null) {
                    startActivity(intent);
                }
            }
        };
        mProfile.setOnClickListener(onClickListener);
        mShare.setOnClickListener(onClickListener);
        mSearch.setOnClickListener(onClickListener);
        mDetail.setOnClickListener(onClickListener);


    }
    public void findViews(){
        mProfile = (Button)findViewById(R.id.profile_settings_Debug);
        mShare = (Button)findViewById(R.id.share_activity);
        mSearch = (Button)findViewById(R.id.basic_search);
        mDetail = (Button)findViewById(R.id.detail_activity);
    }
}
