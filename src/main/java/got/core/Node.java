package got.core;

import got.io.MessageType;
import got.io.Packet;
import got.logic.RequestManager;
import got.pojo.GameInfo;
import got.pojo.event.FamilyInfo;
import got.pojo.event.GameStatus;
import got.ui.MainFrame;
import got.ui.image.ImageLoader;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

public class Node {

    private JChannel channel;

    private GameInfo gameInfo;

    private GameInfo serverGameInfo;

    private ImageLoader imageLoader;

    private RequestManager requestManager;

    public MainFrame gameFrame;

    public BlockingQueue<GameEvent> gameEvents = new ArrayBlockingQueue<GameEvent>(20);

    private String properties;

    private final static String PROP_FORMAT = "TCP(bind_port=7800;bind_addr=%s):"
            + "TCPPING(timeout=3000;initial_hosts=%s[7800];port_range=1;num_initial_members=2):"
            + "MERGE2(min_interval=10000;max_interval=30000):" + "FD_SOCK:" + "FD(timeout=3000;max_tries=3):"
            + "VERIFY_SUSPECT(timeout=1500):" + "BARRIER:"
            + "pbcast.NAKACK(use_mcast_xmit=false;exponential_backoff=500;discard_delivered_msgs=true):" + "UNICAST:"
            + "pbcast.STABLE(stability_delay=1000;desired_avg_gossip=50000;max_bytes=4M):"
            + "pbcast.GMS(print_local_addr=true;join_timeout=3000;view_bundling=true):"
            + "UFC(max_credits=2M;min_threshold=0.4):" + "MFC(max_credits=2M;min_threshold=0.4):"
            + "FRAG2(frag_size=60K):" + "pbcast.STATE_TRANSFER";

    // private Queue<>
    private ExecutorService pool = Executors.newCachedThreadPool();

    public Node() {
        setImageLoader(new ImageLoader());
        getImageLoader().load();
        this.gameFrame = new MainFrame(this);
    }

    public void init(String localAddr, String hostAddr) throws Exception {
        boolean isHost;
        if (hostAddr == null) {
            properties = String.format(PROP_FORMAT, localAddr, localAddr);
            isHost = true;
        } else {
            properties = String.format(PROP_FORMAT, localAddr, hostAddr);
            isHost = false;
        }
        setChannel(new JChannel(properties));
        getChannel().setReceiver(new Receiver());
        getChannel().connect("AGOT");
        serverGameInfo = new GameInfo();
        serverGameInfo.setHost(isHost);
        serverGameInfo.init();
        gameInfo = new GameInfo();
        gameInfo.setHost(isHost);
        gameInfo.init();
        requestManager = new RequestManager(this);
    }

    public JChannel getChannel() {
        return channel;
    }

    public void setChannel(JChannel channel) {
        this.channel = channel;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public GameInfo getServerGameInfo() {
        return serverGameInfo;
    }

    public void setServerGameInfo(GameInfo serverGameInfo) {
        this.serverGameInfo = serverGameInfo;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public FamilyInfo getMyFamilyInfo() {
        return gameInfo.getAddrMap().get(channel.getAddress());
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
                    requestManager.exec(address, packet);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void send(MessageType type) throws Exception {
        Packet packet = new Packet();
        packet.setType(type);
        Message message = new Message();
        message.setObject(packet);
        channel.send(message);
    }

    public void send(MessageType type, Address address) throws Exception {
        Packet packet = new Packet();
        packet.setType(type);
        packet.setReply(false);
        Message message = new Message(address);
        message.setObject(packet);
        channel.send(message);
    }

    public void sendInfo(String info) throws Exception {
        Packet packet = new Packet();
        packet.setType(MessageType.Info);
        packet.setInfo(info);
        Message message = new Message();
        message.setObject(packet);
        channel.send(message);
    }
    
    public void sendInfo(String info,Address address) throws Exception {
        Packet packet = new Packet();
        packet.setType(MessageType.Info);
        packet.setInfo(info);
        Message message = new Message(address);
        message.setObject(packet);
        channel.send(message);
    }

    public void sendJoin(String name) throws Exception {
        Message message = new Message();
        Packet packet = new Packet();
        packet.setType(MessageType.Join);
        packet.setpName(name);
        message.setObject(packet);
        channel.send(message);
    }

    public void sendGameStatusUpdate(MessageType type) throws Exception {
        Packet packet = new Packet();
        packet.setType(type);
        packet.setReply(true);
        packet.setGameStatus(new GameStatus().update(gameInfo));
        Message message = new Message();
        message.setObject(packet);
        channel.send(message);
    }

    public void sendGameStatusToClient() throws Exception {
        Packet packet = new Packet();
        packet.setType(MessageType.Update_Families);
        packet.setReply(false);
        packet.setGameStatus(new GameStatus().update(serverGameInfo));
        Message message = new Message();
        message.setObject(packet);
        channel.send(message);
    }

    public void onQueueEvent(GameEvent event) {
        gameEvents.offer(event);
    }

    public GameEvent deQueueEvent() {
        synchronized (gameEvents) {
            if (gameEvents.size() > 0) {
                return gameEvents.remove();
            } else {
                return null;
            }
        }
    }
}
