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
 */
public class HCPreferences {

    private Preferences prefs;

    public void setPreferences()
    {


        //Préférences de registre par utilisateur
        prefs = Preferences.userNodeForPackage(this.getClass());

        String langDef = System.getProperty("user.language");
        prefs.put("Language", langDef);
    }
}
