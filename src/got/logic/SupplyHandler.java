package got.logic;

import got.core.Node;
import got.core.ShiftStateEvent;
import got.io.MessageType;
import got.io.Packet;

import org.jgroups.Address;

public class SupplyHandler extends EventManager {

    public SupplyHandler(Node node) {
        super(node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        if (!packet.isReply()) {
            logger.debug("Client: Receive Supply Message");
            node.getGameInfo().shiftTO(GameState.Ready_Change_Supply);
            node.onQueueEvent(new ShiftStateEvent());
        } else {
            if (!gameInfo.isHost()) {
                return;
            }
            updateFamilies(packet);
            serverGameInfo.getCdl().get(MessageType.Supply).countDown();
        }
    }

}
