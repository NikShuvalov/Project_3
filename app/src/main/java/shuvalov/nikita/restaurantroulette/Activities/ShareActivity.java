package shuvalov.nikita.restaurantroulette.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import com.twitter.sdk.android.core.internal.TwitterApiConstants;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;
import shuvalov.nikita.restaurantroulette.R;

public class ShareActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "cUr6myy7P8yOwWwrzbIdHbxKZ";
    private static final String TWITTER_SECRET = "qW7wJRMVrWx1qVFgJqeieuy4tF8kiTveBlh8v47ahStdz56ZHp";
    private TwitterLoginButton loginButton;
    private TextView userName, tweetPreview;
    private RelativeLayout loggedIn, notLoggedIn;
    private String tweetText, restaurantName;
    private boolean tweeted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_share);

        tweeted = false;

        restaurantName = "Joe's Pizza";
        tweetText= "Check out the new place i just discovered on INSERT_APP_NAME_HERE!\n" +
                restaurantName;

        userName = (TextView) findViewById(R.id.twitter_name);
        tweetPreview = (TextView) findViewById(R.id.tweet_preview);

        tweetPreview.setText(tweetText);

        loggedIn = (RelativeLayout) findViewById(R.id.is_logged_in);
        notLoggedIn = (RelativeLayout) findViewById(R.id.need_to_login);


        Button button = (Button) findViewById(R.id.tweet_button);
        Button logOut = (Button) findViewById(R.id.log_out_button);

        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
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
                        .text(tweetText);
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
                loginButton.setVisibility(View.VISIBLE);
                setViews();


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
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
            userName.setText(text);
            notLoggedIn.setVisibility(View.GONE);
            loggedIn.setVisibility(View.VISIBLE);
            if (tweeted) {
                finish();
            }

        } else {
            userName.setText(" ");
            loggedIn.setVisibility(View.GONE);
            notLoggedIn.setVisibility(View.VISIBLE);
        }
    }
}