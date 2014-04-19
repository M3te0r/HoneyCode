package com.esgi.honeycode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;


public class TextFieldStreamer extends InputStream implements ActionListener {

    private JTextField tf;
    private String str = null;
    private int pos = 0;

    public TextFieldStreamer(JTextField jtf) {
        tf = jtf;
    }

    //gets triggered everytime that "Enter" is pressed on the textfield
    @Override
    public void actionPerformed(ActionEvent e) {
        str = tf.getText() + "\n";
        pos = 0;
        tf.setText("");
        synchronized (this) {
            this.notifyAll();
        }
    }



    @Override
    public int read() {
        //test if the available input has reached its end
        //and the EOS should be returned 
        if(str != null && pos == str.length()){
            str =null;
            //-1 at end of stream
            return java.io.StreamTokenizer.TT_EOF;
        }
        //no input available, block until more is available
        while (str == null || pos >= str.length()) {
            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        //read an additional character, return it and increment the index
        return str.charAt(pos++);
    }

}