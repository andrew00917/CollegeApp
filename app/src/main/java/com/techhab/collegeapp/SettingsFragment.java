package com.techhab.collegeapp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.techhab.collegeapp.application.CollegeApplication;

/**
 * Created by jhchoe on 11/01/15.
 */
public class SettingsFragment extends PreferenceFragment {

    /**
     *  Reference:
     *  http://developer.android.com/guide/topics/ui/settings.html#DefiningPrefs
     */
    private CollegeApplication application;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (CollegeApplication) getActivity().getApplication();
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        setUpPinOptions();

        Preference forgetPin = findPreference("pref_key_forget_pin");
        forgetPin.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {

                application = (CollegeApplication) getActivity().getApplication();
                // Dummy user information
                final User user = application.getCurrentUser();
                user.setFirstName("Jesus");
                user.setLastName("Fred");
                user.setPassword("12321");


                // Set an EditText view to get user input
                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                final AlertDialog passDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Forget PIN")
                        .setMessage("Please enter your password")
                        .setView(input)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (input.getText().toString().equals(user.getPassword())) {
                                    application.setPinState(false);
                                    setUpPinOptions();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "Incorrect password", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                // Open the soft keyboard
                passDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                passDialog.show();


//                boolean wrapInScrollView = true;
//                new MaterialDialog.Builder(getActivity())
//                        .title("Enter Password")
//                        .customView(R.layout.dialog_password, wrapInScrollView)
//                        .positiveText("OK")
//                        .negativeText("Cancel")
//                        .callback(new MaterialDialog.ButtonCallback() {
//                            @Override
//                            public void onPositive(MaterialDialog dialog) {
////                                if ( **entered password in dialog** equals.(application.getCurrentUser().getPassword()))
//                                application.setPinState(false);
//                                setUpPinOptions();
//                            }
//                        })
//                        .build()
//                        .show();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpPinOptions();
    }

    private void setUpPinOptions() {
        boolean pinState = application.getPinState();
        findPreference("pref_key_setup_pin").setEnabled( ! pinState );
        findPreference("pref_key_modify_pin").setEnabled( pinState );
        findPreference("pref_key_forget_pin").setEnabled( pinState );
    }

}
