package com.example.guessstar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String,Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... strings) {
        URL url = null;
        HttpURLConnection urlConnection = null;
        Bitmap bitmap = null;

        try {

            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection)url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;

        }catch (Exception e){
            Log.i("my_exception",e.toString());
        }finally {
            urlConnection.disconnect();

        }
        return null;
    }
}
