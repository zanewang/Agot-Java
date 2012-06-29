package got.client;

import got.logic.GameState;
import got.pojo.event.FamilyInfo;
import got.pojo.event.TerritoryInfo;
import got.server.ServerGameData;

import java.io.Serializable;
import java.util.Map;

public class ClientData implements Serializable {

    private GameState state;

    private Map<String, TerritoryInfo> terrMap;

    private Map<String, FamilyInfo> famliesMap;

    /**
     * @return the terrMap
     */
    public Map<String, TerritoryInfo> getTerrMap() {
        return terrMap;
    }

    /**
     * @param terrMap
     *            the terrMap to set
     */
    public void setTerrMap(Map<String, TerritoryInfo> terrMap) {
        this.terrMap = terrMap;
    }

    /**
     * @return the famliesMap
     */
    public Map<String, FamilyInfo> getFamliesMap() {
        return famliesMap;
    }

    /**
     * @param famliesMap
     *            the famliesMap to set
     */
    public void setFamliesMap(Map<String, FamilyInfo> famliesMap) {
        this.famliesMap = famliesMap;
    }

    public void update(ServerGameData serverGameData) {
        this.terrMap = serverGameData.getTerrMap();
        this.famliesMap = serverGameData.getFamiliesMap();
    }

    public void update(ClientData clientData) {
        this.terrMap = clientData.getTerrMap();
        this.famliesMap = clientData.getFamliesMap();
    }

    /**
     * @param state the state to set
     */
    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * @return the state
     */
    public GameState getState() {
        return state;
    }
}
