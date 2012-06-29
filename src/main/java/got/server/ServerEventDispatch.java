package got.server;

import got.user.Account;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ServerEventDispatch implements Runnable {

    private ArrayBlockingQueue<Message> msgQueue = new ArrayBlockingQueue<Message>(32567);

    private Server server;

    public ServerEventDispatch(Server server) {
        this.server = server;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            Message msg;
            try {
                msg = msgQueue.take();
                server.getServerEventManager().exec(msg);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void enque(Message msg) {
        msgQueue.add(msg);
    }

    public void pushMessage(Account account, Message message) {
        server.getChannelMap().get(account).write(message);
    }

}
