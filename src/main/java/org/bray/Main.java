package org.bray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * @author      Cade Bray | bray.cade@gmail.com
 */
public class Main {

    /**
     * private logger for the main class.
     */
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    // Coloring for the welcome message.
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void welcome() {
        System.out.println(ANSI_GREEN + "___________________________________________________");
        System.out.println("    _   ______________ __________  ____  __   _____");
        System.out.println("   / | / / ____/_  __//_  __/ __ \\/ __ \\/ /  / ___/");
        System.out.println("  /  |/ / __/   / /    / / / / / / / / / /   \\__ \\");
        System.out.println(" / /|  / /___  / /    / / / /_/ / /_/ / /______/ /");
        System.out.println("/_/ |_/_____/ /_/____/_/  \\____/\\____/_____/____/");
        System.out.println("               /_____/                             ");
        System.out.println("_______________________________v.0.1.1-alpha_______" + ANSI_RESET);
    }

    public static void main(String[] args) {
        // Setting logging level to warning is recommended.
        logger.setLevel(Level.WARNING);
        welcome();
        boolean cont = true;

        while(cont){
            switch (args[0]) {
                case "-a":
                    System.out.println(Processes.arp_parser());
                    break;

                case "-tcp":
                    TCP.check_TCP_ports(args[1], new ArrayList<>(Arrays.asList(Integer.parseInt(args[1]),
                            Integer.parseInt(args[2]))), Integer.parseInt(args[4]));
                    break;

                case "-q":
                    cont = false;
                    break;
            }
        }
    }
}