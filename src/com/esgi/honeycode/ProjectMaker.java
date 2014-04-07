package com.esgi.honeycode;

import java.io.File;

/**
 * Class handling project created by the user
 */
public class ProjectMaker {

    private String projectName;
    private String projectType;
    private Files projectFiles;

    public ProjectMaker(String projectName, String projectType) {
        this.projectName = projectName;
        this.projectType = projectType;
        this.projectFiles = new Files(new File(new HCPreferences().getProjetPath()+PropertiesShared.SEPARATOR+this.projectName));
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


}
