package com.esgi.honeycode;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Mathieu on 11/02/14.
 * Modified by Kevin on 13/02/14.
 */

public class MainWindowUI extends JFrame{


    private JFrame mainWindowUI = new JFrame("HoneyCode");

    private JPanel mainPanel = new JPanel();
    private JPanel consolePane = new JPanel();

    private JLabel lastBuildLabel = new JLabel("Last build : test");

    private JButton buildOptionsButton = new JButton("Build Options");
    private JButton runButton = new JButton("Run");

    private JEditorPane editorPaneMain = new JEditorPane();

    private JMenuBar menuBarMain = new JMenuBar();

    private JMenu file = new JMenu("Fichier");
    private JMenu edit = new JMenu("Edition");
    private JMenu view = new JMenu("Affichage");
    private JMenu plugin = new JMenu("Plugins");
    private JMenu help = new JMenu("Help");

    private JMenuItem  newFile = new JMenuItem("Nouveau fichier");
    private JMenuItem  open = new JMenuItem("Ouvrir...");
    private JMenuItem  rencentFiles = new JMenuItem("Récents");  //Collections d'objets (nom du fichier, chemin) ??
    private JMenuItem saveFile = new JMenuItem("Enregister");
    private JMenuItem saveFileAS = new JMenuItem("Enregister sous");
    private JMenuItem settings = new JMenuItem("Préférences");
    private JMenuItem exitApp = new JMenuItem("Quitter HoneyCode");
    private JMenuItem copy = new JMenuItem("Copier");
    private JMenuItem cut = new JMenuItem("Couper");
    private JMenuItem past = new JMenuItem("Coller");
    private JMenuItem encoding = new JMenuItem("Encodage");
    private JMenuItem consoleView = new JMenuItem("Afficher la console");
    private JMenuItem previewShow = new JMenuItem("Afficher la prévisualisation (HTML)");
    private JMenuItem highLight = new JMenuItem("Colorisation");
    private JMenuItem plugLoad = new JMenuItem("Charger un plugin");
    private JMenuItem plugDown = new JMenuItem("Télécharger des plugins");
    private JMenuItem plugSubmit = new JMenuItem("Soummettre vos plugins");
    private JMenuItem about = new JMenuItem("A propos");
    private JMenuItem docHC = new JMenuItem("Documentation");
    private JMenuItem forum = new JMenuItem("Forum");

    final JFileChooser fileChooserMain = new JFileChooser();

