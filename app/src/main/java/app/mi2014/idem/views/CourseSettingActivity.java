package app.mi2014.idem.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.mi2014.idem.R;
import app.mi2014.idem.adapters.CoursesAdapter;
import app.mi2014.idem.app.AppController;
import app.mi2014.idem.app.Idem;
import app.mi2014.idem.models.Course;
import app.mi2014.idem.utils.PrefUtils;
import cn.pedant.SweetAlert.SweetAlertDialog;
import co.dift.ui.SwipeToAction;

public class CourseSettingActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CoursesAdapter adapter;
    private SwipeToAction swipeToAction;
    private SweetAlertDialog pDialog;
    private Button mSave;

    private List<Course> courses = new ArrayList<>();
    private List<Course> activeCourses = new ArrayList<>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_setting);

        context = this;
        mSave = (Button) findViewById(R.id.save);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        swipeToAction = new SwipeToAction(mRecyclerView, new SwipeToAction.SwipeListener<Course>() {
            @Override
            public boolean swipeLeft(final Course itemData) {
                return true;
            }

            @Override
            public boolean swipeRight(Course itemData) {
                setActiveCourse(itemData);
                return true;
            }

            @Override
            public void onClick(Course itemData) {
            }

            @Override
            public void onLongClick(Course itemData) {
            }
        });

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Get your courses...");
        pDialog.setCancelable(false);
        pDialog.show();

        Idem.GetCourses(PrefUtils.get(PrefUtils.KEY_APP_SESSION, ""), populate(), AppController.onError());

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                pDialog.setTitleText("Active Course Saved!");
                pDialog.setCancelable(false);
                pDialog.setConfirmText("OK");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        String json = new Gson().toJson(activeCourses);
                        Log.d(AppController.TAG, json);
                        PrefUtils.save(PrefUtils.KEY_ACTIVE_COURSE, json);
                        sweetAlertDialog.dismissWithAnimation();
                        finish();
                    }
                });
                pDialog.show();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private Response.Listener populate() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    Log.d(AppController.TAG, status);
                    if (status.equals("ok")) {
                        JSONArray listCourse = response.getJSONArray("courses");
                        Log.d(AppController.TAG, "Total Course : " + listCourse.length());
                        for (int i = 0; i < listCourse.length(); i++) {
                            JSONObject course = listCourse.getJSONObject(i);
                            Log.d(AppController.TAG, "#" + course.getInt("id") + " " + course.getString("title"));
                            courses.add(new Course(course.getInt("id"), course.getString("title")));
                        }
                        adapter = new CoursesAdapter(courses);
                        mRecyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    Log.d(AppController.TAG, "Error: " + e.getMessage());
                    e.printStackTrace();
                }
                pDialog.setContentText("Swipe right to select course");
                pDialog.setTitleText("Set Active Course");
                pDialog.changeAlertType(SweetAlertDialog.NORMAL_TYPE);
                pDialog.setConfirmText("OK");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                });
            }
        };
    }

    private void setActiveCourse(Course course) {
        Toast.makeText(this, "Set " + course.title + " active.", Toast.LENGTH_SHORT).show();
        activeCourses.add(course);
        int pos = courses.indexOf(course);
        courses.remove(course);
        adapter.notifyItemRemoved(pos);
    }

}
