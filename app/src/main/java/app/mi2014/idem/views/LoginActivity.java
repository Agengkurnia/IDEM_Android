package app.mi2014.idem.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import app.mi2014.idem.R;
import app.mi2014.idem.app.AppController;
import app.mi2014.idem.app.Idem;
import app.mi2014.idem.models.User;
import app.mi2014.idem.utils.PrefUtils;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    // UI Declare
    private EditText mNimView;
    private EditText mPasswordView;
    private Button mSignInButton;
    private ViewGroup mLoginForm;
    private ImageView mLogo;
    private TextView mLogoTitle;
    private ScrollView mScrollView;
    private TextView mWelcome;

    private LinearLayout mDummyLayout;
    private Intent intent;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginForm = (ViewGroup) findViewById(R.id.login_form);
        mLogo = (ImageView) findViewById(R.id.logo);
        mLogoTitle = (TextView) findViewById(R.id.logo_title);
        mNimView = (EditText) findViewById(R.id.nim);
        mPasswordView = (EditText) findViewById(R.id.password);
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mScrollView = (ScrollView) findViewById(R.id.scrollForm);
        mWelcome = (TextView) findViewById(R.id.welcome_text);
        mDummyLayout = (LinearLayout) findViewById(R.id.dummy_layout);

        // Create Animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(mLoginForm);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLogo.getLayoutParams();
                params.topMargin = 80;
                mLogo.setLayoutParams(params);
                mLogoTitle.setVisibility(View.GONE);
                mScrollView.setVisibility(View.VISIBLE);
            }
        }, 500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String lastNim = PrefUtils.get(PrefUtils.KEY_USER_NIM, "");
                mNimView.setText(lastNim);
                mNimView.requestFocus();
                if (lastNim != "") mNimView.selectAll();
                showKeyboard(mNimView);
            }
        }, 1000);

        // Listener
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
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void disableButton(boolean state) {
        if (state) {
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
            mSignInButton.setEnabled(false);
            mSignInButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.buttonColorDisabled));
        } else {
            pDialog.dismissWithAnimation();
            mSignInButton.setEnabled(true);
            mSignInButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.buttonColorPrimary));
        }
    }

    private void hideKeyboard(View from) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(from.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void showKeyboard(View from) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(from, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        mNimView.setError(null);
        mPasswordView.setError(null);

        String nim = mNimView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = this.getCurrentFocus();

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
        } else {
            // out focus
            disableButton(true);
            mDummyLayout.requestFocus();
            if (focusView != null) {
                hideKeyboard(focusView);
            }
            // perform the user login attempt.
            Response.Listener afterLogin = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(AppController.TAG, response.toString());
                    String status = null;
                    try {
                        status = response.getString("status");
                        if (!status.equals("ok")) {
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();
                            showKeyboard(mPasswordView);
                        } else {

                            JSONObject res = response.getJSONObject("user");

                            User user = new User(res.getString("nim"), res.getString("name"), res.getString("picture"));

                            PrefUtils.save(PrefUtils.KEY_USER_NIM, user.nim);
                            PrefUtils.save(PrefUtils.KEY_USER_NAME, user.name);
                            PrefUtils.save(PrefUtils.KEY_USER_PICTURE, user.picture);
                            PrefUtils.save(PrefUtils.KEY_APP_SESSION, res.getString("session"));
                            PrefUtils.save(PrefUtils.KEY_APP_TOKEN, response.getString("token"));

                            TransitionManager.beginDelayedTransition(mLoginForm);
                            mScrollView.setVisibility(View.GONE);
                            mLogo.setVisibility(View.GONE);
                            mWelcome.setText("Hi, " + user.name + "!");
                            mWelcome.setVisibility(View.VISIBLE);

                            intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(PrefUtils.KEY_USER_NIM, user.nim);
                            intent.putExtra(PrefUtils.KEY_USER_NAME, user.name);
                            intent.putExtra(PrefUtils.KEY_USER_PICTURE, user.picture);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(intent);
                                    finish();
                                }
                            }, 2000);
                        }

                    } catch (JSONException e) {
                        Log.d(AppController.TAG, e.getMessage());
                    }
                    disableButton(false);
                }
            };

            Response.ErrorListener onError = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AppController.getInstance().getBaseContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    disableButton(false);
                }
            };

            Idem.Login(nim, password, afterLogin, onError);
        }
    }

    private boolean isNimValid(String nim) {
        return nim.length() == 10;
    }

}

