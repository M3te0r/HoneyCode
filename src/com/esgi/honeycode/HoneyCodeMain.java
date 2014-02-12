package com.esgi.honeycode;

import javax.swing.*;

/**
 * Created by Mathieu on 11/02/14.
 */
public class HoneyCodeMain {

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainWindowUI();
            }
        });
    }
}
