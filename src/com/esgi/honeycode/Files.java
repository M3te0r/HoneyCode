package com.esgi.honeycode;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Mathieu on 16/03/14.
 * This Class create an ArrayList of files object
 * Useful to project, can be used for a Hashmap of project
 *
 */
public class Files {

    private ArrayList<File> filesArray;

    public Files() {
        this.filesArray = new ArrayList<>();
    }

    public ArrayList<File> getFilesArray() {
        return filesArray;
    }

    public void setFilesArray(ArrayList<File> filesArray) {
        this.filesArray = filesArray;
    }
}
