package com.example.nik.launch;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ParserTask().execute(); //
    }

    private class ParserTask extends AsyncTask<Void, Void, String>{

        private String rocketNameArray[];
       // private String info[][][];
        private String launchDateUnixArray[];
        private URL iconsMissionPatch[];
        private String details[];
        private String rocketInfo[][];



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

                while((line = reader.readLine()) != null){
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

        protected void onPostExecute(String json){
            super.onPostExecute(json);
            Log.d("JSON:", json);


            try {
                JSONArray launchesJSONArray = new JSONArray(json);
                JSONObject[] launchesArray = new JSONObject[launchesJSONArray.length()];

                rocketNameArray = new String[launchesJSONArray.length()];
                launchDateUnixArray = new String[launchesJSONArray.length()];
                iconsMissionPatch = new URL[launchesJSONArray.length()];
                details = new String[launchesJSONArray.length()];

                rocketInfo = new String[launchesJSONArray.length()][4];

                rocketNameArray[0] = "ROCKET";
                launchDateUnixArray[0] = "launchDateUnixArray";
                launchDateUnixArray[0] = "iconsMissionPatch";
                details[0] = "details";

                for (int i = 1; i < launchesJSONArray.length(); i++){
                    //Получаем элемент JSON-массива
                    launchesArray[i] = (JSONObject)launchesJSONArray.get(i);

                    //Получаем вложенный элемент массива 'rocket_name' через элемент 'rocket'
                    JSONObject rocket1 = launchesArray[i].getJSONObject("rocket");
                    rocketNameArray[i] = rocket1.getString("rocket_name");

                    long dv = Long.valueOf(launchesArray[i].getString("launch_date_unix"))*1000;// its need to be in milisecond
                    Date df = new java.util.Date(dv);
                   // String vv = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);
                    launchDateUnixArray[i] = String.valueOf(new StringBuilder("Date launch: " + new SimpleDateFormat("MM dd, yyyy hh:mma").format(df)));
                    rocketInfo[i][1] = launchDateUnixArray[i];

                    //По аналогии с 'rocket'
                    JSONObject linksString = launchesArray[i].getJSONObject("links");
                    iconsMissionPatch[i] = new URL(linksString.getString("mission_patch"));
                    rocketInfo[i][2] = String.valueOf(iconsMissionPatch[i]);

                    //Детали
                    details[i] = launchesArray[i].getString("details");
                    rocketInfo[i][3] = launchesArray[i].getString("details");


                    Log.d("Name:", rocketNameArray[i]);
                    Log.d("Date", launchDateUnixArray[i]);
                    Log.d("URL", iconsMissionPatch[i].toString());
                    Log.d("Details", details[i]);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            ExpandableListAdapter mAdapter;

            List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
            List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
            for (int i = 1; i < rocketNameArray.length; i++) {
                Map<String, String> curGroupMap = new HashMap<String, String>();
                groupData.add(curGroupMap);
                curGroupMap.put("ROCKET", rocketNameArray[i]);

                List<Map<String, String>> children = new ArrayList<Map<String, String>>();
                for (int j = 1; j < 4; j++) {
                    Map<String, String> curChildMap = new HashMap<String, String>();
                    children.add(curChildMap);
                    curChildMap.put("ROCKET", rocketInfo[i][j]);
               }
                childData.add(children);
            }

            // Set up our adapter
            mAdapter = new SimpleExpandableListAdapter(MainActivity.this, groupData,
                    R.layout.list_item_1,
                    new String[] { "ROCKET" }, new int[] { android.R.id.text1 },
                    childData, R.layout.list_item_2,
                    new String[] { "ROCKET" }, new int[] { android.R.id.text1 });
            ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expListView);
            expandableListView.setAdapter(mAdapter);
        }

    }
}
