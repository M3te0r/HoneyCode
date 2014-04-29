package com.esgi.honeycode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Classe étendant JDialog et implémentant ComponentListener et WindoWListener
 */
public class ModalDialog extends JDialog implements ComponentListener, WindowListener {

    private static final long serialVersionUID = 1L;
    private Component mOwner;
    private boolean mParentOnly = false;
    private static final Image MAIN_IMAGE = new ImageIcon(ModalDialog.class.getResource("/icons/logo.png")).getImage();

    /**
     * Constructeur par défaut
     * @throws HeadlessException
     */
    public ModalDialog() throws HeadlessException
    {
        super();
        init(null);
    }

    /**
     * Constructeur sur la Frame
     * @param a Frame
     * @throws HeadlessException
     */
    public ModalDialog(Frame a) throws HeadlessException
    {
        super(a, "Configuration");
        init(a);

    }

    /**
     * Initialisation de la fenêtre et de ses listeners
     * @param c Component
     */
    private void init(Component c)
    {
        setIconImage(MAIN_IMAGE);
        mOwner = c;
        addComponentListener(this);
        addWindowListener(this);
    }

    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {
        makeModal();

    }

    @Override
    public void componentHidden(ComponentEvent e) {
        makeModal();

    }

    @Override
    public void windowOpened(WindowEvent e) {

        makeModal();

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        makeModal();

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    /**
     * Renvoie la parenté exclusive
     * @return boolean
     */
    public boolean isParentOnly()
    {
        return mParentOnly;
    }


    @Override
    public void setModal(boolean modal) {
        super.setModal(modal);
        makeModal();
    }


    @Override
    public boolean isModal() {
        return (super.isModal() && !mParentOnly);
    }

    private void makeModal()
    {
        if (mOwner!=null)
        {
            mOwner.setEnabled(!super.isModal() || !isVisible());
        }
    }

    @Override
    public void dispose() {
        if (mOwner != null)
        {
            mOwner.setEnabled(true);
        }

        removeComponentListener(this);
        removeWindowListener(this);
        super.dispose();
    }

    @Override
    public void pack() {
        super.pack();
        addListeners();
    }

    /**
     * Ajout des listeners à la fenêtre de dialogue
     * @return boolean Si ok
     */
    private boolean addListeners()
    {
        WindowListener[] listeners = this.getWindowListeners();
        if (listeners != null)
        {
            for (WindowListener listener : listeners)
            {
                if (listener == this)
                {
                    return false;
                }
            }
        }

        addComponentListener(this);
        addWindowListener(this);
        return true;
    }
}
