package com.techhab.collegeapp;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class InfoFragment extends Fragment {

    private CollegeApplication application;

    private Context context;

    private static final int FACILITIES = 1;

    View v;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        application = (CollegeApplication) getActivity().getApplication();

        context = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_info, parent, false);

        if ( getActivity().getActionBar() != null && ! getActivity().getActionBar().isShowing()) {
            getActivity().getActionBar().show();
        }

        ListView infoList = (ListView) v.findViewById(R.id.info_listView);
        String[] values = getResources().getStringArray(R.array.campus_info);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
                , android.R.layout.simple_list_item_1, android.R.id.text1, values);
        infoList.setAdapter(adapter);

        infoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                if (item.equals("Facilities")) {
                    ((InfoActivity) getActivity()).showFragment(FACILITIES, false);
                }

                Toast.makeText(context, "" + item.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        return v;
    }


}
