package com.esgi.honeycode;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * UI class for the project JDialog
 * Asks for the language, type, name of the project
 * Control the project type and project name
 */
public class ProjectTypeCustomDialog extends JDialog{

    private static final Image MAIN_IMAGE = new ImageIcon(MainWindowUI.class.getResource("/icons/logo.png")).getImage();
    private boolean finished = false;
    protected Vector<String> languages;
    private JComboBox<String> languagesComboBox;
    private JTextField projectName = new JTextField("untitled");
    private JButton finishButton;
    private JButton cancelButton;
    private boolean flagOption;
    private boolean flagProjectName;

    /**
     * Creer un nouveau ProjectTypeCustomDialog
     */
    public ProjectTypeCustomDialog()
    {
        //will do internationalization later
        setResizable(false);
        setIconImage(MAIN_IMAGE);
        setTitle("Nouveau projet");
        JPanel projectPanel = new JPanel(new GridLayout(3,2,15,15));
        projectPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        flagOption = false;
        flagProjectName = true;
        finishButton = new JButton("Finish");
        finishButton.setEnabled(false);
        cancelButton = new JButton("Cancel");
        this.languages = new Vector<>();
        this.languages.add("Choose an option");
        this.languages.add("Java project");
        languagesComboBox = new JComboBox<>(languages);
        languagesComboBox.setSelectedIndex(0);
        projectPanel.add(new JLabel("Choose a project type : "));
        projectPanel.add(languagesComboBox);
        projectPanel.add(new JLabel("Project Name : "));
        projectPanel.add(projectName);
        projectPanel.add(finishButton);
        projectPanel.add(cancelButton);
        add(projectPanel);
        pack();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finished = true;
                setVisible(false);
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        projectName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(flagOption && flagProjectName && e.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    finishButton.doClick();
                }
            }
        });



        languagesComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getItem()!=languages.get(0))
                {

                    flagOption = true;
                    if (flagProjectName)
                    {
                        finishButton.setEnabled(true);
                    }

                }
                else
                {
                    flagOption = false;
                    if (finishButton.isEnabled())
                    {
                        finishButton.setEnabled(false);
                    }
                }
            }
        });

        projectName.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

                if (e.getDocument().getLength()>=1)
                {
                    flagProjectName = true;
                    if (flagOption)
                    {
                        finishButton.setEnabled(true);
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                if (e.getDocument().getLength()==0)
                {
                    flagProjectName = false;
                    if (finishButton.isEnabled())
                    {
                        finishButton.setEnabled(false);
                    }
                }
                else
                {
                    flagProjectName = true;
                    if (flagOption)
                    {
                        finishButton.setEnabled(true);
                    }
                }

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
    }

    /**
     * Récupère le langage de l'interface courante
     * @return String désignation du langage
     */
    public String getLanguage() {
        return languagesComboBox.getSelectedItem().toString();
    }

    /**
     * Récupère le nom du projet courant
     * @return String
     */
    public String getProjectName() {
        return projectName.getText();
    }

    public boolean isFinished()
    {
        return finished;
    }


}
