package got.pojo.event;

import got.pojo.Action;
import got.user.Account;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jgroups.Address;

public class FamilyInfo implements Serializable {

    // static configs
    private Color color;
    private int maxPower;
    private String name;
    private Account playerAcc;

    // dynamic configs

    private int supply;
    private int curPower;
    private int ironRank;
    private int bladeRank;
    private int ravenRank;
    private int judgeRank;
    private int competePower;
    private Address address;

    private Set<String> conquerTerritories = new HashSet<String>();

    private Map<Action, Boolean> actionMap = new HashMap<Action, Boolean>();

    private Map<String, Boolean> characterMap = new LinkedHashMap<String, Boolean>();

    private String battleCharacter;

    private int bladeRemain;

    private int raidRemain;

    private int disarmRemain;

    public FamilyInfo() {
        resetAction();
    }

    public FamilyInfo(String name) {
        this();
        this.name = name;
    }

    public void resetAction() {
        for (Action action : Action.values()) {
            getActionMap().put(action, true);
        }
    }

    public int getSupply() {
        return supply;
    }

    public void setSupply(int supply) {
        this.supply = supply;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(int maxPower) {
        this.maxPower = maxPower;
    }

    public int getCurPower() {
        return curPower;
    }

    public void myFamilyInfo(int curPower) {
        this.setCurPower(curPower);
    }

    public int getIronRank() {
        return ironRank;
    }

    public void setIronRank(int ironRank) {
        this.ironRank = ironRank;
    }

    public int getBladeRank() {
        return bladeRank;
    }

    public void setBladeRank(int bladeRank) {
        this.bladeRank = bladeRank;
    }

    public int getRavenRank() {
        return ravenRank;
    }

    public void setRavenRank(int ravenRank) {
        this.ravenRank = ravenRank;
    }

    public void setJudgeRank(int judgeRank) {
        this.judgeRank = judgeRank;
    }

    public int getJudgeRank() {
        return judgeRank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setActionMap(Map<Action, Boolean> actionMap) {
        this.actionMap = actionMap;
    }

    public Map<Action, Boolean> getActionMap() {
        return actionMap;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setCompetePower(int competePower) {
        this.competePower = competePower;
    }

    public int getCompetePower() {
        return competePower;
    }

    public void setCurPower(int curPower) {
        this.curPower = curPower;
    }

    public void resetCompetePower() {
        this.competePower = 0;
    }

    public void resetActions() {
        for (Action action : Action.values()) {
            actionMap.put(action, true);
        }
    }

    public Set<String> getConquerTerritories() {
        return conquerTerritories;
    }

    public void setConquerTerritories(HashSet<String> conquerTerritories) {
        this.conquerTerritories = conquerTerritories;
    }

    public Map<String, Boolean> getCharacterMap() {
        return characterMap;
    }

    public void setCharacterMap(Map<String, Boolean> characterMap) {
        this.characterMap = characterMap;
    }

    public String getBattleCharacter() {
        return battleCharacter;
    }

    public void setBattleCharacter(String battleCharacter) {
        this.battleCharacter = battleCharacter;
    }

    public void setBladeRemain(int bladeRemain) {
        this.bladeRemain = bladeRemain;
    }

    public int getBladeRemain() {
        return bladeRemain;
    }

    public void setRaidRemain(int raidRemain) {
        this.raidRemain = raidRemain;
    }

    public int getRaidRemain() {
        return raidRemain;
    }

    public String toString() {
        String toStr = "";
        toStr += String.format("FamilyName: %s\n", name);
        toStr += String.format("Supply: %d\n", supply);
        toStr += String.format("Power: %d\n", curPower);
        toStr += String.format("Iron: %d\n", ironRank);
        toStr += String.format("Blade: %d\n", bladeRank);
        toStr += String.format("Raven: %d\n", ravenRank);
        toStr += String.format("Territories: %s\n", StringUtils.join(conquerTerritories, ","));
        toStr += String.format("Character: %s\n", battleCharacter);
        toStr += String.format("Address: %s\n", address);
        return toStr;
    }

    public void setDisarmRemain(int disarmRemain) {
        this.disarmRemain = disarmRemain;
    }

    public int getDisarmRemain() {
        return disarmRemain;
    }

    public void setPlayerAcc(Account playerAcc) {
        this.playerAcc = playerAcc;
    }

    public Account getPlayerAcc() {
        return playerAcc;
    }

}