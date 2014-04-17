package com.esgi.honeycode;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class CustomRun :
 * manage to start a process java
 */
public class CustomRun {

    public static void run(String args, String projectOut) throws IOException
    {

        System.out.flush();

        if (args!=null && projectOut != null)
        {
            Runtime runtime = Runtime.getRuntime();
            final Process process = runtime.exec("java -classpath \""+System.getProperty("java.class.path")+System.getProperty("path.separator")+projectOut+PropertiesShared.SEPARATOR+"out\" "+args);



            // Consommation de la sortie standard de l'application externe dans un Thread separe
            new Thread() {
                public void run() {
                    try {

                        String line;
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
                            while((line = reader.readLine()) != null) {
                                System.out.println(line);
                            }
                        }
                    } catch(IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }.start();


            // Consommation de la sortie d'erreur de l'application externe dans un Thread separe
            new Thread() {
                public void run() {
                    try {
                        String line;
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))){
                            while((line = reader.readLine()) != null) {
                                // Traitement du flux d'erreur de l'application si besoin est
                                System.err.println(line);
                            }
                        }
                    } catch(IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }.start();
        }
        }

}
