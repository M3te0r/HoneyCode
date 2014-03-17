package com.esgi.honeycode;

import javax.swing.*;
import java.io.File;

/**
 * Created by Mathieu on 11/02/14.
 * Main class
 */
public class HoneyCodeMain {

    public static void main(String[] args){

        System.out.println(System.getProperty("java.class.path"));

        HCPreferences globalPreferences = new HCPreferences();
        globalPreferences.setPreferences();
        File projectPath = new File(globalPreferences.getProjetPath());
        boolean created = projectPath.mkdir();
        if (!created && !projectPath.exists())
        {
            JOptionPane.showMessageDialog(null, "Impossible de créer le répertoire de projet : "+projectPath);
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainWindowUI();
            }
        });
    }
}
