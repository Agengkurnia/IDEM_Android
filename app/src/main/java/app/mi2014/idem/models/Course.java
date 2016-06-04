package app.mi2014.idem.models;

import android.text.Html;

public class Course {

    public int id;
    public String title;

    public Course(int id, String title) {
        this.id = id;
        this.title = Html.fromHtml(title).toString();
    }

}
