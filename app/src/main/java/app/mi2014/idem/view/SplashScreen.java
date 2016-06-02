package app.mi2014.idem.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;

import app.mi2014.idem.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        ImageView img = (ImageView) findViewById(R.id.imageView);
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.coordinatorLayout));
                            ImageView img = (ImageView) findViewById(R.id.imageView);

                            MarginLayoutParams params = (MarginLayoutParams) img.getLayoutParams();
                            params.topMargin = -300;
                            img.setLayoutParams(params);

                            Snackbar.make((CoordinatorLayout) findViewById(R.id.coordinatorLayout), "Ceritanya abis ini ke menu login", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
