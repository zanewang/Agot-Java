package got.logic;

import got.core.Node;
import got.core.ShiftStateEvent;
import got.io.MessageType;
import got.io.Packet;

import org.jgroups.Address;

public class PowerHandler extends EventManager {

    public PowerHandler(Node node) {
        super(node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        if (!packet.isReply()) {
            logger.debug("Client: Consolidate Power");
            node.getGameInfo().shiftTO(GameState.Ready_Consolidate);
            node.onQueueEvent(new ShiftStateEvent());
        } else {
            if (!gameInfo.isHost()) {
                return;
            }
            updateSpecifyFamily(address, packet);
            serverGameInfo.getCdl().get(MessageType.Power).countDown();
        }
    }

}
