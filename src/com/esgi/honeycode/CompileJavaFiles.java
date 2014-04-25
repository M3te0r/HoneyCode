package com.esgi.honeycode;

import javax.swing.*;
import javax.tools.*;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Class that compiles given java files
 */
public class CompileJavaFiles {

    //Instanciating the java compiler
    private static JavaCompiler compiler;



    private static DiagnosticCollector<JavaFileObject> diagnosticCollector;

    public static boolean doCompilation(String projectSourcePath, String projectPath)
    {
        compiler = ToolProvider.getSystemJavaCompiler();

        if (compiler==null)
        {
                JOptionPane.showMessageDialog(null, "Compilateur java introuvable, veuillez vérifier que celui-ci est bien installé\nVous pouvez retrouver le dernier JDK à l'adresse\n<html><a href=\"http://www.oracle.com/technetwork/java/javase/downloads/index.html\">http://www.oracle.com/technetwork/java/javase/downloads/index.html</a></html>","JDK ou javac introuvable",JOptionPane.ERROR_MESSAGE);
                return false;
        }
        else {
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

            //System.out.flush();


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

                return false;
            }
            else{
                System.out.println("Compilation completed");
                return true;
            }
        }

    }
}
