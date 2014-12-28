package com.techhab.eventdialogbuilder;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by jhchoe on 12/27/14.
 */
public class EventDialogBuilder extends MaterialDialog.Builder {

    private Context context;
    private View layout;

    private MaterialDialog dialog;

    private TextView title;
    private TextView content;
    private ImageView close;

    private ImageButton campus;
    private ImageButton map;
    private ImageButton calendar;
    private ImageButton check;

    private ProgressBar progressbar;

    public EventDialogBuilder(Context context) {
        super(context);
        this.context = context;
    }

    public void onCreate() {
        dialog = build();
    }

    public void setCustomView(View layout) {
        this.layout = layout;
        customView(layout);

        title = (TextView) layout.findViewById(R.id.title);
        content = (TextView) layout.findViewById(R.id.content);
        close = (ImageView) layout.findViewById(R.id.close);

        campus = (ImageButton) layout.findViewById(R.id.campus_button);
        map = (ImageButton) layout.findViewById(R.id.mapit_button);
        calendar = (ImageButton) layout.findViewById(R.id.calendar_button);
        check = (ImageButton) layout.findViewById(R.id.attending_button);
    }

    private void setOnClickListener(String building, String location, String date) {
        final String b = building;
        final String l = location;
        final String d = date;

        campus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, b, Toast.LENGTH_SHORT).show();
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, l, Toast.LENGTH_SHORT).show();
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, d, Toast.LENGTH_SHORT).show();
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Attending", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDialog(String event, String link) {
        if (dialog == null) {
            onCreate();
        }
        if (dialog.getCustomView() == null) {
            customView(R.layout.event_dialog_custom);
            setCustomView(dialog.getCustomView());
        }

        progressbar = (ProgressBar) layout.findViewById(R.id.progress_bar);
        progressbar.setMax(100);
        progressbar.setProgress(0);

        title.setText(event);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        new MyAsyncTask().execute(link);
        dialog.show();
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String...link) {
            publishProgress(10);
            try {
                StringBuffer buffer = new StringBuffer();
                Document doc = Jsoup.connect(link[0]).get();
                int progressInt = 50;
                publishProgress(progressInt);
                Elements elems = doc.select("p");

                String building = "";
                String location = "";
                String date = "";
                for (Element elem : elems) {
                    String className = elem.className();
                    if (className.equals("date")) {
                        date = elem.text().split(": ")[1];
                        buffer.append("\n" + elem.text() + "\n");
                    } else if (className.equals("time")) {
                        date += ", " + elem.text().split(": ")[1];
                        buffer.append("\n" + elem.text() + "\n");
                    } else if (className.equals("duration") || className.equals("sponsor")) {
                        buffer.append("\n" + elem.text() + "\n");
                    } else if (className.equals("location")) {
                        building = elem.text().split(": ")[1];
                        location = building;
                        buffer.append("\n" + elem.text() + "\n");
                    }
                    if (progressInt < 95) {
                        progressInt += 10;
                        publishProgress(progressInt);
                    }
                }
                setOnClickListener(building, location, date);
                publishProgress(98);
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Error";
        }

        @Override
        protected void onProgressUpdate(Integer...progress) {
            progressbar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            content.setText(result);
            progressbar.setVisibility(ProgressBar.GONE);
        }
    }
}
