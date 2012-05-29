package got.ui;

import got.core.Node;
import got.pojo.Action;
import got.pojo.ActionType;
import got.pojo.Arm;
import got.pojo.GameInfo;
import got.pojo.TerritoryType;
import got.pojo.event.FamilyInfo;
import got.pojo.event.TerritoryInfo;
import got.utility.Utility;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

public class DrawManager {

    private Graphics graphics;
    private Node node;
    private GameInfo gameInfo;
    private Point mousePoint;

    public DrawManager(Graphics graphics, Node node) {
        this.graphics = graphics;
        this.node = node;
        this.gameInfo = node.getGameInfo();
    }

    public void draw() {
        switch (node.getGameInfo().getState()) {
        case Game_Init:
            return;
        case Start_Order:
            highlightFamilyTerritory();
            break;
        case Choose_Raid_Order:
            highlightRaidTerritory();
            break;
        case Choose_Raid_Territory:
            highlightRaidedTerritory();
            break;
        case Choose_March_Order:
            highlightMarchTerritory();
            break;
        case Choose_March_Territory:
            highlightMarchableTerritory();
            break;
        case Choose_Muster_Territory:
            highlightMusterTerritory();
            break;
        case Choose_Muster_Ship_Territory:
            highlightMusterShipTerritory();
            drawShip();
            break;
        case Start_Asha_Special:
            highlightAshaSpecialTerritory();
            break;
        case Start_Cersei_Special:
            highlightCerseriSepcialTerritory();
            break;
        }
        for (TerritoryInfo ti : node.getGameInfo().getTerrMap().values()) {
            drawArmy(ti);
            drawAction(ti);
        }
        if (gameInfo.getInfoHistory().size() > 0) {
            // graphics.drawString(gameInfo.getInfoHistory().get(gameInfo.getInfoHistory().size()
            // - 1), 200, 30);
        }
    }

    private void highlightCerseriSepcialTerritory() {
        // TODO Auto-generated method stub
        String battleTerritoryName = gameInfo.getLastSelect();
        TerritoryInfo battleTerritory = gameInfo.getTerrMap().get(battleTerritoryName);
        String loseFamilyName = battleTerritory.getAttackFamilyName().equals("LANNISTER") ? battleTerritory
                .getConquerFamilyName() : battleTerritory.getAttackFamilyName();
        FamilyInfo loseFamilyInfo = gameInfo.getFamiliesMap().get(loseFamilyName);
        for (String terrName : loseFamilyInfo.getConquerTerritories()) {
            TerritoryInfo ti = gameInfo.getTerrMap().get(terrName);
            if (!ti.getAction().equals(Action.NONE)) {
                highlightTerritory(terrName, Color.RED);
            }
        }
    }

    private void highlightAshaSpecialTerritory() {
        // TODO Auto-generated method stub
        String battleTerritoryName = gameInfo.getLastSelect();
        TerritoryInfo battleTerritory = gameInfo.getTerrMap().get(battleTerritoryName);
        List<String> seaConsolidates = gameInfo.getNearbyTerritoryWtihAction(battleTerritoryName, TerritoryType.SEA,
                battleTerritory.getConquerFamilyName(), ActionType.CONSOLIDATE);
        List<String> seaSupports = gameInfo.getNearbyTerritoryWtihAction(battleTerritoryName, TerritoryType.SEA,
                battleTerritory.getConquerFamilyName(), ActionType.Support);
        List<String> landConsolidates = gameInfo.getNearbyTerritoryWtihAction(battleTerritoryName, TerritoryType.LAND,
                battleTerritory.getConquerFamilyName(), ActionType.CONSOLIDATE);
        List<String> landSupports = gameInfo.getNearbyTerritoryWtihAction(battleTerritoryName, TerritoryType.LAND,
                battleTerritory.getConquerFamilyName(), ActionType.Support);
        landSupports.addAll(landConsolidates);
        landSupports.addAll(seaSupports);
        landSupports.addAll(seaConsolidates);
        for (String availTerrName : landSupports) {
            highlightTerritory(availTerrName, Color.RED);
        }
    }

    private void drawShip() {
        // TODO Auto-generated method stub
        graphics.drawImage(node.getImageLoader().getArmyImages().get(node.getMyFamilyInfo().getName()).get(Arm.SHIP),
                getMousePoint().x, getMousePoint().y, null);
    }

    private void highlightMusterShipTerritory() {
        // TODO Auto-generated method stub
        TerritoryInfo musterTI = gameInfo.getTerrMap().get(gameInfo.getLastSelect());
        List<String> mySeas = node.getGameInfo().getNearbyTerritory(musterTI.getName(), TerritoryType.SEA,
                node.getMyFamilyInfo().getName());
        List<String> neturalSeas = node.getGameInfo().getNearbyTerritory(musterTI.getName(), TerritoryType.SEA,
                TerritoryInfo.NEUTRAL_FAMILY);
        for (String mySea : mySeas) {
            if (node.getGameInfo().isEnoughSupplyWithMuster(node.getMyFamilyInfo().getName(), mySea)) {
                highlightTerritory(mySea, Color.RED);
            }
        }
        for (String neturalSea : neturalSeas) {
            if (node.getGameInfo().isEnoughSupplyWithMuster(node.getMyFamilyInfo().getName(), neturalSea)) {
                highlightTerritory(neturalSea, Color.RED);
            }
        }
    }