    public MainWindowUI(){

        //Action Listener declaration + adding listeners to the elements
        ActionListenerMenuBar test = new ActionListenerMenuBar();
        newFile.addActionListener(test);
        about.addActionListener(test);
        open.addActionListener(test);
        exitApp.addActionListener(test);
        plugLoad.addActionListener(test);
        plugDown.addActionListener(test);
        forum.addActionListener(test);
        copy.addActionListener(test);
        cut.addActionListener(test);

        Toolkit tkMain=Toolkit.getDefaultToolkit();
        //get the screen size
        Dimension dimSrceenSize = tkMain.getScreenSize();

        //height of the task bar
        Insets scnMax = tkMain.getScreenInsets(mainWindowUI.getGraphicsConfiguration());
        int taskBarSize = scnMax.bottom;


        //Set location and size according to the screen size and taskbar size
        setLocation(dimSrceenSize.width - getWidth(), dimSrceenSize.height - taskBarSize - getHeight());
        mainWindowUI.setPreferredSize(new Dimension(dimSrceenSize.width - getWidth(), dimSrceenSize.height - taskBarSize - getHeight()));

        //Setting of layout managers for editor pane and console pane
        BorderLayout mainBorderLayout = new BorderLayout();
        GridBagLayout mainConsoleLayout = new GridBagLayout();
        mainPanel.setLayout(mainBorderLayout);
        consolePane.setLayout(mainConsoleLayout);
            consolePane.setPreferredSize(new Dimension(dimSrceenSize.width - getWidth(), 250));


        //Adding menus into menubar
        menuBarMain.add(file);
        file.add(newFile);
        file.add(open);
        file.add(rencentFiles);
        file.add(saveFile);
        file.add(saveFileAS);
        file.add(settings);
        file.add(exitApp);
        menuBarMain.add(edit);
        edit.add(copy);
        edit.add(cut);
        edit.add(past);
        edit.add(encoding);
        menuBarMain.add(view);
        view.add(consoleView);
        view.add(previewShow);
        view.add(highLight);
        menuBarMain.add(plugin);
        plugin.add(plugLoad);
        plugin.add(plugDown);
        plugin.add(plugSubmit);
        menuBarMain.add(help);
        help.add(about);
        help.add(docHC);
        help.add(forum);

        //Joining mainPanel and window
        mainWindowUI.setJMenuBar(menuBarMain);
        mainWindowUI.setContentPane(mainPanel);


        mainWindowUI.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //Listener sur la fermeture de la fenetre
        mainWindowUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(mainWindowUI, "Etes-vous sûr de vouloir quitter HoneyCode ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
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

        //Arranging main elements of the window
        mainPanel.add(editorPaneMain,BorderLayout.CENTER);
        mainPanel.add(consolePane, BorderLayout.SOUTH);

        mainWindowUI.pack();
        mainWindowUI.setVisible(true);

    }
    public class ActionListenerMenuBar implements ActionListener {
        public void actionPerformed (ActionEvent e){
            if(e.getSource() == newFile){
                System.out.println(e.getSource().toString());
                System.out.println("New File");
            }

            if(e.getSource() == about){
                JOptionPane.showMessageDialog(mainWindowUI, "HoneyCode est un projet étudiant développé au sein de l'ESGI, et est libre de droits.\n Développeurs :" +
                        "\n-Kevin MAAREK \n-Mathieu PEQUIN \n-Alexandre FAYETTE \n Promotion 3iAL ", "A propos", JOptionPane.INFORMATION_MESSAGE);

            }

            if(e.getSource() == open){
                int returnVal = fileChooserMain.showOpenDialog(mainWindowUI);

                if (returnVal == JFileChooser.APPROVE_OPTION){
                    File chosenFile = fileChooserMain.getSelectedFile();
                    //Accept all file extensions ?
                    //Gestion de l'ouverture des fichiers...

                }
            }

            if(e.getSource() == exitApp){
                //Parent Component = exitApp (JmenuItem component) or mainWindowUI (the frame) ???
                int confirm = JOptionPane.showConfirmDialog(exitApp, "Etes-vous sûr de vouloir quitter HoneyCode ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
                if (confirm == JOptionPane.YES_OPTION){
                    /*
                    TODO :
                    Same as Window Closing
                     */
                    System.exit(0);
                }

            }

            if(e.getSource() == plugLoad){
                int returnVal = fileChooserMain.showOpenDialog(mainWindowUI);

                if(returnVal == JFileChooser.APPROVE_OPTION){
                    File chosenPlugin = fileChooserMain.getSelectedFile();
                    // Only accept *.jar files
                }
            }
            if(e.getSource() == plugDown){
                    //Throws an exception
                   //Not the good catch, just to test
                try{
                    Desktop.getDesktop().browse(new URI("http://kevinmaarek.fr/"));
                } catch (URISyntaxException | IOException ex){
                    JOptionPane.showMessageDialog(mainWindowUI, ex.getMessage(),"Erreur", JOptionPane.ERROR_MESSAGE,null);
                }

            }

            if(e.getSource() == forum){
                //Not the good catch, just to test
                try{
                    Desktop.getDesktop().browse(new URI("http://kevinmaarek.fr/"));
                } catch (URISyntaxException | IOException ex){
                    JOptionPane.showMessageDialog(mainWindowUI, "Could not open HoneyCode forum Web Page", "Erreur", JOptionPane.ERROR_MESSAGE, null);
                }


            }

            if(e.getSource() == copy){
                StringSelection selectedText = new StringSelection(editorPaneMain.getSelectedText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selectedText,null);
            }

            if(e.getSource() == cut){
                StringSelection selectedText = new StringSelection(editorPaneMain.getSelectedText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selectedText,null);
                editorPaneMain.replaceSelection("");
            }
    }
}

}
