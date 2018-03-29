package cz.vutbr.fit;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressTest {

    public static void main(String[] args) {

        try {
            InetAddress ipv6Long = InetAddress.getByName("ff02:0:0:0:0:0:0:c");
            InetAddress ipv6Short = InetAddress.getByName("ff02::c");

            System.out.println("Are they same? " + ((ipv6Long.equals(ipv6Short)) ? "Yes" : "No"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

}
