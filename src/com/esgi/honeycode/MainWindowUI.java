package com.esgi.honeycode;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;
import sun.tools.jar.resources.jar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.channels.FileChannel;
import java.util.Locale;
import java.util.ResourceBundle;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;


/**
 *
 * Crée la fenêtre pricipale d'HoneyCode avec ses composants
 * Les éléments de la fenêtre sont adaptés à la langue par défaut de l'utilisateur
 */

public class MainWindowUI extends JFrame{

    //Déclarations des différents composants et ressources

    private ResourceBundle bundle;

    //getResource uses "/", it's not using specific-platform separators
    private static final Icon CLOSE_TAB_ICON = new ImageIcon(MainWindowUI.class.getResource("/icons/Cross_close_tab_button.png"));
    private static final Icon CLOSE_TAB_ICON_DISABLED = new ImageIcon(MainWindowUI.class.getResource("/icons/Cross_close_tab_button_disabled.png"));
    private static final Icon TAB_ICON = new ImageIcon(MainWindowUI.class.getResource("/icons/Icon_page_code.gif"));

    private static final Image MAIN_IMAGE = new ImageIcon(MainWindowUI.class.getResource("/icons/logo.png")).getImage();
    private JPanel treePanel;
    private static JTabbedPane tabFile;
    private TreeFileExplorer treeMain;
    private JScrollPane scrollTree;
    private JScrollPane consoleSroll;
    private JSplitPane splited;
    private JSplitPane wholeSplit;
    private JPanel editorPanel;
    private JPanel mainPanel;
    private JPanel consolePane;
    private JPanel subConsolePane;
    private JLabel lastBuildLabel;
    private JLabel explorerLabel;
    private JButton buildButton;
    private JButton runButton;
    private JButton runOptionsButton;
    private InputStream inDef;
    private PrintStream outDef;
    private JMenuBar menuBarMain;
    private JMenu file;
    private JMenu edit;
    private JMenu view;
    private JMenu plugin;
    private JMenu help;
    private JMenuItem newProject;
    private JMenuItem openProject;
    private JMenuItem newFile;
    private JMenuItem open;
    private static JMenuItem saveFile;
    private JMenuItem saveFileAS;
    private JMenuItem settings;
    private JMenuItem exitApp;
    private JMenuItem copy;
    private JMenuItem cut;
    private JMenuItem past;
    private JCheckBoxMenuItem consoleView;
    private JMenuItem plugLoad;
    private JMenuItem plugDown;
    private JMenuItem plugSubmit;
    private JMenuItem about;
    private JMenuItem docHC;
    private JMenuItem checkUpdate;
    private String exitMessage;
    final JFileChooser fileChooserMain;
    final JFileChooser pluginChooser;
    private JTextArea homeMessage;
    private int newFileNumber;
    private static int shortcutKey;
    private ProjectMaker project;
    private JTextArea consoleOutputArea;

    protected static HCPreferences globalPreferences;

    private CustomConsoleOutputStream out;

    public MainWindowUI(){
        /**
         * Initialise l'interface graphique et tous ses composants
         */

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
        newProject = new JMenuItem();
        openProject = new JMenuItem();
        newFile = new JMenuItem();
        open = new JMenuItem();
        saveFile = new JMenuItem();
        saveFileAS = new JMenuItem();
        settings = new JMenuItem();
        exitApp = new JMenuItem();
        copy = new JMenuItem();
        cut = new JMenuItem();
        past = new JMenuItem();
        consoleView = new JCheckBoxMenuItem();
        plugLoad = new JMenuItem();
        plugDown = new JMenuItem();
        plugSubmit = new JMenuItem();
        about = new JMenuItem();
        docHC = new JMenuItem();
        checkUpdate = new JMenuItem();
        runButton = new JButton();
        buildButton = new JButton();
        runOptionsButton = new JButton();
        consoleOutputArea = new JTextArea();
        out = new CustomConsoleOutputStream(consoleOutputArea);
        outDef = System.out;

        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(out));

