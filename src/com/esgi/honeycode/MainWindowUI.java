package com.esgi.honeycode;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private JPanel subConsolePane = new JPanel();

    private JLabel lastBuildLabel = new JLabel("Last build : test");

    private JButton runButton = new JButton("Run");
    private JButton buildOptionsButton = new JButton("Build options");

    private JTextArea consoleOutputArea = new JTextArea("Console Ouptut");
    private JEditorPane editorPaneMain = new JEditorPane();

    private JMenuBar menuBarMain = new JMenuBar();

    private JMenu file = new JMenu();
    private JMenu edit = new JMenu();
    private JMenu view = new JMenu();
    private JMenu plugin = new JMenu("Plugins");
    private JMenu help = new JMenu();

    private JMenuItem newFile = new JMenuItem();
    private JMenuItem open = new JMenuItem();
    private JMenuItem recentFiles = new JMenuItem();  //Collections d'objets (nom du fichier, chemin) ??
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
        saveFileAS.addActionListener(test);
        settings.addActionListener(test);
        runButton.addActionListener(test);

        Toolkit tkMain=Toolkit.getDefaultToolkit();
        //get the screen size
        Dimension dimScreenSize = tkMain.getScreenSize();

        //height of the task bar
        Insets scnMax = tkMain.getScreenInsets(mainWindowUI.getGraphicsConfiguration());
        int taskBarSize = scnMax.bottom;


        //Set location and size according to the screen size and taskbar size
        setLocation(dimScreenSize.width - getWidth(), dimScreenSize.height - taskBarSize - getHeight());
        mainWindowUI.setPreferredSize(new Dimension(dimScreenSize.width - getWidth(), dimScreenSize.height - taskBarSize - getHeight()));
        consolePane.setPreferredSize(new Dimension(dimScreenSize.width - getWidth(), 250));
        subConsolePane.setPreferredSize(new Dimension(dimScreenSize.width - getWidth(), 30));
        consoleOutputArea.setPreferredSize(new Dimension(dimScreenSize.width - getWidth(), 210));

        consolePane.setLayout(new BorderLayout());
        subConsolePane.setLayout(new BorderLayout());
        BorderLayout mainBorderLayout = new BorderLayout();
        mainPanel.setLayout(mainBorderLayout);


        //Récupération de la touche utilisée pour les raccourcis clavier du système
        int shortcutKey = tkMain.getMenuShortcutKeyMask();
        //Raccourcis des JMenuItem
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, shortcutKey));
        exitApp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, shortcutKey));
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcutKey));
        saveFileAS.setAccelerator(KeyStroke.getKeyStroke(shortcutKey+"shift S")); // Syntaxe pour les raccourcis à 3 touches
        /*
        Syntaxe pour les events à une touche de fonction
        Pour events à touche+touche fonction : KeyEvent.VK_F1, InputEvent.CTRL_DOWN_MASK par exemple
        */
        about.setAccelerator(KeyStroke.getKeyStroke("F1"));
        settings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcutKey+InputEvent.ALT_DOWN_MASK)); // en fait ça marche aussi comme ça
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, shortcutKey));
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,shortcutKey));
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, shortcutKey));
        past.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, shortcutKey));
        //Reste des raccourcis

        //Adding menus into menubar
        menuBarMain.add(file);
        file.add(newFile);
        file.add(open);
        file.add(recentFiles);
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

        consolePane.add(subConsolePane, BorderLayout.NORTH);
        consolePane.add(consoleOutputArea, BorderLayout.SOUTH);

        subConsolePane.add(lastBuildLabel, BorderLayout.WEST);
        subConsolePane.add(buildOptionsButton, BorderLayout.CENTER);
        subConsolePane.add(runButton, BorderLayout.EAST);
        consoleOutputArea.setBackground(Color.BLACK);

        editorPaneMain.setEditable(true);
        mainPanel.add(editorPaneMain, BorderLayout.CENTER);
        mainPanel.add(consolePane, BorderLayout.SOUTH);
        mainWindowUI.pack();
        mainWindowUI.setVisible(true);

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
            //Ouverture de la ressource .properties
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
        recentFiles.setText(bundle.getString("recentFiles"));
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
            if (e.getSource() == saveFileAS)
            {
                System.out.print("Save File As");
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

            if (e.getSource() == settings){
                System.out.println("Settings");
            }

            if(e.getSource() == runButton){
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                String dateString = dateFormat.format(date);
                lastBuildLabel.setText("Last build : "+dateString);
            }
    }
}

}
