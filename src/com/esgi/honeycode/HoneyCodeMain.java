package com.esgi.honeycode;

import javax.swing.*;

/**
 * Created by Mathieu on 11/02/14.
 */
public class HoneyCodeMain {

    public static void main(String[] args){


        HCPreferences globablPreferences = new HCPreferences();
        globablPreferences.setPreferences();



        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainWindowUI();
            }
        });
    }
}
