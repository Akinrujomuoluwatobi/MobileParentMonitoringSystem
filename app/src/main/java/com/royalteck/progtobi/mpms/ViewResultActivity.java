package com.royalteck.progtobi.mpms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import Tasks.ResultFetchTask;

public class ViewResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String stud_matric = getIntent().getStringExtra("studmatric");
        String session = getIntent().getStringExtra("session");
        String semester = getIntent().getStringExtra("semester");
        getSupportActionBar().setTitle(stud_matric);
        ResultFetchTask resultFetchTask = new ResultFetchTask(ViewResultActivity.this);
        resultFetchTask.execute(stud_matric, session, semester);


    }


}
