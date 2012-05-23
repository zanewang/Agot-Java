package got.logic;

import got.core.Node;
import got.core.ShiftStateEvent;
import got.io.MessageType;
import got.io.Packet;

import org.jgroups.Address;

public class CompeteIronHandler extends EventManager {

    public CompeteIronHandler(Node node) {
        super(node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        if (!packet.isReply()) {
            logger.debug("Client: The Clash of Kings ");
            logger.debug("Client: Start to compete the Iron");
            node.getMyFamilyInfo().resetCompetePower();
            node.getGameInfo().shiftTO(GameState.Ready_Westeros_Clash_Kings_Iron);
            node.onQueueEvent(new ShiftStateEvent());
        } else {
            if (!gameInfo.isHost()) {
                return;
            }
            logger.debug("Server: Recive the Compete Iron Reply");
            updateSpecifyFamily(address, packet);
            serverGameInfo.reOrderInfluence(MessageType.Compete_Iron);
            serverGameInfo.getCdl().get(MessageType.Compete_Iron).countDown();
        }
    }

}
