package got.logic;

import got.core.Node;
import got.core.ShiftStateEvent;
import got.io.MessageType;
import got.io.Packet;

import org.jgroups.Address;

public class OrderHandler extends EventManager {

    public OrderHandler(Node node) {
        super(node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        if (!packet.isReply()) {
            logger.info("Client: Start to place the order command");
            node.getMyFamilyInfo().resetActions();
            node.getGameInfo().shiftTO(GameState.Ready_Order);
            node.onQueueEvent(new ShiftStateEvent());
        }
        else{
            if (!gameInfo.isHost()) {
                return;
            }
            logger.debug("Server: Receive the Order Reply from " + address);
            updateSpecifyFamily(address, packet);
            serverGameInfo.getCdl().get(MessageType.Order).countDown();
            node.sendInfo(String.format("%s Finished the Planning Phase",  packet.getGameStatus().getAddrMap().get(address).getName()));
        }
    }

}
