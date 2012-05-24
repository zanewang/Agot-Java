package got.logic;

import got.core.Node;
import got.io.MessageType;
import got.io.Packet;

import org.jgroups.Address;

public class UpdateFamiliesHandler extends EventManager {

    public UpdateFamiliesHandler(Node node) {
        super(node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        if (!packet.isReply()) {
            logger.debug("Client: Receive the Update_Families");
            updateClientFamilies(packet);
            node.sendGameStatusUpdate(MessageType.Update_Families);
        } else {
            if (!gameInfo.isHost()) {
                return;
            }
            logger.debug("Server: Count down the Update_Families_R");
            serverGameInfo.getCdl().get(MessageType.Update_Families).countDown();
        }
    }

}
