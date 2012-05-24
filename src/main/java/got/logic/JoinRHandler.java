package got.logic;

import got.core.Node;
import got.core.ShiftStateEvent;
import got.io.Packet;

import org.jgroups.Address;

public class JoinRHandler extends EventManager {

    public JoinRHandler(Node node) {
        super(node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        gameInfo.shiftTO(GameState.Ready_Start);
        node.onQueueEvent(new ShiftStateEvent());
    }

}
