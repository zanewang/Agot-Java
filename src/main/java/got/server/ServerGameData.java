package got.server;

import got.pojo.Action;
import got.pojo.Arm;
import got.pojo.CharacterInfo;
import got.pojo.TerritoryType;
import got.pojo.event.FamilyInfo;
import got.pojo.event.TerritoryInfo;
import got.user.Account;
import got.utility.PointFileReaderWriter;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ServerGameData {
    private Map<String, List<Point>> supplyPoints = new HashMap<String, List<Point>>();

    private Map<String, TerritoryInfo> terrMap = new HashMap<String, TerritoryInfo>();

    private Map<String, FamilyInfo> familiesMap = new HashMap<String, FamilyInfo>();

    private Map<String, CharacterInfo> characterMap = new HashMap<String, CharacterInfo>();

    private Map<String, List<String>> connTerritories = new HashMap<String, List<String>>();

    public void init() throws Exception {
        readTerritoriesXML();
        readTerritoryPolyTxt();
        readPlacementTxt();
        readSupplyPlacementTxt();
        readConnectionsXML();
        readFamiliesXML();
        readCharactersXML();
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
            terrMap.put(ti.getName(), ti);
            connTerritories.put(ti.getName(), new ArrayList<String>());
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
                TerritoryInfo ti = terrMap.get(terrName);
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
            for (TerritoryInfo ti : terrMap.values()) {
                if (ti.getName().equals(name.trim())) {
                    Polygon pg = polys.get(name).get(0);
                    Polygon polygon = new Polygon();
                    for (int i = 0; i < pg.xpoints.length; i++) {
                        polygon.addPoint(pg.xpoints[i] * 5 / 8, pg.ypoints[i] * 5 / 8);
                    }
                    ti.setPoly(polygon);
                    break;
                }
            }
        }
    }

    public void readPlacementTxt() throws FileNotFoundException, IOException {
        Map<String, List<Point>> places = PointFileReaderWriter.readOneToMany(getClass().getResourceAsStream(
                "/got/resource/txt/place.txt"));
        for (String key : places.keySet()) {
            for (Point p : places.get(key)) {
                p.setLocation(p.getX() * 5 / 8, p.getY() * 5 / 8);
            }
        }
        // first 4 placements are for army, the last one is for order action
        for (String name : places.keySet()) {
            List<Point> points = places.get(name);
            TerritoryInfo ti = terrMap.get(name);
            ti.setOrderP(points.remove(points.size() - 1));
            ti.setArmPs(points);
        }
    }

    public void readSupplyPlacementTxt() throws FileNotFoundException, IOException {
        supplyPoints = PointFileReaderWriter.readOneToMany(getClass().getResourceAsStream(
                "/got/resource/txt/supply_place.txt"));
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
            connTerritories.get(t1).add(t2);
            connTerritories.get(t2).add(t1);
        }
    }

    public boolean isJoined(Account account) {
        return false;
    }

    /**
     * @return the supplyPoints
     */
    public Map<String, List<Point>> getSupplyPoints() {
        return supplyPoints;
    }

    /**
     * @param supplyPoints the supplyPoints to set
     */
    public void setSupplyPoints(Map<String, List<Point>> supplyPoints) {
        this.supplyPoints = supplyPoints;
    }

    /**
     * @return the terrMap
     */
    public Map<String, TerritoryInfo> getTerrMap() {
        return terrMap;
    }

    /**
     * @param terrMap the terrMap to set
     */
    public void setTerrMap(Map<String, TerritoryInfo> terrMap) {
        this.terrMap = terrMap;
    }

    /**
     * @return the familiesMap
     */
    public Map<String, FamilyInfo> getFamiliesMap() {
        return familiesMap;
    }

    /**
     * @param familiesMap the familiesMap to set
     */
    public void setFamiliesMap(Map<String, FamilyInfo> familiesMap) {
        this.familiesMap = familiesMap;
    }

    /**
     * @return the characterMap
     */
    public Map<String, CharacterInfo> getCharacterMap() {
        return characterMap;
    }

    /**
     * @param characterMap the characterMap to set
     */
    public void setCharacterMap(Map<String, CharacterInfo> characterMap) {
        this.characterMap = characterMap;
    }

    /**
     * @return the connTerritories
     */
    public Map<String, List<String>> getConnTerritories() {
        return connTerritories;
    }

    /**
     * @param connTerritories the connTerritories to set
     */
    public void setConnTerritories(Map<String, List<String>> connTerritories) {
        this.connTerritories = connTerritories;
    }
}
