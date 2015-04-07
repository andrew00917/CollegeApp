package com.techhab.collegeapp;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;

/**
 * Created by akhavantafti on 4/7/2015.
 */
public abstract class BaseObservableRecyclerFragment extends Fragment {
    protected static final String POSITION = "position";
    protected List<Card> cards;
    protected ObservableScrollCardRecylerView mCardRecyclerView;
    protected Integer tabPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResourceId(), container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            tabPosition = (Integer) arguments.get(POSITION);
        }

        cards = setupRecyclerCards();
        if (cards != null) {
            CardArrayRecyclerViewAdapter recyclerViewAdapter = new CardArrayRecyclerViewAdapter(getActivity(), cards);
            mCardRecyclerView = (ObservableScrollCardRecylerView) view.findViewById(R.id.my_recycler_view);
            mCardRecyclerView.setHasFixedSize(false);
            mCardRecyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));

            Activity parentActivity = getActivity();
            if (parentActivity instanceof ObservableScrollViewCallbacks) {
                // Scroll to the specified offset after layout
                Bundle args = getArguments();
                if (args != null && args.containsKey(BaseObservableRecyclerActivity.ARG_SCROLL_Y)) {
                    final int scrollY = args.getInt(BaseObservableRecyclerActivity.ARG_SCROLL_Y, 0);
                    ViewTreeObserver vto = mCardRecyclerView.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                mCardRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                mCardRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                            //move Card Recycler view
                            ViewPropertyAnimator.animate(mCardRecyclerView).translationY(scrollY).setDuration(0).start();
                        }
                    });
                }
                mCardRecyclerView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
            }
            mCardRecyclerView.setAdapter(recyclerViewAdapter);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected abstract List<Card> setupRecyclerCards();

    protected abstract int getLayoutResourceId();
}
