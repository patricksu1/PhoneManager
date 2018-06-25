package com.example.patrick.phonemanager;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Created by patri on 4/12/2017.
 */

public class fileOperation {
    private Stack<String> dir;
    private ArrayList<String> dirContent;
    private String sdcard;
    private String home;
    private int dirSize;
    private final int BUFFER=2048;
    public fileOperation(){
        dir=new Stack<>();
        dirContent=new ArrayList<>();
        sdcard=Environment.getExternalStorageDirectory().getAbsolutePath();
        home=sdcard;
        dir.push(sdcard);
    }

    private ArrayList<String> display(){
        dirContent.clear();
        File file=new File(dir.peek());
        for(int i=0;i<file.list().length;i++){
            dirContent.add(file.list()[i]);
        }
        Collections.sort(dirContent);

        return dirContent;
    }
    public ArrayList<String> getHome(){
        dir.clear();
        dir.push(home);
        return display();
    }
    public ArrayList<String> setHome(String home){
        dir.clear();
        dir.push(File.separator+home);
        this.home=dir.peek();
        return display();
    }

    public String getCurrentDir() {
        return dir.peek();
    }
    public ArrayList<String> getCurrentDirFile(){
        return display();
    }
    public ArrayList<String> getPreviousDir() {
        int size = dir.size();

        if (size >= 2) {
            dir.pop();
        }
        return display();
    }
    public ArrayList<String> getNextDir(String path, boolean isFullPath) {
        int size = dir.size();

        if(!path.equals(dir.peek()) && !isFullPath) {
            if(size == 1)
                dir.push("/" + path);
            else
                dir.push(dir.peek() + "/" + path);
        }

        else if(!path.equals(dir.peek()) && isFullPath) {
            dir.push(path);
        }

        return display();
    }
    public int createDir(String path, String name) {
        int len = path.length();

        if(len < 1)
            return -1;

        if(path.charAt(len - 1) != '/')
            path += "/";

        if (new File(path+name).mkdir())
            return 0;

        return -1;
    }
    public int copy(String oldPath, String newDir) {
        File old_file = new File(oldPath);
        File new_dir = new File(newDir);
        byte[] data = new byte[BUFFER];
        int read = 0;

        if(old_file.isFile() && new_dir.isDirectory() && new_dir.canWrite()){
            String file_name = oldPath.substring(oldPath.lastIndexOf("/"), oldPath.length());
            File cp_file = new File(newDir + file_name);

            try {
                BufferedOutputStream outputStream = new BufferedOutputStream(
                        new FileOutputStream(cp_file));
                BufferedInputStream inputstream = new BufferedInputStream(
                        new FileInputStream(old_file));

                while((read = inputstream.read(data, 0, BUFFER)) != -1)
                    outputStream.write(data, 0, read);

                outputStream.flush();
                inputstream.close();
                outputStream.close();

            } catch (FileNotFoundException e) {
                Log.e("FileNotFoundException", e.getMessage());
                return -1;

            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
                return -1;
            }

        }else if(old_file.isDirectory() && new_dir.isDirectory() && new_dir.canWrite()) {
            String files[] = old_file.list();
            String dir = newDir + oldPath.substring(oldPath.lastIndexOf("/"), oldPath.length());
            int len = files.length;

            if(!new File(dir).mkdir())
                return -1;

            for(int i = 0; i < len; i++)
                copy(oldPath + "/" + files[i], dir);

        } else if(!new_dir.canWrite())
            return -1;

        return 0;
    }
    public int rename(String filePath, String newName) {
        File file = new File(filePath);
        String extension = "";
        File destination;

        if(file.isFile())
            extension = filePath.substring(filePath.lastIndexOf("."), filePath.length());
        if(newName.length() < 1)
            return -1;
        String temp = filePath.substring(0, filePath.lastIndexOf("/"));

        destination = new File(temp + "/" + newName + extension);
        if(file.renameTo(destination))
            return 0;
        else
            return -1;
    }
    public int delete(String path) {
        File file = new File(path);

        if(file.exists() && file.isFile() && file.canWrite()) {
            file.delete();
            return 0;
        }
        else if(file.exists() && file.isDirectory() && file.canRead()) {
            String[] file_list = file.list();

            if(file_list != null && file_list.length == 0) {
                file.delete();
                return 0;

            } else if(file_list != null && file_list.length > 0) {

                for(int i = 0; i < file_list.length; i++) {
                    File temp_f = new File(file.getAbsolutePath() + "/" + file_list[i]);

                    if(temp_f.isDirectory())
                        delete(temp_f.getAbsolutePath());
                    else if(temp_f.isFile())
                        temp_f.delete();
                }
            }
            if(file.exists())
                if(file.delete())
                    return 0;
        }
        return -1;
    }
    public ArrayList<String> searchInDirectory(String dir, String fileName) {
        ArrayList<String> names = new ArrayList<String>();
        search_file(dir, fileName, names);

        return names;
    }
    public long getDirSize(String path) {
        get_dir_size(new File(path));

        return dirSize;
    }
    private void search_file(String dir, String fileName, ArrayList<String> n) {
        File root_dir = new File(dir);
        String[] list = root_dir.list();
        if(list != null && root_dir.canRead()) {
            int len = list.length;
            for (int i = 0; i < len; i++) {
                File check = new File(dir + "/" + list[i]);
                String name = check.getName();

                if(check.isFile() && name.toLowerCase().
                        contains(fileName.toLowerCase())) {
                    n.add(check.getPath());
                }
                else if(check.isDirectory()) {
                    if(name.toLowerCase().contains(fileName.toLowerCase()))
                        n.add(check.getPath());

                    else if(check.canRead() && !dir.equals("/"))
                        search_file(check.getAbsolutePath(), fileName, n);
                }
            }
        }
    }
    private void get_dir_size(File path) {
        File[] list = path.listFiles();
        int len;

        if(list != null) {
            len = list.length;

            for (int i = 0; i < len; i++) {

                    if(list[i].isFile() && list[i].canRead()) {
                        dirSize += list[i].length();

                    } else if(list[i].isDirectory() && list[i].canRead() ) {
                        get_dir_size(list[i]);
                    }
            }
        }
    }
}
