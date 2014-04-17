package com.esgi.honeycode;

import javax.tools.*;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public static void doCompilation(String projectSourcePath, String projectPath)
    {
        /*
        Listing java sources from src directory
        Easier and simpler than compiling each java source
        when there is class dependencies
        */
        File ff = new File(projectSourcePath);
        FilenameFilter javaFileNameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("java");
            }
        };
        
        List<File> files = Arrays.asList(ff.listFiles(javaFileNameFilter));

        System.out.flush();


        diagnosticCollector = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticCollector, Locale.getDefault(), Charset.defaultCharset());
        //Build a list of all compilations units
        Iterable<? extends JavaFileObject> compilationsUnits1 = fileManager.getJavaFileObjectsFromFiles(files);

        //Prepare any compilation options to be used during compilation

        String[] compilOptions = new String[]{"-d", projectPath+PropertiesShared.SEPARATOR+"out","-classpath","\""+System.getProperty("java.class.path")+System.getProperty("path.separator")+projectPath+PropertiesShared.SEPARATOR+"out\""};
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

 /*  public static void createClassDir()
    {
        File classDirM = new File(classDir+PropertiesShared.SEPARATOR+"Classes");
        boolean created = classDirM.mkdir();
        if (!created && !classDirM.exists())
        {
            JOptionPane.showMessageDialog(null, "Impossible de créer le répertoire de projet : " + classDir);
        }
    }*/
}
