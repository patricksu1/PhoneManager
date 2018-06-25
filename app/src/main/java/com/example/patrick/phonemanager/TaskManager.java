package com.example.patrick.phonemanager;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by patrick on 4/5/2017.
 */

public class TaskManager extends AppCompatActivity {
    private TextView memoryava,processnum;
    private ActivityManager actManager;
    private ArrayAdapter <String>adapter;
    private ListView listView;
    private ArrayList<String> processName;
    private int [] pId;
    private ArrayList<Integer> pid;
    private double totalmemory,memoryavailable;
    private ActivityManager.MemoryInfo memoryInfo;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        memoryInfo=new ActivityManager.MemoryInfo();
        processName=new ArrayList<>();
        pId=new int[100];
        pid=new ArrayList<>();
        listView=(ListView)findViewById(R.id.android_list);
        setDisplay();
        setTextView();
    }
    public void setTextView(){
        memoryava=(TextView)findViewById(R.id.available_mem_label);
        processnum=(TextView)findViewById(R.id.num_processes_label);
        actManager.getMemoryInfo(memoryInfo);
        totalmemory=memoryInfo.totalMem/1024/1024;
        memoryavailable=memoryInfo.availMem/1024/1024;
        memoryava.setText(String.format("Total memory:\t%.2f Mb\nAvailable Memory:\t%.2f Mb",totalmemory,memoryavailable));
        processnum.setText(String.format("Number of Process:\t%d",processName.size()));
    }
    public void setDisplay(){
        actManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for(int i=0;i<actManager.getRunningAppProcesses().size();i++){
            processName.add(i,actManager.getRunningAppProcesses().get(i).processName);
            pid.add(i,actManager.getRunningAppProcesses().get(i).pid);
            pId[i]=actManager.getRunningAppProcesses().get(i).pid;
        }
        adapter=new ArrayAdapter<>
                (this,android.R.layout.simple_list_item_single_choice,processName);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new processListener());
    }
    private class processListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(TaskManager.this);
            CharSequence[] options = {"Details", "Kill"};
            builder.setTitle("Process options");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int choice) {
                    switch (choice) {
                        case 0:
                            Toast.makeText(TaskManager.this, processName.get(position),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Process.killProcess(pId[position]);
                            setDisplay();
                            break;
                    }
                }
            });
            dialog = builder.create();
            dialog.show();

        }
    }
}
