package com.esgi.honeycode;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Class extending OutputStream to redirect
 * System output to JTextArea consoleArea
 */
public class CustomConsoleOutputStream extends OutputStream {

    private JTextArea consoleArea;

    /**
     * Constructeur
     * @param consoleArea JTextArea où effectuer la sortie console
     */
    public CustomConsoleOutputStream(JTextArea consoleArea) {
        this.consoleArea = consoleArea;
    }

    @Override
    public void write(int b) throws IOException {
        // redirects data to the text area
        consoleArea.append(String.valueOf((char)b));

        // scrolls the text area to the end of data
        consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
    }

    @Override
    public void flush() throws IOException {
        //Clear text from consoleArea
        consoleArea.setText(null);
    }
}