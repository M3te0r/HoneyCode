package com.esgi.honeycode;

import org.fife.io.UnicodeWriter;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


/**
 *
 * Main class
 */
public class HoneyCodeMain {

    public static void main(String[] args){

        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // handle exception
        }
        HCPreferences globalPreferences = new HCPreferences();
        try {


            if (Preferences.userNodeForPackage(HoneyCodeMain.class).keys().length<5)
            {
                globalPreferences.setPreferences();
            }

        }catch (BackingStoreException ex)
        {
            JOptionPane.showMessageDialog(null, "Could not create node in registry\n"+ex.getStackTrace());
        }

       File projectPath = new File(globalPreferences.getProjetPath());
        boolean created = projectPath.mkdir();
        if (!created && !projectPath.exists())
        {
            JOptionPane.showMessageDialog(null, "Impossible de créer le répertoire de projet : "+projectPath);
        }

        System.setProperty(UnicodeWriter.PROPERTY_WRITE_UTF8_BOM, "false");

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainWindowUI();
            }
        });
    }
}
