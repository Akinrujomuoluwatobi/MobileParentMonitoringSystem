package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import java.net.URLEncoder;
import java.util.ArrayList;

import Adapters.ResultAdapter;
import Model.Results;

/**
 * Created by PROG. TOBI on 07-Jul-16.
 */
public class ResultFetchTask extends AsyncTask<String, Results, String> {
    Context ctx;
    Activity activity;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressDialog progressDialog;
    ArrayList<Results> arrayList = new ArrayList<>();

    public ResultFetchTask(Context ctx) {
        this.ctx = ctx;
        activity = (Activity) ctx;
    }


    String json_string = "http://elearningapp.eu5.org/mpms_fetchresults.php";

    @Override
    protected String doInBackground(String... params) {
        String matricno = params[0];
        String session = params[1];
        String semester = params[2];

        try {
            URL url = new URL(json_string);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("stud_matricno", "UTF-8") + "=" + URLEncoder.encode(matricno, "UTF-8") + "&"
                    + URLEncoder.encode("session", "UTF-8") + "=" + URLEncoder.encode(session, "UTF-8") + "&"
                    + URLEncoder.encode("semester", "UTF-8") + "=" + URLEncoder.encode(semester, "UTF-8");
            bufferedWriter.write(post_data);
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
                Results result = new Results();
                result.setSubject(jsondetails.getString("course"));
                result.setCa1(jsondetails.getInt("ca1"));
                result.setCa2(jsondetails.getInt("ca2"));
                result.setTotal(jsondetails.getInt("total"));
                result.setExam(jsondetails.getInt("exam"));
                result.setGrade(jsondetails.getString("grade"));
                arrayList.add(result);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        recyclerView = (RecyclerView) activity.findViewById(R.id.resultfetchrecycler);
        layoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ResultAdapter(arrayList);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Result Fetching");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    protected void onProgressUpdate(Results... values) {
        arrayList.add(values[0]);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(String o) {
        progressDialog.dismiss();
    }
}

