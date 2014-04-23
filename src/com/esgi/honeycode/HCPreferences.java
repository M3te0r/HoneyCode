package com.esgi.honeycode;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
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
    private String font;
    private String theme;
    private int stateChange = 0;
    private int themeChanged = 0;
    private int fontChanged = 0;



    public HCPreferences() {


        //Préférences de registre par utilisateur
        this.prefs = Preferences.userNodeForPackage(this.getClass());

        this.langDef = System.getProperty("user.language");
        this.defaultPath = System.getProperty("user.home")+PropertiesShared.SEPARATOR+"HoneyCodeProjects";
        this.font = RSyntaxTextArea.getDefaultFont().getFontName();
        this.theme = "dark";

    }

    public void setPreferences()
    {
        //Lowercase name only
        this.prefs.put("language", this.langDef);
        this.prefs.put("project_path", this.defaultPath);
        this.prefs.put("font", this.font);
        this.prefs.put("theme", this.theme);
    }

    public int getStateChange()
    {
        return this.stateChange;
    }

    public void setStateChange(int stateChange) {
        this.stateChange = stateChange;
    }

    public void setFontChanged(int fontChanged) {
        this.fontChanged = fontChanged;
    }

    public int getFontChanged() {
        return this.fontChanged;
    }

    public int getThemeChanged() {
        return this.themeChanged;
    }

    public void setThemeChanged(int themeChanged) {
        this.themeChanged = themeChanged;
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

    public String getFont(){
        return this.prefs.get("font", this.font);
    }

    public String getTheme() {
        return this.prefs.get("theme", this.theme);
    }

    public void setTheme(String theme) {
        this.theme = theme;
        this.prefs.put("theme", this.theme);
    }

    public void setFont(String font) {
        this.font = font;
        this.prefs.put("font", this.font);
    }

    public void setLangDef(String langDef) {
        this.langDef = langDef;
        this.prefs.put("language", this.langDef);
    }
}
