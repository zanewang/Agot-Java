package got.logic;

import got.core.Node;
import got.core.ShiftStateEvent;
import got.io.MessageType;
import got.io.Packet;
import got.pojo.event.TerritoryInfo;

import org.jgroups.Address;

public class MusteringHandler extends EventManager {

    public MusteringHandler(Node node) {
        super(node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        if (!packet.isReply()) {
            logger.debug("Client : Receive the Muster command");
            for (String terrName : node.getMyFamilyInfo().getConquerTerritories()) {
                TerritoryInfo ti = node.getGameInfo().getTerrMap().get(terrName);
                ti.resetAvailMustering();
            }
            node.getGameInfo().shiftTO(GameState.Ready_Westeros_Mustering);
            node.onQueueEvent(new ShiftStateEvent());
        } else {
            if(!gameInfo.isHost()){
                return;
            }
            updateSpecifyFamily(address,packet);
            serverGameInfo.getCdl().get(MessageType.Mustering).countDown();
        }
    }

}
