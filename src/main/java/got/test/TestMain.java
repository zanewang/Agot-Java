package got.test;

import got.pojo.event.TerritoryInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TestMain {

    /**
     * @param args
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        // TODO Auto-generated method stub
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File("src/got/resource/xml/territory.xml"));
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("Territory");
        List<TerritoryInfo> tis = new ArrayList<TerritoryInfo>();
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
            tis.add(ti);
        }
    }

 }