        fileChooserMain = new JFileChooser();
        pluginChooser = new JFileChooser();
        lastBuildLabel = new JLabel("Last build : none");
        explorerLabel = new JLabel();

        fileChooserMain.setFileHidingEnabled(false);
        fileChooserMain.setAcceptAllFileFilterUsed(false);
        fileChooserMain.setFileHidingEnabled(false);
        fileChooserMain.addChoosableFileFilter(new FileNameExtensionFilter("Project file", "dat"));
        fileChooserMain.addChoosableFileFilter(new FileNameExtensionFilter("Java sources", "java"));
        fileChooserMain.addChoosableFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        pluginChooser.setAcceptAllFileFilterUsed(false);
        pluginChooser.addChoosableFileFilter(new FileNameExtensionFilter("Jar files", "jar"));


        newFileNumber = 0;

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
        final int taskBarSize = scnMax.bottom;

        //Récupération de la touche utilisée pour les raccourcis clavier du système
        shortcutKey = tkMain.getMenuShortcutKeyMask();

        setUILanguage();

        treePanel = new JPanel();

        treeMain = new TreeFileExplorer();

        scrollTree = new JScrollPane(treeMain);
        explorerLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        scrollTree.setColumnHeaderView(explorerLabel);

        homeMessage.setFont(new Font("Courier new", Font.PLAIN, 24));
        editorPanel.add(homeMessage);


