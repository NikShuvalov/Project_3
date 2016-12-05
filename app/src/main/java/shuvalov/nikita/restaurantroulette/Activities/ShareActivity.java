package shuvalov.nikita.restaurantroulette.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
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
    private TwitterLoginButton mTwitterLoginButton;
    private TextView mDialogueUserName;
    private String mTweetText, mRestaurantName;
    private boolean tweeted;
    private Business mBusiness;
    private Button mContinue, mLogOUt, mQuit;

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

        mQuit = (Button) findViewById(R.id.back_button_twitter);
        mQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mTwitterLoginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;

                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
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

            final Dialog continueDialog = new Dialog(this);
            continueDialog.setContentView(R.layout.tweet_pop_up);

            mDialogueUserName = (TextView) continueDialog.findViewById(R.id.username_text);
            String userName = "@" + session.getUserName();
            mDialogueUserName.setText(userName);

            mContinue = (Button) continueDialog.findViewById(R.id.continue_button);
            mLogOUt = (Button) continueDialog.findViewById(R.id.logout_button);

            mContinue.setOnClickListener(new View.OnClickListener() {
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
                    continueDialog.dismiss();
                }
            });

            mLogOUt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TwitterCore.getInstance().logOut();
                    ClearCookies(getApplicationContext());
                    Twitter.logOut();
                    mTwitterLoginButton.setVisibility(View.VISIBLE);
                    continueDialog.dismiss();
                    setViews();
                }
            });

            continueDialog.show();

            if (tweeted) {
                Toast.makeText(ShareActivity.this, "Thanks for sharing!", Toast.LENGTH_SHORT).show();
                finish();
            }

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