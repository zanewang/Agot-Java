package got.logic;


import got.io.MessageType;
import got.io.Packet;
import got.pojo.GameInfo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;


public class ConsoleMain {
    private JChannel channel;
    
    private RequestManager ge;

    private ExecutorService pool = Executors.newCachedThreadPool();

    public void init(String localAddr, boolean isHost) throws Exception {
        channel = new JChannel();
        channel.setReceiver(new Receiver());
        channel.connect("MyCluster");
        GameInfo gs = new GameInfo();
        gs.setHost(isHost);
        gs.init();
//        ge = new RequestManager(gs,channel);
    }

    public void join(String name) throws Exception {
        Message message = new Message();
        Packet packet = new Packet();
        packet.setType(MessageType.Join);
        packet.setpName(name);
        message.setObject(packet);
        channel.send(message);
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new LogInitiator().init();
        // TODO Auto-generated method stub
        ConsoleMain main = new ConsoleMain();
        if (args[0].equals("joiner")) {
            main.init("127.0.0.2", false);
            main.join(args[1]);
        } else {
            main.init("127.0.0.1", true);
            main.join(args[1]);
        }

    }

    class Receiver extends ReceiverAdapter {
        public void receive(Message msg) {

            Packet packet = (Packet) msg.getObject();
            Address address = msg.getSrc();
            pool.submit(new WorkThread(address, packet));
        }

        class WorkThread implements Runnable {

            private Address address;
            private Packet packet;

            public WorkThread(Address address, Packet packet) {
                this.address = address;
                this.packet = packet;
            }

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    ge.exec(address, packet);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}