package got.logic;

import got.core.Node;
import got.core.ShiftStateEvent;
import got.io.Packet;

import org.jgroups.Address;

public class JudgeIronHandler extends EventManager {

    public JudgeIronHandler(Node node) {
        super(node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        if (!packet.isReply()) {
            logger.debug("Client: Receive the Judge Iron Message");
            node.getGameInfo().shiftTO(GameState.Ready_Westeros_Judge_Iron);
            node.onQueueEvent(new ShiftStateEvent());
        } else {
            if (!gameInfo.isHost()) {
                return;
            }
            updateFamilies(packet);
        }

    }

}
