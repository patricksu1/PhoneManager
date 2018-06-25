package com.example.patrick.phonemanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class PhoneManager extends AppCompatActivity {
    private final int MY_PERMISSIONS_REQUEST=0;
    private final int MY_PERMISSIONS_REQUEST1=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_phone_manager);
        request();



        Button SysInfoButton = (Button) findViewById(R.id.systemInfoButton);
        SysInfoButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //SysInfo=new SystemInfo();

                Intent in = new Intent(PhoneManager.this,SystemInfo.class);
                startActivity(in);


            }
        });

        Button FileManagerButton = (Button) findViewById(R.id.fileManagerButton);
        FileManagerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent in =new Intent(PhoneManager.this,FileManager.class);
                startActivity(in);
            }
        });

        Button MemoryManagerButton = (Button) findViewById(R.id.taskManagerButton);
        MemoryManagerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent in =new Intent(PhoneManager.this,TaskManager.class);
                startActivity(in);
            }
        });
    }

    public void request(){
        int readCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeCheck=ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if((readCheck!= PackageManager.PERMISSION_GRANTED)||(writeCheck!=PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST);
        }

    }
    public void request1(){
        int killCheck=ContextCompat.checkSelfPermission(this,Manifest.permission.KILL_BACKGROUND_PROCESSES);
        if(killCheck!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.KILL_BACKGROUND_PROCESSES},MY_PERMISSIONS_REQUEST1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int [] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if ((grantResults.length > 0) && (grantResults[0]) == PackageManager.PERMISSION_GRANTED) {

                }
                else {
                    request();
                }
                break;
            case MY_PERMISSIONS_REQUEST1:
                if ((grantResults.length > 0) && (grantResults[0]) == PackageManager.PERMISSION_GRANTED) {

                }
                else {
                    request1();
                }
                break;
        }
    }

}
