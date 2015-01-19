package com.techhab.collegeapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

public class PINSetupActivity extends ActionBarActivity {
    Thread mThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean wrapInScrollView = true;
        new MaterialDialog.Builder(this)
                .title("Enter Password")
                .customView(R.layout.dialog_password, wrapInScrollView)
                .positiveText("OK")
                .negativeText("Cancel")
                .callback( new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
//                        EditText passDialogText = (EditText) findViewById(R.id.password);
//                        Toast.makeText(getApplicationContext(), passDialogText.getText().toString()+"hooo", Toast.LENGTH_LONG).show();
                        setUp();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        finish();
                    }
                })
                .cancelable(false)
                .build()
                .show();

    }

    private void setUp() {
        setContentView(R.layout.activity_pin);
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
                if ( pintext.length() < 1 ) {
                    return;
                }
                else
                    pintext.getEditableText().delete(pintext.length() - 1, pintext.length());
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
                    finish();
                    Toast.makeText(getApplicationContext(), pintext.getText().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pin, menu);
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
