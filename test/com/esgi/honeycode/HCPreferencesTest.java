package com.esgi.honeycode;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Created by Mathieu on 17/03/14.
 * Test class for HCPreferences class methods
 */
public class HCPreferencesTest {

    private static HCPreferences preferencesTest;
    private static String langDef;
    private static String defaultPath;


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        preferencesTest = new HCPreferences();
        langDef = System.getProperty("user.language");
        defaultPath = System.getProperty("user.home")+System.getProperty("file.separator")+"HoneyCodeProjects";

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        preferencesTest.clear();
    }

    @Test
    public void testSetPreferences() throws Exception {

        preferencesTest.setPreferences();
        assertEquals(langDef, preferencesTest.getUserLanguageReg());
        assertEquals(defaultPath, preferencesTest.getProjetPath());
    }

    @Test
    public void testGetUserLanguageReg() throws Exception {

        assertNotNull(preferencesTest.getUserLanguageReg());
    }

    @Test
    public void testGetProjetPath() throws Exception {
        assertNotNull(preferencesTest.getProjetPath());
    }
}
