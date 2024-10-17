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

    public static void main(String[] args) {
        // Setting logging level to warning is recommended.
        logger.setLevel(Level.WARNING);
        }
    }