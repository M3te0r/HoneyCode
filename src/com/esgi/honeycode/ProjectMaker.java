package com.esgi.honeycode;

import com.sun.istack.internal.NotNull;

import javax.swing.*;
import java.io.*;

/**
 * Class handling project created by the user
 */
public class ProjectMaker implements Serializable{

    private static final long serialVersionUID = 1L;
    private String projectName;
    private String projectType;
    private File projectPath;
    private transient Files projectFiles;

    public ProjectMaker(@NotNull File projectDataFile)
    {
        doDeserialize(projectDataFile);
    }

    public ProjectMaker(@NotNull String projectName, String projectType) {
        this.projectName = projectName;
        this.projectType = projectType;
        this.projectPath = new File(new HCPreferences().getProjetPath()+PropertiesShared.SEPARATOR+this.projectName);
        this.projectFiles = new Files(projectPath);
    }

    /**
     * Retourne tous les fichiers du projet
     * @return Files
     */
    public Files getProjectFiles() {
        return projectFiles;
    }

    /**
     * Retourne le nom du projet
     * @return String
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Retourne le type du projet
     * @return String
     */
    public String getProjectType() {
        return projectType;
    }

    /**
     * Retourne le chemin absolu du projet
     * @return File
     */
    public File getProjectPath() {
        return projectPath;
    }

    /**
     * Ecris tous les fichiers du projet sur un seul sortant
     */
    public void serializeProjectFiles()
    {
        try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(this.projectPath.getAbsolutePath()+PropertiesShared.SEPARATOR+".honeycode"+PropertiesShared.SEPARATOR+"projectfiles")))))
        {
            for (File file : projectFiles.getFilesArray())
            {
                out.writeObject(file);
            }

        }catch (IOException e)
        {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error while saving your project", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crée un fichier reprenant toute la configuration du projet
     */
    public void serializeProjectSettings()
    {
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(this.projectPath.getAbsolutePath()+PropertiesShared.SEPARATOR+".honeycode"+PropertiesShared.SEPARATOR+"project.dat"))))){
            out.writeObject(this.projectName);
            out.writeObject(this.projectType);
            out.writeObject(this.projectPath);
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error while saving your project", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Rétablit une configuration  partir d'un fichier de projet
     */
    private void doDeserialize(File projectPath)
    {
        try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(projectPath))))
        {
            this.projectName = (String)in.readObject();
            this.projectType = (String)in.readObject();
            this.projectPath = (File)in.readObject();
        }catch (ClassNotFoundException e)
        {

        }
        catch (FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error while opening your project", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error while opening your project\n", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        this.projectFiles = new Files(this.projectPath);
        boolean eof = false;
        try (ObjectInputStream inFiles = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(this.projectPath.getAbsolutePath()+PropertiesShared.SEPARATOR+".honeycode"+PropertiesShared.SEPARATOR+"projectfiles"))))){

            while (!eof)
            {
                try {
                    this.projectFiles.addFile((File)inFiles.readObject());
                }catch (EOFException ex)
                {
                    eof = true;
                }
            }

        }catch (FileNotFoundException ex)
        {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error while opening your project\n", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex)
        {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error while opening your project\n", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error while opening your project\n", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

    }

    /**
     * Crée la structure du projet, arborescence etc à partir des fichiers en appelant createProjectStructure()
     */
    public void makeProjectStructure()
    {
        this.projectFiles.createProjectStructure();
    }

}
