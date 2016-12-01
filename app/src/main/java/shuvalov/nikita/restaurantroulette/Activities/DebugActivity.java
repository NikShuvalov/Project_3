package shuvalov.nikita.restaurantroulette.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import shuvalov.nikita.restaurantroulette.R;

public class DebugActivity extends AppCompatActivity {
    Button mDebug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        findViews();
        mDebug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserSettingsActivity.class);
                startActivity(intent);
            }
        });
    }
    public void findViews(){
        mDebug = (Button)findViewById(R.id.profile_settings_Debug);
    }
}
