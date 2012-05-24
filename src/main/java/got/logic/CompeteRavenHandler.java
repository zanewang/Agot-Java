package got.logic;

import got.core.Node;
import got.core.ShiftStateEvent;
import got.io.MessageType;
import got.io.Packet;

import org.jgroups.Address;

public class CompeteRavenHandler extends EventManager {

    public CompeteRavenHandler(Node node) {
        super(node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        if (!packet.isReply()) {
            logger.debug("Client: The Clash of Kings Raven");
            node.getMyFamilyInfo().resetCompetePower();
            node.getGameInfo().shiftTO(GameState.Ready_Westeros_Clash_Kings_Raven);
            node.onQueueEvent(new ShiftStateEvent());
        } else {
            if (!gameInfo.isHost()) {
                return;
            }
            logger.debug("Server: Recive the Compete Raven Reply");
            updateSpecifyFamily(address, packet);
            serverGameInfo.reOrderInfluence(MessageType.Compete_Raven);
            serverGameInfo.getCdl().get(MessageType.Compete_Raven).countDown();
        }
    }

}
