package com.esgi.honeycode;


import java.io.*;
import java.util.Scanner;

/**
 * Class CustomRun :
 * manage to start a process java
 */
public class CustomRun {


    public static void run(String args, final String projectOut) throws IOException {

        System.out.flush();


        if (args != null && projectOut != null) {

            //With ProcessBuilder the err output can be redirected to te standard output
            //Only 2 threads instead of 3 for the err
            ProcessBuilder builder = new ProcessBuilder("java", "-classpath", "\"" + System.getProperty("java.class.path") + System.getProperty("path.separator") + projectOut + PropertiesShared.SEPARATOR + "out\"", args);
            builder.redirectErrorStream(true);
            final Process process = builder.start();
            // Consommation de la sortie standard de de la console
            Thread outThread = new Thread()
            {
                @Override
                public void run() {
                    try {

                        String line;
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                            while ((line = reader.readLine()) != null) {
                                System.out.println(line);
                            }
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            };

            //Conso de l'entrée standard de la console
            Thread inThread = new Thread() {
                @Override
                public void run() {

                    Scanner s = new Scanner(System.in);
                    //Need to control in before !!

                    while (true) {
                        String input = s.nextLine();

                        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(process.getOutputStream()))) {
                            pw.write(input);
                            pw.flush();
                        }

                    }
                }

            };

            outThread.start();
            inThread.start();




        }

    }

}
