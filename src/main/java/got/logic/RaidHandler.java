package got.logic;

import got.core.Node;
import got.io.MessageType;
import got.io.Packet;

import org.jgroups.Address;

public class RaidHandler extends EventManager {

    public RaidHandler(Node node) {
        super(node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        if (!packet.isReply()) {
            logger.debug("Client: Ready to choose the raid command");
            gameInfo.shiftTO(GameState.Ready_Raid);
        } else {
            if (!gameInfo.isHost()) {
                return;
            }
            logger.debug("Server: Receive the Raid_R command");
            updateFamilies(packet);
            serverGameInfo.getCdl().get(MessageType.Raid).countDown();
        }
    }
}
