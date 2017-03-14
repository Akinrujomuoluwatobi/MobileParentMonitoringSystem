package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.royalteck.progtobi.mpms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import Adapters.EventAdapter;
import Adapters.NewsAdapter;
import Model.Event;
import Model.News;

/**
 * Created by PROG. TOBI on 07-Jul-16.
 */
public class FetchNewsBdgTask extends AsyncTask<String, News, String> {
    Activity activity;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressDialog progressDialog;
    ArrayList<News> arrayList = new ArrayList<>();
    ArrayList<Event> arrayList_event = new ArrayList<>();
    String action = "";

    public FetchNewsBdgTask(Activity ctx) {
        this.activity = ctx;

    }


    String json_string = "http://elearningapp.eu5.org/mpms_fetchnews.php";
    String event_json_string = "http://elearningapp.eu5.org/mpms_fetchevent.php";

    @Override
    protected String doInBackground(String... params) {
        action = params[0];

        if (action.equals("newsfetch")) {
            try {
                URL url = new URL(json_string);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                String json_string = stringBuilder.toString().trim();
                Log.d("JSON STRING", json_string);
                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                JSONObject json, jsondetails;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsondetails = jsonArray.getJSONObject(i);
                    News news = new News();
                    news.setHeader(jsondetails.getString("newsheading"));
                    news.setCaption(jsondetails.getString("newscaption"));
                    news.setNews(jsondetails.getString("newscontent"));
                    //news.setDate(jsondetails.getString("newsdate"));
                    arrayList.add(news);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (action.equals("eventsfetch")) {
            try {
                URL url = new URL(event_json_string);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                String json_string = stringBuilder.toString().trim();
                Log.d("JSON STRING", json_string);
                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                JSONObject json, jsondetails;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsondetails = jsonArray.getJSONObject(i);
                    Event event = new Event();
                    event.setHeader(jsondetails.getString("eventtitle"));
                    event.setCaption(jsondetails.getString("eventcaption"));
                    event.setVenue(jsondetails.getString("eventvenue"));
                    event.setEventdate(jsondetails.getString("eventdate"));
                    event.setEventtime(jsondetails.getString("eventtime"));
                    //news.setDate(jsondetails.getString("newsdate"));
                    arrayList_event.add(event);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Loading News and Events");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();


    }

    @Override
    protected void onProgressUpdate(News... values) {
        arrayList.add(values[0]);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(String o) {
        if (action.equals("newsfetch")) {
            recyclerView = (RecyclerView) activity.findViewById(R.id.newsrecycler);
            layoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            adapter = new NewsAdapter(activity, arrayList);
            recyclerView.setAdapter(adapter);
            progressDialog.dismiss();

        } else if (action.equals("eventsfetch")) {
            recyclerView = (RecyclerView) activity.findViewById(R.id.eventrecycler);
            layoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            adapter = new EventAdapter(activity, arrayList_event);
            recyclerView.setAdapter(adapter);
            progressDialog.dismiss();
        }


    }
}

