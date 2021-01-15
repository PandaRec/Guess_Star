package com.example.guessstar;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadContentTask extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        HttpURLConnection urlConnection = null;
        StringBuilder stringBuilder = new StringBuilder();

        try{

            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String line = bufferedReader.readLine();
            while(line !=null){
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
            return stringBuilder.toString();


        }catch (Exception e){
            Log.i("my_exception",e.toString());
        }finally {
            urlConnection.disconnect();
        }
        return null;
    }
}
