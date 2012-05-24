package got.logic;

import got.core.Node;
import got.core.ShiftStateEvent;
import got.io.MessageType;
import got.io.Packet;

import org.jgroups.Address;

public class RetreatHandler extends EventManager {

    public RetreatHandler(Node node) {
        super(node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        if (!packet.isReply()) {
            logger.debug("Client: The Retreat Message ");
            node.getGameInfo().shiftTO(GameState.Ready_Retreat);
            node.onQueueEvent(new ShiftStateEvent());
        } else {
            if (!gameInfo.isHost()) {
                return;
            }
            updateFamilies(packet);
            serverGameInfo.getCdl().get(MessageType.Retreat).countDown();
        }
    }

}
