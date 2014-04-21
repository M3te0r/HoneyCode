package com.esgi.honeycode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Preferences Dialog Class
 *
 */
public class ParamDialog extends ModalDialog{

    private Component mainPane;
    public static final int OK = 1;
    public static final int CANCEL = 0;

    private int cancelState = 0;
    protected JButton applyButton;
    protected JButton okButton;
    protected JButton cancelButton;
    protected JPanel buttonPane;
    private JPanel settingsPane;
    private JSplitPane splitedPane;
    private JPanel settingsTypePanel;
    private JPanel settingsDetailsPane;
    private JLabel themes;
    private JComboBox<String> themesListComboBox;
    private JComboBox<String> userLanguage;
    private JComboBox<Font> userFont;


    public ParamDialog(Frame parent)
    {
        super(parent);
        _init();
    }

    protected void _init()
    {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        c.add(buttonPane, BorderLayout.SOUTH);
        ButtonListener buttonListener = new ButtonListener();
        applyButton = new JButton("Appliquer");
        buttonPane.add(applyButton);
        applyButton.addActionListener(buttonListener);
        okButton = new JButton("OK");
        okButton.addActionListener(buttonListener);
        buttonPane.add(okButton);
        cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(buttonListener);
        buttonPane.add(cancelButton);

    }


    protected class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton)e.getSource();
            if (button == applyButton)
            {
                //apply();
            }
            else if (button == okButton)
            {
               // ok();
            }

            else if (button == cancelButton)
            {
                //cancel();
            }
        }
    }


}
