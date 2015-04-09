package com.techhab.collegeapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.gr;
import com.techhab.collegeapp.application.CollegeApplication;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CourseSearchFragment extends Fragment {

    public static final String ARG_OBJECT = "object";

    private CollegeApplication application;

    private Context context;

    View v;

    private ExpandableListView mListView;

    // @formatter:off
    private static final int[] CATEGORY = new int[] {
        R.array.fine_arts,
        R.array.humanities,
        R.array.interdisciplinary,
        R.array.modern_classical_lang_lit,
        R.array.science_math,
        R.array.physical_education,
        R.array.social_science
    };
    private int lastSelectedGroup;
    // @formatter:on

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

        mListView = (ExpandableListView) v.findViewById(R.id.my_list_view);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(
//                getActivity());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mListView.setLayoutManager(layoutManager);

        String[] dataset = getActivity().getResources().getStringArray(R.array.category_array);

        List<String> listDataHeader = Arrays.asList(dataset);
        Map<String,List<String>> mapDataChild = new HashMap<>();
        int headerSize = CATEGORY.length;
        for (int i = 0; i < headerSize; i++) {
            String header = dataset[i];
            String[] content = getActivity().getResources().getStringArray(CATEGORY[i]);
            mapDataChild.put(header, Arrays.asList(content));
        }
        ExpandableListAdapter adapter = new ExpandableListAdapter(getActivity(), listDataHeader, mapDataChild);

        // Assign adapter to ListView
        mListView.setAdapter(adapter);

        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //always keep expand status for selected view at group position
                if (mListView.isGroupExpanded(groupPosition)) {
                    mListView.collapseGroup(groupPosition);
                    v.findViewById(R.id.img_group).startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_down_ccw));
                } else {
                    mListView.collapseGroup(lastSelectedGroup);
                    mListView.expandGroup(groupPosition);
                    lastSelectedGroup = groupPosition;
                    v.findViewById(R.id.img_group).startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_down_ccw));
                }
                return true;
            }
        });

        // ListView Item Click Listener
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                final boolean isLastElement = position >= parent.getCount();
//
//                // Arrow animation (rotate)
//                TextView text = (TextView) view.findViewById(R.id.category);
//                ImageView arrow = (ImageView) view.findViewById(R.id.action_icon);
//                View divider = view.findViewById(R.id.divider);
//                if (expandedPosition == position) {
//                    /* COLLAPSE */
//                    text.setTextColor(getResources().getColor(R.color.abc_primary_text_material_light));
//                    arrow.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_down_ccw));
//                    divider.setVisibility(View.GONE);
//                    if ( ! isLastElement) {
//                        View nextDivider
//                                = parent.getChildAt(position + 1).findViewById(R.id.divider);
//                        nextDivider.setVisibility(View.GONE);
//                    }
//                    expandedPosition = -1;
//                } else {
//                    /* COLLAPSE PREVIOUS */
//                    if (expandedPosition > -1) {
//                        TextView previousText
//                                = (TextView) parent.getChildAt(expandedPosition).findViewById(R.id.category);
//                        previousText.setTextColor(getResources().getColor(R.color.abc_primary_text_material_light));
//                        ImageView previousArrow
//                                = (ImageView) parent.getChildAt(expandedPosition).findViewById(R.id.action_icon);
//                        previousArrow.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_down_ccw));
//                        View previous
//                                = parent.getChildAt(expandedPosition).findViewById(R.id.divider);
//                        previous.setVisibility(View.GONE);
//                        if ( ! isLastElement) {
//                            View nextDivider
//                                    = parent.getChildAt(expandedPosition + 1).findViewById(R.id.divider);
//                            nextDivider.setVisibility(View.GONE);
//                        }
//                    }
//                    /* EXPAND */
//                    // change text color
//                    text.setTextColor(getResources().getColor(R.color.main00));
//                    // arrow animation
//                    arrow.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_up_ccw));
//                    // show dividers
//                    divider.setVisibility(View.VISIBLE);
//                    if ( ! isLastElement) {
//                        View previousDivider
//                                = parent.getChildAt(position + 1).findViewById(R.id.divider);
//                        previousDivider.setVisibility(View.VISIBLE);
//                    }
//                    expandedPosition = position;
//                }
//                // ListView Clicked item value
//                String itemValue = (String) mListView.getItemAtPosition(position);
//
//                // Show Alert
//                Toast.makeText(getActivity(), "Position :" + position + "  ListItem : "
//                        + itemValue, Toast.LENGTH_LONG).show();ø
//            }
//        });

//        RecyclerAdapter mAdapter = new RecyclerAdapter(dataset, getActivity());
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter{

        private final Context context;
        private final List<String> listDataHeader;
        private final Map<String, List<String>> mapChildData;

        public ExpandableListAdapter(Context context, List<String> listDataHeader, Map<String, List<String>> mapChildData){
            this.context = context;
            this.listDataHeader = listDataHeader;
            this.mapChildData = mapChildData;
        }
        @Override
        public int getGroupCount() {
            return this.listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this.mapChildData.get(listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return listDataHeader.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.mapChildData.get(this.listDataHeader.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //TODO: this is need a separate or generic layout
                convertView = inflater.inflate(R.layout.helper_group, parent, false);
            }
            TextView tvTitle  = (TextView) convertView.findViewById(R.id.helper_group_tvQuestion);
            tvTitle.setText(headerTitle);

            ImageView imgGroup = (ImageView) convertView.findViewById(R.id.img_group);
            if (isExpanded) {
                imgGroup.setBackgroundResource(R.drawable.ic_action_collapse);
            } else {
                imgGroup.setBackgroundResource(R.drawable.ic_action_expand);
            }


            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String content = (String) getChild(groupPosition, childPosition);
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //TODO: this is need a separate or generic layout
                convertView = inflater.inflate(R.layout.helper_item, null);
            }
            TextView tvTitle  = (TextView) convertView.findViewById(R.id.helper_item_tvAnswer);
            tvTitle.setText(content);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

//    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
//        private String[] mDataset;
//        private Context mContext;
//
//        public RecyclerAdapter(String[] dataset, Context context) {
//            mDataset = dataset;
//            mContext = context;
//        }
//
//        // Not use static
//        public class ViewHolder extends RecyclerView.ViewHolder {
//
//            public TextView mTextView;
//
//            public ViewHolder(View itemView) {
//                super(itemView);
//                mTextView = (TextView) itemView.findViewById(R.id.category);
//                itemView.setOnTouchListener(new View.OnTouchListener(){
//
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        switch (event.getAction()) {
//                            case MotionEvent.ACTION_DOWN:
//                                ((AcademicActivity) getActivity()).buttonPressed(v);
//                                break;
//                            case MotionEvent.ACTION_CANCEL:
//                            case MotionEvent.ACTION_OUTSIDE:
//                                ((AcademicActivity) getActivity()).buttonReleased(v);
//                                break;
//                            case MotionEvent.ACTION_UP:
//                                ((AcademicActivity) getActivity()).buttonReleased(v);
//                                Toast.makeText(mContext, mTextView.getText().toString(), Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                        return true;
//                    }
//                });
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return mDataset.length;
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            holder.mTextView.setText(mDataset[position]);
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(
//                    R.layout.course_search_listview_row, parent, false);
//            return new ViewHolder(view);
//        }
//    }
}
