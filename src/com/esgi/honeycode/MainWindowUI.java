package com.esgi.honeycode;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
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

    //Déclarations des différents composants et ressources

    private ResourceBundle bundle;

    private static final Icon CLOSE_TAB_ICON = new ImageIcon(MainWindowUI.class.getResource(".."+PropertiesShared.SEPARATOR+".."+PropertiesShared.SEPARATOR+".."+PropertiesShared.SEPARATOR+"ressources"+PropertiesShared.SEPARATOR+"Cross_close_tab_button.png"));
    private static final Icon CLOSE_TAB_ICON_DISABLED = new ImageIcon(MainWindowUI.class.getResource(".."+PropertiesShared.SEPARATOR+".."+PropertiesShared.SEPARATOR+".."+PropertiesShared.SEPARATOR+"ressources"+PropertiesShared.SEPARATOR+"Cross_close_tab_button_disabled.png"));
    private static final Icon TAB_ICON = new ImageIcon(MainWindowUI.class.getResource(".."+PropertiesShared.SEPARATOR+".."+PropertiesShared.SEPARATOR+".."+PropertiesShared.SEPARATOR+"ressources"+PropertiesShared.SEPARATOR+"Icon_page_code.gif"));

    private static final Image MAIN_IMAGE = new ImageIcon(MainWindowUI.class.getResource(".."+PropertiesShared.SEPARATOR+".."+PropertiesShared.SEPARATOR+".."+PropertiesShared.SEPARATOR+"ressources"+PropertiesShared.SEPARATOR+"main.png")).getImage();
    private JPanel treePanel;
    private JTabbedPane tabFile;
    private JTree treeMain;
    private JScrollPane scrollTree;
    private JScrollPane consoleSroll;
    private JSplitPane splited;
    private JSplitPane wholeSplit;
    private JPanel editorPanel;
    private JPanel mainPanel;
    private JPanel consolePane;
    private JPanel subConsolePane;
    private JLabel lastBuildLabel = new JLabel("Last build : test");
    private JButton buildButton;
    private JButton runButton;
    private JButton buildOptionsButton;

    private JTextArea consoleOutputArea;
    private JMenuBar menuBarMain;
    private JMenu file;
    private JMenu edit;
    private JMenu view;
    private JMenu plugin;
    private JMenu help;
    private JMenuItem newFile;
    private JMenuItem open;
    private JMenuItem recentFiles;
    private JMenuItem saveFile;
    private JMenuItem saveFileAS;
    private JMenuItem settings;
    private JMenuItem exitApp;
    private JMenuItem copy;
    private JMenuItem cut;
    private JMenuItem past;
    private JMenuItem encoding;
    private JMenuItem consoleView;
    private JMenuItem previewShow;
    private JMenuItem highLight;
    private JMenuItem plugLoad;
    private JMenuItem plugDown;
    private JMenuItem plugSubmit;
    private JMenuItem about;
    private JMenuItem docHC;
    private JMenuItem forum;
    private JMenuItem checkUpdate;
    private String exitMessage;
    final JFileChooser fileChooserMain;
    final JFileChooser pluginChooser;
    private JTextArea homeMessage;
    private int newFileNumber;
    private int tabCount;
    private Files filesArray;
    private static int shortcutKey;

    private HCPreferences globalPreferences;

    private CustomConsoleOutputStream out;

    public MainWindowUI(){

        setIconImage(MAIN_IMAGE);

        globalPreferences = new HCPreferences();
        //Instanciation des composants
        setTitle("HoneyCode");

        mainPanel = new JPanel();
        editorPanel = new JPanel(new GridLayout(1,1));
        tabFile = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        consolePane = new JPanel();
        subConsolePane = new JPanel();
        menuBarMain = new JMenuBar();
        file = new JMenu();
        edit = new JMenu();
        view = new JMenu();
        plugin = new JMenu("Plugins");
        help = new JMenu();
        newFile = new JMenuItem();
        open = new JMenuItem();
        recentFiles = new JMenuItem();  //Collections d'objets (nom du fichier, chemin) ??
        saveFile = new JMenuItem();
        saveFileAS = new JMenuItem();
        settings = new JMenuItem();
        exitApp = new JMenuItem();
        copy = new JMenuItem();
        cut = new JMenuItem();
        past = new JMenuItem();
        encoding = new JMenuItem();
        consoleView = new JMenuItem();
        previewShow = new JMenuItem();
        highLight = new JMenuItem();
        plugLoad = new JMenuItem();
        plugDown = new JMenuItem();
        plugSubmit = new JMenuItem();
        about = new JMenuItem();
        docHC = new JMenuItem();
        forum = new JMenuItem();
        checkUpdate = new JMenuItem();
        runButton = new JButton();
        buildButton = new JButton();
        buildOptionsButton = new JButton();
        consoleOutputArea = new JTextArea();
        out = new CustomConsoleOutputStream(consoleOutputArea);
        System.setOut(new PrintStream(out));
        fileChooserMain = new JFileChooser();
        pluginChooser = new JFileChooser();

        fileChooserMain.setAcceptAllFileFilterUsed(false);
        fileChooserMain.addChoosableFileFilter(new FileNameExtensionFilter("Java sources", "java"));
        fileChooserMain.addChoosableFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        pluginChooser.setAcceptAllFileFilterUsed(false);
        pluginChooser.addChoosableFileFilter(new FileNameExtensionFilter("Jar files", "jar"));

        newFileNumber = 0;
        filesArray = new Files();

        homeMessage = new JTextArea();
        homeMessage.setEditable(false);
        homeMessage.setEnabled(false);
        homeMessage.setDisabledTextColor(Color.BLACK);
        homeMessage.setBackground(Color.LIGHT_GRAY);

        Toolkit tkMain=Toolkit.getDefaultToolkit();
        //get the screen size
        Dimension dimScreenSize = tkMain.getScreenSize();

        //height of the task bar
        Insets scnMax = tkMain.getScreenInsets(getGraphicsConfiguration());
        int taskBarSize = scnMax.bottom;

        //Récupération de la touche utilisée pour les raccourcis clavier du système
        shortcutKey = tkMain.getMenuShortcutKeyMask();

        setUILanguage();

        treeMain = new JTree();
        treePanel = new JPanel();
        scrollTree = new JScrollPane(treePanel);

        homeMessage.setFont(new Font("Courier new", Font.PLAIN, 24));
        editorPanel.add(homeMessage);
        editorPanel.setPreferredSize(new Dimension(600,500));

        //Qu'on me redonne la définition de vertical et horizontal
        splited = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,scrollTree,editorPanel);
        splited.setDividerSize(2);
        wholeSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splited,consolePane);
        wholeSplit.setDividerSize(3);

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
        saveFile.addActionListener(test);
        saveFileAS.addActionListener(test);
        settings.addActionListener(test);
        runButton.addActionListener(test);
        buildButton.addActionListener(test);
        past.addActionListener(test);

        consolePane.setPreferredSize(new Dimension(dimScreenSize.width - getWidth(), 250));
        subConsolePane.setPreferredSize(new Dimension(dimScreenSize.width - getWidth(), 30));

        treePanel.setLayout(new BorderLayout());
        consolePane.setLayout(new BorderLayout());
        subConsolePane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        mainPanel.setLayout(new BorderLayout());


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
        help.add(checkUpdate);

        saveFile.setEnabled(false);
        saveFileAS.setEnabled(false);


        setJMenuBar(menuBarMain);
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        consoleSroll = new JScrollPane(consoleOutputArea);
        consolePane.add(subConsolePane, BorderLayout.NORTH);
        consolePane.add(consoleSroll, BorderLayout.CENTER);

        subConsolePane.add(lastBuildLabel);
        subConsolePane.add(buildOptionsButton);
        subConsolePane.add(buildButton);
        subConsolePane.add(runButton);
        consoleOutputArea.setBackground(Color.DARK_GRAY);
        consoleOutputArea.setForeground(Color.WHITE);


        treePanel.add(treeMain, BorderLayout.CENTER);
        mainPanel.add(wholeSplit, BorderLayout.CENTER);

        //Set prefrered size before pack, then pack will adapt with the prefered size
        setPreferredSize(new Dimension(dimScreenSize.width - getWidth(), dimScreenSize.height - taskBarSize));
        pack();
        //Set location and size according to the screen size and taskbar size
        setLocation(dimScreenSize.width - getWidth(), dimScreenSize.height - taskBarSize - getHeight());
        setVisible(true);

        //Listener sur la fermeture de la fenetre
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(exitApp), exitMessage, "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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

    private void setUILanguage()
    {
        //Maybe moving it to class field if using other reg key ?


        String userLanguage = globalPreferences.getUserLanguageReg();
        /*
        Lit le fichier qui correspond à la langue de l'utilisateur
        Si un composant est traduit ici, les fichiers .properties doivent tous contenir le couple nomComposant=traduction
        */

        if (userLanguage.equals("en") || userLanguage.equals("fr"))
        {
            //Ouverture de la ressource .properties
            bundle = ResourceBundle.getBundle("HoneyCode_"+userLanguage); // remplacer userLanguage par "en" pour test

            //Set JOptionPane locale to user language
            //Trying to find for the others component such as JFileChooser, setDefaultLocale has no effect on it
            if (userLanguage.equals("en"))
            {
                // Change the default JVM Locale
                Locale.setDefault(Locale.ENGLISH);
                // Change the default LookAndFeel Locale
                UIManager.getDefaults().setDefaultLocale(Locale.ENGLISH);
                // Change default Locale for new components
                JComponent.setDefaultLocale(Locale.ENGLISH);
                fileChooserMain.setLocale(Locale.ENGLISH);
                //Forcing LookAndFeel update
                fileChooserMain.updateUI();

            }
            else if (userLanguage.equals("fr"))
            {
                //Same as "en" userLanguage condition
                Locale.setDefault(Locale.FRENCH);
                UIManager.getDefaults().setDefaultLocale(Locale.FRENCH);
                JComponent.setDefaultLocale(Locale.FRENCH);
                fileChooserMain.setLocale(Locale.FRENCH);
                fileChooserMain.updateUI();

            }

        }
        //Si ni fr ni en par défaut en anglais
        else {
            bundle = ResourceBundle.getBundle("HoneyCode_en");
            Locale.setDefault(Locale.ENGLISH);
            UIManager.getDefaults().setDefaultLocale(Locale.ENGLISH);
            JComponent.setDefaultLocale(Locale.ENGLISH);
            fileChooserMain.setLocale(Locale.ENGLISH);
            fileChooserMain.updateUI();
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
        checkUpdate.setText(bundle.getString("checkUpdate"));
        runButton.setText(bundle.getString("runButton"));
        buildButton.setText(bundle.getString("buidButton"));
        buildOptionsButton.setText(bundle.getString("buildOptionsButton"));
        consoleOutputArea.setText(bundle.getString("consoleOutputArea"));
        exitMessage = bundle.getString("exitMessage");
        homeMessage.setText(bundle.getString("homeMessage"));



        //...Suite des traductions...

    }

    private static void addCloseableTab(final JTabbedPane tabbedPane, final RTextScrollPane c, final String title, final Icon icon)
    {

        tabbedPane.addTab(null, c);


        int pos = tabbedPane.indexOfComponent(c);

        //Create a FlowLayout taht will space things 5px apart
        FlowLayout f = new FlowLayout(FlowLayout.CENTER, 5,0);

        //Make a small Jpanel with the layout and make it non-opaque
        JPanel panelTab = new JPanel(f);
        panelTab.setOpaque(false);

        //Add a Jlabel with title and the left side tab icon
        JLabel labelTitle = new JLabel(title);
        labelTitle.setIcon(icon);

        //Create a Jbutton for the close tab button
        JButton buttonClose = new JButton();
        buttonClose.setOpaque(false);

        //Configure icon and rollover icon for button
        buttonClose.setRolloverIcon(CLOSE_TAB_ICON);
        buttonClose.setRolloverEnabled(true);
        buttonClose.setIcon(CLOSE_TAB_ICON_DISABLED);

        //Set border  nulls so the button doesn't make the tab too big
        buttonClose.setBorder(null);

        //Make sure the button can't get focus, otherwise it looks funny
        buttonClose.setFocusable(false);

        //Put the panel together
        panelTab.add(labelTitle);
        panelTab.add(buttonClose);

        //Add a thin border to keep the image below the top edge of the tab when the tab is selected
        panelTab.setBorder(BorderFactory.createEmptyBorder(2,0,0,0));

        //Now assign the component for the tab
        tabbedPane.setTabComponentAt(pos, panelTab);

        //Add the listener that removes the tab
        buttonClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.remove(c);
            }
        });

        tabbedPane.setSelectedComponent(c);

        AbstractAction closeTabAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.remove(c);
            }
        };

        //Create a KeyStroke
        KeyStroke controlW = KeyStroke.getKeyStroke(KeyEvent.VK_W,shortcutKey);

        //Get the appropriate input map using the Jcomponent constants
        InputMap inputMap = c.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        //Add the key binding for the keyStroke to the action name
        inputMap.put(controlW, "closeTab");

        //Now add a single binding for the action name to the anon action
        c.getActionMap().put("closeTab", closeTabAction);
    }

    private class ActionListenerMenuBar implements ActionListener {
        public void actionPerformed (ActionEvent e){
            if(e.getSource() == newFile){
                if (homeMessage.isShowing())
                {
                    if (!saveFileAS.isEnabled() && !saveFile.isEnabled())
                    {
                        saveFile.setEnabled(true);
                        saveFileAS.setEnabled(true);
                    }

                    editorPanel.remove(homeMessage);

                    editorPanel.add(tabFile);
                    editorPanel.updateUI();
                }
                newFileNumber += 1;

                Icon icon = TAB_ICON;
                addCloseableTab(tabFile, new RTextScrollPane(new RSyntaxTextArea()),"new "+newFileNumber, icon);

            }
            if (e.getSource() == saveFileAS)
            {

                fileChooserMain.setCurrentDirectory(new File(globalPreferences.getProjetPath()));
                int status = fileChooserMain.showSaveDialog(JOptionPane.getFrameForComponent(saveFileAS));
                if (status == JFileChooser.APPROVE_OPTION)
                {

                    FileHandler file = new FileHandler(fileChooserMain.getSelectedFile());
                    RTextScrollPane rTextScrollPane = (RTextScrollPane)tabFile.getSelectedComponent();
                    RSyntaxTextArea rSyntaxTextArea = (RSyntaxTextArea)rTextScrollPane.getViewport().getView();
                    file.writeFile((RSyntaxDocument)rSyntaxTextArea.getDocument());
                    filesArray.getFilesArray().add(fileChooserMain.getSelectedFile());
                    //Can't figure why the fuck it only sets the text at the second attempt with save file as
                    ((JLabel) ((JPanel) tabFile.getTabComponentAt(tabFile.getSelectedIndex())).getComponent(0)).setText(fileChooserMain.getSelectedFile().getName());
                    tabFile.setToolTipTextAt(tabFile.getSelectedIndex(),fileChooserMain.getSelectedFile().getAbsolutePath());
                    filesArray.getFilesArray().add(fileChooserMain.getSelectedFile());

                }

            }

            if (e.getSource() == saveFile)
            {
                RTextScrollPane rTextScrollPane = (RTextScrollPane)tabFile.getSelectedComponent();
                RSyntaxTextArea rSyntaxTextArea = (RSyntaxTextArea)rTextScrollPane.getViewport().getView();
                if (tabFile.getToolTipTextAt(tabFile.getSelectedIndex())==null)
                {

                    fileChooserMain.setCurrentDirectory(new File(globalPreferences.getProjetPath()));
                    int status = fileChooserMain.showSaveDialog(JOptionPane.getFrameForComponent(saveFileAS));
                    if (status == JFileChooser.APPROVE_OPTION)
                    {

                        FileHandler file = new FileHandler(fileChooserMain.getSelectedFile());

                        file.writeFile((RSyntaxDocument)rSyntaxTextArea.getDocument());
                        filesArray.getFilesArray().add(fileChooserMain.getSelectedFile());
                        tabFile.setTitleAt(tabFile.getSelectedIndex(), fileChooserMain.getSelectedFile().getName());
                        tabFile.setToolTipTextAt(tabFile.getSelectedIndex(),fileChooserMain.getSelectedFile().getAbsolutePath());
                        filesArray.getFilesArray().add(fileChooserMain.getSelectedFile());
                    }
                }
                else {
                    FileHandler file = new FileHandler(new File(tabFile.getToolTipTextAt(tabFile.getSelectedIndex())));
                    file.writeFile((RSyntaxDocument)rSyntaxTextArea.getDocument());

                }
            }

            if(e.getSource() == about){
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(about), "HoneyCode est un projet étudiant développé au sein de l'ESGI, et est libre de droits.\n Développeurs :" +
                        "\n-Kevin MAAREK \n-Mathieu PEQUIN \n-Alexandre FAYETTE \n Promotion 3iAL ", "A propos", JOptionPane.INFORMATION_MESSAGE);

            }

            if(e.getSource() == open){
                int returnVal = fileChooserMain.showOpenDialog(JOptionPane.getFrameForComponent(open));

                if (returnVal == JFileChooser.APPROVE_OPTION){
                    File chosenFile = fileChooserMain.getSelectedFile();
                    FileHandler fileHandler = new FileHandler(chosenFile);
                    if (homeMessage.isShowing())
                    {
                        editorPanel.remove(homeMessage);

                        editorPanel.add(tabFile);
                        editorPanel.updateUI();
                    }

                    addCloseableTab(tabFile,new RTextScrollPane(new RSyntaxTextArea(fileHandler.readFile())), chosenFile.getName(),TAB_ICON);
                    tabFile.setToolTipTextAt(tabFile.getSelectedIndex(),chosenFile.getAbsolutePath());

                    if (!saveFileAS.isEnabled() && !saveFile.isEnabled())
                    {
                        saveFile.setEnabled(true);
                        saveFileAS.setEnabled(true);
                    }

                }
            }

            if(e.getSource() == exitApp){
                int confirm = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(exitApp), "Etes-vous sûr de vouloir quitter HoneyCode ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION){
                    /*
                    TODO :
                    Same as Window Closing
                     */
                    System.exit(0);
                }
            }

            if(e.getSource() == plugLoad){


                int returnVal = pluginChooser.showOpenDialog(JOptionPane.getFrameForComponent(plugLoad));

                if(returnVal == JFileChooser.APPROVE_OPTION){

                    File chosenPlugin = pluginChooser.getSelectedFile();
                }
            }
            if(e.getSource() == plugDown){
                //Throws an exception
                //Not the good catch, just to test
                try{
                    Desktop.getDesktop().browse(new URI("http://honeycode.kevinmaarek.fr/"));
                } catch (URISyntaxException | IOException ex){
                    JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(plugDown), ex.getMessage(),"Erreur", JOptionPane.ERROR_MESSAGE,null);
                }

            }

            if(e.getSource() == forum){
                //Not the good catch, just to test
                try{
                    Desktop.getDesktop().browse(new URI("http://honeycode.kevinmaarek.fr/"));
                } catch (URISyntaxException | IOException ex){
                    JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(forum), "Could not open HoneyCode forum Web Page", "Erreur", JOptionPane.ERROR_MESSAGE, null);
                }

            }

            if(e.getSource() == copy){
                RTextScrollPane RSrollPane = (RTextScrollPane)tabFile.getSelectedComponent();
                RSyntaxTextArea ed = (RSyntaxTextArea)RSrollPane.getViewport().getView();
                StringSelection selectedText = new StringSelection(ed.getSelectedText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selectedText,null);
            }
            if(e.getSource() == cut){
                RTextScrollPane RSrollPane = (RTextScrollPane)tabFile.getSelectedComponent();
                RSyntaxTextArea ed = (RSyntaxTextArea)RSrollPane.getViewport().getView();

                StringSelection selectedText = new StringSelection(ed.getSelectedText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selectedText,null);
                ed.replaceSelection("");
            }

            if (e.getSource() == past)
            {
                String result;
                RTextScrollPane RSrollPane = (RTextScrollPane)tabFile.getSelectedComponent();
                RSyntaxTextArea ed = (RSyntaxTextArea)RSrollPane.getViewport().getView();
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable contents = clipboard.getContents(null);
                boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
                if (hasTransferableText) {
                    try {
                        result = (String)contents.getTransferData(DataFlavor.stringFlavor);
                        ed.replaceSelection(result);
                    }
                    catch (UnsupportedFlavorException | IOException ex){
                        JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(past), ex);
                    }
                }
            }

            if (e.getSource() == settings){
                System.out.println("Settings");
            }

            if(e.getSource() == buildButton){
                if (tabFile.isShowing() && tabFile.getToolTipTextAt(tabFile.getSelectedIndex()).endsWith(".java"))
                {
                    CompileJavaFiles.createClassDir(new File(tabFile.getToolTipTextAt(tabFile.getSelectedIndex())).getParent());
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                    String dateString = dateFormat.format(date);
                    lastBuildLabel.setText("Last build : " + dateString);
                    File[] cc = {new File(tabFile.getToolTipTextAt(tabFile.getSelectedIndex()))};
                    System.out.flush();
                    CompileJavaFiles.doCompilation(cc,null);
                }
            }
        }
    }

}
