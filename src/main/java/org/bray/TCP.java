package org.bray;

import org.jetbrains.annotations.NotNull;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author      Cade Bray | bray.cade@gmail.com
 */
public class TCP {

    /**
     * private logger for the TCP class.
     */
    private static final Logger logger = Logger.getLogger(TCP.class.getName());


    /**
     * Function for running TCP port checks on a device.
     * <p>
     * Time Complexity is O(n).
     * </p>
     * <p>
     * Running checks for TCP ports on a device.
     * </p>
     * @param  host provide an address to check such as 10.10.1.14
     * @param port_range is a two integer array of the starting port and ending port. Cannot be Null.
     * @param timeout is an integer in milliseconds before the service moves from one port to the next.
     * @return Returns an ArrayList of Integers that has all open ports. If you'd like it provided in a log file enable
     * logging at an Info level.
     */
    public static @NotNull ArrayList<Integer> check_TCP_ports(String host, @NotNull
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

    /**
     * Function to perform a full TCP port scan on the local subnet.
     * <p>
     * Time Complexity is O(n * m * i), where n is the number of hosts, m is the number of ports,
     * and i is the number of network interfaces.
     *
     * <p>
     * This function uses the ARP table to identify hosts on the local subnet and then performs a TCP port scan
     * on each host within the specified port range.
     *
     * @param port_range an ArrayList of two integers specifying the starting and ending ports to scan. Cannot be null.
     * @param timeout an integer specifying the timeout in milliseconds for each port scan attempt.
     * @return Returns a Map where the key is the host address and the value is an ArrayList of open ports on that host.
     */
    public static Map<String, ArrayList<Integer>> local_subnet_Full_TCP(ArrayList<Integer> port_range, int timeout) {
        // This Array will contain the return of ports open on a host.
        Map<String, ArrayList<Integer>> ports_open = new HashMap<>();
        Map<String, ArrayList<String[]>> stringArrayListMap = Processes.arp_parser();
        for (String key: stringArrayListMap.keySet()) {
            for (String[] entry: stringArrayListMap.get(key)) {
                ports_open.put(entry[0], check_TCP_ports(entry[0], port_range, timeout));
            }
        }
        return ports_open;
    }

    public static void main(String[] args) {
        // Setting logging level to warning will output only connections.
        // For closed ports switch to info level logging.
        logger.setLevel(Level.WARNING);

        // ArrayList of size two initialized with ports. Change ports here for expanded/narrowed range.
        ArrayList<Integer> ports_to_scan = new ArrayList<>(Arrays.asList(Integer.parseInt(args[1]),
                Integer.parseInt(args[2])));

        //Input Host as first parameter and timeout in milliseconds as last parameter.
        //Map<String, ArrayList<Integer>> ports = local_subnet_Full_TCP(ports_to_scan, Integer.parseInt(args[3]));
        ArrayList<Integer> ports = check_TCP_ports(args[0], ports_to_scan, Integer.parseInt(args[3]));
        System.out.println(ports);
    }
}
