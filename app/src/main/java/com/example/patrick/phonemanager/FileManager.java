package com.example.patrick.phonemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;


/**
 * Created by patrick on 4/5/2017.
 */

public class FileManager extends AppCompatActivity {
    private ArrayList<String> mStringArray;
    private ListView listView;
    private fileOperation fo;
    private ArrayAdapter<String> adapter;
    private boolean isSelect=false;
    private ArrayList<Boolean> isCheck=new ArrayList<>();
    private LinearLayout hidden;
    private Stack<String> selectStack=new Stack<>();
    private int selectCount=0;
    private boolean copied=false;
    private boolean move=false;
    private boolean bySearch=false;
    private int [] buttonID={R.id.button_back,R.id.button_home,R.id.button_select,R.id.button_property,R.id.button_new,R.id.button_search};
    private int [] hiddenButtonID={R.id.hidden_copy,R.id.hidden_move,R.id.hidden_delete,R.id.hidden_paste};
    private Button []button=new Button[buttonID.length];
    private Button [] hiddenButton=new Button[hiddenButtonID.length];
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        listView=(ListView) findViewById(R.id.file_list);
        fo=new fileOperation();
        mStringArray=fo.getHome();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,mStringArray);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        createButton();
        for(int i=0;i<500;i++){
            isCheck.add(false);
        }
        setPathLabelView();
        setStorageLabelView();
        setDetailLabelView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName=parent.getItemAtPosition(position).toString();
                String path = fo.getCurrentDir() + File.separator + fileName;
                File file=new File(path);
                int count=0;
                if(isSelect){

                    selectCount=0;
                    if(!copied&&!move) {
                        if(!selectStack.empty()) {
                            for(int i=0;i<selectStack.size();i++) {
                                if (selectStack.get(i).equals(path)) {
                                    count++;
                                    Log.d("count",count+"");
                                }
                            }
                            if(count==0){
                                selectStack.push(path);
                                selectCount++;
                                isCheck.set(position, true);
                            }
                            else{
                                selectStack.remove(path);
                                selectCount--;
                                isCheck.set(position, false);
                            }
                        }
                        else{
                            selectStack.push(path);
                            selectCount++;
                            isCheck.set(position, true);
                        }
                    }
                    else{
                        selectStack.clear();
                        copied=false;
                        move=false;
                        selectStack.push(path);
                        selectCount++;
                        isCheck.set(position, true);
                    }
                }
                else {


                    if(file.isFile()){
                        String extension=fileName.substring(fileName.lastIndexOf('.'),fileName.length());

                        if(extension.equalsIgnoreCase(".pdf")){

                            Intent intent = new Intent();
                            intent.setAction(android.content.Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(file),
                                    "application/pdf");
                            Intent chooser= Intent.createChooser(intent,"Open File");
                            try {listView.clearChoices();
                                startActivity(chooser);
                                mStringArray=fo.getCurrentDirFile();
                                adapter=new ArrayAdapter<String>(FileManager.this,android.R.layout.simple_list_item_multiple_choice,mStringArray);
                                listView.setAdapter(adapter);
                            } catch (Exception e) {
                                Toast.makeText(FileManager.this, "Sorry, couldn't find a pdf viewer",
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                        else if(extension.equalsIgnoreCase(".html")){

                            Intent intent = new Intent();
                            intent.setAction(android.content.Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(file),
                                    "application/html");
                            Intent chooser= Intent.createChooser(intent,"Open File");
                            try {
                                listView.clearChoices();
                                startActivity(chooser);
                                mStringArray=fo.getCurrentDirFile();
                                adapter=new ArrayAdapter<String>(FileManager.this,android.R.layout.simple_list_item_multiple_choice,mStringArray);
                                listView.setAdapter(adapter);
                            } catch (Exception e) {

                                Toast.makeText(FileManager.this, "Sorry, couldn't find a html viewer",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                        else if(extension.equalsIgnoreCase(".dox")
                                ||extension.equalsIgnoreCase(".docx")){
                            listView.clearChoices();
                            Intent intent = new Intent();
                            intent.setAction(android.content.Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(file),
                                    "application/docx");
                            Intent chooser= Intent.createChooser(intent,"Open File");
                            try {
                                listView.clearChoices();
                                startActivity(chooser);
                                mStringArray=fo.getCurrentDirFile();
                                adapter=new ArrayAdapter<String>(FileManager.this,android.R.layout.simple_list_item_multiple_choice,mStringArray);
                                listView.setAdapter(adapter);
                            } catch (Exception e) {

                                Toast.makeText(FileManager.this, "Sorry, couldn't find a docx viewer",
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                        else if(extension.equalsIgnoreCase(".jpg")
                                ||extension.equalsIgnoreCase(".jpeg")){
                            listView.clearChoices();
                            Intent intent = new Intent();
                            intent.setAction(android.content.Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(file),
                                    "application/docx");
                            Intent chooser= Intent.createChooser(intent,"Open File");
                            try {
                                listView.clearChoices();
                                startActivity(chooser);
                                mStringArray=fo.getCurrentDirFile();
                                adapter=new ArrayAdapter<String>(FileManager.this,android.R.layout.simple_list_item_multiple_choice,mStringArray);
                                listView.setAdapter(adapter);
                            } catch (Exception e) {

                                Toast.makeText(FileManager.this, "Sorry, couldn't find a jpg viewer",
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                        else{
                            String type=extension.substring(extension.lastIndexOf(".")+1,extension.length());
                            listView.clearChoices();
                            Intent intent = new Intent();
                            intent.setAction(android.content.Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(file),
                                    String.format("application/%s",type));
                            Intent chooser= Intent.createChooser(intent,"Open File");
                            try {
                                listView.clearChoices();
                                startActivity(chooser);
                                mStringArray=fo.getCurrentDirFile();
                                adapter=new ArrayAdapter<String>(FileManager.this,android.R.layout.simple_list_item_multiple_choice,mStringArray);
                                listView.setAdapter(adapter);
                            } catch (Exception e) {

                                Toast.makeText(FileManager.this, String.format("Sorry, couldn't find a %s viewer",type),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    if(file.isDirectory()) {
                        mStringArray = fo.getNextDir(path, true);
                        adapter = new ArrayAdapter<String>(FileManager.this, android.R.layout.simple_list_item_multiple_choice, mStringArray);
                        listView.setAdapter(adapter);
                        setPathLabelView();
                        setStorageLabelView();
                        setDetailLabelView();
                    }
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                final boolean before = isSelect;
                isSelect=true;
                LayoutInflater li = LayoutInflater.from(FileManager.this);
                View promptsView = li.inflate(R.layout.rename_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        FileManager.this);
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    String newName = "";
                                    public void onClick(DialogInterface dialog, int id) {
                                        newName = "" + userInput.getText();
                                        boolean repeat=false;
                                        newName = "" + userInput.getText();
                                        for(int i=0;i<mStringArray.size();i++){
                                            if (newName.equalsIgnoreCase(mStringArray.get(i))){
                                                repeat=true;
                                            }
                                        }
                                        if(!selectStack.empty()){
                                            selectStack.pop();
                                        }
                                        if(repeat){
                                            Toast.makeText(FileManager.this,"Name already exists, please use another name", Toast.LENGTH_SHORT).show();
                                            listView.clearChoices();
                                            mStringArray = fo.getCurrentDirFile();
                                            adapter = new ArrayAdapter<String>(FileManager.this, android.R.layout.simple_list_item_multiple_choice, mStringArray);
                                            listView.setAdapter(adapter);
                                            setDetailLabelView();
                                        }else {
                                            String path=fo.getCurrentDir()+File.separator+parent.getItemAtPosition(position);
                                            fo.rename(path,newName);
                                            //hidden.setVisibility(LinearLayout.GONE);
                                            if(!before) {
                                                isSelect = false;
                                            }
                                            listView.clearChoices();
                                            mStringArray = fo.getCurrentDirFile();
                                            adapter = new ArrayAdapter<String>(FileManager.this, android.R.layout.simple_list_item_multiple_choice, mStringArray);
                                            listView.setAdapter(adapter);
                                            setDetailLabelView();
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if(!selectStack.empty()){
                                            selectStack.pop();
                                        }
                                        if(!before) {
                                            isSelect = false;
                                        }
                                        listView.clearChoices();
                                        mStringArray = fo.getCurrentDirFile();
                                        adapter = new ArrayAdapter<String>(FileManager.this, android.R.layout.simple_list_item_multiple_choice, mStringArray);
                                        listView.setAdapter(adapter);
                                        setDetailLabelView();
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return false;
            }




        });
    }
    public void createButton(){

        for(int i=0;i<button.length;i++){
            button[i]= (Button) findViewById(buttonID[i]);
            button[i].setOnClickListener(new OnClickListener());
        }
        for(int i=0;i<hiddenButton.length;i++){
            hiddenButton[i]=(Button)findViewById(hiddenButtonID[i]);
            hiddenButton[i].setOnClickListener(new OnHiddenClickListener());
        }
    }

    private class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_back:
                    if(isSelect){
                        isSelect=false;
                        hidden.setVisibility(View.GONE);
                        listView.clearChoices();
                        if(!copied&&!move){
                            selectStack.clear();
                        }
                    }

                    if(bySearch){
                        mStringArray=fo.getCurrentDirFile();
                        bySearch=false;
                    }else {
                        String curr1=fo.getCurrentDir();
                        mStringArray = fo.getPreviousDir();
                        String curr2=fo.getCurrentDir();
                        if(curr1.equals(curr2)){
                            Toast.makeText(FileManager.this,"Current directory is home directory", Toast.LENGTH_SHORT).show();
                        }
                    }


                    for(int i=0;i<button.length;i++) {
                        button[i].setClickable(true);
                        button[i].setVisibility(View.VISIBLE);
                    }

                    adapter=new ArrayAdapter<String>(FileManager.this,android.R.layout.simple_list_item_multiple_choice,mStringArray);
                    listView.setAdapter(adapter);
                    setPathLabelView();
                    break;
                case R.id.button_home:
                    if(isSelect) {
                        isSelect = false;
                        hidden.setVisibility(View.GONE);
                        listView.clearChoices();
                        if(!copied&&!move){
                            selectStack.clear();
                        }
                    }
                    for(int i=0;i<button.length;i++) {
                        button[i].setClickable(true);
                        button[i].setVisibility(View.VISIBLE);
                    }

                    String curr1=fo.getCurrentDir();
                    mStringArray=fo.getHome();
                    String curr2=fo.getCurrentDir();
                    if(curr1.equals(curr2)){
                        Toast.makeText(FileManager.this,"Current directory is home directory", Toast.LENGTH_SHORT).show();
                    }
                    adapter=new ArrayAdapter<String>(FileManager.this,android.R.layout.simple_list_item_multiple_choice,mStringArray);
                    listView.setAdapter(adapter);
                    setPathLabelView();
                    break;
                case R.id.button_select:
                    hidden=(LinearLayout)FileManager.this.findViewById(R.id.hidden_buttons);
                    setDetailLabelView();
                    for(int i=0;i<hiddenButton.length;i++) {
                        if (hiddenButtonID[i] == R.id.hidden_paste) {
                            if (selectStack.empty()) {
                                hiddenButton[i].setVisibility(View.INVISIBLE);
                            }
                            else {
                                hiddenButton[i].setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    if (!isSelect) {
                        listView.setLongClickable(false);
                        hidden.setVisibility(LinearLayout.VISIBLE);
                        for(int i=0;i<button.length;i++){
                            if(buttonID[i]!=R.id.button_select){
                                button[i].setVisibility(View.INVISIBLE);
                            }
                        }
                        Toast.makeText(FileManager.this,"file operation mode",Toast.LENGTH_SHORT).show();
                        isSelect=true;
                    }
                    else{
                        listView.setLongClickable(true);
                        for(int i=0;i<button.length;i++){
                            if(buttonID[i]!=R.id.button_select){
                                button[i].setVisibility(View.VISIBLE);
                            }
                        }
                        hidden.setVisibility(LinearLayout.GONE);
                        Toast.makeText(FileManager.this,"file explorer mode",Toast.LENGTH_SHORT).show();
                        isSelect=false;
                        listView.clearChoices();
                        if(!copied&&!move){
                            selectStack.clear();
                        }
                    }
                    setDetailLabelView();
                    break;
                case R.id.button_property:
                    if(isSelect) {
                        isSelect = false;
                        hidden.setVisibility(View.GONE);
                        listView.clearChoices();
                        if(!copied&&!move){
                            selectStack.clear();
                        }
                    }
                    Intent in = new Intent(FileManager.this,Property.class);
                    in.putExtra("PATH_NAME",fo.getCurrentDir());
                    FileManager.this.startActivity(in);
                    break;
                case R.id.button_new:
                    newFolder();
                    break;
                case R.id.button_search:
                    search();
                    break;
            }
        }
        public void newFolder(){
            LayoutInflater li = LayoutInflater.from(FileManager.this);
            View promptsView = li.inflate(R.layout.rename_dialog, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    FileManager.this);
            alertDialogBuilder.setView(promptsView);
            final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                String newName = "";
                                public void onClick(DialogInterface dialog, int id) {
                                    boolean repeat=false;
                                    newName = "" + userInput.getText();
                                    for(int i=0;i<mStringArray.size();i++){
                                        if (newName.equalsIgnoreCase(mStringArray.get(i))){
                                            repeat=true;
                                        }
                                    }
                                    if(repeat){
                                        Toast.makeText(FileManager.this,"Name already exists, please use another name", Toast.LENGTH_SHORT).show();

                                    }else {
                                        int i=fo.createDir(fo.getCurrentDir(), newName);
                                        if(i==0){
                                            Toast.makeText(FileManager.this,"new folder created successful",Toast.LENGTH_SHORT);
                                        }
                                        else{
                                            Toast.makeText(FileManager.this,"new folder created failed",Toast.LENGTH_SHORT);
                                        }
                                        mStringArray = fo.getCurrentDirFile();
                                        adapter = new ArrayAdapter<String>(FileManager.this, android.R.layout.simple_list_item_multiple_choice, mStringArray);
                                        listView.setAdapter(adapter);
                                    }
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    isSelect=false;
                                    mStringArray=fo.getCurrentDirFile();
                                    adapter=new ArrayAdapter<String>(FileManager.this,android.R.layout.simple_list_item_multiple_choice,mStringArray);
                                    listView.setAdapter(adapter);
                                    setDetailLabelView();
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        public void search(){
            LayoutInflater li = LayoutInflater.from(FileManager.this);
            View promptsView = li.inflate(R.layout.rename_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    FileManager.this);
            alertDialogBuilder.setView(promptsView);
            final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                String newName = "";
                                public void onClick(DialogInterface dialog, int id) {
                                    newName = "" + userInput.getText();
                                    bySearch=true;
                                    mStringArray=fo.searchInDirectory(fo.getCurrentDir(),newName);
                                    if(mStringArray.size()==0){
                                        mStringArray.add("No such result");
                                    }
                                    adapter=new ArrayAdapter<String>(FileManager.this,android.R.layout.simple_list_item_1,mStringArray);
                                    listView.setAdapter(adapter);
                                    for(int i=2;i<button.length;i++) {
                                        button[i].setClickable(false);
                                        button[i].setVisibility(View.INVISIBLE);
                                    }
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mStringArray=fo.getCurrentDirFile();
                                    adapter=new ArrayAdapter<String>(FileManager.this,android.R.layout.simple_list_item_multiple_choice,mStringArray);
                                    listView.setAdapter(adapter);
                                    setDetailLabelView();
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private class OnHiddenClickListener implements View.OnClickListener {
        public void setVisiable(){
            for(int i=0;i<button.length;i++){
                button[i].setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onClick(View v) {
            Log.d("size before act",""+selectStack.size());
            switch (v.getId()){
                case R.id.hidden_move:
                    move();
                    break;
                case R.id.hidden_copy:
                    copy();
                    break;
                case R.id.hidden_paste:
                    paste();
                    break;
                case R.id.hidden_delete:
                    delete();
                    break;
            }
            Log.d("size after act",""+selectStack.size());
        }
        public void copy(){
            if(selectStack.empty()){
                AlertDialog.Builder error=new AlertDialog.Builder(FileManager.this);
                error.setMessage("Please select a file or folder to copy");
                error.show();
            }else {
                setDetailLabelView();
                hidden.setVisibility(LinearLayout.GONE);
                isSelect = false;
                listView.clearChoices();
                copied = true;
                setVisiable();
            }
        }
        public void move(){
            if(selectStack.empty()){
                AlertDialog.Builder error=new AlertDialog.Builder(FileManager.this);
                error.setMessage("Please select a file or folder to move");
                error.show();
            }else {
                copy();
                move = true;
                setVisiable();
            }
        }
        public void paste(){
            Stack<String>temp=new Stack<>();
            for(int i=0;i<selectStack.size();i++){
                temp.push(selectStack.get(i));
            }

            while(!temp.empty()){
                    fo.copy(temp.pop(),fo.getCurrentDir());
                Log.d("before delete",selectStack.size()+"");
            }
            if(move==true){

                delete();
            }
            else{
                selectStack.clear();
            }
            move=false;
            hidden.setVisibility(LinearLayout.GONE);
            isSelect=false;
            listView.clearChoices();
            copied=false;
            setVisiable();
            mStringArray=fo.getCurrentDirFile();
            adapter=new ArrayAdapter<String>(FileManager.this,android.R.layout.simple_list_item_multiple_choice,mStringArray);
            listView.setAdapter(adapter);
            setDetailLabelView();
        }
        public void delete(){
            if(selectStack.empty()){
                AlertDialog.Builder error=new AlertDialog.Builder(FileManager.this);
                error.setMessage("Please select a file or folder to delete");
                error.show();
            }
            else {
                while (!selectStack.empty()) {
                    Log.d("delete", selectStack.peek());
                    fo.delete(selectStack.pop());
                    setVisiable();
                }
                hidden.setVisibility(LinearLayout.GONE);
                isSelect = false;
                listView.clearChoices();
                mStringArray = fo.getCurrentDirFile();
                adapter = new ArrayAdapter<String>(FileManager.this, android.R.layout.simple_list_item_multiple_choice, mStringArray);
                listView.setAdapter(adapter);
                setDetailLabelView();
            }
        }
    }
    public void setPathLabelView(){
        TextView pathIs=(TextView)findViewById(R.id.path_label);
        pathIs.setText("path is: "+fo.getCurrentDir());
    }
    public void setDetailLabelView(){
        TextView detail_label=(TextView)findViewById(R.id.detail_label);
        detail_label.setText("Holding "+selectStack.size()+" file(s)");
    }
    public void setStorageLabelView(){
        TextView storage=(TextView)findViewById(R.id.storage_label);
        StatFs fs=new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long total=fs.getBlockCount()*fs.getBlockSize()/1024;
        long free=fs.getAvailableBlocks()*fs.getBlockSize()/1024;
        storage.setText(String.format("sdcard: Total %.2f GB " +
                        "\t\tAvailable %.2f GB",
                (double)total / (1024 * 1024), (double)free / (1024 * 1024)));
    }
}
