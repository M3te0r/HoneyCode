package com.esgi.honeycode;

import com.sun.istack.internal.NotNull;

import java.io.File;
import java.util.HashSet;

/**
 * Created by Mathieu on 16/03/14.
 * This Class create an HashSet of files object
 * Useful to project, can be used for a Hashmap of project
 *
 */
public class Files {

    private HashSet<File> filesSet;

    public Files() {
        this.filesSet = new HashSet<>();
    }

    public HashSet<File> getFilesArray() {
        return filesSet;
    }

    public void setFilesArray(HashSet<File> filesArray) {
        this.filesSet = filesArray;
    }

    public void addFile(@NotNull File file)
    {
        this.filesSet.add(file);
    }

    public void removeFile(@NotNull File file)
    {
        this.filesSet.remove(file);
    }

    public void clearSet()
    {
        this.filesSet.clear();
    }
}
