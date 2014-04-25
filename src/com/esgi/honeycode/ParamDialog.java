package com.esgi.honeycode;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Vector;

/**
 * Preferences Dialog Class
 *
 */
public class ParamDialog extends ModalDialog{

    private Component mainPane;
    public static final int OK = 1;
    public static final int CANCEL = 0;
    private final JFileChooser jdkChooser = new JFileChooser();
    private JTextField jdkChosen;
    private int cancelState = 0;
    protected JButton applyButton;
    protected JButton okButton;
    protected JButton cancelButton;
    protected JPanel buttonPane;
    private JPanel settingsPane;
    private JComboBox<String> themesListComboBox;
    private JComboBox<String> userLanguage;
    private JComboBox<String> userFont;
    private JSeparator sep;

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
        settingsPane = new JPanel();
        getSettings();
        sep = new JSeparator(JSeparator.HORIZONTAL);

        settingsPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        settingsPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3,3,3,3);
        settingsPane.add(new JLabel("Options générales"),gbc);
        gbc.gridy += 1;
        settingsPane.add(sep,gbc);
        gbc.gridy += 1;
        settingsPane.add(new JLabel("Langue : "), gbc); // A translate
        gbc.gridx += 1;
        settingsPane.add(userLanguage, gbc);
        gbc.gridy += 1;
        gbc.gridx = 0;
        settingsPane.add(new JLabel("JDK : "), gbc);
        gbc.gridx += 1;
        jdkChosen = new JTextField();
        jdkChosen.setText(MainWindowUI.globalPreferences.getJavaDev());
        jdkChosen.setEditable(false);
        jdkChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jdkChooser.setCurrentDirectory(new File(MainWindowUI.globalPreferences.getJavaDev()));
        jdkChosen.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int ret = jdkChooser.showOpenDialog(JOptionPane.getFrameForComponent(jdkChosen));
                if (ret == JFileChooser.APPROVE_OPTION)
                {
                    File jdk = jdkChooser.getSelectedFile();
                    jdkChosen.setText(jdk.getAbsolutePath());
                }

            }
        });
        settingsPane.add(jdkChosen, gbc);

        gbc.gridy += 1;
        gbc.gridx = 0;
        settingsPane.add(sep, gbc);
        gbc.gridy += 1;
        settingsPane.add(new JLabel("Edition"),gbc);
        gbc.gridy += 1;
        settingsPane.add(new JLabel("Thème :"),gbc);
        gbc.gridx +=1;
        settingsPane.add(themesListComboBox, gbc);
        gbc.gridx = 0;
        gbc.gridy +=1;
        settingsPane.add(new JLabel("Police de caractères :"),gbc);
        gbc.gridx += 1;
        settingsPane.add(userFont, gbc);


        c.add(settingsPane, BorderLayout.CENTER);
    }

    private void getSettings() {
        String[] languages = {"fr", "en"};
        userLanguage = new JComboBox<>(languages);
        if (MainWindowUI.globalPreferences.getUserLanguageReg().equals(languages[0]))
        {
            userLanguage.setSelectedIndex(0);
        }
        else if (MainWindowUI.globalPreferences.getUserLanguageReg().equals(languages[1]))
        {
            userLanguage.setSelectedIndex(1);
        }
        else
        {
            userLanguage.setSelectedIndex(1);
        }


        Vector<String> themes = new Vector<>(6);
        themes.addAll(Arrays.asList("dark","default","default-alt","eclipse","idea","vs"));


        //Doesn't work if in jar, so for now...
        /*File f = new File(getClass().getResource("/themes").getFile());

        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith("xml");
            }
        };

        for (File file : f.listFiles(filter)) {
            themes.add(file.getName().substring(0, file.getName().indexOf(".")));
        }*/

        themesListComboBox = new JComboBox<>(themes);
        int j;
        for (j = 0;j<themes.size();j++)
        {
            if (themes.elementAt(j).equals(MainWindowUI.globalPreferences.getTheme()))
            {
                break;
            }
        }

        themesListComboBox.setSelectedIndex(j);

        Vector<String> fonts = new Vector<>();
        fonts.add(RSyntaxTextArea.getDefaultFont().getName());
        fonts.add(new Font("Courier New", Font.PLAIN, RSyntaxTextArea.getDefaultFont().getSize()).getName());
        userFont = new JComboBox<>(fonts);
        int i;
        for (i = 0;i<fonts.size()-1;++i)
        {
            if (fonts.elementAt(i).equals(MainWindowUI.globalPreferences.getFont()))
            {
                break;
            }
        }

        userFont.setSelectedIndex(i);
    }


    public void ok()
    {
        cancelState = OK;
        apply();
        close();
    }

    public void apply()
    {
        if (!MainWindowUI.globalPreferences.getFont().equals(userFont.getSelectedItem()))
        {
            MainWindowUI.globalPreferences.setFont((String)userFont.getSelectedItem());
            MainWindowUI.globalPreferences.setFontChanged(1);
        }

        if (!MainWindowUI.globalPreferences.getUserLanguageReg().equals(userLanguage.getSelectedItem()))
        {
            MainWindowUI.globalPreferences.setLangDef((String)userLanguage.getSelectedItem());
            MainWindowUI.globalPreferences.setStateChange(1);
        }

        if (!MainWindowUI.globalPreferences.getTheme().equals(themesListComboBox.getSelectedItem()))
        {
            MainWindowUI.globalPreferences.setTheme((String)themesListComboBox.getSelectedItem());
            MainWindowUI.globalPreferences.setThemeChanged(1);
        }
        MainWindowUI.globalPreferences.setJavaDev(jdkChosen.getText());
        System.setProperty("java.home", jdkChosen.getText()+PropertiesShared.SEPARATOR+"jre");
        String[] lib = System.getProperty("java.library.path").split(";");
        lib[0] = jdkChosen.getText()+PropertiesShared.SEPARATOR+"bin";
        String l = Arrays.toString(lib);
        System.setProperty("java.library.path",l );
    }

    public void cancel()
    {
        cancelState = CANCEL;
        close();
    }

    public void close()
    {
        dispose();
    }



    protected class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton)e.getSource();
            if (button == applyButton)
            {
                apply();
            }
            else if (button == okButton)
            {
               ok();
            }

            else if (button == cancelButton)
            {
                cancel();
            }
        }
    }

}
