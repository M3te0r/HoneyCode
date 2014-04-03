package com.esgi.honeycode;

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
        this.projectFiles = new Files();
    }


}
