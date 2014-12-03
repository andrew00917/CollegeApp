package com.techhab.collegeapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.techhab.collegeapp.application.CollegeApplication;


public class CourseSearchFragment extends Fragment {

    public static final String ARG_OBJECT = "object";

    private CollegeApplication application;

    private Context context;

    View v;

    private Spinner termSpinner;

    private Spinner subjectSpinner1;
    private Spinner subjectSpinner2;
    private Spinner subjectSpinner3;
    private Spinner subjectSpinner4;
    private Spinner subjectSpinner5;

    private Spinner levelSpinner1;
    private Spinner levelSpinner2;
    private Spinner levelSpinner3;
    private Spinner levelSpinner4;
    private Spinner levelSpinner5;

    public CourseSearchFragment() {
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
        v = inflater.inflate(R.layout.fragment_course_search, parent, false);

        ArrayAdapter<CharSequence> termAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.term_array, android.R.layout.simple_spinner_item);
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> subjectAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.subject_array, android.R.layout.simple_spinner_item);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.course_level_array, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        termSpinner = (Spinner) v.findViewById(R.id.spinner_term);
        termSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        termAdapter, R.layout.term_spinner_nothing_selected, getActivity()
                )
        );

        subjectSpinner1 = (Spinner) v.findViewById(R.id.spinner_subject_1);
        subjectSpinner1.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        subjectAdapter, R.layout.subject_spinner_nothing_selected, getActivity()
                )
        );
        subjectSpinner2 = (Spinner) v.findViewById(R.id.spinner_subject_2);
        subjectSpinner2.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        subjectAdapter, R.layout.subject_spinner_nothing_selected, getActivity()
                )
        );
        subjectSpinner3 = (Spinner) v.findViewById(R.id.spinner_subject_3);
        subjectSpinner3.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        subjectAdapter, R.layout.subject_spinner_nothing_selected, getActivity()
                )
        );
        subjectSpinner4 = (Spinner) v.findViewById(R.id.spinner_subject_4);
        subjectSpinner4.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        subjectAdapter, R.layout.subject_spinner_nothing_selected, getActivity()
                )
        );
        subjectSpinner5 = (Spinner) v.findViewById(R.id.spinner_subject_5);
        subjectSpinner5.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        subjectAdapter, R.layout.subject_spinner_nothing_selected, getActivity()
                )
        );

        levelSpinner1 = (Spinner) v.findViewById(R.id.spinner_course_level_1);
        levelSpinner1.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        levelAdapter, R.layout.course_level_spinner_nothing_selected, getActivity()
                )
        );
        levelSpinner2 = (Spinner) v.findViewById(R.id.spinner_course_level_2);
        levelSpinner2.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        levelAdapter, R.layout.course_level_spinner_nothing_selected, getActivity()
                )
        );
        levelSpinner3 = (Spinner) v.findViewById(R.id.spinner_course_level_3);
        levelSpinner3.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        levelAdapter, R.layout.course_level_spinner_nothing_selected, getActivity()
                )
        );
        levelSpinner4 = (Spinner) v.findViewById(R.id.spinner_course_level_4);
        levelSpinner4.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        levelAdapter, R.layout.course_level_spinner_nothing_selected, getActivity()
                )
        );
        levelSpinner5 = (Spinner) v.findViewById(R.id.spinner_course_level_5);
        levelSpinner5.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        levelAdapter, R.layout.course_level_spinner_nothing_selected, getActivity()
                )
        );

        return v;
    }


}
