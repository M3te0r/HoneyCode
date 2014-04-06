package com.esgi.honeycode;

import org.fife.io.UnicodeWriter;

import javax.swing.*;
import java.io.File;

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
        globalPreferences.setPreferences();
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
