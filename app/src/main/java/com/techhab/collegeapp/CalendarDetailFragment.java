package com.techhab.collegeapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
    private int selectedPosition = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get argument
        Bundle arguments = getArguments();
        if (arguments != null) {
            selectedPosition = arguments.getInt("position", -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_detail, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        String[] dataset = new String[]{"Fall", "Winter", "Spring"};

        if (selectedPosition != -1) {
            RecyclerAdapter
                    mAdapter = new RecyclerAdapter(dataset[selectedPosition], getActivity());
            mRecyclerView.setAdapter(mAdapter);
        }

        return view;
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private String mDataset;
        private Context mContext;

        public RecyclerAdapter(String dataset, Context context) {
            mDataset = dataset;
            mContext = context;
        }

        // Not use static
        public class ViewHolder extends RecyclerView.ViewHolder {
            private final ImageView imgViewStatusTop;
            private final TextView tvSeasionName;
            private final LinearLayout layoutContainer;

            public ViewHolder(View itemView) {
                super(itemView);
                imgViewStatusTop = (ImageView) itemView.findViewById(R.id.card_image_view_top);

                tvSeasionName = (TextView) itemView.findViewById(R.id.tv_calendar_session);
                layoutContainer = (LinearLayout) itemView.findViewById(R.id.layout_container);
            }
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            //set seassion title
            holder.imgViewStatusTop.setVisibility(View.GONE);
            holder.tvSeasionName.setText(mDataset);

            String[] content = getActivity().getResources().getStringArray(CONTENT[selectedPosition]);
            String[] date = getActivity().getResources().getStringArray(DATE_2014[selectedPosition]);

            //set up view for content
            for (int i = 0; i < content.length; i++) {
                LinearLayout layoutContent = new LinearLayout(getActivity());
                layoutContent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layoutContent.setOrientation(LinearLayout.HORIZONTAL);

                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f);

                TextView tvContent = new TextView(getActivity());
                tvContent.setText(content[i]);
                tvContent.setTextAppearance(getActivity(), R.style.SingleLineListTextOnly_SubContent);
                tvContent.setLayoutParams(layoutParams);

                layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                TextView tvDate = new TextView(getActivity());
                tvDate.setText(date[i]);
                tvDate.setTextAppearance(getActivity(), R.style.SingleLineListTextOnly_SubContent);
                tvDate.setLayoutParams(layoutParams);

                layoutContent.addView(tvContent);
                layoutContent.addView(tvDate);

                holder.layoutContainer.addView(layoutContent);
            }
        }

        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.academic_calendar_recycle, parent, false);
            return new ViewHolder(view);
        }
    }

}
