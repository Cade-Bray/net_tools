package org.bray;

import org.jetbrains.annotations.NotNull;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * @author      Cade Bray | bray.cade@gmail.com
 */
public class Main {

    /**
     * private logger for the main class.
     */
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * Function to parse the arp table of a device.
     * <p>
     * Time Complexity is O(n).
     *
     * @return Returns an ArrayList of Maps that contain the IP address as the key and the arp entry as the value.
     */
    public static ArrayList<Map<String, ArrayList<String>>> arp_parser() {
        ArrayList<Map<String, ArrayList<String>>> arp_table = new ArrayList<>();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("arp", "-a");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

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
            String line;
            while ((line = reader.readLine()) != null) {
                // Splitting the line by whitespace.
                String[] arp_entry = line.split("\\s+");
                // Adding the arp entry to the arp_table.
                arp_table.add(
                        // Using Map.of to create a new map with the key as the IP address and the value as the arp entry.
                        Map.of(arp_entry[0], new ArrayList<>(
                            // Adding the arp entry to the arp_table.
                            Arrays.asList(arp_entry[1], arp_entry[2], arp_entry[3]))
                        )
                    );
                logger.log(Level.INFO, "Extracted arp entry on Interface {0}./n" +
                        "Internet Address: {1}./n" +
                        "Physical Address: {2}./n" +
                        "Type: {3}", new Object[]{arp_entry[0], arp_entry[1], arp_entry[2], arp_entry[3]});
            }

            reader.close();
        } catch (IOException e) {
            // IOException occurred. Need to log as severe.
            logger.log(Level.SEVERE, "Exception occurred: {0}, Type: {1}",
                    new Object[]{e.getMessage(), e.getClass().getName()});
        }
        return arp_table;
    }

    /**
     * Function for running port checks on a device.
     * <p>
     * Time Complexity is O(n).
     *
     * <p>
     * Running checks for TCP ports on a device.
     *
     * @param  host provide an address to check such as 10.10.1.14
     * @param port_range is a two integer array of the starting port and ending port. Cannot be Null.
     * @param timeout is an integer in milliseconds before the service moves from one port to the next.
     * @return Returns an ArrayList of Integers that has all open ports. If you'd like it provided in a log file enable
     * logging at an Info level.
     */
    public static @NotNull ArrayList<Integer> check_TCP_ports(String host, @org.jetbrains.annotations.NotNull
    ArrayList<Integer> port_range, int timeout){

        // This Array will contain the return of ports open on a host.
        ArrayList<Integer> ports = new ArrayList<>();
        for (int port = port_range.getFirst(); port <= port_range.getLast(); port++) {

            // Try-catch used for socket creation failures.
            try (Socket socket = new Socket()) {
                // Using InetSocketAddress and the overloaded form of socket.connect that allows for timeout.
                socket.connect(new InetSocketAddress(host, port), timeout);

                // Port is open
                ports.add(port);
                logger.log(Level.WARNING, "[OPEN] Port {0} is open on {1}",
                        new Object[]{port, host});

            } catch (Exception e) {
                // TODO: Narrow exception types.
                // Port is closed or unreachable.
                logger.log(Level.INFO, "[CLOSED] Port {0} is closed or unreachable on {1}",
                        new Object[]{port, host});
            }
        }
        return ports;
    }

    public static void main(String[] args) {
        // Setting logging level to warning will output only connections.
        // For closed ports switch to info level logging.
        logger.setLevel(Level.WARNING);

        // ArrayList of size two initialized with ports. Change ports here for expanded/narrowed range.
        ArrayList<Integer> ports_to_scan = new ArrayList<>(Arrays.asList(Integer.parseInt(args[1]),
                Integer.parseInt(args[2])));

        //Input Host as first parameter and timeout in milliseconds as last parameter.
        //check_TCP_ports(args[0], ports_to_scan, Integer.parseInt(args[3]));

        // Running arp_parser function.
        arp_parser();
        }
    }