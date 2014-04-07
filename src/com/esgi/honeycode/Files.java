package com.esgi.honeycode;

import com.sun.istack.internal.NotNull;

import javax.swing.*;
import java.io.File;
import java.util.HashSet;

/**
 * Created by Mathieu on 16/03/14.
 * This Class create an HashSet of files object
 * Useful to project, can be used for a Hashmap of project
 *
 */
public class Files {

    private File projectPath;
    private HashSet<File> filesSet;

    public Files(@NotNull File projectPath) {
        this.projectPath = projectPath;
        this.filesSet = new HashSet<>();
        createProjectStructure();
    }

    public void createProjectStructure()
    {
        boolean created = projectPath.mkdir();
        if (!created && !projectPath.exists())
        {
            JOptionPane.showMessageDialog(null, "Impossible de créer le répertoire de projet : " + projectPath);
        }
        created = (new File(this.projectPath.getAbsolutePath()+PropertiesShared.SEPARATOR+"src").mkdir());
        if (!created && !projectPath.exists())
        {
            JOptionPane.showMessageDialog(null, "Impossible de créer le répertoire de src du projet : " + projectPath);
        }
    }

    public File getProjectPath() {
        return projectPath;
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
