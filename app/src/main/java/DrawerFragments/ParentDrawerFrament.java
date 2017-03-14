package DrawerFragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.royalteck.progtobi.mpms.ParentContactLcActivity;
import com.royalteck.progtobi.mpms.R;
import com.royalteck.progtobi.mpms.ViewResultActivity;

import Model._User;
import db.DbLocal;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParentDrawerFrament extends Fragment {


    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View container_view;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    public ParentDrawerFrament() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreference(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;

        }


    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        container_view = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_clos) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mDrawerLayout + "");

                }
                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
            /*
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);

                }

            }
            */
        };


        if (mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(container_view);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();

            }
        });
    }

    public static String readFromPreference(Context context, String preferenceName, String defaultVale) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultVale);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        TextView viewresult, viewregstatus, contactlc, feedback;

        View layout = inflater.inflate(R.layout.parent_drawer_fragment2, container, false);

        viewresult = (TextView) layout.findViewById(R.id.parent_viewresult);
        viewregstatus = (TextView) layout.findViewById(R.id.parent_viewregstatus);
        contactlc = (TextView) layout.findViewById(R.id.parent_contactlc);
        feedback = (TextView) layout.findViewById(R.id.parent_givefeedback);

        _User u = new DbLocal(getActivity()).fetch_User();
        final String matric = u.getUsername();

        viewresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder[] builder = {new AlertDialog.Builder(getActivity())};
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = (inflater.inflate(R.layout.dialogparent, null));
                builder[0].setView(dialogView);
                builder[0].setTitle("Result Details");
                final Spinner sessionsp;
                final String[] session = new String[1];
                final String[] semester = {""};
                sessionsp = (Spinner) dialogView.findViewById(R.id.sessionpadia);
                sessionsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        session[0] = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                RadioGroup semestergrp;
                semestergrp = (RadioGroup) dialogView.findViewById(R.id.semestergrppa);
                semestergrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        switch (checkedId) {
                            case R.id.firstsem:

                                semester[0] = "first";
                                break;
                            case R.id.secondsem:

                                semester[0] = "second";
                                break;
                        }
                    }
                });


                builder[0].setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Stud_matric = input_field.getText().toString();
                        Intent intent = new Intent(getActivity(), ViewResultActivity.class);
                        intent.putExtra("studmatric", matric);
                        intent.putExtra("session", session[0]);
                        intent.putExtra("semester", semester[0]);
                        startActivity(intent);

                    }
                });

                builder[0].setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //dialogInterface.cancel();
                    }
                });

                builder[0].show();
            }
        });

        viewregstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        contactlc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ParentContactLcActivity.class);
                startActivity(intent);
            }
        });


        return layout;
    }

}
