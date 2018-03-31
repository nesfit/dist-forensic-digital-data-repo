package cz.vutbr.fit;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class CompareTest {

    public static void main(String[] args) {
        CompareTest ct = new CompareTest();
        ct.ipv6();
        ct.ipv4();
        ct.short_();

        ct.numbers();
    }

    public void ipv6() {
        try {
            InetAddress ipv6Long = InetAddress.getByName("ff02:0:0:0:0:0:0:c");
            InetAddress ipv6Short = InetAddress.getByName("ff02::c");
            System.out.println("Are IPv6 addrs same? " + ((ipv6Long.equals(ipv6Short)) ? "Yes" : "No"));
            System.out.println("Are IPv6 addrs same? " + ((ipv6Long == ipv6Short) ? "Yes" : "No"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void ipv4() {
        try {
            InetAddress ipv4_a = InetAddress.getByName("181.63.11.202");
            InetAddress ipv4_b = InetAddress.getByName("181.63.11.202");
            System.out.println("Are IPv4 addrs same? " + ((ipv4_a.equals(ipv4_b)) ? "Yes" : "No"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void short_() {
        short value = 11;
        Short object = Short.valueOf(value);
        System.out.println("Are shorts same? " + ((object.equals(value)) ? "Yes" : "No"));
        System.out.println("Are shorts same? " + ((object == value) ? "Yes" : "No"));
    }

    public void numbers() {
        byte b = 4;
        short s = 4;
        System.out.println("Are byte and short same? " + ((b == s) ? "Yes" : "No"));

        int i = 4;
        long l = 4L;
        System.out.println("Are int and long same? " + ((i == l) ? "Yes" : "No"));

        Object o = 4;
        Object f = Long.valueOf(4L);
        Object k = 4L;
        System.out.println("Are objects same? " + ((o.equals(f)) ? "Yes" : "No"));
        System.out.println("Are objects same? " + ((k == f) ? "Yes" : "No"));
    }

}