    private void highlightMusterTerritory() {
        for (String terrName : node.getMyFamilyInfo().getConquerTerritories()) {
            TerritoryInfo ti = gameInfo.getTerrMap().get(terrName);
            if (ti.getMustering() > 0) {
                highlightTerritory(terrName, Color.RED);
            }
        }
    }

    private void highlightMarchableTerritory() {
        List<String> marchableTerrs = gameInfo.getMarchableTerritory(gameInfo.getLastSelect(), node.getMyFamilyInfo()
                .getName());
        for (String terrName : marchableTerrs) {
            highlightTerritory(terrName, Color.RED);
        }
    }

    private void highlightMarchTerritory() {
        FamilyInfo myFamilyInfo = node.getMyFamilyInfo();
        for (String terrName : myFamilyInfo.getConquerTerritories()) {
            TerritoryInfo ti = gameInfo.getTerrMap().get(terrName);
            if (ti.getAction().getType().equals(ActionType.March))
                highlightTerritory(terrName, myFamilyInfo.getColor());
        }
    }

    private void highlightRaidedTerritory() {
        // TODO Auto-generated method stub
        TerritoryInfo raidTerr = gameInfo.getTerrMap().get(gameInfo.getLastSelect());
        for (String raidedTerrName : node.getGameInfo().getConnTerritories().get(raidTerr.getName())) {
            TerritoryInfo raidedTerr = node.getGameInfo().getTerrMap().get(raidedTerrName);
            Action raidedaction = raidedTerr.getAction();
            if (!raidedaction.getType().equals(ActionType.March) && !raidedaction.getType().equals(ActionType.DEFENSE)
                    && !raidedaction.equals(Action.NONE))
                highlightTerritory(raidedTerrName, Color.black);
        }
    }

    private void highlightRaidTerritory() {
        // TODO Auto-generated method stub
        FamilyInfo myFamilyInfo = node.getMyFamilyInfo();
        for (String terrName : myFamilyInfo.getConquerTerritories()) {
            TerritoryInfo ti = gameInfo.getTerrMap().get(terrName);
            if (ti.getAction().getType().equals(ActionType.Raid))
                highlightTerritory(terrName, myFamilyInfo.getColor());
        }
    }

    private void highlightFamilyTerritory() {
        // TODO Auto-generated method stub
        FamilyInfo myFamilyInfo = node.getMyFamilyInfo();
        for (String terrName : myFamilyInfo.getConquerTerritories()) {
            TerritoryInfo ti = gameInfo.getTerrMap().get(terrName);
            highlightTerritory(terrName, myFamilyInfo.getColor());
        }

    }

    private void highlightTerritory(String terrName, Color color) {
        TerritoryInfo ti = gameInfo.getTerrMap().get(terrName);
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        g2d.setColor(color);
        g2d.fillPolygon(ti.getPoly());
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    private void drawOrder(TerritoryInfo ti) {
        Image image = Utility.loadImage("got/resource/Knight-icon.png");
        image = Utility.sacleImage(image, 0.25, 0.25);
        graphics.drawImage(image, ti.getOrderP().x, ti.getOrderP().y, null);
    }

    private void drawArmy(TerritoryInfo ti) {
        int placed = 0;
        for (Arm arm : ti.getConquerArms().keySet()) {
            int num = ti.getConquerArms().get(arm);
            for (int i = 0; i < num; i++) {
                Point p = ti.getArmPs().get(placed++);
                graphics.drawImage(node.getImageLoader().getArmyImages().get(ti.getConquerFamilyName()).get(arm), p.x,
                        p.y, null);
            }
        }
        placed = 0;
        for (Arm arm : ti.getAttackArms().keySet()) {
            int num = ti.getAttackArms().get(arm);
            for (int i = 0; i < num; i++) {
                Point p = ti.getArmPs().get(placed++);
                graphics.drawImage(node.getImageLoader().getArmyImages().get(ti.getAttackFamilyName()).get(arm),
                        p.x + 5, p.y + 5, null);
            }
        }
        
        placed = ti.getConquerArmNum();
        for (Arm arm : ti.getRetreatArms().keySet()) {
            int num = ti.getRetreatArms().get(arm);
            for (int i = 0; i < num; i++) {
                Point p = ti.getArmPs().get(placed++);
                graphics.drawImage(node.getImageLoader().getRetreatArmyImages().get(ti.getConquerFamilyName()).get(arm),
                        p.x + 5, p.y + 5, null);
            }
        }
    }

    private void drawAction(TerritoryInfo ti) {
        // TODO Auto-generated method stub
        Point p = ti.getOrderP();
        if (!ti.getAction().equals(Action.NONE)) {
            graphics.drawImage(node.getImageLoader().getActionImages().get(ti.getAction()), p.x, p.y, null);
        }
    }

    public void setMousePoint(Point mousePoint) {
        this.mousePoint = mousePoint;
    }

    public Point getMousePoint() {
        return mousePoint;
    }
}
