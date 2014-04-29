import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Alex on 25/04/14.
 */
public class Plugmessage {

    private static JMenu newMenu;
    private static JMenuItem newItem;

    public static void hc_Plugin_MenuBar(JMenuBar bar){

        ActionListen act = new ActionListen();
        newMenu = new JMenu("Plugin1");
        newItem = new JMenuItem("Afficher message");

        bar.add(newMenu);
        newMenu.add(newItem);
        newItem.addActionListener(act);
        bar.revalidate();
    }

    public static void hc_Plugin_Editor(JTabbedPane editor){
        //Modification de l'éditeur, actions getComponent() possibles
    }

    public static void hc_Plugin_Console(JPanel console){
        //Modification de la console, actions getComponent() possibles
    }

    private static class ActionListen implements ActionListener {
        public void actionPerformed (ActionEvent e){
            if(e.getSource() == newItem){
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(newItem), "Message plugin", "A propos", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
