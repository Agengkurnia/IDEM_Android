package app.mi2014.idem.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.mi2014.idem.R;
import app.mi2014.idem.utils.PrefUtils;

public class SplashScreen extends AppCompatActivity {

    // UI Declare
    private ViewGroup mLoginForm;
    private ImageView mLogo;
    private TextView mLogoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        mLoginForm = (ViewGroup) findViewById(R.id.login_form);
        mLogo = (ImageView) findViewById(R.id.logo);
        mLogoTitle = (TextView) findViewById(R.id.logo_title);

        // Create Animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(mLoginForm);
                mLogo.setVisibility(View.VISIBLE);
                mLogoTitle.setVisibility(View.VISIBLE);
            }
        }, 500);

        int splashDelay = PrefUtils.get(PrefUtils.KEY_APP_SESSION, "") == "" ? 2000 : 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PrefUtils.get(PrefUtils.KEY_APP_SESSION, "") == "") {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                } else {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    intent.putExtra(PrefUtils.KEY_USER_NIM, PrefUtils.get(PrefUtils.KEY_USER_NIM, ""));
                    intent.putExtra(PrefUtils.KEY_USER_NAME, PrefUtils.get(PrefUtils.KEY_USER_NAME, ""));
                    intent.putExtra(PrefUtils.KEY_USER_PICTURE, PrefUtils.get(PrefUtils.KEY_USER_PICTURE, ""));
                    startActivity(intent);
                }
                finish();
            }
        }, splashDelay);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
