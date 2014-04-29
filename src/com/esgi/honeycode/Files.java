package com.esgi.honeycode;

import com.sun.istack.internal.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.Serializable;
import java.util.HashSet;

/**
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

    /**
     *  Crée la structure du projet, arborescence etc à partir des fichiers
     */
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

    /**
     * Retourne le chemin absolu du projet
     * @return File
     */
    public File getProjectPath() {
        return projectPath;
    }

    /**
     * Retourne une table des fichiers
     * @return HashSet
     */
    public HashSet<File> getFilesArray() {
        return filesSet;
    }

    /**
     * Ajoute un fichier à la table de fichiers
     * @param file Fichier à ajouter
     */
    public void addFile(@NotNull File file)
    {
        this.filesSet.add(file);
    }

    /**
     * Retire le fichier spécifié de la table de fichiers
     * @param file Fichier à retirer
     */
    public void removeFile(@NotNull File file)
    {
        this.filesSet.remove(file);
    }

    /**
     * Nettoie le tableau de fichiers de toutes ses entrées
     */
    public void clearSet()
    {
        this.filesSet.clear();
    }
}
