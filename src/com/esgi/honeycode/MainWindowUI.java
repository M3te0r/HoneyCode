package com.esgi.honeycode;

import javax.swing.*;
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
import java.util.ResourceBundle;

/**
 * Created by Mathieu on 11/02/14.
 * Modified by Kevin on 13/02/14.
 * Crée la fenêtre pricipale d'HoneyCode avec ses composants
 * Les éléments de la fenêtre sont adaptés à la langue par défaut de l'utilisateur
 */

public class MainWindowUI extends JFrame{

    private ResourceBundle bundle;

    private JFrame mainWindowUI = new JFrame("HoneyCode");

    private JPanel mainPanel = new JPanel();
    private JPanel consolePane = new JPanel();

    private JEditorPane editorPaneMain = new JEditorPane();

    private JMenuBar menuBarMain = new JMenuBar();

    private JMenu file = new JMenu();
    private JMenu edit = new JMenu();
    private JMenu view = new JMenu();
    private JMenu plugin = new JMenu("Plugins");
    private JMenu help = new JMenu();

    private JMenuItem newFile = new JMenuItem();
    private JMenuItem open = new JMenuItem("Ouvrir...");
    private JMenuItem rencentFiles = new JMenuItem();  //Collections d'objets (nom du fichier, chemin) ??
    private JMenuItem saveFile = new JMenuItem();
    private JMenuItem saveFileAS = new JMenuItem();
    private JMenuItem settings = new JMenuItem();
    private JMenuItem exitApp = new JMenuItem();
    private JMenuItem copy = new JMenuItem();
    private JMenuItem cut = new JMenuItem();
    private JMenuItem past = new JMenuItem();
    private JMenuItem encoding = new JMenuItem();
    private JMenuItem consoleView = new JMenuItem();
    private JMenuItem previewShow = new JMenuItem();
    private JMenuItem highLight = new JMenuItem();
    private JMenuItem plugLoad = new JMenuItem();
    private JMenuItem plugDown = new JMenuItem();
    private JMenuItem plugSubmit = new JMenuItem();
    private JMenuItem about = new JMenuItem();
    private JMenuItem docHC = new JMenuItem();
    private JMenuItem forum = new JMenuItem();

    final JFileChooser fileChooserMain = new JFileChooser();

    public MainWindowUI(){

        setUILanguage();

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

        BorderLayout mainBorderLayout = new BorderLayout();
        mainPanel.setLayout(mainBorderLayout);

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
        mainPanel.add(editorPaneMain,BorderLayout.CENTER);
        mainWindowUI.pack();
        mainWindowUI.setVisible(true);
    }

    //Return the user default language
    private String getUserLanguage()
    {
        return System.getProperty("user.language");
    }

    private void setUILanguage()
    {
        String userLanguage = getUserLanguage();
        /*
        Lit le fichier qui correspond à la langue de l'utilisateur
        Si un composant est traduit ici, les fichiers .properties doivent tous contenir le couple nomComposant=traduction
        */

        if (userLanguage.equals("en") || userLanguage.equals("fr"))
        {
            bundle = ResourceBundle.getBundle("HoneyCode_"+userLanguage); // remplacer userLanguage par "en" pour test
        }
        //Si ni fr ni en par défaut en anglais
        else {
            bundle = ResourceBundle.getBundle("HoneyCode_en");
        }

        file.setText(bundle.getString("file"));
        view.setText(bundle.getString("view"));
        edit.setText(bundle.getString("edit"));
        help.setText(bundle.getString("help"));

        newFile.setText(bundle.getString("newFile"));
        open.setText(bundle.getString("open"));
        rencentFiles.setText(bundle.getString("recentFiles"));
        saveFile.setText(bundle.getString("saveFile"));
        saveFileAS.setText(bundle.getString("saveFileAS"));
        settings.setText(bundle.getString("settings"));
        exitApp.setText(bundle.getString("exitApp"));
        copy.setText(bundle.getString("copy"));
        cut.setText(bundle.getString("cut"));
        past.setText(bundle.getString("past"));
        encoding.setText(bundle.getString("encoding"));
        consoleView.setText(bundle.getString("consoleView"));
        previewShow.setText(bundle.getString("previewShow"));
        highLight.setText(bundle.getString("highlight"));
        plugLoad.setText(bundle.getString("plugLoad"));
        plugDown.setText(bundle.getString("plugDown"));
        plugSubmit.setText(bundle.getString("plugSubmit"));
        about.setText(bundle.getString("about"));
        docHC.setText(bundle.getString("docHC"));
        forum.setText(bundle.getString("forum"));

        //...Suite des traductions...

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
