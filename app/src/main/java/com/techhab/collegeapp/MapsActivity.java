package com.techhab.collegeapp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Kevin on 11/9/2014.
 */
public class MapsActivity extends ActionBarActivity {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static boolean academicBuildings = false;
    private static boolean residentialHalls = false;
    private static boolean showAll = false;
    private static Menu mMenu;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_maps, mMenu);
        return super.onCreateOptionsMenu(mMenu);
    }
    public void chooseBuildings() {
        new MaterialDialog.Builder(this)
                .title("View Buildings")
                .items(new String[]{"Academic", "Residential", "Offices",
                                        "Recreational", "Other", "All"})
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMulti() {
                    @Override
                    public void onSelection(MaterialDialog dialog, Integer[] which,
                                            CharSequence[] text) {
                        Log.e("Choose buildings dialog on selection", which.toString());
//                        if (which != null) {
//                            for (int i : which) {
//
////                                if (i == 0) {
////
////                                }
////                                if 0 {
////                                    Academic
////                                }
////                                else if {
////
////                                }
//                            }
//                        }
//                        else {
//                            //none selected Error
//
//                        }
                    }
                })
                .positiveText("OK")
                .positiveColor(getResources().getColor(R.color.kzooOrange))
                .negativeText("Cancel")
                .autoDismiss(false)
                .negativeColor(getResources().getColor(R.color.gray))
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_buildings_select:
                chooseBuildings();
                return true;
            default:
                return true;
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        //clear map of markers
        mMap.clear();

        //KCollege
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.290304, -85.601896), 17.0f));
//        CameraUpdateFactory.zoomTo(5);
//        if (!(academicBuildings&&residentialHalls)) {
//            showAll = true;
//        }
//        else showAll = false;
        if (academicBuildings || showAll) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(42.290304, -85.601896))
                    .title("Kalamazoo College")
                    .snippet("Here lies the hopes and dreams of thousands.Buried."));
        }
        if (residentialHalls || showAll) {
            //Mandelle Hall
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(42.290056, -85.60088))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Mandelle Hall")
                    .snippet("Is great description. nine-outta-seven."));

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(42.29006, -85.6007))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Test")
                    .snippet("Disappear!"));
        }

    }
}
