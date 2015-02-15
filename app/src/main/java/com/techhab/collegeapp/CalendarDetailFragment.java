package com.techhab.collegeapp;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by akhavantafti on 2/10/2015.
 */
public class CalendarDetailFragment extends Fragment {

    private static final int[] CONTENT = new int[]{
            R.array.Fall,
            R.array.Winter,
            R.array.Spring
    };

    private static final int[] DATE_2014 = new int[]{
            R.array.Fall_2014,
            R.array.Winter_2014,
            R.array.Spring_2014
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_detail, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        return view;
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private String[] mDataset;
        private Context mContext;

        public RecyclerAdapter(String[] dataset, Context context) {
            mDataset = dataset;
            mContext = context;
        }

        // Not use static
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView mTextView;
            public TableLayout tableView;

            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.term);
                tableView = (TableLayout) itemView.findViewById(R.id.table_layout);
            }
        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mTextView.setText(mDataset[position]);
            final TableLayout table = holder.tableView;

            String[] content = getActivity().getResources().getStringArray(CONTENT[position]);
            String[] date = getActivity().getResources().getStringArray(DATE_2014[position]);
            TableRow.LayoutParams rowParam = new TableRow.LayoutParams();
            rowParam.setMargins(1, 1, 1, 1);
            TextView text;
            for (int i = 0; i < (content.length >= date.length ? content.length : date.length); i++) {
                TableRow row = new TableRow(getActivity());

                for (int j = 0; j < 2; j++) {
                    text = new TextView(getActivity());
                    text.setTextColor(getResources().getColor(R.color.abc_primary_text_material_light));
                    text.setTextSize(14f);
                    text.setSingleLine();
                    if (j == 0) {
                        text.setPadding(16, 8, 8, 8);
                        text.setGravity(Gravity.START);
                        text.setTypeface(null, Typeface.BOLD);
                        text.setText(content[i]);
                    } else {
                        text.setPadding(8, 8, 0, 8);
                        text.setGravity(Gravity.START);
                        text.setText(date[i]);
                    }
                    row.addView(text, rowParam);
                }
                table.addView(row);
            }
        }

        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.academic_calendar_recycle, parent, false);
            return new RecyclerAdapter.ViewHolder(view);
        }
    }

}
