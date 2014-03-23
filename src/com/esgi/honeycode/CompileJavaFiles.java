package com.esgi.honeycode;

import javax.swing.*;
import javax.tools.*;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Class that compiles given java files
 */
public class CompileJavaFiles {


    private static final Logger logger = Logger.getLogger(CompileJavaFiles.class.getName());

    //Instanciating the java compiler
    private static final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    private static DiagnosticCollector<JavaFileObject> diagnosticCollector;
    private static String className;

    public static void doCompilation(File[] files, String[] compilOptions )
    {
        diagnosticCollector = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticCollector, Locale.getDefault(), Charset.defaultCharset());
        //Build a list of all compilations units
        Iterable<? extends JavaFileObject> compilationsUnits1 = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));

        //Prepare any compilation options to be used during compilation
        compilOptions = new String[]{"-d", files[0].getParent()+PropertiesShared.SEPARATOR+"Classes"};
        Iterable<String> compilationOptions = Arrays.asList(compilOptions);

        JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, fileManager, diagnosticCollector, compilationOptions, null, compilationsUnits1);

        boolean compilationStatus = compilerTask.call();

        if (!compilationStatus)
        {//If compilation error occurs
            System.out.println("Compilation failed\n");

            for (Diagnostic diagnostic : diagnosticCollector.getDiagnostics())
            {
                System.out.format("Error on line %d in %s", diagnostic.getLineNumber(), diagnostic);
            }
        }
        else{
            System.out.println("Compilation completed");
        }
    }

    public static void createClassDir(String classDir)
    {
        File classDirM = new File(classDir+PropertiesShared.SEPARATOR+"Classes");
        boolean created = classDirM.mkdir();
        if (!created && !classDirM.exists())
        {
            JOptionPane.showMessageDialog(null, "Impossible de créer le répertoire de projet : " + classDir);
        }
    }
}
