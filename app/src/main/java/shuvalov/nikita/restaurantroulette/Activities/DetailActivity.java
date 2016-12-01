package shuvalov.nikita.restaurantroulette.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Category;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Location;

public class DetailActivity extends AppCompatActivity {
    public TextView mBusinessName, mRating, mPrice,
            mPhoneNumber, mAddress, mOpenOrClosed;
    public ImageView mBusinessImage, mShare, mPhoneButton, mMapFrame;
    public Business mBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Reference to Views
        mBusinessName = (TextView) findViewById(R.id.business_name);
        mRating = (TextView) findViewById(R.id.rating);
        mPrice = (TextView) findViewById(R.id.pricing);
        mPhoneNumber = (TextView) findViewById(R.id.phone_number);
        mAddress = (TextView) findViewById(R.id.address);
        mOpenOrClosed = (TextView) findViewById(R.id.open_or_closed);

        // TODO: add bindDataToView() method

        mBusinessImage = (ImageView) findViewById(R.id.business_image);
        mShare = (ImageView) findViewById(R.id.share_image_view);
        mPhoneButton = (ImageView) findViewById(R.id.call_image_view);
        mMapFrame = (ImageView) findViewById(R.id.map_frame);

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
    }

    public void bindDataToView(Business business){
        mBusinessName.setText(business.getName());
        mRating.setText(String.valueOf(business.getRating()));
        mPrice.setText(business.getPrice());
        mPhoneNumber.setText(business.getPhone());

        Location location = business.getLocation();
        mAddress.setText(location.getAddress1() + " " + location.getCity());
        mOpenOrClosed.setText((business.getIsClosed())?"Closed":"Open");
    }
}
