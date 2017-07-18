package KaisaBid;


import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.client.utils.DateUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by zhouchuang on 2017/6/24.
 */
public class KaisaBidStart {
    public static void main(String[] args) {
       /* String str_send = "2341234";
        try {
            DatagramSocket ds = new DatagramSocket();
            InetAddress server = InetAddress.getByName("127.0.0.1");
            DatagramPacket dp_send= new DatagramPacket(str_send.getBytes(),str_send.length(),server,11567);
            ds.send(dp_send);
            ds.close();
        } catch (Exception e) {
            System.out.println("服务器异常: " + e.getMessage());
        }*/

        MyFrame window = new MyFrame();
    }
}
