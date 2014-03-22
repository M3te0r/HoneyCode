package com.esgi.honeycode;

import javax.tools.*;
import java.nio.charset.Charset;

/**
 * Class that compiles given java files
 */
public class CompileJavaFiles {


    public static void CompileJava(String[] args)
    {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> dc = new DiagnosticCollector<>();

        StandardJavaFileManager sjfm = compiler.getStandardFileManager(dc, null, Charset.defaultCharset());

        Iterable<? extends JavaFileObject> fileObjects = sjfm.getJavaFileObjects(args);

        compiler.getTask(null, sjfm, dc,null, null, fileObjects).call();

        for (Diagnostic d : dc.getDiagnostics())
        {
            System.out.println(d.getMessage(null));
            System.out.printf("Line number = %d\n", d.getLineNumber());
            System.out.printf("File = %s\n", d.getSource());

        }

    }

}
