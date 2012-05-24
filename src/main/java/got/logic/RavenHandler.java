package got.logic;

import got.core.Node;
import got.core.ShiftStateEvent;
import got.io.MessageType;
import got.io.Packet;

import org.jgroups.Address;

public class RavenHandler extends EventManager {

    public RavenHandler(Node node) {
        super(node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        if (!packet.isReply()) {
            logger.debug("Client: update with the new order command");
            node.getGameInfo().shiftTO(GameState.Ready_Raven);
            node.onQueueEvent(new ShiftStateEvent());
        }
        else{
            if(!gameInfo.isHost()){
                return;
            }
            updateSpecifyFamily(address,packet);
            serverGameInfo.getCdl().get(MessageType.Raven).countDown();
        }
    }

}
