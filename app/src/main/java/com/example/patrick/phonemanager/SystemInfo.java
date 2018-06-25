package com.example.patrick.phonemanager;


import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by patrick on 4/5/2017.
 */

public class SystemInfo extends AppCompatActivity {
    private LinearLayout mLayout;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sys_info);
        mLayout = (LinearLayout) findViewById(R.id.layout);
        setText("** Device:");
        setText( "Board: "+android.os.Build.BOARD);
        setText( "Brand: "+android.os.Build.BRAND);
        setText( "Device: "+android.os.Build.DEVICE);
        setText( "Model: "+android.os.Build.MODEL);
        setText("Hardware: "+ Build.HARDWARE);
        setText("Product: "+android.os.Build.PRODUCT);
        setText( "TAGS: "+android.os.Build.TAGS);
        setText("Manufacturer: "+Build.MANUFACTURER);
        setText( "** OS:");
        setText("API: "+ Build.VERSION.SDK);
        setText( "Build release: "+android.os.Build.VERSION.RELEASE + ", Inc: '"+android.os.Build.VERSION.INCREMENTAL+"'");
        setText( "Display build: "+android.os.Build.DISPLAY);
        setText( "Finger print: "+android.os.Build.FINGERPRINT);
        setText( "Build ID: "+android.os.Build.ID);
        setText( "Type: "+android.os.Build.TYPE);
        setText( "User: "+android.os.Build.USER);
        setText("Time: "+Build.TIME);
    }
    private void setText(String text)
    {
        TextView label = new TextView(this);
        label.setText(text);
        mLayout.addView(label);
    }
}
