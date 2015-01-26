package com.techhab.collegeapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements NavigationDrawerCallbacks  {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * Keys for profile intent
     *      user id
     *      user email
     */
    private static final String USER_ID_KEY = "user_id";
    private static final String USER_EMAIL_KEY = "user_email";

    private String userId = "";
    private String userEmail = "";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mProfileListView, mDrawerListView, mDrawerListViewSettingsSupport;
    private View mFragmentContainerView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    private ProfileNavAdapter mProfileAdapter;
    private NavAdapter mNavAdapter;
    private SettingsSupportNavAdapter mNavAdapterSettingsSupport;

    public NavigationDrawerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    private LinearLayout mProfileLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // inflate the parent view (the entire layout)
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mProfileListView = (ListView) view.findViewById(R.id.profile_list);
        mDrawerListView = (ListView) view.findViewById(R.id.drawer_list);
        mDrawerListViewSettingsSupport = (ListView) view.findViewById(R.id.settings_support);
        mProfileLayout = (LinearLayout) view.findViewById(R.id.profile_layout);

        TextView id = (TextView) mProfileLayout.findViewById(R.id.nav_user_name);
        TextView email = (TextView) mProfileLayout.findViewById(R.id.user_email);
        userId = id.getText().toString();
        userEmail = email.getText().toString();

        mProfileAdapter = new ProfileNavAdapter(getActivity());
        mProfileListView.setAdapter(mProfileAdapter);
        mNavAdapter = new NavAdapter(getActivity());
        mDrawerListView.setAdapter(mNavAdapter);
        mNavAdapterSettingsSupport = new SettingsSupportNavAdapter(getActivity());
        mDrawerListViewSettingsSupport.setAdapter(mNavAdapterSettingsSupport);

        // set on click listener
        // TODO: add click animation to profileLayout
        mProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            selectItem(mCurrentSelectedPosition);

            mDrawerLayout.closeDrawer(Gravity.START);

            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            intent.putExtra(USER_ID_KEY, userId);
            intent.putExtra(USER_EMAIL_KEY, userEmail);
            getActivity().startActivity(intent);
            }
        });
        mProfileListView.setOnItemClickListener(mProfileAdapter);
        mDrawerListView.setOnItemClickListener(mNavAdapter);
        mDrawerListViewSettingsSupport.setOnItemClickListener(mNavAdapterSettingsSupport);

        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return view;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar mToolbar) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if ( ! isAdded()) return;
                getActivity().invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if ( ! isAdded()) return;
                if ( ! mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                }

                getActivity().invalidateOptionsMenu();
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    public void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
//        if (mDrawerLayout != null && isDrawerOpen()) {
//            inflater.inflate(R.menu.global, menu);
//        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        /*if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }*/
        if ((item.getItemId() == android.R.id.home)) {
             if (isDrawerOpen()) {
                mDrawerLayout.closeDrawer(mFragmentContainerView);
             } else {
                mDrawerLayout.openDrawer(mFragmentContainerView);
             }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selectItem(position);
    }

    /**
     * Custom ListView adapter for the profile nav drawer
     */
    class ProfileNavAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        private Context context;

        String[] profile_drawer_items;

        // Array of drawables. MUST BE IN THE SAME ORDER AS THE nav_drawer_items STRING ARRAY!
        int[] images = { R.drawable.ic_walk_grey600_48dp, R.drawable.ic_calendar };

        public ProfileNavAdapter(Context context) {
            this.context = context;
            profile_drawer_items = context.getResources().getStringArray(R.array.profile_drawer_items);
        }

        @Override
        public int getCount() {
            return profile_drawer_items.length;
        }

        @Override
        public Object getItem(int position) {
            return profile_drawer_items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = null;
            if (convertView == null) {
                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.nav_row, parent, false);
            } else {
                row = convertView;
            }
            TextView titleTextView = (TextView) row.findViewById(R.id.textView);
            ImageView titleImageView = (ImageView) row.findViewById(R.id.imageView);

            titleTextView.setText(profile_drawer_items[position]);
            titleImageView.setImageResource(images[position]);
            return row;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
//            Toast.makeText(getActivity(), "Position: " + position, Toast.LENGTH_SHORT).show();
            switch (position) {
                case 0:
                    profileOnClick();
                    break;
                default:
                    // TODO: NOT a default behavior
                    profileOnClick();
            }
        }

        private void profileOnClick() {
            selectItem(mCurrentSelectedPosition);

            mDrawerLayout.closeDrawer(Gravity.START);

            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            intent.putExtra(USER_ID_KEY, userId);
            intent.putExtra(USER_EMAIL_KEY, userEmail);
            getActivity().startActivity(intent);
        }
    }

    /**
     * Custom ListView adapter for the nav drawer
     */
    class NavAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        private Context context;

        String[] nav_drawer_items;

        // Array of drawables. MUST BE IN THE SAME ORDER AS THE nav_drawer_items STRING ARRAY!
