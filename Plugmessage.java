import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Alex on 25/04/14.
 */
public class Plugmessage {

    private static JMenu newMenu;
    private static JMenuItem newItem;

    public static void run(JMenuBar bar){

        ActionListen act = new ActionListen();
        newMenu = new JMenu("Importation");
        newItem = new JMenuItem("yoloswag");

        bar.add(newMenu);
        newMenu.add(newItem);
        newItem.addActionListener(act);
        bar.revalidate();
    }

    public static void message(int i){
            System.out.println("YOLOOOOOOOOOO"+i);
    }

    private static class ActionListen implements ActionListener {
        public void actionPerformed (ActionEvent e){
            if(e.getSource() == newItem){
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(newItem), "Message plugin", "A propos", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
