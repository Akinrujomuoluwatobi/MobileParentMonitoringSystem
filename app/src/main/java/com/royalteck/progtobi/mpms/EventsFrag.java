package com.royalteck.progtobi.mpms;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Tasks.FetchNewsBdgTask;

/**
 * Created by PROG. TOBI on 05-Feb-17.
 */
public class EventsFrag extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.eventfraglayout, container, false);
        FetchNewsBdgTask fetchStudentsBackgroundTask = new FetchNewsBdgTask(getActivity());
        fetchStudentsBackgroundTask.execute("eventsfetch");

        return rootView;
    }
}
