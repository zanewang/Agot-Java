package got.logic;

import got.core.Node;
import got.core.ShiftStateEvent;
import got.io.MessageType;
import got.io.Packet;

import org.jgroups.Address;

public class GeneralHandler extends EventManager {

    public GeneralHandler(Node node) {
        super(node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        if (!packet.isReply()) {
            logger.debug("Client: Receive the General Message");
            node.getGameInfo().shiftTO(GameState.Ready_Battle_General);
            node.onQueueEvent(new ShiftStateEvent());
        } else {
            logger.debug("Server: Recive the General_R Message from " + address);
            if (!gameInfo.isHost()) {
                return;
            }
            updateSpecifyFamily(address, packet);
            serverGameInfo.getCdl().get(MessageType.General).countDown();
        }
    }

}
