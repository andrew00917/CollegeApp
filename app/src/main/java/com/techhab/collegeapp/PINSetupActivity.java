package com.techhab.collegeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.techhab.collegeapp.application.CollegeApplication;

public class PINSetupActivity extends ActionBarActivity {
    private CollegeApplication application;
    private String pin = null;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (CollegeApplication) getApplication();


        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        AlertDialog passDialog = new AlertDialog.Builder(this)
                .setTitle("Enter Password")
                .setMessage("Please enter your password")
                .setView(input)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if ( input.getText().toString().equals(application.getCurrentUser().getPassword()) ) {
                            setUp();
                        }

                        else {
                            Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                })
                .setCancelable(false)
                .create();
        // Open the soft keyboard
        passDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        passDialog.show();

    }

    private void setUp() {
        setContentView(R.layout.activity_pin);
        message = (TextView) findViewById(R.id.pin_message);
        message.setText("Please enter a PIN");
        message.setTextColor(Color.GRAY);

        final TextView pintext = (TextView) findViewById(R.id.pintext);
        Button btn1 = (Button) findViewById(R.id.button1);
        Button btn2 = (Button) findViewById(R.id.button2);
        Button btn3 = (Button) findViewById(R.id.button3);
        Button btn4 = (Button) findViewById(R.id.button4);
        Button btn5 = (Button) findViewById(R.id.button5);
        Button btn6 = (Button) findViewById(R.id.button6);
        Button btn7 = (Button) findViewById(R.id.button7);
        Button btn8 = (Button) findViewById(R.id.button8);
        Button btn9 = (Button) findViewById(R.id.button9);
        Button btn0 = (Button) findViewById(R.id.button0);
        findViewById(R.id.button_password).setVisibility(View.GONE);
        ImageButton btnback = (ImageButton) findViewById(R.id.button_backspace);

        btn1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("1");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("2");
            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("3");
            }
        });
        btn4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("4");
            }
        });
        btn5.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("5");
            }
        });
        btn6.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("6");
            }
        });
        btn7.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("7");
            }
        });
        btn8.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("8");
            }
        });
        btn9.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("9");
            }
        });
        btn0.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("0");
            }
        });
        btnback.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if ( pintext.length() > 0 ) {
                    pintext.getEditableText().delete(pintext.length() - 1, pintext.length());
                }
            }
        });

        pintext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ( pintext.length() > 3 ) {
                    String pinString = pintext.getText().toString();
                    // re-enter pin
                    if ( pin == null ) {
                        pin = pinString;
                        pintext.setText("");
                        message.setText("Re-enter your PIN");
                        message.setTextColor(Color.GRAY);
                    }
                    // pin has been entered once before
                    else {
                        // pin has been re-entered correctly
                        if ( pinString.equals(pin) ) {
                            application.setPin(Integer.parseInt(pinString));
                            application.setPinState(true);
                            finish();
                        }
                        // pin has been re-entered incorrectly
                        else {
                            pin = null;
                            pintext.setText("");
                            message.setText("Your PIN does not match. Please retry");
                            message.setTextColor(Color.RED);
                        }

                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds rssItemList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_with_search, menu);
        //create search interface
        SearchableCreator.makeSearchable(this, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
