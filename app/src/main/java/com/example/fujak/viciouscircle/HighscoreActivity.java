package com.example.fujak.viciouscircle;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.net.URL;

public class HighscoreActivity extends Activity {

    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;
    private static String URL = "https://api.myjson.com/bins/t3n3n";
    private TextView title;
    private TextView title2;
    private TextView title3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_highscore);

        title = (TextView) findViewById(R.id.title1);
        title2 = (TextView) findViewById(R.id.title2);
        title3 = (TextView) findViewById(R.id.title3);
        title.setText(getResources().getString(R.string.hc_loading));
        updateConnectedFlags();
        loadPage();
    }

    private void updateConnectedFlags() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }

    }


    private void loadPage() {
        if (wifiConnected || mobileConnected) {
            // AsyncTask subclass
            new DownloadHighscores(this).execute(URL);
        } else {
            title.setText(getResources().getString(R.string.hc_notconnected));
        }
    }


    private class DownloadHighscores extends AsyncTask<String, Void, List<Entry>> {

        Context context;

        public DownloadHighscores(Context context) {
            this.context = context;
        }

        @Override
        protected List<Entry> doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return null; //getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {

                return null; //getResources().getString(R.string.xml_error);
            }
        }

        @Override
        protected void onPostExecute(List<Entry> result) {
            if(result == null) {

                title.setText(getResources().getString(R.string.hc_loadfailed));
                return;
            }
            HighscoreActivity ref = (HighscoreActivity) context; //

            // Displays the HTML string in the UI via a WebView

            Log.d("Result","length = " + result.size());

            title.setText(getResources().getString(R.string.hc_name));
            title2.setText(getResources().getString(R.string.hc_level));
            title3.setText(getResources().getString(R.string.hc_date));
            HCAdapter adapter = new HCAdapter(context,
                    R.layout.score, result);
            ListView lv = (ListView)findViewById(R.id.listView1);
            lv.setAdapter(adapter);


         //   ref.setEntries(result);   //

        }

    }

    private List<Entry> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        HCJsonParser parser = new HCJsonParser();
        List<Entry> entries = null;

        try {
            stream = downloadUrl(urlString);
            entries = parser.parse(stream);
        }
        catch (Exception e) {

        }
        finally
         {
            if (stream != null) {
                stream.close();

            }
        }
        return entries;
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        System.setProperty("http.keepAlive", "false");
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

}

 class Entry{
    long date;
    String name;
    int level;

    public Entry(String name, int level, long date)
    {
        this.date = date;
        this.name = name;
        this.level = level;

    }

}

class HCJsonParser{
    private static final String ns = null;
    String result = null;
    JSONObject json = null;
    List<Entry> entries = new ArrayList<Entry>();
    public List<Entry> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } finally {
            in.close();
        }
        try {
            json = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray scores = json.getJSONArray("scores");

            for(int i = 0; i < scores.length(); i++){
                JSONObject c = scores.getJSONObject(i);

                // Storing each json item in variable
                String name = c.getString("name");
                String level = c.getString("level");
                String timestamp = c.getString("timestamp");

                Entry e = new Entry(name, Integer.valueOf(level), Long.valueOf(timestamp));
                entries.add(e);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return entries;
    }
}

 class HCAdapter extends ArrayAdapter<Entry> {
        Context context;
        int layoutResourceId;
        List<Entry> data = null;

    public HCAdapter(Context context, int layoutResourceId, List<Entry> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {
         View row = convertView;
         EntryHolder holder = null;

         if(row == null)
         {
             LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
             row = inflater.inflate(layoutResourceId, parent, false);

             holder = new EntryHolder();
             holder.textName = (TextView)row.findViewById(R.id.textName);
             holder.textLevel = (TextView)row.findViewById(R.id.textLevel);
             holder.textDate = (TextView)row.findViewById(R.id.textDate);

             row.setTag(holder);
         }
         else
         {
             holder = (EntryHolder)row.getTag();
         }


         Entry entry = data.get(position);
             holder.textName.setText(entry.name);
             holder.textLevel.setText(Integer.toString(entry.level));

             Date date = new Date(entry.date);
             SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
             String formattedDate = formatter.format(date);
             holder.textDate.setText(formattedDate);


         //    int resId = context.getResources().getIdentifier(entry.zeme,"strings",context.getPackageName());          context.getString(resId)
         return row;
     }
     static class EntryHolder
     {
         TextView textName;
         TextView textLevel;
         TextView textDate;
     }
}

