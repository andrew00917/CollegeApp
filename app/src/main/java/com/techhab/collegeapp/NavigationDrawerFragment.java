package com.techhab.collegeapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.techhab.collegeapp.application.CollegeApplication;

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

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView, mDrawerListViewSettingsSupport;
    private View mFragmentContainerView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

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
        mDrawerListView = (ListView) view.findViewById(R.id.drawer_list);
        mDrawerListViewSettingsSupport = (ListView) view.findViewById(R.id.settings_support);
        mProfileLayout = (LinearLayout) view.findViewById(R.id.profile_layout);

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
                TextView id = (TextView) v.findViewById(R.id.nav_user_name);
                TextView email = (TextView) v.findViewById(R.id.user_email);
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra(USER_ID_KEY, id.getText().toString());
                intent.putExtra(USER_EMAIL_KEY, email.getText().toString());
                getActivity().startActivity(intent);
            }
        });
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
     * Custom ListView adapter for the nav drawer
     */
    class NavAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        private Context context;

        String[] nav_drawer_items;

        // Array of drawables. MUST BE IN THE SAME ORDER AS THE nav_drawer_items STRING ARRAY!
        int[] images = { R.drawable.phone, R.drawable.bookmark, R.drawable.logout };

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
         *  Build and show material dialog for emergency call
         */
        private void emergencyCallDialog() {
            new MaterialDialog.Builder(getActivity())
                    .title("Choose who to call...")
                    .items(new String[]{"Campus Security", "Health"})
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view,
                                                int which, CharSequence text) {
                            if (which == 0) {
                                // TODO: call security
                                dialog.dismiss();
                            } else if (which == 1) {
                                // TODO: call health
                                dialog.dismiss();
                            }
                        }
                    })
                    .negativeText("Cancel")
                    .autoDismiss(false)
                    .negativeColor(getResources().getColor(R.color.abc_secondary_text_material_light))
                    .callback(new MaterialDialog.Callback() {
                        // Material Dialog library needs this empty method
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            // There is no positive button existing
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .cancelable(false)
                    .show();
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
