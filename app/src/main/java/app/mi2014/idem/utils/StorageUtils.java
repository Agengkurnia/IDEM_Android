package app.mi2014.idem.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import app.mi2014.idem.app.AppController;

/**
 * Created by Fz on 02/06/2016.
 */
public class StorageUtils {

    public static File getOutputMediaFile(String filename) {
        File mediaStorageDir = new File(AppController.getInstance().getBaseContext().getFilesDir() + "/img");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        return new File(mediaStorageDir.getPath() + File.separator + filename + ".jpg");
    }

    public static void storeImage(String filename, Bitmap image) {
        File pictureFile = StorageUtils.getOutputMediaFile(filename);
        if (pictureFile == null) {
            Log.d(AppController.TAG, "Error creating media file, check storage permissions: ");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(AppController.TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(AppController.TAG, "Error accessing file: " + e.getMessage());
        }
    }
}
