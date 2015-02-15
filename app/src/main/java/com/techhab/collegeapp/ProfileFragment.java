package com.techhab.collegeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.techhab.collegeapp.application.CollegeApplication;


/**
 * Created by Jae Hyun Choe 1/13/2015
 */
public class ProfileFragment extends Fragment {

    public static final String ARG_NAME = "user_name";
    public static final String ARG_ID = "user_id";
    public static final String ARG_EMAIL = "user_email";
    private CollegeApplication application;
    private String userName;
    private String userId;
    private String userEmail;

    View v;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId User Id.
     * @param userEmail User Email.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(String userName, String userId, String userEmail) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, userName);
        args.putString(ARG_ID, userId);
        args.putString(ARG_EMAIL, userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(ARG_NAME);
            userId = getArguments().getString(ARG_ID);
            userEmail = getArguments().getString(ARG_EMAIL);
        }
        application = (CollegeApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView name = (TextView) v.findViewById(R.id.profile_user_full_name);
        TextView email = (TextView) v.findViewById(R.id.profile_user_email);
        TextView id = (TextView) v.findViewById(R.id.profile_user_id);
        name.setText(userName);
        email.setText(userEmail);
        id.setText("ID Number: " + userId);

        ListView mListView = (ListView) v.findViewById(R.id.profile_listview);
        mListView.setDivider(null);

        // TODO: find a way to include summary for each title
        // summary provided for existing titles in strings.xml
        // Suggestion: use recycler view instead of listview.
        String[] titles = getActivity().getResources().getStringArray(R.array.profile_titles);
        String[] subtitles = getActivity().getResources().getStringArray(R.array.profile_subtitles);

        /*ArrayAdapter<String> mAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.profile_listview_row, R.id.title, titles);*/

        ProfileAdapter profileAdapter = new ProfileAdapter(getActivity(),
                R.layout.profile_listview_row, titles, subtitles);

        // Assign adapter to ListView
        mListView.setAdapter(profileAdapter);

        // ListView Item Click Listener
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                Toast.makeText(getActivity(), "Position: " + position, Toast.LENGTH_SHORT).show();
                if ( application.getPinState() ) {
                    LoginPINFragment fragment =  new LoginPINFragment();
                    fragment.setToFragment(position);
                    changeFragment(fragment);
                }
                else {
                    // AlertDialog to sign in with password
                    final EditText input = new EditText(getActivity());
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    AlertDialog passDialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Enter Password")
                            .setMessage("Please enter your password")
                            .setView(input)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    if (input.getText().toString().equals(application.getCurrentUser().getPassword())) {
                                        Fragment fragment;
                                        if (position == 0) {
                                            // change to MyCourses fragment
                                            fragment = new MyCoursesFragment();
                                        } else if (position == 1) {
                                            //
                                            fragment = new MyCoursesFragment();
                                        } else {
                                            //
                                            fragment = new MyCoursesFragment();
                                        }
                                        changeFragment(fragment);
                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "Incorrect password", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .create();
                    // Open the soft keyboard
                    passDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    passDialog.show();
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class ProfileAdapter extends ArrayAdapter<String> {

        Context context;
        int layoutResourceId;
        String[] titles = null;
        String[] subtitles = null;


        public ProfileAdapter(Context context, int layoutResourceId, String[] titles, String[] subtitles) {
            super(context, layoutResourceId, titles);

            this.context = context;
            this.layoutResourceId = layoutResourceId;
            this.titles = titles;
            this.subtitles = subtitles;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewParent) {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId, viewParent, false);
            }

            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView subtitle = (TextView) convertView.findViewById(R.id.subtitle);

            title.setText(titles[position]);
            subtitle.setText(subtitles[position]);

            return convertView;
        }

    }
    private void changeFragment(Fragment fragment) {
        ProfileActivity activity = (ProfileActivity) getActivity();
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(((ViewGroup)(getView().getParent())).getId(), fragment);
        transaction.commit();
    }

}
