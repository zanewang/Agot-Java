package got.logic;

import got.core.Node;
import got.io.MessageType;
import got.io.Packet;
import got.pojo.event.FamilyInfo;

import java.util.concurrent.CountDownLatch;

import org.jgroups.Address;

public class JoinHandler extends EventManager {

    public JoinHandler(Node node) {
        super(node);
        // TODO Auto-generated constructor stub
    }

    private synchronized void join(Address address, Packet packet) throws Exception {
        if (!gameInfo.isHost()) {
            return;
        }
        int currentPlayerNum = serverGameInfo.getAddrMap().size();
        FamilyInfo info = serverGameInfo.getFamiliesMap().get(serverGameInfo.getShuffledFamilies().remove(0));
        info.setAddress(address);
        serverGameInfo.getAddrMap().put(address, info);
        logger.debug(address + " random choose the family " + info.getName());
        CountDownLatch updateFamiliesCDL = new CountDownLatch(currentPlayerNum);
        serverGameInfo.getCdl().put(MessageType.Update_Families, updateFamiliesCDL);
        node.sendGameStatusToClient();
        updateFamiliesCDL.await();
        node.send(MessageType.Join_R);
       
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        join(address, packet);
    }

}
