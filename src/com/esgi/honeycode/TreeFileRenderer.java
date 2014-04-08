package com.esgi.honeycode;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.io.File;

/**
 * Class rendering the Tree cell
 * with the name, icon from system
 */
public class TreeFileRenderer extends DefaultTreeCellRenderer {

    public TreeFileRenderer() {
        super();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JLabel label = (JLabel)super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        File file = (File)value;
        FileSystemView sys = FileSystemView.getFileSystemView();
        label.setText(sys.getSystemDisplayName(file));
        label.setIcon(sys.getSystemIcon(file));
        return label;
    }
}
