package com.royalteck.progtobi.mpms;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Tasks.Mysingleton;

public class AddEventsActivity extends AppCompatActivity {
    EditText Eventheading, Eventcaption, EventVenue;
    TextView Datetxt, Timetxt;
    Button addevent;
    AlertDialog.Builder builder;
    ProgressBar regpb;
    String serverUrl="http://elearningapp.eu5.org/mpms_event.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Event");

        Eventheading = (EditText) findViewById(R.id.eventhead);
        EventVenue = (EditText) findViewById(R.id.eventvenue);
        Eventcaption = (EditText) findViewById(R.id.eventcaption);
        Datetxt = (TextView) findViewById(R.id.datetxtv);
        Timetxt = (TextView) findViewById(R.id.timetxtv);
        regpb = (ProgressBar) findViewById(R.id.uploadeventspb);
        addevent = (Button) findViewById(R.id.uploadevent);
        builder = new AlertDialog.Builder(AddEventsActivity.this);
        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validate()) {
                    return;
                }

                final String eventheading, eventvenue, eventcaption;
                eventheading = Eventheading.getText().toString();
                eventvenue = EventVenue.getText().toString();
                eventcaption = Eventcaption.getText().toString();

                if (!validate()) {
                    return;
                }

                if (Datetxt.getText().toString() == "" || Timetxt.getText().toString() == ""){
                    builder.setTitle("Missing Data");
                    displayAlert("Event Date or Time not set");
                    return;
                }

                if (!isOnline(AddEventsActivity.this)) {
                    Toast.makeText(AddEventsActivity.this, "No network connection", Toast.LENGTH_LONG).show();
                    return;
                }
                final String date = Datetxt.getText().toString();
                final String time = Timetxt.getText().toString();

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
                                Toast.makeText(AddEventsActivity.this, message, Toast.LENGTH_LONG).show();
                            } else if (code.equals("reg_success")) {
                                String message = jsonObject.getString("message");
                                regpb.setVisibility(View.GONE);
                                Toast.makeText(AddEventsActivity.this, message, Toast.LENGTH_LONG).show();
                                Eventheading.setText("");
                                EventVenue.setText("");
                                Eventcaption.setText("");
                                Datetxt.setText("");
                                Timetxt.setText("");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddEventsActivity.this, "Error Uploading Event... Try Again", Toast.LENGTH_SHORT).show();
                        regpb.setVisibility(View.GONE);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("eventtitle", eventheading);
                        params.put("eventvenue", eventvenue);
                        params.put("eventcaption", eventcaption);
                        params.put("eventdate", date);
                        params.put("eventtime", time);

                        return params;
                    }


                };

                Mysingleton.getInstance(AddEventsActivity.this).addtorequestque(stringRequest);
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


    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            TextView textView = (TextView) getActivity().findViewById(R.id.timetxtv);
            Log.d("Time", "onTimeSet: " + hourOfDay + ":" + minute);
            textView.setText(hourOfDay + ":" + minute);
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            TextView textView = (TextView) getActivity().findViewById(R.id.datetxtv);
            textView.setText(new StringBuilder().append(year)
                    .append("-").append(month + 1).append("-").append(day)
                    .append(" "));

        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public boolean validate() {
        boolean valid = true;

        String eventheading = Eventheading.getText().toString();
        String eventvenue = EventVenue.getText().toString();
        String eventcaption = Eventcaption.getText().toString();


        if (eventheading.isEmpty()) { //|| !android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches())
            Eventheading.setError("News Caption Field Empty");
            valid = false;
        } else {
            Eventheading.setError(null);
        }

        if (eventvenue.isEmpty()) {
            EventVenue.setError("News heading Field Empty");
            valid = false;
        } else {
            EventVenue.setError(null);
        }

        if (eventcaption.isEmpty()) {
            Eventcaption.setError("News Content Field Empty");
            valid = false;
        } else {
            Eventcaption.setError(null);
        }


        return valid;
    }

    public void displayAlert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

    }
}
