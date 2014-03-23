package com.esgi.honeycode;

/**
 * Class enum
 * Minimize system calls
 */
public enum PropertiesShared {

    //Objet directement construit
    SEPARATOR(System.getProperty("file.separator"));

    private String name = "";

    //Constructeur
    PropertiesShared(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
