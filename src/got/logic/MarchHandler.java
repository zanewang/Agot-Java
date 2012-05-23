package got.logic;

import got.core.Node;
import got.core.ShiftStateEvent;
import got.io.MessageType;
import got.io.Packet;

import org.jgroups.Address;

public class MarchHandler extends EventManager {

    public MarchHandler(Node node) {
        super(node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        if (!packet.isReply()) {
            node.getGameInfo().shiftTO(GameState.Ready_March);
            node.onQueueEvent(new ShiftStateEvent());
        } else {
            if (!gameInfo.isHost()) {
                return;
            }
            logger.debug("Server: recive the March command");
            updateFamilies(packet);
            serverGameInfo.getCdl().get(MessageType.March).countDown();
        }
    }

}
