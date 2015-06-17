package com.xy.lr.java.io;

import java.io.File;

/**
 * Created by xylr on 15-6-17.
 */
public class FileList {
    public static void filelist(String filePath){
        File file = new File(filePath);
        File[] files = file.listFiles();
        if(files != null){
            for(File f : files){
                if(f.isDirectory()){
                    filelist(f.getAbsolutePath());
                }else{
                    System.out.println(f.getAbsolutePath());
                }
            }
        }
    }
    public static void main(String[] args){
        filelist("data/");
    }
}
