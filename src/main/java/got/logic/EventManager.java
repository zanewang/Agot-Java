package got.logic;

import got.core.Node;
import got.io.Packet;
import got.pojo.GameInfo;
import got.pojo.event.GameStatus;

import org.apache.log4j.Logger;
import org.jgroups.Address;
import org.jgroups.JChannel;

public abstract class EventManager implements EventHandler {

    protected Logger logger = Logger.getLogger(this.getClass().getName());
    protected GameInfo gameInfo;
    protected GameInfo serverGameInfo;
    protected JChannel channel;

    protected Node node;

    public EventManager(Node node) {
        this.node = node;
        this.gameInfo = node.getGameInfo();
        this.serverGameInfo = node.getServerGameInfo();
        this.channel = node.getChannel();
    }

    protected void updateSpecifyFamily(Address address, Packet packet) {

        GameStatus gameStatus = packet.getGameStatus();
        String familyName = gameStatus.getAddrMap().get(address).getName();
        for (String updateTerrName : gameStatus.getAddrMap().get(address).getConquerTerritories()) {
            serverGameInfo.getTerrMap().put(updateTerrName, gameStatus.getTerrMap().get(updateTerrName));
        }
        serverGameInfo.getFamiliesMap().put(familyName, gameStatus.getFamliesMap().get(familyName));
        serverGameInfo.getAddrMap().put(address, gameStatus.getAddrMap().get(address));

    }

    protected void updateFamilies(Packet packet) {
        GameStatus gameStatus = packet.getGameStatus();
        serverGameInfo.setAddrMap(gameStatus.getAddrMap());
        serverGameInfo.setTerrMap(gameStatus.getTerrMap());
        serverGameInfo.setFamiliesMap(gameStatus.getFamliesMap());
        serverGameInfo.setWildAttakRate(gameStatus.getWildAttackRate());
        if (gameStatus.getLastSelectTerrName() != null) {
            serverGameInfo.setLastSelect(gameStatus.getLastSelectTerrName());
        }
    }
    
    protected void updateClientFamilies(Packet packet) {
        GameStatus gameStatus = packet.getGameStatus();
        gameInfo.setAddrMap(gameStatus.getAddrMap());
        gameInfo.setTerrMap(gameStatus.getTerrMap());
        gameInfo.setFamiliesMap(gameStatus.getFamliesMap());
        gameInfo.setWildAttakRate(gameStatus.getWildAttackRate());
        if (gameStatus.getLastSelectTerrName() != null) {
            gameInfo.setLastSelect(gameStatus.getLastSelectTerrName());
        }
    }
}
