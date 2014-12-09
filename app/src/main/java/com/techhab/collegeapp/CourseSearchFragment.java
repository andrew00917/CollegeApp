package com.techhab.collegeapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.techhab.collegeapp.application.CollegeApplication;


public class CourseSearchFragment extends Fragment {

    public static final String ARG_OBJECT = "object";

    private CollegeApplication application;

    private Context context;

    View v;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        String[] dataset = getActivity().getResources().getStringArray(R.array.subject_array);

        RecyclerAdapter mAdapter = new RecyclerAdapter(dataset, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private String[] mDataset;
        private Context mContext;

        public RecyclerAdapter(String[] dataset, Context context) {
            mDataset = dataset;
            mContext = context;
        }

        // Not use static
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.subject);
                itemView.setOnTouchListener(new View.OnTouchListener(){

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                ((AcademicActivity) getActivity()).buttonPressed(v);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_OUTSIDE:
                                ((AcademicActivity) getActivity()).buttonReleased(v);
                                break;
                            case MotionEvent.ACTION_UP:
                                ((AcademicActivity) getActivity()).buttonReleased(v);
                                Toast.makeText(mContext, mTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mDataset[position]);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.course_search_recycle, parent, false);
            return new ViewHolder(view);
        }
    }
}
