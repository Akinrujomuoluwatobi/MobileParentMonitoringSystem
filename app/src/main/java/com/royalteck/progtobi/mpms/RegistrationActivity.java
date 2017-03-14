package com.royalteck.progtobi.mpms;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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

public class RegistrationActivity extends AppCompatActivity {
    String sex = "";
    EditText surname, othername, matric, email, parent_no;
    AlertDialog.Builder builder;
    String serverUrl="http://elearningapp.eu5.org/mpms_register.php";
    ProgressBar regpb;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        regpb = (ProgressBar) findViewById(R.id.reg_studpb);

        surname = (EditText) findViewById(R.id.stud_surname);
        othername = (EditText) findViewById(R.id.stud_othername);
        matric = (EditText) findViewById(R.id.stud_matricno);
        email = (EditText) findViewById(R.id.stud_email);
        parent_no = (EditText) findViewById(R.id.parent_no);
        register = (Button) findViewById(R.id.registerbtn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()) {
                    return;
                }

                final String Surname, Othername, Matric, Email, Parent_no;
                Surname = surname.getText().toString();
                Othername = othername.getText().toString();
                Matric = matric.getText().toString();
                Email = email.getText().toString();
                Parent_no = parent_no.getText().toString();

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
                                Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_LONG).show();
                            } else if (code.equals("reg_success")) {
                                String message = jsonObject.getString("message");
                                regpb.setVisibility(View.GONE);
                                builder = new AlertDialog.Builder(RegistrationActivity.this);
                                builder.setTitle("Notification");
                                builder.setMessage("Notification sent to parent");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        surname.setText("");
                                        othername.setText("");
                                        matric.setText("");
                                        email.setText("");
                                        parent_no.setText("");

                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setCancelable(false);
                                alertDialog.show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegistrationActivity.this, "Error Registering... Try Again", Toast.LENGTH_SHORT).show();
                        regpb.setVisibility(View.GONE);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("surname", Surname);
                        params.put("othername", Othername);
                        params.put("matric", Matric);
                        params.put("email", Email);
                        params.put("parentno", Parent_no);
                        params.put("sex", sex);

                        return params;
                    }


                };

                Mysingleton.getInstance(RegistrationActivity.this).addtorequestque(stringRequest);


            }
        });


    }


    public void selectSex(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.male:
                if (checked) {
                    sex = "male";
                } else
                    sex = "";
                break;

            case R.id.female:
                if (checked) {
                    sex = "female";
                } else
                    sex = "";
                break;

        }

    }

    public boolean validate() {
        boolean valid = true;

        String surnamestr = surname.getText().toString();
        String othernamestr = othername.getText().toString();
        String matricstr = matric.getText().toString();
        String emailstr = email.getText().toString();
        String phonestr = parent_no.getText().toString();

        if (surnamestr.isEmpty()) { //|| !android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches())
            surname.setError("Surname");
            valid = false;
        } else {
            surname.setError(null);
        }

        if (othernamestr.isEmpty()) {
            othername.setError("Other Names");
            valid = false;
        } else {
            othername.setError(null);
        }

        if (matricstr.isEmpty()) {
            matric.setError("Matric No");
            valid = false;
        } else {
            matric.setError(null);
        }

        if (emailstr.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailstr).matches()) {
            email.setError("Valid E-mail Address");
            valid = false;
        } else {
            email.setError(null);
        }
        if (phonestr.isEmpty()) {
            parent_no.setError("Valid Parent Phone Number");
            valid = false;
        } else {
            parent_no.setError(null);
        }

        return valid;
    }
}
