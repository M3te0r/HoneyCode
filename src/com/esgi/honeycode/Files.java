package com.esgi.honeycode;

import com.sun.istack.internal.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by Mathieu on 16/03/14.
 * This Class create an HashSet of files object
 * Useful to project, can be used for a Hashmap of project
 *
 */
public class Files implements Serializable{

    private static final long serialVersionUID = 1L;
    private transient File projectPath;
    private HashSet<File> filesSet;

    public Files(@NotNull File projectPath) {
        /**
         * Constructeur
         */
        this.projectPath = projectPath;
        this.filesSet = new HashSet<>();
    }

    public void createProjectStructure()
    {
        /**
         *  Crée la structure du projet, arborescence etc à partir des fichiers
         */
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
        created = (new File(this.projectPath.getAbsolutePath()+PropertiesShared.SEPARATOR+".honeycode").mkdir());
        if (!created && !projectPath.exists())
        {
            JOptionPane.showMessageDialog(null, "Impossible de créer le répertoire de .honeycode du projet : " + projectPath);
        }
        created = (new File(this.projectPath.getAbsolutePath()+PropertiesShared.SEPARATOR+"out").mkdir());
        if (!created && !projectPath.exists())
        {
            JOptionPane.showMessageDialog(null, "Impossible de créer le répertoire out du projet : " + projectPath);
        }
    }

    public File getProjectPath() {
        /**
         * Retourne le chemin absolu du projet
         * @return File
         */
        return projectPath;
    }


    public HashSet<File> getFilesArray() {
        /**
         * Retourne une table des fichiers
         * @return HashSet
         */
        return filesSet;
    }


    public void addFile(@NotNull File file)
    {
        /**
         * Ajoute un fichier à la table de fichiers
         * @param file Fichier à ajouter
         */
        this.filesSet.add(file);
    }

    public void removeFile(@NotNull File file)
    {
        /**
         * Retire le fichier spécifié de la table de fichiers
         * @param file Fichier à retirer
         */
        this.filesSet.remove(file);
    }

    public void clearSet()
    {
        /**
         * Nettoie le tableau de fichiers de toutes ses entrées
         */
        this.filesSet.clear();
    }
}
