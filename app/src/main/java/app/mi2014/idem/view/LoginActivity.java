package app.mi2014.idem.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import app.mi2014.idem.AppController;
import app.mi2014.idem.R;
import app.mi2014.idem.model.User;
import app.mi2014.idem.utils.Idem;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mNimView;
    private EditText mPasswordView;
    private Button mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mNimView = (EditText) findViewById(R.id.nim);
        mNimView.clearFocus();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

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
                            ViewGroup root = (ViewGroup) findViewById(R.id.login_form);
                            TransitionManager.beginDelayedTransition(root);

                            ImageView img = (ImageView) findViewById(R.id.logo);
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) img.getLayoutParams();
                            params.topMargin = 100;
                            img.setLayoutParams(params);

                            TextView logoTitle = (TextView) findViewById(R.id.logo_title);
                            logoTitle.setVisibility(View.GONE);

                            ScrollView sv = (ScrollView) findViewById(R.id.scrollForm);
                            sv.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        };
        timerThread.start();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        mNimView.setError(null);
        mPasswordView.setError(null);
        mSignInButton.setEnabled(false);

        String nim = mNimView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Validation
        if (TextUtils.isEmpty(nim)) {
            mNimView.setError(getString(R.string.error_field_required));
            focusView = mNimView;
            cancel = true;
        } else if (!isNimValid(nim)) {
            mNimView.setError(getString(R.string.error_invalid_nim));
            focusView = mNimView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            mSignInButton.setEnabled(true);
        } else {
            // perform the user login attempt.
            Response.Listener afterLogin = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("IDEM", response.toString());
                    String status = null;
                    try {
                        status = response.getString("status");
                        if (!status.equals("ok")) {
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();
                        } else {
                            JSONObject res = response.getJSONObject("user");
                            User user = new User(res.getString("nim"), res.getString("name"), res.getString("picture"));
                            Log.d("IDEM", user.nim);
                            Log.d("IDEM", user.name);

                            ViewGroup root = (ViewGroup) findViewById(R.id.login_form);
                            TransitionManager.beginDelayedTransition(root);

                            ImageView img = (ImageView) findViewById(R.id.logo);
                            img.setVisibility(View.GONE);

                            CircleImageView dp = (CircleImageView) findViewById(R.id.profile_image);
                            dp.setImageBitmap(user.picture);
                            dp.setVisibility(View.VISIBLE);

                            ScrollView sv = (ScrollView) findViewById(R.id.scrollForm);
                            sv.setVisibility(View.GONE);

                            TextView welcome = (TextView) findViewById(R.id.welcome_text);
                            welcome.setText("Hi, " + user.name + "!");
                            welcome.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        Log.d("IDEM", e.getMessage());
                    }
                    mSignInButton.setEnabled(true);
                }
            };

            Response.ErrorListener onError = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AppController.getInstance().getBaseContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    mSignInButton.setEnabled(true);
                }
            };

            Idem.Login(nim, password, afterLogin, onError);
        }
    }

    private boolean isNimValid(String nim) {
        return nim.length() == 10;
    }

}
