package got.server;

import got.client.ClientData;
import got.logic.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class ServerEventManager {
    private Map<MessageType, EventHandler> emap = new HashMap<MessageType, EventHandler>();
    private Server server;

    public ServerEventManager(Server server) {
        this.server = server;
        emap.put(MessageType.CONNECT, new ConnectEvent());
    }

    public void exec(Message message) throws Exception {
        emap.get(message.getType()).handler(message);
    }

    class ConnectEvent implements EventHandler {

        @Override
        public void handler(Message message) throws Exception {
            // TODO Auto-generated method stub
            if (!server.getServerGameData().isJoined(message.getAccount())) {
                Message msg = new Message();
                msg.setMsg("Server Reply");
                ClientData cd = new ClientData();
                cd.update(server.getServerGameData());
                msg.setClientData(cd);
                server.getServerEventDispatch().pushMessage(message.getAccount(), msg);
            } else {

            }
        }
    }
}
