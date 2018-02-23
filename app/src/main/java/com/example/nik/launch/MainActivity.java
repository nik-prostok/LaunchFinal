package com.example.nik.launch;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ParserTask().execute();
    }

    private class ParserTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("https://api.spacexdata.com/v2/launches?launch_year=2017");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                resultJson = stringBuffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            Log.d("JSON:", json);

            ExpandableListView elv = (ExpandableListView) findViewById(R.id.expListView);

            final ArrayList<Rocket> rockets = getData(json);

            CustomAdapter adapter = new CustomAdapter(MainActivity.this, rockets);

            elv.setAdapter(adapter);

            elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPos,
                                            int childPos, long id) {
                 //  Toast.makeText(getApplicationContext(), rockets.get(groupPos).getChild(childPos), Toast.LENGTH_SHORT).show();
                  //  return false;
                    Log.d("URL", rockets.get(groupPos).getChild(childPos));
                   Uri address = Uri.parse(rockets.get(groupPos).getChild(childPos));
                    Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
                    startActivity(openlinkIntent);
                    return false;
                }
            });
        }
    }

    //Метод преобразующий JSON в ArrayList с нужными полями
    private ArrayList<Rocket> getData(String jsonString) {

        ArrayList<Rocket> rockets = new ArrayList<Rocket>();

        try {
            JSONArray launchesJSONArray = new JSONArray(jsonString);
            if (launchesJSONArray != null) {
                for (int i = 0; i < launchesJSONArray.length(); i++) {

                    JSONObject rocketJSON = launchesJSONArray.getJSONObject(i);
                    JSONObject rocketInfo = rocketJSON.getJSONObject("rocket");

                    //Конвертация времени
                    long dv = Long.valueOf(rocketJSON.getString("launch_date_unix"))*1000;
                    Date df = new java.util.Date(dv);


                    JSONObject links = rocketJSON.getJSONObject("links");

                    String mission_patch = links.getString("mission_patch");
                    Log.d("mission_patch", mission_patch);

                    Rocket rocket = new Rocket(String.valueOf(new StringBuilder(rocketInfo.getString("rocket_name") +
                            " (flight: ")),
                            String.valueOf(new StringBuilder("Date launch: " +
                                    new SimpleDateFormat("MM dd, yyyy hh:mma").format(df))),
                            rocketJSON.getString("details"),
                            links.getString("mission_patch"),
                            new Integer(rocketJSON.getString("flight_number" )),
                            links.getString("video_link"),
                            links.getString("article_link"));
                    rockets.add(rocket);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rockets;
    }
}