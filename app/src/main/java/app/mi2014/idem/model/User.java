package app.mi2014.idem.model;

import android.graphics.Bitmap;

/**
 * Created by Fz on 01/06/2016.
 */

public class User {
    public String nim;
    public String name;
    public Bitmap picture;

    public User(String nim, String name, Bitmap picture) {
        this.nim = nim;
        this.name = name;
        this.picture = picture;
    }

    public User(String nim, String name, String encodedImage) {
        this.nim = nim;
        this.name = name;
//        encodedImage = encodedImage.substring(encodedImage.indexOf(",") + 1);
//        byte[] decodedString = Base64.decode(encodedImage.getBytes(), Base64.DEFAULT);
//        this.picture = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        this.picture = null;
    }

}

