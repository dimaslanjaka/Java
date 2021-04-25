package sample;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.dimaslanjaka.kotlin.ConsoleColors.Companion.println;

public class InetInfo {
    public static void main(String[] args) {
        try {
            check();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private static void check() throws SocketException {
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface n = e.nextElement();
            Enumeration<InetAddress> ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = ee.nextElement();
                println(i.getHostAddress());
            }
        }
    }
}
