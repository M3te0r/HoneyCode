package com.esgi.honeycode;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Class creating the Jtree used in a project
 * list the files and dirs in the root
 * to add the nodes to the JTree
 */
public class TreeFileExplorer extends JTree implements TreeSelectionListener, ActionListener{

    private File root;
    private DefaultMutableTreeNode rootNode;

    public TreeFileExplorer()
    {
        super(new String[] {"Nothing to show"});
    }

    public void init(File projectPath)
    {
        removeAll();
        this.root = projectPath;
        listFiles();
        ((DefaultTreeModel) getModel()).setRoot(this.rootNode);
        setCellRenderer(new TreeFileRenderer());
        setToggleClickCount(2);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        addTreeSelectionListener(this);

        final JPopupMenu popupMenuDir = new JPopupMenu();
        JMenuItem newClass = new JMenuItem("New class");
        newClass.addActionListener(this);
        newClass.setActionCommand("Newclass");
        JMenuItem deleteDir = new JMenuItem("Delete directory");
        deleteDir.setActionCommand("DeleteDir");
        popupMenuDir.add(newClass);
        popupMenuDir.add(deleteDir);

        final JPopupMenu popupMenuFile = new JPopupMenu();
        JMenuItem deleteFile = new JMenuItem("Delete file");
        deleteFile.setActionCommand("DeleteFile");
        popupMenuFile.add(deleteFile);

        addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                TreePath selPath = getPathForLocation(e.getX(), e.getY());
                if (selPath == null) {
                    return;
                } else {
                    setSelectionPath(selPath);
                }
                if (e.isPopupTrigger()) {

                    File selectedFileOnTree = (File) ((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject();

                    if (selectedFileOnTree.isDirectory()) {
                        popupMenuDir.show(e.getComponent(), e.getX(), e.getY());
                    } else if (selectedFileOnTree.isFile()) {
                        popupMenuFile.show(e.getComponent(), e.getX(), e.getY());
                    }

                }

                else if (e.getClickCount()==2)
                {

                    File selectedFileOnTree = (File) ((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject();
                    if (selectedFileOnTree.isFile())
                    {
                        FileHandler fileHandler = new FileHandler(selectedFileOnTree);
                        MainWindowUI.addCloseableTab(new RTextScrollPane(new RSyntaxTextArea(fileHandler.readFile())),selectedFileOnTree.getName(), selectedFileOnTree.getAbsolutePath());
                    }

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                TreePath selPath = getPathForLocation(e.getX(), e.getY());
                if (selPath == null) {
                    return;
                } else {
                    setSelectionPath(selPath);
                }
                if (e.isPopupTrigger()) {

                    File selectedFileOnTree = (File) ((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject();

                    if (selectedFileOnTree.isDirectory()) {
                        popupMenuDir.show(e.getComponent(), e.getX(), e.getY());
                    } else if (selectedFileOnTree.isFile()) {
                        popupMenuFile.show(e.getComponent(), e.getX(), e.getY());
                    }

                }
            }
        });

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        DefaultMutableTreeNode dmtn,node;
        TreePath path = getSelectionPath();
        dmtn = (DefaultMutableTreeNode)path.getLastPathComponent();
        File fileSelected = (File)dmtn.getUserObject();

        if (e.getActionCommand().equals("Newclass"))
        {
            String newClassInput = JOptionPane.showInputDialog(null,"Nom de la classe :", "Nouvelle classe", JOptionPane.QUESTION_MESSAGE);
            if (newClassInput!=null)
            {
                File newFile = new File(fileSelected.getAbsolutePath()+PropertiesShared.SEPARATOR+newClassInput+".java");
                try
                {
                    boolean created = newFile.createNewFile();
                    if (!created && !newFile.exists())
                    {
                        JOptionPane.showMessageDialog(null, "Impossible de créer la classe : "+newClassInput);
                    }
                    else {
                        node = new DefaultMutableTreeNode(newFile);
                        dmtn.add(node);
                        ((DefaultTreeModel)getModel()).nodeStructureChanged(dmtn);
                    }
                }catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(null, "Impossible de créer la classe : "+newClassInput);
                }


            }
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {


    }

    private void listFiles()
    {
        this.rootNode = new DefaultMutableTreeNode();
        //String f = this.root.getAbsolutePath();

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(this.root);

            try {
                File files[] = this.root.listFiles();
                Arrays.sort(files, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        boolean dirf1 = o1.isDirectory();
                        boolean dirf2 = o2.isDirectory();
                        if (dirf1 && !dirf2)
                        {
                            return -1;
                        }
                        if (!dirf1 && dirf2)
                        {
                            return 1;
                        }
                        return  o1.getPath().compareTo(o2.getPath());
                    }
                });
                for (File nom : files)
                {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(nom);
                    newNode.add(this.listFile(nom,node));
                }
            }catch (NullPointerException e){}
            this.rootNode.add(newNode);

    }

    private DefaultMutableTreeNode listFile(File file, DefaultMutableTreeNode node)
    {
        if (file.isFile())
        {
            return new DefaultMutableTreeNode(file);//-getName
        }
        else {
            for (File name : file.listFiles())
            {
                DefaultMutableTreeNode subNode;
                if (name.isDirectory())
                {
                    subNode = new DefaultMutableTreeNode(name);
                    node.add(this.listFile(name, subNode));
                }
                else
                {
                    subNode = new DefaultMutableTreeNode(name);
                }

                node.add(subNode);
            }
            return node;
        }
    }
}
