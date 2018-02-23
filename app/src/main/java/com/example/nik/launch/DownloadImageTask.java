package com.example.nik.launch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by Nik on 21.02.2018.
 */

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {

        int width = result.getWidth();
        int height = result.getHeight();

        int halfWidth = width / 5;
        int halfHeight = height / 5;

// Выводим уменьшенную в два раза картинку в ImageView
        Bitmap bmHalf = Bitmap.createScaledBitmap(result, halfWidth,
                halfHeight, false);
        bmImage.setImageBitmap(bmHalf);
    }
}
