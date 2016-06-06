package app.mi2014.idem.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import app.mi2014.idem.R;
import app.mi2014.idem.app.AppController;
import app.mi2014.idem.utils.PrefUtils;
import app.mi2014.idem.views.fragments.CourseFragment;
import app.mi2014.idem.views.fragments.HomeFragment;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // UI Declare
    private NavigationView mNavView;
    private View mHeaderLayout;
    private TextView mProfileName;
    private TextView mProfileNim;
    private CircleImageView mProfileImage;

    private String jsonActiveCourse;
    private SweetAlertDialog pDialog;
    private int currentMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        jsonActiveCourse = PrefUtils.get(PrefUtils.KEY_ACTIVE_COURSE, "");

        Log.d(AppController.TAG, "CREATE: " + jsonActiveCourse);

        if (jsonActiveCourse.equals("")) {
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
            pDialog.setTitleText("Opps,");
            pDialog.setContentText("It's first time you login, please set your active course.");
            pDialog.setCancelable(false);
            pDialog.setConfirmText("OK");
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    Intent intent = new Intent(MainActivity.this, CourseSettingActivity.class);
                    startActivity(intent);
                }
            });
            pDialog.show();
        }

        Intent intent = getIntent();

        String nim = intent.getStringExtra(PrefUtils.KEY_USER_NIM);
        String name = intent.getStringExtra(PrefUtils.KEY_USER_NAME);
        String urlPic = intent.getStringExtra(PrefUtils.KEY_USER_PICTURE);

        setContentView(R.layout.activity_main);

        mNavView = (NavigationView) findViewById(R.id.nav_view);
        mHeaderLayout = mNavView.getHeaderView(0);

        mProfileName = (TextView) mHeaderLayout.findViewById(R.id.profile_name);
        mProfileNim = (TextView) mHeaderLayout.findViewById(R.id.profile_nim);
        mProfileImage = (CircleImageView) mHeaderLayout.findViewById(R.id.profile_image);

        mProfileName.setText(name);
        mProfileNim.setText(nim);

        Log.d(AppController.TAG, urlPic);

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(urlPic, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(AppController.TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    mProfileImage.setImageBitmap(response.getBitmap());
                }
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavView.setNavigationItemSelectedListener(this);

        // Default Selected Fragment Content
        mNavView.getMenu().getItem(0).setChecked(true);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment defaultContent = new HomeFragment();
        ft.replace(R.id.content_holder, defaultContent);
        ft.commit();
        currentMenu = mNavView.getMenu().getItem(0).getItemId();
        Log.d(AppController.TAG, "Menu: " + currentMenu);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment newFragment = null;

        if (id == R.id.nav_home) {
            newFragment = new HomeFragment();
        } else if (id == R.id.nav_course) {
            newFragment = new CourseFragment();
        } else if (id == R.id.nav_setting) {
            newFragment = new HomeFragment();
        } else if (id == R.id.nav_about) {
            newFragment = new HomeFragment();
        } else if (id == R.id.nav_share) {
            newFragment = new HomeFragment();
        } else if (id == R.id.nav_send) {
            PrefUtils.save(PrefUtils.KEY_ACTIVE_COURSE, "");
            newFragment = new HomeFragment();
        } else if (id == R.id.nav_signout) {
            confirmSignOut();
        }

        if (newFragment != null && currentMenu != id && id != R.id.nav_signout) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_holder, newFragment);
            ft.commit();
            currentMenu = id;
            Log.d(AppController.TAG, "Fragment: " + currentMenu);
        }

        if (id != R.id.nav_signout) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    private void confirmSignOut() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitleText("Confirmation");
        pDialog.setContentText("Are you sure want to sign out ?");
        pDialog.setCancelable(false);
        pDialog.setCancelText("No");
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(true);
        pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.cancel();
            }
        });
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            private SweetAlertDialog respon;

            @Override
            public void onClick(SweetAlertDialog sDialog) {
                respon = sDialog;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PrefUtils.save(PrefUtils.KEY_APP_SESSION, "");
                        respon.setContentText("Logout Success!");
                        respon.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        respon.showCancelButton(false);
                        respon.setConfirmText("OK");
                        respon.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                    }
                }, 1000);
            }
        });
        pDialog.show();
    }

    @Override
    public void onResume() {
        jsonActiveCourse = PrefUtils.get(PrefUtils.KEY_ACTIVE_COURSE, "");
        if (pDialog != null && !jsonActiveCourse.equals("")) pDialog.dismiss();
        Log.d(AppController.TAG, "RESUME: " + jsonActiveCourse);
        super.onResume();
    }
}
