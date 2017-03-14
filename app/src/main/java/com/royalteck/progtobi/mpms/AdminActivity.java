package com.royalteck.progtobi.mpms;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Tasks.Mysingleton;

public class AdminActivity extends AppCompatActivity {
    EditText newscap, newshead, newscont;
    Button uploadnews, Events;
    String serverUrl="http://elearningapp.eu5.org/mpms_news.php";
    ProgressBar regpb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar1);
        setSupportActionBar(toolbar);

        newscap = (EditText) findViewById(R.id.newscaption);
        newshead = (EditText) findViewById(R.id.newsheader);
        newscont = (EditText) findViewById(R.id.news);
        regpb = (ProgressBar) findViewById(R.id.uploadnewspb);

        uploadnews = (Button) findViewById(R.id.uploadnews);

        uploadnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validate()) {
                    return;
                }

                final String Newscap, Newshead, Newscont;
                Newscap = newscap.getText().toString();
                Newshead = newshead.getText().toString();
                Newscont = newscont.getText().toString();

                if (!validate()) {
                    return;
                }

                if (!isOnline(AdminActivity.this)) {
                    Toast.makeText(AdminActivity.this, "No network connection", Toast.LENGTH_LONG).show();
                    return;
                }

                regpb.setVisibility(View.VISIBLE);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if (code.equals("reg_failed")) {
                                String message = jsonObject.getString("message");
                                regpb.setVisibility(View.GONE);
                                Toast.makeText(AdminActivity.this, message, Toast.LENGTH_LONG).show();
                            } else if (code.equals("reg_success")) {
                                String message = jsonObject.getString("message");
                                regpb.setVisibility(View.GONE);
                                Toast.makeText(AdminActivity.this, message, Toast.LENGTH_LONG).show();
                                newscap.setText("");
                                newscont.setText("");
                                newshead.setText("");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        regpb.setVisibility(View.GONE);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("newsheading", Newshead);
                        params.put("newscaption", Newscap);
                        params.put("newscontent", Newscont);

                        return params;
                    }


                };

                Mysingleton.getInstance(AdminActivity.this).addtorequestque(stringRequest);

            }

            //code to check online details
            private boolean isOnline(Context mContext) {
                ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    return true;
                }
                return false;
            }

        });

        Events = (Button) findViewById(R.id.addevent);
        Events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(AdminActivity.this, AddEventsActivity.class);
                startActivity(in);
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String Newscap = newscap.getText().toString();
        String Newshead = newshead.getText().toString();
        String Newscont = newscont.getText().toString();


        if (Newscap.isEmpty()) { //|| !android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches())
            newscap.setError("News Caption Field Empty");
            valid = false;
        } else {
            newscap.setError(null);
        }

        if (Newshead.isEmpty()) {
            newshead.setError("News heading Field Empty");
            valid = false;
        } else {
            newshead.setError(null);
        }

        if (Newscont.isEmpty()) {
            newscont.setError("News Content Field Empty");
            valid = false;
        } else {
            newscont.setError(null);
        }

        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_admin, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.reg_stud) {
            Intent intent = new Intent(AdminActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
