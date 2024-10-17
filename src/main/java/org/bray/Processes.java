package org.bray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
     * Time Complexity is O(n^2).
     *
     * @return Returns an ArrayList of Maps that contain the IP address as the key and the arp entry as the value.
     */
    public static ArrayList<Map<String, ArrayList<String>>> arp_parser() {

        // Structure explained.
        // arp_table - ArrayList
        // ├── Map - arp interface key as (10.10.1.15 --- 0x10) to arp entry Arraylist of strings
        // │   ├── arp entry array item - Internet Address example (10.10.1.1)
        // │   ├── arp entry array item - Physical Address example (00-00-00-00-00-00)
        // │   └── arp entry array item - Type example (static|dynamic)
        // └── Map - arp interface key as (10.10.1.16 --- 0x2e) to arp entry Arraylist of strings
        //     ├── arp entry array item - Internet Address example (10.10.1.55)
        //     ├── arp entry array item - Physical Address example (00-00-00-00-00-01)
        //     └── arp entry array item - Type example (static|dynamic)
        ArrayList<Map<String, ArrayList<String>>> arp_table = new ArrayList<>();

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

                    for (Map<String, ArrayList<String>> map : arp_table) {
                        if (map.containsKey(arp_interface)) {
                            // Adding the arp entry to the arp_table that already has an arp_interface.
                            map.get(arp_interface).add(arp_entry[1]);
                            map.get(arp_interface).add(arp_entry[2]);
                            map.get(arp_interface).add(arp_entry[3]);
                            // Logging the arp entry.
                            logger.log(Level.INFO, "Extracted arp entry on {0}; " +
                                    "Internet Address: {1}; " +
                                    "Physical Address: {2}; " +
                                    "Type: {3}; ", new Object[]{arp_interface, arp_entry[0], arp_entry[1], arp_entry[2]});
                            arp_entry = new String[0];
                            break;
                        }
                    }

                    // Adding the arp entry to the arp_table that does not have an arp_interface added yet.
                    // This is done by checking if the arp_entry is empty
                    // Because if the arp_entry is empty then it was already assigned to a map.
                    if (arp_entry.length != 0) {
                        arp_table.add(
                                // Using Map.of to create a new map with the key as the IP address and the value as the arp entry.
                                Map.of(arp_interface, new ArrayList<>(
                                        // Adding the arp entry to the arp_table.
                                        Arrays.asList(arp_entry[1], arp_entry[2], arp_entry[3]))
                                )
                        );
                        // Logging the arp entry.
                        logger.log(Level.INFO, "Extracted arp entry on {0}; " +
                                "Internet Address: {1}; " +
                                "Physical Address: {2}; " +
                                "Type: {3}; ", new Object[]{arp_interface, arp_entry[0], arp_entry[1], arp_entry[2]});
                    }
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
        ArrayList<Map<String, ArrayList<String>>> A = arp_parser();
        System.out.println(A);
    }
}
