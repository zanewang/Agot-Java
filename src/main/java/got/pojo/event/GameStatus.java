package got.pojo.event;

import got.pojo.GameInfo;

import java.io.Serializable;
import java.util.Map;

import org.jgroups.Address;

public class GameStatus implements Serializable {

    private Map<String, TerritoryInfo> terrMap;

    private Map<Address, FamilyInfo> addrMap;

    private Map<String, FamilyInfo> famliesMap;

    private String lastSelectTerrName;
    
    private int wildAttackRate;

    public Map<String, TerritoryInfo> getTerrMap() {
        return terrMap;
    }

    public Map<Address, FamilyInfo> getAddrMap() {
        return addrMap;
    }

    public Map<String, FamilyInfo> getFamliesMap() {
        return famliesMap;
    }

    public int getWildAttackRate() {
        return wildAttackRate;
    }

    public String getLastSelectTerrName() {
        return lastSelectTerrName;
    }

    public GameStatus update(GameInfo gameInfo) {
        terrMap = gameInfo.getTerrMap();
        addrMap = gameInfo.getAddrMap();
        famliesMap = gameInfo.getFamiliesMap();
        lastSelectTerrName = gameInfo.getLastSelect();
        wildAttackRate = gameInfo.getWildAttakRate();
        return this;
    }


}
