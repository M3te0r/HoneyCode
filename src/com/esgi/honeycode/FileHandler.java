package com.esgi.honeycode;

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

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.sourceFile))){


            bufferedWriter.write(document.getText(document.getStartPosition().getOffset(), document.getLength()));


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
     * @return DefaultStyledDoument : the content of the file
     */
    public RSyntaxDocument readFile(){

        RSyntaxDocument syntaxDocument = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_JAVA); // Can be none

        int buf;
        StringBuffer buffer= new StringBuffer(); // Maybe a StringBuilder if not multi-threaded

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceFile))){

           while((buf = bufferedReader.read())!=-1){

               buffer.append((char)buf);
           }

            syntaxDocument.insertString(syntaxDocument.getStartPosition().getOffset(),buffer.toString(),null);
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
