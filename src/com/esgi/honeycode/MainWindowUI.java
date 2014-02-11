package com.esgi.honeycode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Mathieu on 11/02/14.
 */
public class MainWindowUI extends JComponent{


    private JFrame mainWindowUI = new JFrame("HoneyCode");
    private JPanel mainPanel = new JPanel();
    private JEditorPane editorPaneMain = new JEditorPane();
    private JMenuBar menuBarMain = new JMenuBar();
    private JMenu file = new JMenu("Fichier");
    private JMenu edit = new JMenu("Edition");
    private JMenu view = new JMenu("Affichage");
    private JMenu plugin = new JMenu("Plugins");
    private JMenu help = new JMenu("Help");

    public MainWindowUI(){


        Toolkit tkMain=Toolkit.getDefaultToolkit();
        //get the screen size
        Dimension dimSrceenSize = tkMain.getScreenSize();

        //height of the task bar
        Insets scnMax = tkMain.getScreenInsets(mainWindowUI.getGraphicsConfiguration());
        int taskBarSize = scnMax.bottom;


        //Set location and size according to the screen size and taskbar size
        setLocation(dimSrceenSize.width - getWidth(), dimSrceenSize.height - taskBarSize - getHeight());
        mainWindowUI.setPreferredSize(new Dimension(dimSrceenSize.width - getWidth(), dimSrceenSize.height - taskBarSize - getHeight()));

        //Container panel = mainWindowUI.getContentPane();

        BorderLayout mainBorderLayout = new BorderLayout();
        //mainWindowUI.setLayout(mainBorderLayout);
        mainPanel.setLayout(mainBorderLayout);

        //Adding menus into menubar
        menuBarMain.add(file);
        menuBarMain.add(edit);
        menuBarMain.add(view);
        menuBarMain.add(plugin);
        menuBarMain.add(help);
        mainWindowUI.setJMenuBar(menuBarMain);


        mainWindowUI.setContentPane(mainPanel);

        mainWindowUI.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //Listener sur la fermeture de la fenetre
        mainWindowUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(mainWindowUI, "Etes-vous sûr de vouloir quitter HoneyCode ?","Confirmation",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                if (confirm==JOptionPane.YES_OPTION){
                    /*
                    TODO :
                          Check des fichiers ouverts et demandes si sauvegarde, avec Oui -> entraine sauvegarde puis fermeture
                          avec Non --> Fichier non sauvegardé puis fermeture
                          avec Annuler --> Annule la fermeure de l'application
                     */
                    System.exit(0);
                }
            }
        });


        editorPaneMain.setEditable(true);
        mainPanel.add(editorPaneMain,BorderLayout.CENTER);


        mainWindowUI.pack();
        mainWindowUI.setVisible(true);







    }
}
