package got.client;

import got.server.Message;

import java.util.concurrent.ArrayBlockingQueue;

import org.jboss.netty.channel.Channel;

public class ClientMessageDispatch implements Runnable {
    private ArrayBlockingQueue<Message> msgQueue = new ArrayBlockingQueue<Message>(32567);

    private Thread thread;
    private Channel channel;

    public ClientMessageDispatch() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            try {
                Message msg = msgQueue.take();
                channel.write(msg);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void enque(Message msg) {
        msgQueue.offer(msg);
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
