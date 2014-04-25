package com.esgi.honeycode;

import com.sun.istack.internal.Nullable;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
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

    public void init(final File projectPath)
    {
        removeAll();
        this.root = projectPath;
        listFiles(null,null);
        ((DefaultTreeModel) getModel()).setRoot(this.rootNode);
        setCellRenderer(new TreeFileRenderer());
        setToggleClickCount(2);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        addTreeSelectionListener(this);

        final JPopupMenu popupMenuDir = new JPopupMenu();
        JMenuItem newClass = new JMenuItem("New class");
        newClass.addActionListener(this);
        newClass.setActionCommand("Newclass");
        JMenuItem newDir = new JMenuItem("New directory");
        newDir.setActionCommand("newDir");
        newDir.addActionListener(this);
        JMenuItem renameDir = new JMenuItem("Rename directory");
        renameDir.setActionCommand("renameDir");
        renameDir.addActionListener(this);
        JMenuItem deleteDir = new JMenuItem("Delete directory");
        deleteDir.setActionCommand("DeleteDir");
        deleteDir.addActionListener(this);
        JMenuItem showDirExplorer = new JMenuItem("Show directory in explorer");
        showDirExplorer.setActionCommand("showFileExplorer");
        showDirExplorer.addActionListener(this);
        popupMenuDir.add(newClass);
        popupMenuDir.add(newDir);
        popupMenuDir.add(renameDir);
        popupMenuDir.add(deleteDir);
        popupMenuDir.add(showDirExplorer);

        final JPopupMenu popupMenuFile = new JPopupMenu();
        JMenuItem deleteFile = new JMenuItem("Delete file");
        deleteFile.setActionCommand("DeleteFile");
        deleteFile.addActionListener(this);
        JMenuItem renameFile = new JMenuItem("Rename file");
        renameFile.setActionCommand("renameFile");
        renameFile.addActionListener(this);
        JMenuItem showFileExplorer = new JMenuItem("Show file in explorer");
        showFileExplorer.setActionCommand("showFileExplorer");
        showFileExplorer.addActionListener(this);
        popupMenuFile.add(renameFile);
        popupMenuFile.add(deleteFile);
        popupMenuFile.add(showFileExplorer);

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

                } else if (e.getClickCount() == 2) {

                    File selectedFileOnTree = (File) ((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject();
                    if (selectedFileOnTree.isFile()) {
                        FileHandler fileHandler = new FileHandler(selectedFileOnTree);
                        MainWindowUI.addCloseableTab(new RTextScrollPane(new RSyntaxTextArea(fileHandler.readFile())), selectedFileOnTree.getName(), selectedFileOnTree.getAbsolutePath());
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

        if (e.getActionCommand().equals("newDir"))
        {
            String newDirInput = JOptionPane.showInputDialog(null,"Nom du dossier :", "Nouveau dossier", JOptionPane.QUESTION_MESSAGE);
            if (newDirInput!=null)
            {
                File newDir = new File(fileSelected.getAbsolutePath()+PropertiesShared.SEPARATOR+newDirInput);
                    boolean created = newDir.mkdir();
                    if (!created && !newDir.exists())
                    {
                        JOptionPane.showMessageDialog(null, "Impossible de créer le dossier : "+newDirInput);
                    }
                    else {
                        node = new DefaultMutableTreeNode(newDir);
                        dmtn.add(node);
                        ((DefaultTreeModel)getModel()).nodeStructureChanged(dmtn);
                    }
            }
        }
        if (e.getActionCommand().equals("renameDir"))
        {
            String renamedDir = JOptionPane.showInputDialog(JOptionPane.getFrameForComponent(this),"Rename directory "+fileSelected.getName()+" to :");
            if (renamedDir!=null)
            {
                File rename = new File(fileSelected.getParent()+PropertiesShared.SEPARATOR+renamedDir);
                if (fileSelected.renameTo(rename)){
                    dmtn.removeFromParent();
                    ((DefaultTreeModel)getModel()).nodeStructureChanged(dmtn);

                    node = ((DefaultMutableTreeNode)path.getParentPath().getLastPathComponent());
                    listFiles(rename,node );
                    ((DefaultTreeModel)getModel()).nodeStructureChanged(node);
                }
            }
        }



        if (e.getActionCommand().equals("DeleteDir"))
        {
            int res = JOptionPane.showConfirmDialog(null, "Delete this directory with all his sub directories and files ?", "Delete confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (res == JOptionPane.YES_OPTION)
            {
                try {
                    deleteAll(fileSelected);

                    //I know, there is no check if the dir and all sub-dir and files were really deleted
                    dmtn.removeFromParent();
                    ((DefaultTreeModel)getModel()).nodeStructureChanged(dmtn);

                }catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }
            }
        }

        if (e.getActionCommand().equals("DeleteFile"))
        {
            if (fileSelected.isFile())
            {
                int res = JOptionPane.showConfirmDialog(null, "Delete this file ?", "Delete confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (res == JOptionPane.YES_OPTION)
                {

                    if (fileSelected.delete())
                    {
                        dmtn.removeFromParent();
                        ((DefaultTreeModel)getModel()).nodeStructureChanged(dmtn);
                    }
                }
            }
        }

        if (e.getActionCommand().equals("renameFile"))
        {
            String name = JOptionPane.showInputDialog(JOptionPane.getFrameForComponent(this), "Rename file "+fileSelected.getName()+" to :",fileSelected.getName());
            if (name!=null)
            {
                File renamedFile = new File(fileSelected.getParent()+PropertiesShared.SEPARATOR+name);
                if (fileSelected.renameTo(renamedFile))
                {
                    dmtn.removeFromParent();
                    ((DefaultTreeModel)getModel()).nodeStructureChanged(dmtn);
                    node = ((DefaultMutableTreeNode)path.getParentPath().getLastPathComponent());
                    node.add(new DefaultMutableTreeNode(renamedFile));
                    ((DefaultTreeModel)getModel()).nodeStructureChanged(node);
                    MainWindowUI.setNewTabTextRenamedFile(fileSelected.getName(),renamedFile.getName(),renamedFile.getAbsolutePath());
                }
            }
        }
        if (e.getActionCommand().equals("showFileExplorer"))
        {
            try
            {
                if (fileSelected.isDirectory())
                {
                    Desktop.getDesktop().open(fileSelected);
                }
                else if (fileSelected.isFile())
                {
                    Desktop.getDesktop().open(fileSelected.getParentFile());
                }

            }catch (IOException ex)
            {
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), "Could not open file with explorer","Error while showing file", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private void  deleteAll(File f) throws IOException
    {
        if (f.isDirectory())
        {
            for (File c : f.listFiles())
            {
                deleteAll(c);
            }
        }
        if (!f.delete()){
            throw new FileNotFoundException("Failed to delete file : "+f);
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {


    }

    private void listFiles(@Nullable File dirRenamed, @Nullable DefaultMutableTreeNode renameDirNode)
    {
        if (renameDirNode!=null)
        {
            this.rootNode = renameDirNode;
        }
        else
        {
            this.rootNode = new DefaultMutableTreeNode();
        }


        DefaultMutableTreeNode newNode;
        if (dirRenamed!=null)
        {
            newNode = new DefaultMutableTreeNode(dirRenamed);
        }
        else
        {
            newNode = new DefaultMutableTreeNode(this.root);
        }


            try {
                //File filter not to list 'out' folder with classes
                FileFilter ff = new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return !pathname.getName().equals("out");
                    }
                };

                File files[];

                if (renameDirNode!=null && dirRenamed!= null)
                {
                    files = dirRenamed.listFiles(ff);
                }
                else {
                    files = this.root.listFiles(ff);
                }
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
            return new DefaultMutableTreeNode(file);
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
