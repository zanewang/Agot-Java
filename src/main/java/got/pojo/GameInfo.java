package got.pojo;

import got.io.MessageType;
import got.logic.GameState;
import got.pojo.event.FamilyInfo;
import got.pojo.event.GameInfoHelper;
import got.pojo.event.TerritoryInfo;
import got.utility.PointFileReaderWriter;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.jgroups.Address;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GameInfo {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private GameState state = GameState.Game_Init;

    private String lastSelect;

    private int wildAttakRate = 0;

    private boolean isHost = false;

    private List<String> shuffledFamilies = new ArrayList<String>();

    private List<WesterosDeckI> shuffledWesterosDeckI = new ArrayList<WesterosDeckI>();
    private List<WesterosDeckII> shuffledWesterosDeckII = new ArrayList<WesterosDeckII>();
    private List<WesterosDeckIII> shuffledWesterosDeckIII = new ArrayList<WesterosDeckIII>();

    private Map<Arm, Integer> marchArms = new HashMap<Arm, Integer>();

    private Map<Address, FamilyInfo> addrmap = new HashMap<Address, FamilyInfo>();

    private Map<String, List<Point>> supplyPoints = new HashMap<String, List<Point>>();

    private Map<String, TerritoryInfo> terrMap = new HashMap<String, TerritoryInfo>();

    private Map<String, FamilyInfo> familiesMap = new HashMap<String, FamilyInfo>();

    private Map<String, CharacterInfo> characterMap = new HashMap<String, CharacterInfo>();

    private Map<String, List<String>> connTerritories = new HashMap<String, List<String>>();

    private Map<MessageType, CountDownLatch> cdl = new HashMap<MessageType, CountDownLatch>();

    private List<String> infoHistory = new ArrayList<String>();

    public int getPlayerNum() {
        return this.addrmap.size();
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean isHost) {
        this.isHost = isHost;
    }

    public Map<MessageType, CountDownLatch> getCdl() {
        return cdl;
    }

    public void setCdl(Map<MessageType, CountDownLatch> cdl) {
        this.cdl = cdl;
    }

    public Map<Address, FamilyInfo> getAddrMap() {
        return addrmap;
    }

    public void setAddrMap(Map<Address, FamilyInfo> addrmap) {
        this.addrmap = addrmap;
    }

    public void init() throws Exception {
        readTerritoriesXML();
        readTerritoryPolyTxt();
        readPlacementTxt();
        readSupplyPlacementTxt();
        readConnectionsXML();
        readFamiliesXML();
        readCharactersXML();
        initMarchArms();

        shuffleFamilies();
        shuffleWestorsDeckI();
        shuffleWestorsDeckII();
        shuffleWestorsDeckIII();
    }

    private void initMarchArms() {
        marchArms.put(Arm.KNIGHT, 0);
        marchArms.put(Arm.FOOTMAN, 0);
        marchArms.put(Arm.SHIP, 0);
    }

    private void shuffleFamilies() {
        // for(FamilyInfo fi : familiesMap.values()){
        // shuffledFamilies.add(fi);
        // }
        // Random rand = new Random(System.currentTimeMillis());
        // Collections.shuffle(shuffledFamilies, rand);
        // for test only
        getShuffledFamilies().add("GREYJOY");
        getShuffledFamilies().add("LANNISTER");
        getShuffledFamilies().add("STARK");

    }

    public void shuffleWestorsDeckI() {
        for (WesterosDeckI card : WesterosDeckI.values()) {
            for (int i = 0; i < card.getCardNum(); i++) {
                shuffledWesterosDeckI.add(card);
            }
        }
        Random rand = new Random(System.currentTimeMillis());
        Collections.shuffle(shuffledWesterosDeckI, rand);
    }

    public void shuffleWestorsDeckII() {
        for (WesterosDeckII card : WesterosDeckII.values()) {
            for (int i = 0; i < card.getCardNum(); i++) {
                shuffledWesterosDeckII.add(card);
            }
        }
        Random rand = new Random(System.currentTimeMillis());
        Collections.shuffle(shuffledWesterosDeckII, rand);
    }

    public void shuffleWestorsDeckIII() {
        for (WesterosDeckIII card : WesterosDeckIII.values()) {
            for (int i = 0; i < card.getCardNum(); i++) {
                shuffledWesterosDeckIII.add(card);
            }
        }
        Random rand = new Random(System.currentTimeMillis());
        Collections.shuffle(shuffledWesterosDeckIII, rand);
    }

    public List<FamilyInfo> getFamiliesJudgeRanking() {
        List<FamilyInfo> familiesRanking = new ArrayList<FamilyInfo>();
        for (int i = 1; i <= 5; i++) {
            for (FamilyInfo fi : addrmap.values()) {
                if (fi.getJudgeRank() == i) {
                    familiesRanking.add(fi);
                    break;
                }
            }
        }
        return familiesRanking;
    }

    public List<FamilyInfo> getFamiliesIronRanking() {
        List<FamilyInfo> familiesRanking = new ArrayList<FamilyInfo>();
        for (int i = 1; i <= 5; i++) {
            for (FamilyInfo fi : addrmap.values()) {
                if (fi.getIronRank() == i) {
                    familiesRanking.add(fi);
                    break;
                }
            }
        }
        return familiesRanking;
    }

    public List<FamilyInfo> getFamiliesBladeRanking() {
        List<FamilyInfo> familiesRanking = new ArrayList<FamilyInfo>();
        for (int i = 1; i <= 5; i++) {
            for (FamilyInfo fi : addrmap.values()) {
                if (fi.getBladeRank() == i) {
                    familiesRanking.add(fi);
                    break;
                }
            }
        }
        return familiesRanking;
    }

    public List<FamilyInfo> getFamiliesRavenRanking() {
        List<FamilyInfo> familiesRanking = new ArrayList<FamilyInfo>();
        for (int i = 1; i <= 5; i++) {
            for (FamilyInfo fi : addrmap.values()) {
                if (fi.getRavenRank() == i) {
                    familiesRanking.add(fi);
                    break;
                }
            }
        }
        return familiesRanking;
    }

    public List<FamilyInfo> orderByCompetePower() {
        List<FamilyInfo> competeFamilies = new ArrayList<FamilyInfo>();
        for (FamilyInfo fi : addrmap.values()) {
            competeFamilies.add(fi);
        }
        Collections.sort(competeFamilies, new Comparator<FamilyInfo>() {

            @Override
            public int compare(FamilyInfo o1, FamilyInfo o2) {
                // TODO Auto-generated method stub
                return (o1.getCompetePower() > o2.getCompetePower() ? -1 : (o1.getCompetePower() == o2
                        .getCompetePower() ? 0 : 1));
            }

        });
        return competeFamilies;
    }

    public void readTerritoriesXML() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc;
        doc = dBuilder.parse(getClass().getResourceAsStream("/got/resource/xml/territory.xml"));
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("Territory");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            TerritoryInfo ti = new TerritoryInfo();
            Node nNode = nList.item(temp);
            String name = nNode.getAttributes().getNamedItem("Name").getNodeValue();
            String mustering = nNode.getAttributes().getNamedItem("Mustering").getNodeValue();
            String supply = nNode.getAttributes().getNamedItem("Supply").getNodeValue();
            String power = nNode.getAttributes().getNamedItem("Power").getNodeValue();
            String port = nNode.getAttributes().getNamedItem("Port").getNodeValue();
            String type = nNode.getAttributes().getNamedItem("Type").getNodeValue();
            ti.setName(name);
            ti.setMustering(Integer.valueOf(mustering));
            ti.setSupply(Integer.valueOf(supply));
            ti.setPower(Integer.valueOf(power));
            ti.setType(TerritoryType.values()[Integer.valueOf(type)]);
            ti.setAction(Action.NONE);
            for (Arm arm : Arm.values()) {
                ti.getConquerArms().put(arm, 0);
            }
            getTerrMap().put(ti.getName(), ti);
            getConnTerritories().put(ti.getName(), new ArrayList<String>());
        }
    }

    public void readFamiliesXML() throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(getClass().getResourceAsStream("/got/resource/xml/family.xml"));
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("Family");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            FamilyInfo fi = new FamilyInfo();
            Node nNode = nList.item(temp);
            String name = nNode.getAttributes().getNamedItem("Name").getNodeValue();
            String iron = nNode.getAttributes().getNamedItem("Iron").getNodeValue();
            String blade = nNode.getAttributes().getNamedItem("Blade").getNodeValue();
            String raven = nNode.getAttributes().getNamedItem("Raven").getNodeValue();
            String color = nNode.getAttributes().getNamedItem("Color").getNodeValue();
            fi.setName(name);
            fi.setIronRank(Integer.valueOf(iron));
            fi.setBladeRank(Integer.valueOf(blade));
            fi.setRavenRank(Integer.valueOf(raven));
            fi.setCurPower(5);
            Field field = Class.forName("java.awt.Color").getField(color);
            fi.setColor((Color) field.get(null));
            Element eElement = (Element) nNode;
            NodeList terris = eElement.getElementsByTagName("Territory");
            int buckets = 0;
            for (int j = 0; j < terris.getLength(); j++) {
                Node terr = terris.item(j);
                String terrName = terr.getAttributes().getNamedItem("Name").getNodeValue();
                int terrKnights = Integer.valueOf(terr.getAttributes().getNamedItem("Knight").getNodeValue());
                int terrFootMen = Integer.valueOf(terr.getAttributes().getNamedItem("FootMan").getNodeValue());
                int terrShips = Integer.valueOf(terr.getAttributes().getNamedItem("Ship").getNodeValue());
                TerritoryInfo ti = getTerrMap().get(terrName);
                ti.getConquerArms().put(Arm.KNIGHT, terrKnights);
                ti.getConquerArms().put(Arm.FOOTMAN, terrFootMen);
                ti.getConquerArms().put(Arm.SHIP, terrShips);
                ti.setConquerFamilyName(fi.getName());
                fi.getConquerTerritories().add(ti.getName());
                buckets += ti.getSupply();
            }
            fi.setSupply(buckets);
            this.familiesMap.put(fi.getName(), fi);
        }
    }

    public void readCharactersXML() throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(getClass().getResourceAsStream("/got/resource/xml/characters.xml"));
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("Character");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            CharacterInfo ci = new CharacterInfo();
            Node nNode = nList.item(temp);
            String name = nNode.getAttributes().getNamedItem("Name").getNodeValue();
            String familyName = nNode.getAttributes().getNamedItem("FamilyName").getNodeValue();
            String power = nNode.getAttributes().getNamedItem("Power").getNodeValue();
            String sword = nNode.getAttributes().getNamedItem("Sword").getNodeValue();
            String shield = nNode.getAttributes().getNamedItem("Shield").getNodeValue();
            String special = nNode.getAttributes().getNamedItem("Special").getNodeValue();

            ci.setName(name);
            ci.setFamilyName(familyName);
            ci.setPower(Integer.valueOf(power));
            ci.setSword(Integer.valueOf(sword));
            ci.setShield(Integer.valueOf(shield));
            ci.setSpecial(special);
            familiesMap.get(ci.getFamilyName()).getCharacterMap().put(ci.getName(), true);
            characterMap.put(ci.getName(), ci);
        }
    }

    public void readTerritoryPolyTxt() throws FileNotFoundException, IOException {
        Map<String, List<Polygon>> polys = PointFileReaderWriter.readOneToManyPolygons(getClass().getResourceAsStream(
                "/got/resource/txt/polygons.txt"));
        for (String name : polys.keySet()) {
            for (TerritoryInfo ti : getTerrMap().values()) {
                if (ti.getName().equals(name.trim())) {
                    ti.setPoly(polys.get(name).get(0));
                    break;
                }
            }
        }
    }

    public void readPlacementTxt() throws FileNotFoundException, IOException {
        Map<String, List<Point>> places = PointFileReaderWriter.readOneToMany(getClass().getResourceAsStream(
                "/got/resource/txt/place.txt"));
        // first 4 placements are for army, the last one is for order action
        for (String name : places.keySet()) {
            List<Point> points = places.get(name);
            TerritoryInfo ti = terrMap.get(name);
            ti.setOrderP(points.remove(points.size() - 1));
            ti.setArmPs(points);
        }
    }

    public void readSupplyPlacementTxt() throws FileNotFoundException, IOException {
        setSupplyPoints(PointFileReaderWriter.readOneToMany(getClass().getResourceAsStream(
                "/got/resource/txt/supply_place.txt")));
    }

    public void readConnectionsXML() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(getClass().getResourceAsStream("/got/resource/xml/connection.xml"));
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("Connection");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            String t1 = nNode.getAttributes().getNamedItem("t1").getNodeValue();
            String t2 = nNode.getAttributes().getNamedItem("t2").getNodeValue();
            // System.out.println(t1 + " : " + t2);
            getConnTerritories().get(t1).add(t2);
            getConnTerritories().get(t2).add(t1);
        }
    }

    public List<String> getBattleTerritories() {
        List<String> battleFamilies = new ArrayList<String>();
        for (TerritoryInfo ti : terrMap.values()) {
            // attack family is empty means no battle happen
            if (ti.getAttackFamilyName() == null) {
                continue;
            }

            if (!ti.getConquerFamilyName().equals(ti.getAttackFamilyName())) {
                // attack family is not empty and defense attack is not same
                // family. battle happen.
                battleFamilies.add(ti.getName());
            } else {
                // no battle, just march to self's territory, update the arm and
                // reset the attack family.
                for (Arm arm : ti.getAttackArms().keySet()) {
                    ti.getConquerArms().put(arm, ti.getAttackArms().get(arm));
                    ti.setAttackFamilyName(null);
                }
            }
        }
        return battleFamilies;
    }

    public List<FamilyInfo> getSupportFamilies() {
        return new ArrayList<FamilyInfo>();
    }

    public GameState getState() {
        return state;
    }

    public void shiftTO(GameState state) {
        logger.debug("Client : Game shift from " + this.state.toString() + " to " + state.toString());
        this.state = state;
    }

    public void setTerrMap(Map<String, TerritoryInfo> terrMap) {
        this.terrMap = terrMap;
    }

    public Map<String, TerritoryInfo> getTerrMap() {
        return terrMap;
    }

    public String getLastSelect() {
        return lastSelect;
    }

    public void setLastSelect(String lastSelect) {
        this.lastSelect = lastSelect;
    }

    public Map<String, List<String>> getConnTerritories() {
        return connTerritories;
    }

    public void setConnTerritories(Map<String, List<String>> connTerritories) {
        this.connTerritories = connTerritories;
    }

    public Map<Arm, Integer> getMarchArms() {
        return marchArms;
    }

    public void setMarchArms(Map<Arm, Integer> marchArms) {
        this.marchArms = marchArms;
    }

    public void battleNeutral() {
        // TODO Auto-generated method stub
        for (TerritoryInfo ti : terrMap.values()) {
            // if (ti.getIsBattle()) {
            // ti.setConquerFamilyName(ti.getAttackFamilyName());
            // ti.getConquerArms().put(Arm.KNIGHT,
            // ti.getAttackArms().get(Arm.KNIGHT));
            // ti.getConquerArms().put(Arm.FOOTMAN,
            // ti.getAttackArms().get(Arm.FOOTMAN));
            // ti.getConquerArms().put(Arm.SHIP,
            // ti.getAttackArms().get(Arm.SHIP));
            // ti.setIsBattle(false);
            // familiesMap.get(ti.getConquerFamilyName()).getTerrMap().put(ti.getName(),
            // ti);
            // logger.debug(ti.getConquerFamilyName() + " win the battle");
            //
            // }
        }
    }

    public Map<String, FamilyInfo> getFamiliesMap() {
        return familiesMap;
    }

    public void setFamiliesMap(Map<String, FamilyInfo> familiesMap) {
        this.familiesMap = familiesMap;
    }

    public Boolean canRaid(String source, String target) {
        TerritoryInfo sourceTerr = terrMap.get(source);
        TerritoryInfo targetTerr = terrMap.get(target);
        if (sourceTerr.getType().equals(TerritoryType.LAND) && targetTerr.getType().equals(TerritoryType.SEA)) {
            return false;
        }
        if (isConnnect(source, target) == false) {
            return false;
        }
        if (targetTerr.getAction().getType().equals(ActionType.March)) {
            return false;
        }
        if (targetTerr.getAction().getType().equals(ActionType.DEFENSE)) {
            return false;
        }
        if (targetTerr.getAction().getType().equals(ActionType.None)) {
            return false;
        }
        return true;
    }

    public Boolean canMarch(String source, String target, String familyName) {
        return this.getMarchableTerritory(source, familyName).contains(target);
    }

    public Boolean isConnnect(String start, String end) {
        return connTerritories.get(start).contains(end);
    }

    public Boolean canMarchBySea(String source, String target) {
        return false;
    }

    public List<String> getMarchableTerritory(String start, String familyName) {
        TerritoryInfo startTerr = terrMap.get(start);
        List<String> marchableTerrs = new ArrayList<String>();
        if (startTerr.getType().equals(TerritoryType.LAND)) {
            marchableTerrs.addAll(getNearbyTerritory(start, TerritoryType.LAND));
            List<String> mySeas = getNearbyTerritory(start, TerritoryType.SEA, familyName);
            List<String> connSeas = new ArrayList<String>();
            dfsSea(mySeas, connSeas, familyName);
            for (String sea : connSeas) {
                marchableTerrs.addAll(getNearbyTerritory(sea, TerritoryType.LAND));
            }
        } else if (startTerr.getType().equals(TerritoryType.SEA)) {
            marchableTerrs.addAll(getNearbyTerritory(start, TerritoryType.SEA));
        } else if (startTerr.getType().equals(TerritoryType.PORT)) {
            marchableTerrs.addAll(getNearbyTerritory(start, TerritoryType.SEA));
        }
        return marchableTerrs;
    }

    public List<String> getNearbyTerritory(String start, TerritoryType type) {
        TerritoryInfo startTerr = terrMap.get(start);
        List<String> nearbyTerrs = new ArrayList<String>();
        for (String connSeaName : connTerritories.get(start)) {
            TerritoryInfo connTerr = terrMap.get(connSeaName);
            if (connTerr.getType().equals(type)) {
                nearbyTerrs.add(connSeaName);
            }
        }
        return nearbyTerrs;
    }

    public List<String> getNearbyTerritory(String start, TerritoryType type, String familyName) {
        TerritoryInfo startTerr = terrMap.get(start);
        List<String> nearbyTerrs = new ArrayList<String>();
        for (String connSeaName : connTerritories.get(start)) {
            TerritoryInfo connTerr = terrMap.get(connSeaName);
            if (connTerr.getType().equals(type) && connTerr.getConquerFamilyName().equals(familyName)) {
                nearbyTerrs.add(connSeaName);
            }
        }
        return nearbyTerrs;
    }

    public List<String> getNearbyTerritoryWtihAction(String start, TerritoryType type, String familyName,
            ActionType actionType) {
        TerritoryInfo startTerr = terrMap.get(start);
        List<String> nearbyTerrs = new ArrayList<String>();
        for (String connSeaName : connTerritories.get(start)) {
            TerritoryInfo connTerr = terrMap.get(connSeaName);
            if (connTerr.getType().equals(type) && connTerr.getConquerFamilyName().equals(familyName)
                    && connTerr.getType().equals(actionType)) {
                nearbyTerrs.add(connSeaName);
            }
        }
        return nearbyTerrs;
    }

    private void dfsSea(List<String> seas, List<String> connSeas, String familyName) {
        for (String sea : seas) {
            if (!connSeas.contains(sea)) {
                connSeas.add(sea);
                dfsSea(getNearbyTerritory(sea, TerritoryType.SEA, familyName), connSeas, familyName);
            }
        }
    }

    public Boolean isEnoughSupply(String familyName) {
        FamilyInfo fi = familiesMap.get(familyName);
        List<Integer> armsRequireSupplies = new ArrayList<Integer>();
        for (String terrName : fi.getConquerTerritories()) {
            TerritoryInfo terr = terrMap.get(terrName);
            if (terr.getConquerArmNum() > 1) {
                armsRequireSupplies.add(terr.getConquerArmNum());
            }
        }
        Collections.sort(armsRequireSupplies, Collections.reverseOrder());
        int[] availSupplies = GameInfoHelper.getSupplies(fi.getSupply());
        if (armsRequireSupplies.size() > availSupplies.length) {
            return false;
        }
        for (int i = 0; i < armsRequireSupplies.size(); i++) {
            if (armsRequireSupplies.get(i) > availSupplies[i]) {
                return false;
            }
        }
        return true;
    }

    public Boolean isEnoughSupplyWithMuster(String familyName, String musterTerrName) {
        FamilyInfo fi = familiesMap.get(familyName);
        List<Integer> armsRequireSupplies = new ArrayList<Integer>();
        for (String terrName : fi.getConquerTerritories()) {
            TerritoryInfo terr = terrMap.get(terrName);
            int totalArm = terr.getConquerArmNum();
            if (terrName.equals(musterTerrName)) {
                totalArm += 1;
            }
            if (terr.getConquerArmNum() > 1) {
                armsRequireSupplies.add(terr.getConquerArmNum());
            }
        }
        Collections.sort(armsRequireSupplies, Collections.reverseOrder());
        int[] availSupplies = GameInfoHelper.getSupplies(fi.getSupply());
        if (armsRequireSupplies.size() > availSupplies.length) {
            return false;
        }
        for (int i = 0; i < armsRequireSupplies.size(); i++) {
            if (armsRequireSupplies.get(i) > availSupplies[i]) {
                return false;
            }
        }
        return true;
    }

    public Boolean needIronJudgemenet() {
        Set<Integer> diff = new HashSet<Integer>();
        for (FamilyInfo fi : familiesMap.values()) {
            diff.add(fi.getCompetePower());
        }

        return diff.size() < addrmap.size();

    }

    public void reOrderInfluence(MessageType type) {
        logger.debug("Server: Re-Order the Influence");
        List<FamilyInfo> families = orderByCompetePower();
        for (int i = 0; i < families.size(); i++) {
            switch (type) {
            case Judge_Iron:
                families.get(i).setIronRank(i + 1);
                break;
            case Judge_Blade:
                families.get(i).setBladeRank(i + 1);
                break;
            case Judge_Raven:
                families.get(i).setRavenRank(i + 1);
                break;
            case Wild_Attack:
                families.get(i).setJudgeRank(i + 1);
                break;
            }
        }
    }

    public void resetSupplyBuckets(String familyName) {
        FamilyInfo fi = familiesMap.get(familyName);
        int buckets = 0;
        for (String terrName : fi.getConquerTerritories()) {
            buckets += terrMap.get(terrName).getSupply();
        }
        fi.setSupply(buckets);
    }

    public Map<String, List<Point>> getSupplyPoints() {
        return supplyPoints;
    }

    public void setSupplyPoints(Map<String, List<Point>> supplyPoints) {
        this.supplyPoints = supplyPoints;
    }

    public void setShuffledFamilies(List<String> shuffledFamilies) {
        this.shuffledFamilies = shuffledFamilies;
    }

    public List<String> getShuffledFamilies() {
        return shuffledFamilies;
    }

    public List<String> searchNearbyTerriotryWithSupport(String tiName, String attackFamilyName,
            String defenseFamilyName) {
        List<String> supportTerrs = new ArrayList<String>();
        List<String> supportTerrNames = new ArrayList<String>();
        TerritoryInfo ti = terrMap.get(tiName);
        if (ti.getType().equals(TerritoryType.SEA)) {
            supportTerrNames = getNearbyTerritory(ti.getName(), TerritoryType.SEA);
            supportTerrNames.addAll(getNearbyTerritory(ti.getName(), TerritoryType.PORT));

        } else if (ti.getType().equals(TerritoryType.LAND)) {
            supportTerrNames = getNearbyTerritory(ti.getName(), TerritoryType.SEA);
            supportTerrNames.addAll(getNearbyTerritory(ti.getName(), TerritoryType.LAND));
        }
        for (String supportTerrName : supportTerrNames) {
            TerritoryInfo supportti = terrMap.get(supportTerrName);
            if (supportti.getAction().getType().equals(ActionType.Support)
                    && !supportti.getConquerFamilyName().equals(attackFamilyName)
                    && !supportti.getConquerFamilyName().equals(defenseFamilyName)) {
                supportTerrs.add(supportTerrName);
            }
        }
        return supportTerrs;
    }

    public int getTerritoryPowerTokens(String familyName) {
        int tokens = 0;
        FamilyInfo fi = familiesMap.get(familyName);
        for (String terrName : fi.getConquerTerritories()) {
            TerritoryInfo ti = terrMap.get(terrName);
            tokens += ti.getPower();
        }
        return tokens;
    }

    public int getTerritoryConsolidateActions(String familyName) {
        int tokens = 0;
        FamilyInfo fi = familiesMap.get(familyName);
        for (String terrName : fi.getConquerTerritories()) {
            TerritoryInfo ti = terrMap.get(terrName);
            if (ti.getAction().getType().equals(ActionType.CONSOLIDATE))
                tokens += 1 + ti.getPower();
        }
        return tokens;
    }

    public int getUsedStarOrders(String familyName) {
        int num = 0;
        FamilyInfo fi = familiesMap.get(familyName);
        for (Action order : fi.getActionMap().keySet()) {
            if (order.isStar() && !fi.getActionMap().get(order)) {
                num += 1;
            }
        }
        return num;
    }

    public String powerVS(String terrName) {
        TerritoryInfo ti = terrMap.get(terrName);
        int defenseFamilyCapabilities = ti.getConquerCapabilities() + ti.getConquerRate() + ti.getConquerSeaSupport()
                + ti.getConquerLandSupport();
        int attackFamilyCapabilities = ti.getAttackCapabilities() + ti.getAttackRate() + ti.getAttackSeaSupport()
                + ti.getAttackLandSupport();
        return String.format("Attack %s %d Powers VS Defense %s %d Powers", ti.getAttackFamilyName(),
                attackFamilyCapabilities, ti.getConquerFamilyName(), defenseFamilyCapabilities);
    }

    public Map<String, CharacterInfo> getCharacterMap() {
        return characterMap;
    }

    public void setCharacterMap(Map<String, CharacterInfo> characterMap) {
        this.characterMap = characterMap;
    }

    public List<WesterosDeckI> getShuffledWesterosDeckI() {
        return shuffledWesterosDeckI;
    }

    public void setShuffledWesterosDeckI(List<WesterosDeckI> shuffledWesterosDeckI) {
        this.shuffledWesterosDeckI = shuffledWesterosDeckI;
    }

    public List<WesterosDeckII> getShuffledWesterosDeckII() {
        return shuffledWesterosDeckII;
    }

    public void setShuffledWesterosDeckII(List<WesterosDeckII> shuffledWesterosDeckII) {
        this.shuffledWesterosDeckII = shuffledWesterosDeckII;
    }

    public List<WesterosDeckIII> getShuffledWesterosDeckIII() {
        return shuffledWesterosDeckIII;
    }

    public void setShuffledWesterosDeckIII(List<WesterosDeckIII> shuffledWesterosDeckIII) {
        this.shuffledWesterosDeckIII = shuffledWesterosDeckIII;
    }

    public void setInfoHistory(List<String> infoHistory) {
        this.infoHistory = infoHistory;
    }

    public List<String> getInfoHistory() {
        return infoHistory;
    }

    public void setWildAttakRate(int wildAttakRate) {
        this.wildAttakRate = wildAttakRate;
    }

    public int getWildAttakRate() {
        return wildAttakRate;
    }

}
