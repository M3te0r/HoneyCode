package com.esgi.honeycode;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class CustomRun :
 * manage to start a process java
 */
public class CustomRun {

    /**
     *     Lancement d'un process
     *
     *     @param classToBuild
     *                Classe Java à lancer
     *     @param args
     *                Arguments d'éxecution
     *     @param projectOut
     *                Chemin du repertoir out
     */
    public static void run(String classToBuild,  List<String> args, final String projectOut) throws IOException {

        System.out.flush();
        if (classToBuild != null && projectOut != null) {

            //With ProcessBuilder the err output can be redirected to te standard output
            //Only 2 threads instead of 3 for the err
            List<String> commands = new ArrayList<String>();
            commands.add("Java");
            commands.add("-classpath");
            commands.add(System.getProperty("java.class.path") + System.getProperty("path.separator") + projectOut + PropertiesShared.SEPARATOR + "out");
            commands.add(classToBuild);
            for(int i = 0 ; i < args.size() ; i++){
                commands.add(args.get(i));
            }
            ProcessBuilder builder = new ProcessBuilder(commands);

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
                        //while(s.hasNextLine())
                        // {
                        String input = s.nextLine();

                        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(process.getOutputStream()))) {
                            pw.write(input);
                            pw.flush();
                        }
                        // }


                    }
                }

            };

            outThread.start();
            inThread.start();




        }

    }

}