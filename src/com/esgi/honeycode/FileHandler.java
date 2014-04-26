package com.esgi.honeycode;

import org.fife.io.UnicodeReader;
import org.fife.io.UnicodeWriter;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.io.*;

/**
 * Created by Mathieu on 10/03/14.
 * This class manage to open a file
 * Get the content of this file char by char and,
 * build a StringBuffer to insert into a new DefaultStyledDocument and returns it
 *
 * Manage to write a file by receiving the current StyledDocument,
 * And writes the text of this Document into the file
 */
public class FileHandler {

    private File sourceFile;

    public FileHandler(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     *
     * @param document = the StyledDocument from the JEditorPane
     */

    public void writeFile(RSyntaxDocument document){

        try(UnicodeWriter unicodeWriter = new UnicodeWriter(new BufferedOutputStream(new FileOutputStream(this.sourceFile)),"UTF-8")){

            unicodeWriter.write(document.getText(document.getStartPosition().getOffset(), document.getLength()));
            if (document.getProperty("stateChange")==1)
            {
                document.putProperty("stateChange",0);
            }
        }catch (FileNotFoundException ex)
        {
            JOptionPane.showMessageDialog(null, "Could not open such file : "+sourceFile.getName());
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null,ex.getStackTrace());
        }
        catch (BadLocationException ex)
        {
            JOptionPane.showMessageDialog(null, "Cannot find text location\n"+ex.getMessage());
        }
    }

    /**
     *
     * @return RSyntaxDocument : the content of the file with the syntax highlighting
     */
    public RSyntaxDocument readFile(){



        RSyntaxDocument syntaxDocument = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_NONE);

        if (this.sourceFile.getName().endsWith("java"))
        {
            syntaxDocument.setSyntaxStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        }

        int buf;
        StringBuffer buffer= new StringBuffer(); // Maybe a StringBuilder if not multi-threaded

        try(UnicodeReader unicodeReader = new UnicodeReader(new BufferedInputStream(new FileInputStream(this.sourceFile)))){

           while((buf = unicodeReader.read())!=-1){

               buffer.append((char)buf);
           }

            syntaxDocument.insertString(syntaxDocument.getStartPosition().getOffset(), buffer.toString(), null);
        }
        catch (BadLocationException ex){

            JOptionPane.showMessageDialog(null, "Could not find text location");
        }
        catch (FileNotFoundException ex)
        {
            JOptionPane.showMessageDialog(null, "Could not find "+sourceFile.getName());
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null, "Could not read from "+sourceFile.getName());
        }
        return syntaxDocument;
    }
}
