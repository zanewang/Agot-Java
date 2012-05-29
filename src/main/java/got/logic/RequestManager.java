package got.logic;

import got.core.Node;
import got.core.ShiftStateEvent;
import got.io.MessageType;
import got.io.Packet;
import got.ui.InfoStatusDialog;

import java.util.HashMap;
import java.util.Map;

import org.jgroups.Address;

public class RequestManager {

    private Map<MessageType, EventManager> emap = new HashMap<MessageType, EventManager>();

    public RequestManager(Node node) {
        emap.put(MessageType.Join, new JoinHandler(node));
        emap.put(MessageType.Join_R, new JoinRHandler(node));
        emap.put(MessageType.Start, new StartHandler(node));

        emap.put(MessageType.Info, new InfoHandler(node));
        emap.put(MessageType.Update_Families, new UpdateFamiliesHandler(node));

        emap.put(MessageType.Order, new OrderHandler(node));
        emap.put(MessageType.Raven, new RavenHandler(node));
        emap.put(MessageType.Raid, new RaidHandler(node));
        emap.put(MessageType.March, new MarchHandler(node));
        emap.put(MessageType.Support, new SupportHandler(node));
        emap.put(MessageType.Power, new PowerHandler(node));
        emap.put(MessageType.Mustering, new MusteringHandler(node));
        emap.put(MessageType.Supply, new SupplyHandler(node));
        emap.put(MessageType.General, new GeneralHandler(node));
        emap.put(MessageType.Retreat, new RetreatHandler(node));
        emap.put(MessageType.Blade, new BladeHandler(node));
        emap.put(MessageType.Wild_Attack, new WildAttackHandler(node));
        emap.put(MessageType.Disarm, new DisarmHandler(node));
        emap.put(MessageType.Ransom, new RansomHandler(node));

        emap.put(MessageType.Compete_Iron, new CompeteIronHandler(node));
        emap.put(MessageType.Compete_Blade, new CompeteBladeHandler(node));
        emap.put(MessageType.Compete_Raven, new CompeteRavenHandler(node));

        emap.put(MessageType.Judge_Blade, new JudgeBladeHandler(node));
        emap.put(MessageType.Judge_Iron, new JudgeIronHandler(node));
        emap.put(MessageType.Judge_Raven, new JudgeRavenHandler(node));
        emap.put(MessageType.Judge_Disarm, new JudgeDisarmHandler(node));
        emap.put(MessageType.Judge_General, new JudgeGeneralHandler(node));

        emap.put(MessageType.Asha_Special, new AshaSpecialHandler(node));
        emap.put(MessageType.Cersei_Special, new CerseiSpecialHandler(node));
        emap.put(MessageType.LuWin_Special, new LuWinSpecialHandler(node));
        emap.put(MessageType.Renly_Special, new RenlySpecialHandler(node));
        emap.put(MessageType.Melisandre_Special, new MelisandreSpecialHandler(node));

    }

    public void exec(Address address, Packet packet) throws Exception {
        emap.get(packet.getType()).handler(address, packet);
    }

    class InfoHandler extends EventManager {

        public InfoHandler(Node node) {
            super(node);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void handler(Address address, Packet packet) throws Exception {
            // TODO Auto-generated method stub
            node.getGameInfo().getInfoHistory().add(packet.getInfo());
            // fix me the new object expenses;
            if (node.gameFrame.getState() == 0) {
                node.gameFrame.addAndUpdate(new InfoStatusDialog(node, packet.getInfo()));
            }
        }

    }

    class AshaSpecialHandler extends EventManager {

        public AshaSpecialHandler(Node node) {
            super(node);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void handler(Address address, Packet packet) throws Exception {
            // TODO Auto-generated method stub
            if (!gameInfo.isHost()) {
                node.getGameInfo().shiftTO(GameState.Ready_Asha_Special);
                node.onQueueEvent(new ShiftStateEvent());
            } else {
                // String name =
                // packet.getGameStatus().getAddrMap().get(address).getName();
                // node.sendInfo(String.format("Family %s finish casting the special ability",
                // name));
                updateFamilies(packet);
                serverGameInfo.getCdl().get(MessageType.Asha_Special).countDown();
            }
        }
    }

    class CerseiSpecialHandler extends EventManager {

        public CerseiSpecialHandler(Node node) {
            super(node);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void handler(Address address, Packet packet) throws Exception {
            // TODO Auto-generated method stub
            if (!gameInfo.isHost()) {
                node.getGameInfo().shiftTO(GameState.Ready_Cersei_Special);
                node.onQueueEvent(new ShiftStateEvent());
            } else {
                updateFamilies(packet);
                serverGameInfo.getCdl().get(MessageType.Cersei_Special).countDown();
            }
        }

    }

