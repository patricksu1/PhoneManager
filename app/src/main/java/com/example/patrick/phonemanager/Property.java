package com.example.patrick.phonemanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.File;
import java.util.Date;

/**
 * Created by patri on 4/19/2017.
 */

public class Property extends AppCompatActivity {
    private static final int KB = 1024;
    private static final int MG = KB * KB;
    private static final int GB = MG * KB;
    private String pathName;
    private TextView nameLabel, pathLabel, dirLabel,
            fileLabel, timeLabel, totalLabel;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        Intent i = getIntent();
        if(i != null) {
            if(i.getAction() != null && i.getAction().equals(Intent.ACTION_VIEW)) {
                pathName = i.getData().getPath();

                if(pathName == null)
                    pathName = "";
            }
            else {
                pathName = i.getExtras().getString("PATH_NAME");
            }
        }
        nameLabel = (TextView)findViewById(R.id.name_label);
        pathLabel = (TextView)findViewById(R.id.path_label);
        dirLabel = (TextView)findViewById(R.id.dirs_label);
        fileLabel = (TextView)findViewById(R.id.files_label);
        timeLabel = (TextView)findViewById(R.id.time_stamp);
        totalLabel = (TextView)findViewById(R.id.total_size);
        Button back = (Button)findViewById(R.id.back_button);
        back.setOnClickListener(new ButtonHandler());

        new BackgroundWork().execute(pathName);
    }
    private class BackgroundWork extends AsyncTask<String, Void, Long> {
        private ProgressDialog dialog;
        private String displaySize;
        private int fileCount = 0;
        private int dirCount = 0;

        protected void onPreExecute(){
            dialog = ProgressDialog.show(Property.this, "", "Calculating information...", true, true);
        }

        protected Long doInBackground(String... vals) {
            fileOperation fo = new fileOperation();
            File dir = new File(vals[0]);
            long size = 0;
            int len = 0;

            File[] list = dir.listFiles();
            if(list != null)
                len = list.length;

            for (int i = 0; i < len; i++){
                if(list[i].isFile())
                    fileCount++;
                else if(list[i].isDirectory())
                    dirCount++;
            }

            if(vals[0].equals("/")) {
                StatFs fss = new StatFs(Environment.getRootDirectory().getPath());
                size = fss.getAvailableBlocks() * (fss.getBlockSize() / KB);

                displaySize = (size > GB) ?
                        String.format("%.2f Gb ", (double)size / MG) :
                        String.format("%.2f Mb ", (double)size / KB);

            }else if(vals[0].equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                StatFs fs = new StatFs(Environment.getExternalStorageDirectory()
                        .getPath());
                size = fs.getBlockCount() * (fs.getBlockSize() / KB);

                displaySize = (size > GB) ?
                        String.format("%.2f Gb ", (double)size / GB) :
                        String.format("%.2f Gb ", (double)size / MG);

            } else {
                size = fo.getDirSize(vals[0]);

                if (size > GB)
                    displaySize = String.format("%.2f Gb ", (double)size / GB);
                else if (size < GB && size > MG)
                    displaySize = String.format("%.2f Mb ", (double)size / MG);
                else if (size < MG && size > KB)
                    displaySize = String.format("%.2f Kb ", (double)size/ KB);
                else
                    displaySize = String.format("%.2f bytes ", (double)size);
            }

            return size;
        }

        protected void onPostExecute(Long result) {
            File dir = new File(pathName);

            nameLabel.setText(dir.getName());
            pathLabel.setText(dir.getAbsolutePath());
            dirLabel.setText(dirCount + " folders ");
            fileLabel.setText(fileCount + " files ");
            totalLabel.setText(displaySize);
            timeLabel.setText(new Date(dir.lastModified()) + " ");
            dialog.cancel();
        }
    }

    private class ButtonHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.back_button)
                finish();
        }
    }
}
