package org.bray;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author      Cade Bray | bray.cade@gmail.com
 */
public class UDP {

    /**
     * private logger for the UDP class.
     */
    private static final Logger logger = Logger.getLogger(UDP.class.getName());

    /**
     * Scans a single UDP port on a given host.
     *
     * @param host the host to scan.
     * @param port the port to scan.
     * @param timeout the timeout in milliseconds for the scan.
     * @return true if the port is open, false otherwise.
     */
    public static boolean scanPort(String host, int port, int timeout) {
        try {
            InetAddress address = InetAddress.getByName(host);
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(timeout);

            // Send an empty packet to the port
            byte[] buf = new byte[1];
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);

            // Try to receive a response
            socket.receive(packet);
            socket.close();
            return true;
        } catch (SocketTimeoutException e) {
            // No response received, port is closed or filtered
            return false;
        } catch (Exception e) {
            // Other exceptions, port is closed or filtered
            return false;
        }
    }

    /**
     * Scans a range of UDP ports on a given host.
     *
     * @param host the host to scan.
     * @param startPort the starting port of the range.
     * @param endPort the ending port of the range.
     * @param timeout the timeout in milliseconds for each port scan.
     * @return a list of open ports.
     */
    public static List<Integer> scanPorts(String host, int startPort, int endPort, int timeout) {
        List<Integer> openPorts = new ArrayList<>();
        for (int port = startPort; port <= endPort; port++) {
            if (scanPort(host, port, timeout)) {
                openPorts.add(port);
            }
        }
        return openPorts;
    }

    public static void main(String[] args) {
        List<Integer> openPorts = scanPorts(args[0], Integer.parseInt(args[1]),
                Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        System.out.println("Open UDP ports: " + openPorts);
    }
}