    class LuWinSpecialHandler extends EventManager {

        public LuWinSpecialHandler(Node node) {
            super(node);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void handler(Address address, Packet packet) throws Exception {
            // TODO Auto-generated method stub
            if (!gameInfo.isHost()) {
                node.getGameInfo().shiftTO(GameState.Ready_LuWin_Special);
                node.onQueueEvent(new ShiftStateEvent());
            } else {
                updateFamilies(packet);
                serverGameInfo.getCdl().get(MessageType.LuWin_Special).countDown();
            }
        }

    }

    class RenlySpecialHandler extends EventManager {

        public RenlySpecialHandler(Node node) {
            super(node);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void handler(Address address, Packet packet) throws Exception {
            // TODO Auto-generated method stub
            if (!gameInfo.isHost()) {
                node.getGameInfo().shiftTO(GameState.Ready_Renly_Special);
                node.onQueueEvent(new ShiftStateEvent());
            } else {
                updateFamilies(packet);
                serverGameInfo.getCdl().get(MessageType.Renly_Special).countDown();
            }
        }

    }

    class MelisandreSpecialHandler extends EventManager {

        public MelisandreSpecialHandler(Node node) {
            super(node);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void handler(Address address, Packet packet) throws Exception {
            // TODO Auto-generated method stub
            if (!gameInfo.isHost()) {
                node.getGameInfo().shiftTO(GameState.Ready_Melisandre_Special);
                node.onQueueEvent(new ShiftStateEvent());
            } else {
                updateFamilies(packet);
                serverGameInfo.getCdl().get(MessageType.Melisandre_Special).countDown();
            }
        }

    }

    class WildAttackHandler extends EventManager {

        public WildAttackHandler(Node node) {
            super(node);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void handler(Address address, Packet packet) throws Exception {
            // TODO Auto-generated method stub
            if (!packet.isReply()) {
                node.getGameInfo().shiftTO(GameState.Ready_Wild_Attack);
                node.onQueueEvent(new ShiftStateEvent());
            } else {
                if (!gameInfo.isHost()) {
                    return;
                }
                updateSpecifyFamily(address, packet);
                serverGameInfo.getCdl().get(MessageType.Wild_Attack).countDown();
            }
        }
    }

    class JudgeDisarmHandler extends EventManager {

        public JudgeDisarmHandler(Node node) {
            super(node);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void handler(Address address, Packet packet) throws Exception {
            // TODO Auto-generated method stub
            if (!packet.isReply()) {
                node.getGameInfo().shiftTO(GameState.Ready_Judge_Disarm);
                node.onQueueEvent(new ShiftStateEvent());
            } else {
                if (!gameInfo.isHost()) {
                    return;
                }
                updateSpecifyFamily(address, packet);
                serverGameInfo.getCdl().get(MessageType.Wild_Attack).countDown();
            }
        }

    }

    class JudgeGeneralHandler extends EventManager {

        public JudgeGeneralHandler(Node node) {
            super(node);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void handler(Address address, Packet packet) throws Exception {
            // TODO Auto-generated method stub
            if (!packet.isReply()) {
                node.getGameInfo().shiftTO(GameState.Ready_Judge_General);
                node.onQueueEvent(new ShiftStateEvent());
            } else {
                if (!gameInfo.isHost()) {
                    return;
                }
                updateFamilies(packet);
                serverGameInfo.getCdl().get(MessageType.Judge_General).countDown();
            }
        }

    }

    class DisarmHandler extends EventManager {

        public DisarmHandler(Node node) {
            super(node);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void handler(Address address, Packet packet) throws Exception {
            // TODO Auto-generated method stub
            if (!packet.isReply()) {
                node.getGameInfo().shiftTO(GameState.Ready_Disarm);
                node.onQueueEvent(new ShiftStateEvent());
            } else {
                if (!gameInfo.isHost()) {
                    return;
                }
                updateSpecifyFamily(address, packet);
                serverGameInfo.getCdl().get(MessageType.Disarm).countDown();
            }
        }

    }

    class RansomHandler extends EventManager {

        public RansomHandler(Node node) {
            super(node);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void handler(Address address, Packet packet) throws Exception {
            // TODO Auto-generated method stub
            if (!packet.isReply()) {
                node.getGameInfo().shiftTO(GameState.Ready_Ransom);
                node.onQueueEvent(new ShiftStateEvent());
            } else {
                if (!gameInfo.isHost()) {
                    return;
                }
                updateSpecifyFamily(address, packet);
                serverGameInfo.getCdl().get(MessageType.Ransom).countDown();
            }
        }

    }
}