//        int[] images = { R.drawable.numeric_1_box, R.drawable.phone, R.drawable.bookmark,
//                R.drawable.logout };
        int[] images = { R.drawable.ic_info_outline, R.drawable.phone, R.drawable.logout };

        public NavAdapter(Context context) {
            this.context = context;
            nav_drawer_items = context.getResources().getStringArray(R.array.nav_drawer_items);
        }

        @Override
        public int getCount() {
            return nav_drawer_items.length;
        }

        @Override
        public Object getItem(int position) {
            return nav_drawer_items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = null;
            if (convertView == null) {
                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.nav_row, parent, false);
            } else {
                row = convertView;
            }
            TextView titleTextView = (TextView) row.findViewById(R.id.textView);
            ImageView titleImageView = (ImageView) row.findViewById(R.id.imageView);

            titleTextView.setText(nav_drawer_items[position]);
            titleImageView.setImageResource(images[position]);
            return row;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
//            Toast.makeText(getActivity(), "Position: " + position, Toast.LENGTH_SHORT).show();
            switch (position) {
                case 0:
                    // build a dialog and show
                    emergencyCallDialog();
                    break;
                default:
                    // TODO: NOT a default behavior
                    emergencyCallDialog();
            }
        }

        /**
         *  Build and show material dialog for fragment_emergency call
         */
        private void emergencyCallDialog() {
            List<Emergency> emergencyList = new ArrayList<>();
            emergencyList.add(new Emergency("Campus Security","(269) 337-7321"));
            emergencyList.add(new Emergency("City of Kalamazoo Police","(517) 763-1377"));
            emergencyList.add(new Emergency("Sexual Assault Dean-On-Call","(517) 763-1377"));
            MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                    .title("Emergency Contacts")
                    .adapter(new EmergencyAdapter(getActivity(), emergencyList))
                    .dividerColorRes(R.color.gray)
                    .build();
            ListView lvEmergency = dialog.getListView();
            if (lvEmergency != null)
            {
                lvEmergency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Emergency emergency = (Emergency) parent.getItemAtPosition(position);
                        phoneCall(emergency.getPhone());
                    }
                });
            }
            dialog.show();
        }
    }

    private void phoneCall(String phoneNumber) {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(phoneIntent);
    }

    class EmergencyAdapter extends BaseAdapter
    {

        private Context context;
        private List<Emergency> emergencyList;

        public EmergencyAdapter(Context context, List<Emergency> emergencyList)
        {
            this.context =context;
            this.emergencyList = emergencyList;
        }

        @Override
        public int getCount() {
            return emergencyList.size();
        }

        @Override
        public Emergency getItem(int position) {
            return emergencyList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.fragment_emergency, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.emergency_tvName);
                viewHolder.tvPhone = (TextView) convertView.findViewById(R.id.emergency_tvPhone);
                convertView.setTag(viewHolder);

            }
            viewHolder = (ViewHolder) convertView.getTag();
            Emergency emergency = getItem(position);
            viewHolder.tvName.setText(emergency.getName());
            viewHolder.tvPhone.setText(emergency.getPhone());
            return convertView;
        }

        class ViewHolder
        {
            TextView tvName;
            TextView tvPhone;
        }
    }


    class Emergency
    {
        String name;
        String phone;
        public Emergency(String name, String phone)
        {
            this.name = name;
            this.phone = phone;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    /**
     * Custom ListView adapter for the Settings & Support section of the nav drawer
     */
    class SettingsSupportNavAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        private Context context;

        String[] nav_drawer_items;

        // Array of drawables. MUST BE IN THE SAME ORDER AS THE nav_drawer_items STRING ARRAY!
        int[] images = { R.drawable.cog, R.drawable.help_circle };

        public SettingsSupportNavAdapter(Context context) {
            this.context = context;
            nav_drawer_items = context.getResources().getStringArray(R.array.nav_drawer_settings_support);
        }

        @Override
        public int getCount() {
            return nav_drawer_items.length;
        }

        @Override
        public Object getItem(int position) {
            return nav_drawer_items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = null;
            if (convertView == null) {
                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.nav_row, parent, false);
            } else {
                row = convertView;
            }
            TextView titleTextView = (TextView) row.findViewById(R.id.textView);
            ImageView titleImageView = (ImageView) row.findViewById(R.id.imageView);

            titleTextView.setText(nav_drawer_items[position]);
            titleImageView.setImageResource(images[position]);
            return row;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
//            Toast.makeText(getActivity(), "Position: " + position, Toast.LENGTH_SHORT).show();
            Intent intent = null;
            switch (position) {
                case 0:
                    intent = new Intent(getActivity(), SettingsActivity.class);
                    break;
                default:
                    intent = new Intent(getActivity(), SettingsActivity.class);
            }
            getActivity().startActivity(intent);
        }
    }
}
