package shuvalov.nikita.restaurantroulette.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import io.fabric.sdk.android.Fabric;
import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.RestaurantSearchHelper;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;

public class ShareActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "cUr6myy7P8yOwWwrzbIdHbxKZ";
    private static final String TWITTER_SECRET = "qW7wJRMVrWx1qVFgJqeieuy4tF8kiTveBlh8v47ahStdz56ZHp";
    private TwitterLoginButton mTwitterLoginButton, testButton;
    private TextView mUserName, mTweetPreview;
    private RelativeLayout loggedIn, notLoggedIn;
    private String mTweetText, mRestaurantName;
    private boolean tweeted;
    private Business mBusiness;
    private Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_share);

        tweeted = false;

        int pos = getIntent().getIntExtra(OurAppConstants.BUSINESS_POSITION_INTENT_KEY, 1);
        mBusiness = RestaurantSearchHelper.getInstance().getBusinessAtPosition(pos);

        mRestaurantName = mBusiness.getName();
        mTweetText = "Check out the new place I just discovered on Restaurant Roulette!\n" +
                mRestaurantName;

        mUserName = (TextView) findViewById(R.id.twitter_name);
        mTweetPreview = (TextView) findViewById(R.id.tweet_preview);

        mTweetPreview.setText(mTweetText);

        loggedIn = (RelativeLayout) findViewById(R.id.is_logged_in);
        notLoggedIn = (RelativeLayout) findViewById(R.id.need_to_login);


        Button button = (Button) findViewById(R.id.tweet_button);
        Button logOut = (Button) findViewById(R.id.log_out_button);

        mTwitterLoginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model

                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
                Fabric.with(ShareActivity.this, new Twitter(authConfig));



                TweetComposer.Builder builder = new TweetComposer.Builder(ShareActivity.this)
                        .text(mTweetText)
                        .image(getImageUri(mBusiness.getImageUrl()))
                        .url(getURL(mBusiness.getUrl()))
                        ;
                builder.show();
                tweeted = true;
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterCore.getInstance().logOut();
                ClearCookies(getApplicationContext());
                Twitter.logOut();
                mTwitterLoginButton.setVisibility(View.VISIBLE);
                setViews();


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the mTwitterLoginButton hears the result from any
        // Activity that it triggered.
        mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setViews();

    }

    public static void ClearCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    private void setViews () {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        TwitterSession session = Twitter.getInstance().core.getSessionManager().getActiveSession();

        if (session != null) {
            String text = "Logged in as: " + session.getUserName();
            mUserName.setText(text);
            notLoggedIn.setVisibility(View.GONE);
            loggedIn.setVisibility(View.VISIBLE);
            if (tweeted) {
                Toast.makeText(ShareActivity.this, "Thanks for sharing!", Toast.LENGTH_SHORT).show();
                finish();
            }

        } else {
            mUserName.setText(" ");
            loggedIn.setVisibility(View.GONE);
            notLoggedIn.setVisibility(View.VISIBLE);
        }
    }

    public Uri getImageUri (String url) {
        Uri imageUri = Uri.parse(url);
        return imageUri;
    }

    public URL getURL (String url) {
        URL imageURL = null;
        try {
            imageURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return imageURL;
    }




}