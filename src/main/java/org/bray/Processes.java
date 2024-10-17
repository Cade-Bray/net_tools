package org.bray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author      Cade Bray | bray.cade@gmail.com
 */
public class Processes {

    /**
     * private logger for the main class.
     */
    private static final Logger logger = Logger.getLogger(Processes.class.getName());

    /**
     * Function to parse the arp table of a device.
     * <p>
     * Time Complexity is O(n).
     *
     * @return Returns a Map of the arp table. Constructed in the following way:
     * arp_table - Map
     *       ├── [key] arp interface as key ex. (10.10.1.15 --- 0x10)
     *       └── [value] Arraylist String[] of values
     *             ├── arp entry array item - Internet Address example (10.10.1.1)
     *             ├── arp entry array item - Physical Address example (00-00-00-00-00-00)
     *             └── arp entry array item - Type example (static|dynamic)
     * Logging at an Info level will output all arp entries.
     */
    public static Map<String, ArrayList<String[]>> arp_parser() {
        Map<String, ArrayList<String[]>> arp_table = new HashMap<>();

        // Try-catch used for process creation failures.
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("arp", "-a");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // Start up variables for the loop.
            String line;
            String arp_interface = "";
            String[] arp_entry;

            while ((line = reader.readLine()) != null) {
                // Splitting the line by whitespace.
                try {
                    // Skip if the line is empty or contains Internet Address.
                    if (line.isEmpty() || line.contains("Internet Address")) {
                        continue;
                    } else if (line.contains("Interface")) {
                        // Extracting the interface from the line.
                        arp_interface = line;
                        continue;
                    } else {
                        // Splitting the line by whitespace.
                        arp_entry = line.split("\\s+");
                    }

                    // Creating the arp table.
                    if (arp_table.containsKey(arp_interface)) {
                        // Adding the arp entry to the arp table that already exists.
                        arp_table.get(arp_interface).add(new String[]{arp_entry[1], arp_entry[2], arp_entry[3]});
                    } else {
                        // Creating a new arp table entry.
                        ArrayList<String[]> temp = new ArrayList<>();
                        temp.add(new String[]{arp_entry[1], arp_entry[2], arp_entry[3]});
                        arp_table.put(arp_interface, temp);
                    }

                    // Logging the arp entry.
                    logger.log(Level.INFO, "Extracted arp entry on {0}; " +
                            "Internet Address: {1}; " +
                            "Physical Address: {2}; " +
                            "Type: {3}; ", new Object[]{arp_interface, arp_entry[0], arp_entry[1], arp_entry[2]});
                } catch (ArrayIndexOutOfBoundsException e) {
                    // ArrayIndexOutOfBoundsException occurred. Need to log as severe.
                    logger.log(Level.SEVERE, "Exception occurred: {0}, Type: {1}",
                            new Object[]{e.getMessage(), e.getClass().getName()});
                }
            }

            reader.close();
        } catch (IOException e) {
            // IOException occurred. Need to log as severe.
            logger.log(Level.SEVERE, "Exception occurred: {0}, Type: {1}",
                    new Object[]{e.getMessage(), e.getClass().getName()});
        }
        return arp_table;
    }
    public static void main(String[] args) {
        // Setting logging level to INFO will output all arp entries.
        logger.setLevel(Level.INFO);

        // Running arp_parser function.
        arp_parser();
    }
}
