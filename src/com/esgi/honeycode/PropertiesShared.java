package com.esgi.honeycode;

/**
 * Class enum
 * Minimize system calls
 */
public enum PropertiesShared {

    //Objet directement construit
    SEPARATOR(System.getProperty("file.separator"));


    private String name = "";

    /**
     * Constructeur
     * @param name Chaine à utiliser
     */
    PropertiesShared(String name){
        this.name = name;
    }

    /**
     * Retourne la chaine correspondante
     * @return String
     */
    @Override
    public String toString() {
        return name;
    }
}
