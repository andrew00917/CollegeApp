package com.techhab.collegeapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Jae Hyun Choe 1/13/2015
 */
public class ProfileFragment extends Fragment {

    public static final String ARG_ID = "user_id";
    public static final String ARG_EMAIL = "user_email";

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
    public static ProfileFragment newInstance(String userId, String userEmail) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
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
            userId = getArguments().getString(ARG_ID);
            userEmail = getArguments().getString(ARG_EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView id = (TextView) v.findViewById(R.id.profile_user_id);
        TextView email = (TextView) v.findViewById(R.id.profile_user_email);
        id.setText(userId);
        email.setText(userEmail);

        ListView mListView = (ListView) v.findViewById(R.id.profile_listview);
        mListView.setDivider(null);

        // TODO: find a way to include summary for each title
        // summary provided for existing titles in strings.xml
        // Suggestion: use recycler view instead of listview.
        String[] titles = getActivity().getResources().getStringArray(R.array.profile_titles);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(getActivity()
                , R.layout.profile_listview_row, R.id.title, titles);

        // Assign adapter to ListView
        mListView.setAdapter(mAdapter);

        // ListView Item Click Listener
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Position: " + position, Toast.LENGTH_SHORT).show();
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

}
