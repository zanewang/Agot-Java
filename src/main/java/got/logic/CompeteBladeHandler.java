package got.logic;

import got.core.Node;
import got.core.ShiftStateEvent;
import got.io.MessageType;
import got.io.Packet;

import org.jgroups.Address;

public class CompeteBladeHandler extends EventManager {

    public CompeteBladeHandler(Node node) {
        super(node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        if (!packet.isReply()) {
            logger.debug("Client: The Clash of Kings Blade ");
            node.getMyFamilyInfo().resetCompetePower();
            node.getGameInfo().shiftTO(GameState.Ready_Westeros_Clash_Kings_Blade);
            node.onQueueEvent(new ShiftStateEvent());
        } else {
            if (!gameInfo.isHost()) {
                return;
            }
            logger.debug("Server: Recive the Compete Blade Reply");
            updateSpecifyFamily(address, packet);
            serverGameInfo.reOrderInfluence(MessageType.Compete_Blade);
            serverGameInfo.getCdl().get(MessageType.Compete_Blade).countDown();
        }
    }

}
