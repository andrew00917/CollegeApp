package com.techhab.collegeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by akhavantafti on 1/25/2015.
 */
public class HelpAndFeedBackFragment extends Fragment {
    int lastExpandGroup = -1;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_and_feedback, container, false);
        final ExpandableListView lvHelper = (ExpandableListView) view.findViewById(R.id.help_feedback_lvHelp);
        List<Helper> helpers = new ArrayList<>();
        helpers.add(new Helper("Question 1", " Answer 1"));
        helpers.add(new Helper("Question 2"," Answer 2"));
        helpers.add(new Helper("Question 3","Answer 3"));
        lvHelper.setAdapter(new HelperAdapter(getActivity(), helpers));
        lvHelper.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandGroup != -1 && groupPosition != lastExpandGroup)
                {
                    lvHelper.collapseGroup(lastExpandGroup);
                }
                lastExpandGroup = groupPosition;
            }
        });


        LinearLayout llSendFeedBack = (LinearLayout) view.findViewById(R.id.layout_feedBack);
        llSendFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FeedBackActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private class HelperAdapter extends BaseExpandableListAdapter
    {
        List<Helper> helperList = new ArrayList<>();
        private Context context;

        public HelperAdapter(Context context, List<Helper> helperList)
        {
            this.helperList = helperList;
            this.context = context;
        }

        @Override
        public int getGroupCount() {
            return helperList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return helperList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return helperList.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.helper_group, null);
            }
            TextView tvGroup = (TextView) convertView.findViewById(R.id.helper_group_tvQuestion);
            tvGroup.setText(helperList.get(groupPosition).question);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.helper_item, null);
            }
            TextView tvGroup = (TextView) convertView.findViewById(R.id.helper_item_tvAnswer);
            tvGroup.setText(helperList.get(groupPosition).answer);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

    private class Helper
    {
        private String question;
        private String answer;

        private Helper(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    }
}
