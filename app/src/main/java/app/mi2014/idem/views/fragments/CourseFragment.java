package app.mi2014.idem.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.mi2014.idem.R;
import app.mi2014.idem.adapters.CoursesAdapter;
import app.mi2014.idem.models.Course;
import app.mi2014.idem.utils.PrefUtils;
import co.dift.ui.SwipeToAction;

public class CourseFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CoursesAdapter adapter;
    private SwipeToAction swipeToAction;

    private List<Course> activeCourses = new ArrayList<>();

    public CourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_course, container, false);
        Context context = view.getContext();

        mRecyclerView = (RecyclerView) view;

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        swipeToAction = new SwipeToAction(mRecyclerView, new SwipeToAction.SwipeListener<Course>() {
            @Override
            public boolean swipeLeft(final Course itemData) {
                return true;
            }

            @Override
            public boolean swipeRight(Course itemData) {
                return true;
            }

            @Override
            public void onClick(Course itemData) {
            }

            @Override
            public void onLongClick(Course itemData) {
            }
        });

        Type type = new TypeToken<List<Course>>() {
        }.getType();
        activeCourses = new Gson().fromJson(PrefUtils.get(PrefUtils.KEY_ACTIVE_COURSE, ""), type);

        CoursesAdapter adapter = new CoursesAdapter(activeCourses);
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
