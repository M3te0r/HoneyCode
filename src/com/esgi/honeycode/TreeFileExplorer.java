package com.esgi.honeycode;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Class creating the Jtree used in a project
 * list the files and dirs in the root
 * to add the nodes to the JTree
 */
public class TreeFileExplorer extends JTree implements TreeSelectionListener{

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
        setToggleClickCount(2);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        addTreeSelectionListener(this);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {


    }

    private void listFiles()
    {
        this.rootNode = new DefaultMutableTreeNode();
        String f = this.root.getAbsolutePath();

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(f);

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
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(nom.getName());
                    newNode.add(this.listFile(nom,node));
                }
            }catch (NullPointerException e){}
            this.rootNode.add(newNode);

    }

    private DefaultMutableTreeNode listFile(File file, DefaultMutableTreeNode node)
    {
        if (file.isFile())
        {
            return new DefaultMutableTreeNode(file.getName());
        }
        else {
            for (File name : file.listFiles())
            {
                DefaultMutableTreeNode subNode;
                if (name.isDirectory())
                {
                    subNode = new DefaultMutableTreeNode(name.getName());
                    node.add(this.listFile(name, subNode));
                }
                else
                {
                    subNode = new DefaultMutableTreeNode(name.getName());
                }

                node.add(subNode);
            }
            return node;
        }
    }
}
