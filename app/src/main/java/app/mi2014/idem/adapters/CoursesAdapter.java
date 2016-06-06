package app.mi2014.idem.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.mi2014.idem.R;
import app.mi2014.idem.models.Course;
import co.dift.ui.SwipeToAction;

public class CoursesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Course> items;


    /**
     * References to the views for each data item
     **/
    public class CourseViewHolder extends SwipeToAction.ViewHolder<Course> {

        public TextView mCourseTitle;

        public CourseViewHolder(View v) {
            super(v);
            mCourseTitle = (TextView) v.findViewById(R.id.course_title);
        }
    }

    /**
     * Constructor
     **/
    public CoursesAdapter(List<Course> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_setting, parent, false);

        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Course item = items.get(position);
        CourseViewHolder vh = (CourseViewHolder) holder;
        vh.mCourseTitle.setText(item.title);
        vh.data = item;
    }
}