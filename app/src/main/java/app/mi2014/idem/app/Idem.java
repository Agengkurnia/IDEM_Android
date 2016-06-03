package app.mi2014.idem.app;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

/**
 * Created by Fz on 01/06/2016.
 */
public class Idem {
    private static final String API_BASE = "http://idem.cepat.us/api";

    public static void Login(String user, String pass, Response.Listener afterLogin, Response.ErrorListener onError) {
        String API_URL = API_BASE + "/auth/login/?src=android&user=" + user + "&pass=" + pass;
        JsonObjectRequest ApiRequest = new JsonObjectRequest(Request.Method.GET, API_URL, null, afterLogin, onError);
        ApiRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(ApiRequest);
    }

    public static void isLoggedin() {

    }
}
