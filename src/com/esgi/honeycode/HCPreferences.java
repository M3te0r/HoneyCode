package com.esgi.honeycode;

import javax.swing.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Created by Mathieu on 25/02/14.
 * Classe permettant de configurer l'application avec des fichiers de configuration propres au
 * système (registre pour Win, .plist pour Mac, xml pour Linux)
 * Sauvegarde les paramètres de l'utilisateur
 * Sous les systèmes Win 7/8/8.1 la JVM n'a pas les droits pour créer de nouveau noeuds dans le registre
 * Les clés sont cependant bien crées, mais déclenche une exception sur le Secutity Manager
 * Il faut donc lancer IntelliJ IDEA en mode administrateur pour que la JVM ait les droits d'écriture
 *
 * /!\ Les valeurs de registre sont encodés en BASE64, utiliser seulement des noms en minuscules des underscores /!\
 */
public class HCPreferences {

    private Preferences prefs;
    private String langDef;
    private String defaultPath;


    public HCPreferences() {
        //Préférences de registre par utilisateur
        this.prefs = Preferences.userNodeForPackage(this.getClass());
        this.langDef = System.getProperty("user.language");
        this.defaultPath = System.getProperty("user.home")+System.getProperty("file.separator")+"HoneyCodeProjects";
    }

    public void setPreferences()
    {
        //Lowercase name only
        this.prefs.put("language", this.langDef);
        this.prefs.put("project_path", this.defaultPath);



    }

    public void clear()
    {
        try {
            this.prefs.clear();
        }
        catch (BackingStoreException ex)
        {
            JOptionPane.showMessageDialog(null, ex.getStackTrace());
        }
    }

    public String getUserLanguageReg()
    {
        return this.prefs.get("language", this.langDef);
    }

    public String getProjetPath()
    {
        return this.prefs.get("project_path", this.defaultPath);
    }
}