        //Qu'on me redonne la définition de vertical et horizontal
        splited = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,treePanel,editorPanel);
        splited.setDividerSize(2);
        wholeSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splited,consolePane);
        wholeSplit.setDividerSize(3);
        wholeSplit.setDividerLocation(wholeSplit.getInsets().top + 650);

        ActionListenerMenuBar test = new ActionListenerMenuBar();
        newProject.addActionListener(test);
        openProject.addActionListener(test);
        newFile.addActionListener(test);
        about.addActionListener(test);
        open.addActionListener(test);
        exitApp.addActionListener(test);
        plugLoad.addActionListener(test);
        plugDown.addActionListener(test);
        copy.addActionListener(test);
        cut.addActionListener(test);
        saveFile.addActionListener(test);
        saveFileAS.addActionListener(test);
        settings.addActionListener(test);
        runButton.addActionListener(test);
        runOptionsButton.addActionListener(test);
        buildButton.addActionListener(test);
        past.addActionListener(test);

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
        file.add(newProject);
        file.add(openProject);
        file.add(newFile);
        file.add(open);
        file.add(saveFile);
        file.add(saveFileAS);
        file.add(settings);
        file.add(exitApp);
        menuBarMain.add(edit);
        edit.add(copy);
        edit.add(cut);
        edit.add(past);
        menuBarMain.add(view);
        view.add(consoleView);
        menuBarMain.add(plugin);
        plugin.add(plugLoad);
        plugin.add(plugDown);
        plugin.add(plugSubmit);
        menuBarMain.add(help);
        help.add(about);
        help.add(docHC);
        help.add(checkUpdate);

        cut.setEnabled(false);
        past.setEnabled(false);
        copy.setEnabled(false);
        saveFile.setEnabled(false);
        saveFileAS.setEnabled(false);
        setJMenuBar(menuBarMain);
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        consoleOutputArea.setEditable(false);
        consoleSroll = new JScrollPane(consoleOutputArea);
        JTextField textField = new JTextField();

        final TextFieldStreamer fieldStreamer = new TextFieldStreamer(textField);
        textField.addActionListener(fieldStreamer);
        textField.setToolTipText("Console input");

        inDef = System.in;

        System.setIn(fieldStreamer);

        consoleView.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
                    System.setIn(inDef);
                } else {
                    System.setOut(new PrintStream(out));
                    System.setIn(fieldStreamer);
                }


            }
        });
        consolePane.add(textField, BorderLayout.SOUTH);
        consolePane.add(subConsolePane, BorderLayout.NORTH);
        consolePane.add(consoleSroll, BorderLayout.CENTER);

        subConsolePane.add(lastBuildLabel);
        subConsolePane.add(runOptionsButton);
        subConsolePane.add(buildButton);
        subConsolePane.add(runButton);
        consoleOutputArea.setBackground(Color.DARK_GRAY);
       consoleOutputArea.setForeground(Color.WHITE);


        treePanel.add(scrollTree, BorderLayout.CENTER);
        mainPanel.add(wholeSplit, BorderLayout.CENTER);

        tabFile.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabFile.getTabCount()==0)
                {
                    editorPanel.remove(tabFile);
                    editorPanel.add(homeMessage);
                    saveFile.setEnabled(false);
                    saveFileAS.setEnabled(false);
                    cut.setEnabled(false);
                    copy.setEnabled(false);
                    past.setEnabled(false);
                    editorPanel.repaint();
                }
                else if (tabFile.getTabCount()>0)
                {
                    if (!cut.isEnabled() && !past.isEnabled() && !copy.isEnabled() && !saveFileAS.isEnabled() && !saveFile.isEnabled())
                    {
                        saveFile.setEnabled(true);
                        saveFileAS.setEnabled(true);
                        cut.setEnabled(true);
                        copy.setEnabled(true);
                        past.setEnabled(true);
                        editorPanel.remove(homeMessage);
                        editorPanel.add(tabFile);
                        editorPanel.repaint();
                    }

                }
            }
        });

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
                /**
                 * Fonction de fermeture de la fenêtre, via le bouton de base
                 */
                int confirm = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(exitApp), exitMessage, "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (tabFile.getTabCount()>0)
                    {
                        tabFile.setSelectedIndex(0);
                        while (tabFile.getTabCount()>0)
                        {
                            ((JButton)((JPanel)tabFile.getTabComponentAt(0)).getComponent(1)).doClick();
                        }
                    }
                    System.exit(0);
                }
            }
        });

        //Load des plugins
        loadPlugins();

    }

    private void setUILanguage()
    {
        /**
         * Définit le langage de l'interface et initialise les composants textuels dans la langue choisie/par défaut,
         * grâce aux fichiers de propriétés du Resource Bundle
         */
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

        newProject.setText(bundle.getString("newProject"));
        openProject.setText(bundle.getString("openProject"));
        newFile.setText(bundle.getString("newFile"));
        open.setText(bundle.getString("open"));
        saveFile.setText(bundle.getString("saveFile"));
        saveFileAS.setText(bundle.getString("saveFileAS"));
        settings.setText(bundle.getString("settings"));
        exitApp.setText(bundle.getString("exitApp"));
        copy.setText(bundle.getString("copy"));
        cut.setText(bundle.getString("cut"));
        past.setText(bundle.getString("past"));
        consoleView.setText(bundle.getString("consoleView"));
        plugLoad.setText(bundle.getString("plugLoad"));
        plugDown.setText(bundle.getString("plugDown"));
        plugSubmit.setText(bundle.getString("plugSubmit"));
        about.setText(bundle.getString("about"));
        docHC.setText(bundle.getString("docHC"));
        checkUpdate.setText(bundle.getString("checkUpdate"));
        runButton.setText(bundle.getString("runButton"));
        buildButton.setText(bundle.getString("buidButton"));
        runOptionsButton.setText(bundle.getString("runOptionsButton"));
        consoleOutputArea.setText(bundle.getString("consoleOutputArea"));
        exitMessage = bundle.getString("exitMessage");
        homeMessage.setText(bundle.getString("homeMessage"));
        explorerLabel.setText(bundle.getString("explorerLabel"));


        //...Suite des traductions...

    }

    protected static void setNewTabTextRenamedFile(String oldTitle, String newTitle, String tooltip)
    {
        /**
         * Renomme un TabbedText avec le titre choisi
         * @param oldTitle Ancien titre du TabbedText
         * @param newTitle Nouveau titre
         * @param tooltip chemin absolu du fichier édité dans le TabbedText
         */
        for (int i = 0; i<tabFile.getTabCount();i++ )
        {
            if (oldTitle.equals(((JLabel)((JPanel)tabFile.getTabComponentAt(tabFile.getSelectedIndex())).getComponent(0)).getText()))
            {
                ((JLabel)((JPanel)tabFile.getTabComponentAt(i)).getComponent(0)).setText(newTitle);
                tabFile.setToolTipTextAt(i,tooltip);
                break;
            }
        }
    }

    protected static void setNewTabText(String title, String tooltip)
    {
        /**
         * Cree un nouveau TabbedText avec le titre choisi
         * @param title Titre du nouveau fichier choisi
         *@param tootip Chemin absolu du fichier
         */

        ((JLabel)((JPanel)tabFile.getTabComponentAt(tabFile.getSelectedIndex())).getComponent(0)).setText(title);
        tabFile.setToolTipTextAt(tabFile.getSelectedIndex(), tooltip);
    }

    private void changeFontCurrentTab()
    {
        /**
         * Change la police actuelle de l'éditeur
         */
        for(int i = 0;i<tabFile.getTabCount();i++)
        {
            ((RTextScrollPane)tabFile.getComponentAt(i)).getTextArea().setFont(new Font(globalPreferences.getFont(),Font.PLAIN,13));
        }
    }

    private void changeThemeCurrentTab()
    {
        /**
         * Change le theme actuel de l'editeur
         */
        try {
            //Themes will be modifiable in settings
            Theme theme = Theme.load(MainWindowUI.class.getResourceAsStream("/themes/"+globalPreferences.getTheme()+".xml"));
            for(int i = 0;i<tabFile.getTabCount();i++)
            {
                theme.apply((RSyntaxTextArea) ((RTextScrollPane) tabFile.getComponentAt(i)).getTextArea());
            }
        }
        catch (IOException ex){
            JOptionPane.showMessageDialog(null, "error while loading theme");
        }

    }

    protected static void addCloseableTab(final RTextScrollPane c, final String title, final String tooltip)
    {
        /**
         * Ajoute un onglet fermable d'edition a la barre d'onglets
         * @param c
         * RTextScrollPane courant, sert au lien
         * @param title
         * Titre de l'onglet
         * @param tooltip
         * Chemin absolu du fichier correspondant
         *
         */
        try {
            //Themes will be modifiable in settings
            Theme theme = Theme.load(MainWindowUI.class.getResourceAsStream("/themes/"+globalPreferences.getTheme()+".xml"));
            theme.apply((RSyntaxTextArea)c.getTextArea());

        }
        catch (IOException ex){
            JOptionPane.showMessageDialog(null, "error while loading theme");
        }



        c.getTextArea().setFont(new Font(globalPreferences.getFont(), Font.PLAIN, 13));

        CustomCompletionProvider p = new CustomCompletionProvider("java"); //Will be replaced
        CompletionProvider provider = p.getProvider(); //An unique CompletionProvider can be used for all RSyntaxArea but not AutoCompletion
        AutoCompletion ac = new AutoCompletion(provider);
        ac.setAutoCompleteEnabled(true);
        ac.setAutoActivationEnabled(true);
        ac.install((RSyntaxTextArea) c.getTextArea());
        ((RSyntaxDocument)((RSyntaxTextArea)c.getTextArea()).getDocument()).putProperty("stateChange", 0);
        ((RSyntaxDocument)((RSyntaxTextArea)c.getTextArea()).getDocument()).addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

                if (e.getDocument().getProperty("stateChange") == 0) {
                    e.getDocument().putProperty("stateChange", 1);
                }
            }
        });

        final int tabCount = tabFile.getTabCount();
        boolean showingTab = false;
        for (int x = 0; x<tabCount;x++)
        {
            if (tabFile.getTabComponentAt(x).isDisplayable() && tabFile.getToolTipTextAt(x).equals(tooltip))
            {
                showingTab = true;
            }
        }

        if (!showingTab)
        {
            tabFile.addTab(null, c);
            int pos = tabFile.indexOfComponent(c);

            //Create a FlowLayout taht will space things 5px apart
            FlowLayout f = new FlowLayout(FlowLayout.CENTER, 5,0);

            //Make a small Jpanel with the layout and make it non-opaque
            final JPanel panelTab = new JPanel(f);
            panelTab.setOpaque(false);

            //Add a Jlabel with title and the left side tab icon
            JLabel labelTitle = new JLabel(title);
            labelTitle.setIcon(TAB_ICON);

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
            tabFile.setTabComponentAt(pos, panelTab);
            tabFile.setToolTipTextAt(pos,tooltip);

          /*  tabFile.getTabComponentAt(pos).addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (tabFile.getToolTipTextAt(tabFile.getSelectedIndex())!=null)
                    {

                        StringBuffer title = new StringBuffer(JOptionPane.getFrameForComponent(tabFile).getTitle());
                        try {
                            title.insert(title.indexOf("HoneyCode"), new String(new File(tabFile.getToolTipTextAt(tabFile.getSelectedIndex())).getCanonicalPath()));
                        }catch (IOException ex)
                        {

                        }

                        System.out.println(title);
                    }


                }
            });*/
            //Add the listener that removes the tab
            buttonClose.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (((RSyntaxDocument)((RSyntaxTextArea)c.getTextArea()).getDocument()).getProperty("stateChange")==1)
                    {
                        int res = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(tabFile),"Voulez vous enregistrez les modifications ?", "Enregistrer les modifications ?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (res == JOptionPane.YES_OPTION)
                        {
                            saveFile.doClick();
                        }
                    }

                    tabFile.remove(c);
                }
            });

            tabFile.setSelectedComponent(c);

            AbstractAction closeTabAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tabFile.remove(c);
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

    }

    private void loadPlugins(){
        /**
         * Charge tous les plugins .jar du dossier "plugins" et invoque les 3 premières méthodes de chacun,
         * en leur transmettant respectivement la JMenuBar, le JTabbedPane et le JPanel de la console
         * (modèle de développement souhaité)
         */
        File pluginDirectory = new File(System.getProperty("user.home")+PropertiesShared.SEPARATOR+"HoneyPlugins");
        boolean created = pluginDirectory.mkdir();
        if (!created &&  !pluginDirectory.exists())
        {
            JOptionPane.showMessageDialog(null, "Impossible de créer le répertoire de plugins : "+ pluginDirectory);
        }
        else{
            File[] filelist = pluginDirectory.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    if (file.getName().endsWith(".jar")) {
                        return true;
                    }
                    return false;
                }
            });
            if(filelist.length>0){
                try{
                    for (File f : filelist){

                        JarInputStream jarFile = new JarInputStream(new FileInputStream(f));
                        JarEntry jarElement;
                        String classname="";
                        do{
                            jarElement = jarFile.getNextJarEntry();
                            if (jarElement != null && !jarElement.getName().contains("$") && jarElement.getName().contains(".class")){
                                classname = jarElement.getName().replace(".class", "");
                            }
                        }while(jarElement != null);
                        try{
                            URL[] urls = new URL[] { f.toURI().toURL() };
                            ClassLoader loader = new URLClassLoader(urls);
                            Class<?> c = loader.loadClass(classname);

                            Method[] methods = c.getMethods();
                            methods[0].invoke(c, getJMenuBar());
                            methods[1].invoke(c, tabFile);
                            methods[2].invoke(c, consolePane);
                            /*
                            Method menuModifier = c.getMethod("hc_Plugin_MenuBar", JMenuBar.class);
                            menuModifier.invoke(c, getMenuBar());
                            Method editorModifier = c.getMethod("hc_Plugin_Editor", JTabbedPane.class);
                            editorModifier.invoke(c, tabFile);
                            Method consoleModifier = c.getMethod("hc_Plugin_Console", JPanel.class);
                            consoleModifier.invoke(c, consolePane);
                            */

                        }
                        catch(MalformedURLException | ClassNotFoundException | IllegalAccessException | InvocationTargetException /*| NoSuchMethodException*/  ex){
                            ex.printStackTrace();
                        }
                        jarFile.close();
                    }

                }
                catch(IOException e){
                   e.printStackTrace();
                }
            }
        }
    }

    private class ActionListenerMenuBar implements ActionListener {
        public void actionPerformed (ActionEvent e){
            /**
             * Fonction principale de déclenchement d'évenement
             * @param e
             * Servira, utilisé comme e.getSource(), à localiser l'endroit du clic, et à effectuer les actions voulues
             */

            if (e.getSource() == newProject)
            {
                ProjectTypeCustomDialog projectFrame = new ProjectTypeCustomDialog();
                projectFrame.setModal(true);
                projectFrame.setLocationRelativeTo(JOptionPane.getFrameForComponent(newProject));
                projectFrame.setVisible(true);

                if (projectFrame.isFinished())
                {
                    project = new ProjectMaker(projectFrame.getProjectName(), projectFrame.getLanguage());
                    project.makeProjectStructure();
                    project.serializeProjectSettings();
                    project.serializeProjectFiles();
                    treeMain.init(new File(globalPreferences.getProjetPath()+PropertiesShared.SEPARATOR+projectFrame.getProjectName()));
                    setTitle(project.getProjectName()+" - ["+project.getProjectPath()+"] - "+ "HoneyCode");


                }

            }

            if (e.getSource() == openProject) {
                int returnVal = fileChooserMain.showOpenDialog(JOptionPane.getFrameForComponent(openProject));

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File chosenFile = fileChooserMain.getSelectedFile();
                    //If there is already a project running then the Jtree will be reloaded and tabs will be closed
                    if (ProjectMaker.class.isInstance(project))
                    {
                        if (tabFile.getTabCount()>0)
                        {
                            tabFile.removeAll();
                        }

                    }
                    project = new ProjectMaker(chosenFile);
                    treeMain.init(project.getProjectFiles().getProjectPath());
                    setTitle(project.getProjectName()+" - ["+project.getProjectPath()+"] - "+ "HoneyCode");
                }
            }
            if(e.getSource() == newFile){
                newFileNumber += 1;

                addCloseableTab(new RTextScrollPane(new RSyntaxTextArea()),"new "+newFileNumber,"new "+newFileNumber);

            }
            if (e.getSource() == saveFileAS)
            {
                fileChooserMain.setCurrentDirectory(new File(globalPreferences.getProjetPath()));
                int status = fileChooserMain.showSaveDialog(JOptionPane.getFrameForComponent(saveFileAS));
                if (status == JFileChooser.APPROVE_OPTION)
                {
                    FileHandler file = new FileHandler(fileChooserMain.getSelectedFile());
                    RTextScrollPane rTextScrollPane = (RTextScrollPane)tabFile.getSelectedComponent();
                    RSyntaxTextArea rSyntaxTextArea = (RSyntaxTextArea)rTextScrollPane.getTextArea();
                    file.writeFile((RSyntaxDocument)rSyntaxTextArea.getDocument());
                    setNewTabText(fileChooserMain.getSelectedFile().getName(), fileChooserMain.getSelectedFile().getAbsolutePath());
                }
            }

            if (e.getSource() == saveFile)
            {
                RTextScrollPane rTextScrollPane = (RTextScrollPane)tabFile.getSelectedComponent();
                RSyntaxTextArea rSyntaxTextArea = (RSyntaxTextArea)rTextScrollPane.getTextArea();
                File file1 = new File(tabFile.getToolTipTextAt(tabFile.getSelectedIndex()));
                if (!file1.exists())
                {
                    fileChooserMain.setCurrentDirectory(new File(globalPreferences.getProjetPath()));

                    int status = fileChooserMain.showSaveDialog(JOptionPane.getFrameForComponent(saveFileAS));
                    if (status == JFileChooser.APPROVE_OPTION)
                    {
                        FileHandler file = new FileHandler(fileChooserMain.getSelectedFile());
                        file.writeFile((RSyntaxDocument)rSyntaxTextArea.getDocument());
                        setNewTabText(fileChooserMain.getSelectedFile().getName(),fileChooserMain.getSelectedFile().getAbsolutePath());
                    }
                }
                else {
                    FileHandler file = new FileHandler(new File(tabFile.getToolTipTextAt(tabFile.getSelectedIndex())));
                    file.writeFile((RSyntaxDocument)rSyntaxTextArea.getDocument());
                }
            }

            if(e.getSource() == about){
                 JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(about), "HoneyCode est un projet étudiant développé dans le cadre de l'ESGI" +
                        "\n PEQUIN Mathieu \n MAAREK Kevin \n FAYETTE Alexandre", "A propos", JOptionPane.INFORMATION_MESSAGE);
            }
            if(e.getSource() == open){
                int returnVal = fileChooserMain.showOpenDialog(JOptionPane.getFrameForComponent(open));

                if (returnVal == JFileChooser.APPROVE_OPTION){
                    File chosenFile = fileChooserMain.getSelectedFile();
                    FileHandler fileHandler = new FileHandler(chosenFile);
                   addCloseableTab(new RTextScrollPane(new RSyntaxTextArea(fileHandler.readFile())), chosenFile.getName(), chosenFile.getAbsolutePath());
                    tabFile.setToolTipTextAt(tabFile.getSelectedIndex(),chosenFile.getAbsolutePath());
                }
            }
            if(e.getSource() == exitApp){
                int confirm = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(exitApp), "Etes-vous sûr de vouloir quitter HoneyCode ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION){
                    if (tabFile.getTabCount()>0)
                    {
                        tabFile.setSelectedIndex(0);
                        while (tabFile.getTabCount()>0)
                        {
                            ((JButton)((JPanel)tabFile.getTabComponentAt(0)).getComponent(1)).doClick();
                        }
                    }
                    System.exit(0);
                }
            }

            if(e.getSource() == plugLoad){

                int returnVal = pluginChooser.showOpenDialog(JOptionPane.getFrameForComponent(plugLoad));

                if(returnVal == JFileChooser.APPROVE_OPTION){
                    File pluginDirectory = new File(System.getProperty("user.home")+PropertiesShared.SEPARATOR+"HoneyPlugins");
                    boolean created = pluginDirectory.mkdir();
                    if (!created &&  !pluginDirectory.exists())
                    {
                        JOptionPane.showMessageDialog(null, "Impossible de créer le répertoire de plugins : "+ pluginDirectory);
                    }
                    else{
                        File chosenPlugin = pluginChooser.getSelectedFile();
                        FileChannel in = null; // canal d'entrée
                        FileChannel out = null; // canal de sortie

                        try {
                            in = new FileInputStream(chosenPlugin).getChannel();
                            out = new FileOutputStream(System.getProperty("user.home")+PropertiesShared.SEPARATOR+"HoneyPlugins"+
                                    PropertiesShared.SEPARATOR+chosenPlugin.getName()).getChannel();
                            in.transferTo(0, in.size(), out);
                            JOptionPane.showMessageDialog(null, "Le plugin a été copié dans le dossier plugin de l'application, veuillez relancer l'application pour le charger");
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        finally {
                            if(in != null) {
                                try {
                                    in.close();
                                }
                                catch(IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            if(out != null) {
                                try {
                                    out.close();
                                }
                                catch(IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            if(e.getSource() == plugDown){
                //Throws an exception
                //Not the good catch, just to test
                try{
                    Desktop.getDesktop().browse(new URI("http://honeycode.kevinmaarek.fr/pages/plugins/"));
                } catch (URISyntaxException | IOException ex){
                    JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(plugDown), ex.getMessage(),"Erreur", JOptionPane.ERROR_MESSAGE,null);
                }

            }

            if(e.getSource() == copy){
                RTextScrollPane RSrollPane = (RTextScrollPane)tabFile.getSelectedComponent();
                RSyntaxTextArea ed = (RSyntaxTextArea)RSrollPane.getTextArea();
                StringSelection selectedText = new StringSelection(ed.getSelectedText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selectedText,null);
            }
            if(e.getSource() == cut){
                RTextScrollPane RSrollPane = (RTextScrollPane)tabFile.getSelectedComponent();
                RSyntaxTextArea ed = (RSyntaxTextArea)RSrollPane.getTextArea();

                StringSelection selectedText = new StringSelection(ed.getSelectedText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selectedText,null);
                ed.replaceSelection("");
            }

            if (e.getSource() == past)
            {
                String result;
                RTextScrollPane RSrollPane = (RTextScrollPane)tabFile.getSelectedComponent();
                RSyntaxTextArea ed = (RSyntaxTextArea)RSrollPane.getTextArea();
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
                ParamDialog paramDialog = new ParamDialog(JOptionPane.getFrameForComponent(settings));
                paramDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                paramDialog.setModal(true);
                paramDialog.setLocationRelativeTo(getFocusOwner());
                paramDialog.setResizable(false);
                paramDialog.pack();
                paramDialog.setVisible(true);

                if (globalPreferences.getThemeChanged() == 1)
                {
                    if (tabFile.getTabCount()>0)
                    {
                        changeThemeCurrentTab();
                    }
                    globalPreferences.setThemeChanged(0);
                }

                if (globalPreferences.getFontChanged() == 1)
                {
                    if (tabFile.getTabCount()>0)
                    {
                        changeFontCurrentTab();
                    }
                    globalPreferences.setFontChanged(0);
                }

                if (globalPreferences.getStateChange() == 1){
                    globalPreferences.setStateChange(0);
                    dispose();
                    pack();
                    setUILanguage();
                    setVisible(true);

                }

            }

            if (e.getSource() == runButton)
            {
                if (tabFile.isVisible() && tabFile.isShowing() && tabFile.isFocusable() && project instanceof ProjectMaker)
                {
                    String askedClassToRun = JOptionPane.showInputDialog(JOptionPane.getFrameForComponent(runButton),"Nom de la classe à exécuter\nUsage :\nMyNewClass \nor\n com.program.test\n","Classe à executer",JOptionPane.QUESTION_MESSAGE);
                    if (askedClassToRun!=null)
                    {
                        try{
                            CustomRun.run(askedClassToRun, "arg_test", project.getProjectPath().getAbsolutePath());
                        }catch (IOException ex)
                        {
                            System.out.println("error running file\n");
                        }
                    }

                }
            }

            if(e.getSource() == runOptionsButton){
                if (tabFile.isVisible() && tabFile.isShowing() && tabFile.isFocusable() && project instanceof ProjectMaker){
                    String inputRunOptions = JOptionPane.showInputDialog(JOptionPane.getFrameForComponent(runOptionsButton),"Entrez les options d'éxécution","args:",JOptionPane.QUESTION_MESSAGE);
                    if (inputRunOptions!=null){
                        System.out.println(inputRunOptions);
                    }

                }
            }

            if(e.getSource() == buildButton){
            if (project instanceof ProjectMaker)
            {
                if (tabFile.isShowing() && project.getProjectType().equals("Java project"))
                {

                        boolean compiled = CompileJavaFiles.doCompilation(project.getProjectPath().getAbsolutePath()+PropertiesShared.SEPARATOR+"src", project.getProjectPath().getAbsolutePath());
                        if (compiled)
                        {
                            Date date = new Date();
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                            String dateString = dateFormat.format(date);
                            lastBuildLabel.setText("Last build : " + dateString);
                        }
                    }


            }
            }
        }
    }

}
