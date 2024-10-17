package org.bray;

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

    public static void welcome() {
        System.out.println("___________________________________________________");
        System.out.println("    _   ______________ __________  ____  __   _____");
        System.out.println("   / | / / ____/_  __//_  __/ __ \\/ __ \\/ /  / ___/");
        System.out.println("  /  |/ / __/   / /    / / / / / / / / / /   \\__ \\");
        System.out.println(" / /|  / /___  / /    / / / /_/ / /_/ / /______/ /");
        System.out.println("/_/ |_/_____/ /_/____/_/  \\____/\\____/_____/____/");
        System.out.println("               /_____/                             ");
        System.out.println("_______________________________v.0.1.0-alpha_______");
    }

    public static void main(String[] args) {
        // Setting logging level to warning is recommended.
        logger.setLevel(Level.WARNING);
        welcome();
        }
    }