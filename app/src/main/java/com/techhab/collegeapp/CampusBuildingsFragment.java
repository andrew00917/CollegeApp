package com.techhab.collegeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;
import it.gmariotti.cardslib.library.view.ForegroundLinearLayout;


public class CampusBuildingsFragment extends Fragment {

    private static final String POSITION = "position";
    private static final int NUMBER_CARD_IN_COMMON_TAB = 4;
    private static final int NUMBER_CARD_IN_DORMITORIES_TAB = 6;
    private static final int NUMBER_CARD_IN_DEPARTMENTS_TAB = 7;
    //show define in xml
    private static String[][] titles = new String[][]{
            {"HICKS CENTER", "Upjohn Library","Mail Center", "BookStore" },
            {"Crissey Hall", "DeWaters", "Harmon", "Hoben", "Severn", "Trowbridge" },
            {"Anderson Athletic Center", "Arcus Center", "Dow Science Center", "Humphrey House", "Light Fine Arts", "Nelda K. Balch Playhouse", "Olds·Upton Science" }
    };

    private List<Card> cards;

    public CampusBuildingsFragment() {
        // Required empty public constructor
    }

    public static Fragment createNewInstance(int position) {
        Fragment fragment = new CampusBuildingsFragment();
        Bundle args = new Bundle();
        //POSITION 0 for COMMON tab
        //POSITION 1 for DORMITORIES tab
        //POSITION 2 for DEPARTMENTS tab
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_campus_buildings, container, false);

        Integer tabPosition = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            tabPosition = (Integer) arguments.get(POSITION);
        }
        cards = new ArrayList<>();
        if (tabPosition != null) {
            switch (tabPosition) {
                case 0:
                    //set up card view for Common tab
                    setupRecycleViewForCommon(tabPosition, view);
                    break;
                case 1:
                    //set up card view for Dormitories tab
                    setupRecycleViewForDormitories(tabPosition, view);
                    break;
                case 2:
                    //set up card view for departments tab
                    setupRecycleViewForDepartments(tabPosition, view);
                    break;
                default:
                    break;
            }
        }
        if (cards != null) {
            CardArrayRecyclerViewAdapter recyclerViewAdapter = new CardArrayRecyclerViewAdapter(getActivity(), cards);
            CardRecyclerView cardRecyclerView = (CardRecyclerView) view.findViewById(R.id.my_recycler_view);
            cardRecyclerView.setHasFixedSize(false);
            cardRecyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));

            cardRecyclerView.setAdapter(recyclerViewAdapter);
        }
        return view;
    }

    private void setupRecycleViewForCommon(Integer tabPosition, View view) {
        String expandDescription[] = new String[NUMBER_CARD_IN_COMMON_TAB];
        expandDescription[0] = "This is a description of Hicks Center We get a reference to the ExpandableListView, expandableListView.\n" +
                "\n" +
                "Then we call MyDataProvider.getDataHashMap() to get the HashMap containing the lists of countries and cities. We assign this to our HashMap, countriesHashMap.";

        expandDescription[1] = "This is a description for Hoben Halllllllllllllllll";
        expandDescription[2] = "This is a description for Hoben Halllllllllllllllll";
        expandDescription[3] = "This is a description for Hoben Halllllllllllllllll";
        //set up recycle adapter
        cards = new ArrayList<Card>();
        for (int i = 0; i < expandDescription.length; i++) {
            //initialize new card
            Card card = initializeCardView(titles[tabPosition][i], i, expandDescription[i]);
            cards.add(card);
        }
    }

    private void setupRecycleViewForDormitories(Integer tabPosition, View view) {
        String expandDescription[] = new String[NUMBER_CARD_IN_DORMITORIES_TAB];
        expandDescription[0] = "This is a description of Hicks Center We get a reference to the ExpandableListView, expandableListView.\n" +
                "\n" +
                "Then we call MyDataProvider.getDataHashMap() to get the HashMap containing the lists of countries and cities. We assign this to our HashMap, countriesHashMap.";

        expandDescription[1] = "This is a description for Hoben Halllllllllllllllll";
        expandDescription[2] = "This is a description for Hoben Halllllllllllllllll";
        expandDescription[3] = "This is a description for Hoben Halllllllllllllllll";
        expandDescription[4] = "This is a description for Hoben Halllllllllllllllll";
        expandDescription[5] = "This is a description for Hoben Halllllllllllllllll";
        //set up recycle adapter
        cards = new ArrayList<Card>();
        for (int i = 0; i < expandDescription.length; i++) {
            Card card = initializeCardView(titles[tabPosition][i], i, expandDescription[i]);
            cards.add(card);
        }
    }

    private void setupRecycleViewForDepartments(Integer tabPosition, View view) {
        String expandDescription[] = new String[NUMBER_CARD_IN_DEPARTMENTS_TAB];
        expandDescription[0] = "This is a description of Hicks Center We get a reference to the ExpandableListView, expandableListView.\n" +
                "\n" +
                "Then we call MyDataProvider.getDataHashMap() to get the HashMap containing the lists of countries and cities. We assign this to our HashMap, countriesHashMap.";

        expandDescription[1] = "This is a description for Hoben Halllllllllllllllll";
        expandDescription[2] = "This is a description for Hoben Halllllllllllllllll";
        expandDescription[3] = "This is a description for Hoben Halllllllllllllllll";
        expandDescription[4] = "This is a description for Hoben Halllllllllllllllll";
        expandDescription[5] = "This is a description for Hoben Halllllllllllllllll";
        expandDescription[6] = "This is a description for Hoben Halllllllllllllllll";

        //set up recycle adapter
        cards = new ArrayList<Card>();
        for (int i = 0; i < expandDescription.length; i++) {
            Card card = initializeCardView(titles[tabPosition][i], i, expandDescription[i]);
            cards.add(card);
        }
    }

    private Card initializeCardView(String cardTitle, int index, String expandTitle) {
        //create new card
        CustomCard card = new CustomCard(getActivity(), index, cardTitle);

        //create new Card Expand
        CardExpand cardExpand = new CardExpand(getActivity());
        cardExpand.setTitle(expandTitle);
        card.addCardExpand(cardExpand);

        ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().enableForExpandAction();
        card.setViewToClickToExpand(viewToClickToExpand);

        card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                card.doToogleExpand();
            }
        });

        return card;
    }

    class CustomCard extends Card {

        private String cardTitle;
        int position;
        TextView tvTitle;
        Button btnMapIt;
        FrameLayout banner;

        CustomCard(Context context, int index, String cardTitle) {
            this(context);
            this.position = index;
            this.cardTitle = cardTitle;
        }

        public CustomCard(Context context) {
            super(context, R.layout.buildings_recycle);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            tvTitle = (TextView) view.findViewById(R.id.building_title);
            tvTitle.setText(cardTitle);

            btnMapIt = (Button) view.findViewById(R.id.mapit_button);
            btnMapIt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent activityChangeIntent = new Intent(mContext, MapsActivity.class);
                    startActivity(activityChangeIntent);
                }
            });

            banner = (FrameLayout) view.findViewById(R.id.building_banner);
            if (position == 0) {
                banner.setBackgroundResource(R.drawable.domo);
            } else if (position == 1) {
                banner.setBackgroundResource(R.drawable.k_banner_night);
            }
        }
    }
}
