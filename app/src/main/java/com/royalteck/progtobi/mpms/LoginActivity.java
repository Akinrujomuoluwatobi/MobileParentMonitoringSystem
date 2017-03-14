package com.royalteck.progtobi.mpms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import Model._User;
import Tasks.Mysingleton;
import db.DbLocal;

public class LoginActivity extends AppCompatActivity {
    EditText Username, Password;
    Button loginbtn;
    String login_url = "http://elearningapp.eu5.org/mpms_login.php";
    AlertDialog.Builder builder;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        builder = new AlertDialog.Builder(LoginActivity.this);
        pb = (ProgressBar) findViewById(R.id.pb);


        loginbtn = (Button) findViewById(R.id.loginbtn);


        loginbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*String username = Username.getText().toString();
                String password = Password.getText().toString();
                String lcusername = "olusola";
                String lcpassword = "ajayi";
                String pausername = "olufunke";
                String papassword = "ajayi";
                String adminusername = "admin";
                String adminpassword = "admin";
                if (username.equals(lcusername) && password.equals(lcpassword)) {
                    Intent intent = new Intent(LoginActivity.this, LevelCordActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();

                } else if (username.equals(pausername) && password.equals(papassword)) {
                    Intent intent = new Intent(LoginActivity.this, ParentActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();

                } else if (username.equals(adminusername) && password.equals(adminpassword)) {
                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();

                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Login Details", Toast.LENGTH_LONG).show();
                }*/
                final String username = Username.getText().toString();
                final String password = Password.getText().toString();
                if (!isOnline(LoginActivity.this)) {
                    Toast.makeText(LoginActivity.this, "No network connection", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!validate()) {
                    return;
                }
                pb.setVisibility(View.VISIBLE);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if (code.equals("login_failed")) {
                                builder.setTitle("Login Error");
                                displayAlert(jsonObject.getString("message"));
                                pb.setVisibility(View.GONE);
                            } else if (code.equals("login_success")) {
                                String usertype = jsonObject.getString("usertype");
                                if (usertype.equals("parent")) {
                                    Intent intent = new Intent(getApplication(), ParentActivity.class);
                                    _User user = new _User();
                                    user.setUsername(jsonObject.getString("username"));
                                    user.setPassword(jsonObject.getString("password"));
                                    new DbLocal(LoginActivity.this).add_User(user);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                } else if (usertype.equals("levelcord")) {
                                    Intent intent = new Intent(getApplication(), LevelCordActivity.class);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                } else if (usertype.equals("admin")) {
                                    Intent intent = new Intent(getApplication(), AdminActivity.class);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        pb.setVisibility(View.GONE);

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", username);
                        params.put("password", password);
                        return params;
                    }
                };
                Mysingleton.getInstance(LoginActivity.this).addtorequestque(stringRequest);
            }

            private boolean isOnline(Context mContext) {
                ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    return true;
                }
                return false;
            }
        });

    }

    public boolean validate() {
        boolean valid = true;

        String usernamestr = Username.getText().toString();
        String passwordstr = Password.getText().toString();

        if (usernamestr.isEmpty()) { //|| !android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches())
            Username.setError("enter username");
            valid = false;
        } else {
            Username.setError(null);
        }

        if (passwordstr.isEmpty()) {
            Password.setError("Enter Password");
            valid = false;
        } else {
            Password.setError(null);
        }

        return valid;
    }

    public void displayAlert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Username.setText("");
                Password.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
