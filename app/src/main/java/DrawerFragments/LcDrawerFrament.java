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

import com.royalteck.progtobi.mpms.LvContactParentActivity;
import com.royalteck.progtobi.mpms.R;
import com.royalteck.progtobi.mpms.ViewResultActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class LcDrawerFrament extends Fragment {


    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View container_view;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    String Stud_matric;

    public LcDrawerFrament() {
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
        final View layout = inflater.inflate(R.layout.lc_drawer_fragment2, container, false);
        TextView lc_viewresult1, lc_viewstudreg, lc_contactstudpa;
        lc_viewresult1 = (TextView) layout.findViewById(R.id.lc_viewresult);
        lc_viewresult1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder[] builder = {new AlertDialog.Builder(getActivity())};
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = (inflater.inflate(R.layout.dialoglevelcord, null));
                builder[0].setView(dialogView);
                builder[0].setTitle("Student Details");
                final EditText matric;
                final Spinner sessionsp;
                final String[] session = new String[1];
                final String[] semester = {""};
                matric = (EditText) dialogView.findViewById(R.id.studmatricdia);
                sessionsp = (Spinner) dialogView.findViewById(R.id.sessionlvdia);
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
                semestergrp = (RadioGroup) dialogView.findViewById(R.id.semestergrp);
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
                        intent.putExtra("studmatric", matric.getText().toString());
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

        lc_viewstudreg = (TextView) layout.findViewById(R.id.lc_viewregstatus);
        lc_viewstudreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter Student Matric");

                final EditText input_field = new EditText(getActivity());

                builder.setView(input_field);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Stud_matric = input_field.getText().toString();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();
                    }
                });

                builder.show();
            }
        });

        lc_contactstudpa = (TextView) layout.findViewById(R.id.lc_contactparent);
        lc_contactstudpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LvContactParentActivity.class);
                startActivity(intent);
            }
        });

        return layout;
    }


}
