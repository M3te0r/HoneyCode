package com.esgi.honeycode;

import com.sun.istack.internal.NotNull;

import javax.swing.*;
import java.io.*;

/**
 * Class handling project created by the user
 */
public class ProjectMaker implements Serializable{

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

    public Files getProjectFiles() {
        return projectFiles;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectType() {
        return projectType;
    }

    public void serializeProjectFiles()
    {
        try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(this.projectPath.getAbsolutePath()+PropertiesShared.SEPARATOR+".honeycode"+PropertiesShared.SEPARATOR+"project.dat")))))
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

    public void serializeProjectSettings()
    {
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(this.projectPath.getAbsolutePath()+PropertiesShared.SEPARATOR+".honeycode"+PropertiesShared.SEPARATOR+"settings.dat"))))){
            out.writeObject(this.projectName);
            out.writeObject(this.projectType);
            out.writeObject(this.projectPath);
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error while saving your project", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void doDeserialize(File projectPath)
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
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error while opening your project\n"+e.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        this.projectFiles = new Files(this.projectPath);
        boolean eof = false;
        try (ObjectInputStream inFiles = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(this.projectPath.getAbsolutePath()+PropertiesShared.SEPARATOR+".honeycode"+PropertiesShared.SEPARATOR+"project.dat"))))){

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
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error while opening your project\n"+ex.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (ClassNotFoundException ex)
        {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error while opening your project\n"+ex.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error while opening your project\n"+ex.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void makeProjectStructure()
    {
        this.projectFiles.createProjectStructure();
    }

}
