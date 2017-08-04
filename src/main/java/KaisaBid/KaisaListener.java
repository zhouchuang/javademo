package KaisaBid;

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by zhouchuang on 2017/7/19.
 */
public class KaisaListener implements Runnable {
    private DatagramSocket datagramSocket ;
    private JButton listener;
    public KaisaListener(DatagramSocket datagramSocket , JButton listener){
        this.datagramSocket = datagramSocket;
        this.listener = listener;
    }
    @Override
    public void run() {
        byte[] recvBuf = new byte[1024];
        while (true){
            DatagramPacket recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
            try{
                datagramSocket.receive(recvPacket);
            }catch (Exception e){
                e.printStackTrace();
            }
            String recvStr = new String(recvPacket.getData() , 0 ,recvPacket.getLength());
            System.out.println(recvStr);
            if(recvStr.equals("restart")){
                listener.doClick();
            }
        }
    }
}
