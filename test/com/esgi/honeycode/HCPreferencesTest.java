package com.esgi.honeycode;

import org.junit.*;
import static org.junit.Assert.*;


import java.util.prefs.Preferences;

/**
 * Created by Mathieu on 17/03/14.
 * Test class for HCPreferences class methods
 */
public class HCPreferencesTest {

    private static Preferences prefs;
    private static String langDef;
    private static String defaultPath;


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        prefs = Preferences.userNodeForPackage(Class.forName("com.esgi.honeycode.HCPreferencesTest"));
        langDef = System.getProperty("user.language");
        defaultPath = System.getProperty("user.home")+System.getProperty("file.separator")+"HoneyCodeProjects";
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        prefs.clear();
    }

    @Test
    public void testSetPreferences() throws Exception {

        prefs.put("languagetest", langDef);
        assertEquals(langDef, prefs.get("languagetest", langDef));
        prefs.put("projectpathtest", defaultPath);
        assertEquals(defaultPath,  prefs.get("projectpathtest", defaultPath));
    }

    @Test
    public void testGetUserLanguageReg() throws Exception {

        assertNotNull(prefs.get("languagetest", langDef));
    }

    @Test
    public void testGetProjetPath() throws Exception {
        assertNotNull(prefs.get("projectpaththest",defaultPath));
    }
}
