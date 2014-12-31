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
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        //Menu
        toolbar.inflateMenu(R.menu.menu_maps);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.action_choose_buildings:
                        Toast.makeText(getApplicationContext(),"Choose Buildings",Toast.LENGTH_SHORT).show();
                        chooseBuildings();
                        return true;
                }

                return false;
            }
        });


        setUpMapIfNeeded();
    }


    public void chooseBuildings() {
        new MaterialDialog.Builder(this)
                .title("View Buildings")
                .items(new CharSequence[]{"Academic", "Residential", "Offices",
                        "Recreational", "Other", "All"})
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == 0) {
                            academicBuildings = true;
                            setUpMap();
                        }
                        else if (which == 1) {
                            residentialHalls = true;
                            setUpMap();
                        }

                    }
                })
                .positiveText("Choose")
                .positiveColor(getResources().getColor(R.color.kzooOrange))
                .negativeText("Cancel")
                .autoDismiss(true)
                .negativeColor(getResources().getColor(R.color.gray))
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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
                    .snippet("Is great place. nine-outta-seven."));

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(42.29006, -85.6007))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Test")
                    .snippet("Disappear!"));
        }

    }
}
