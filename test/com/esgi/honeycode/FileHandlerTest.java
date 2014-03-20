package com.esgi.honeycode;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;


/**
 * Test class for FileHandler class
 */
public class FileHandlerTest {

    private static FileHandler fileHandlerTest;
    private static File fileTest;
    private static final String fileSep = System.getProperty("file.separator");
    private static  RSyntaxDocument testDocument;

    @Before
    public void setUp() throws Exception {
        fileTest = new File("test"+fileSep+"test.txt");
        fileHandlerTest = new FileHandler(fileTest);
        testDocument = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_NONE);
        testDocument.insertString(testDocument.getStartPosition().getOffset(), "This is a test", null);
    }

    @Test
    public void testWriteFile() throws Exception {
       fileHandlerTest.writeFile(testDocument);
       assertEquals("This is a test", fileHandlerTest.readFile().getText(testDocument.getStartPosition().getOffset(),testDocument.getLength()));
    }

    @Test
    public void testReadFile() throws Exception {

        assertNotNull(fileHandlerTest.readFile());
        RSyntaxDocument test = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_NONE);
        test.insertString(fileHandlerTest.readFile().getStartPosition().getOffset(),fileHandlerTest.readFile().getText(fileHandlerTest.readFile().getStartPosition().getOffset(),fileHandlerTest.readFile().getLength()), null);
        assertEquals("This is a test", test.getText(test.getStartPosition().getOffset(),test.getLength()));
    }
}
