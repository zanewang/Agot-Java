package got.core;

import got.io.MessageType;
import got.logic.GameState;
import got.pojo.Action;
import got.pojo.ActionType;
import got.pojo.Arm;
import got.pojo.GameInfo;
import got.pojo.TerritoryType;
import got.pojo.event.FamilyInfo;
import got.pojo.event.GameInfoHelper;
import got.pojo.event.TerritoryInfo;
import got.ui.DisarmamentDialog;
import got.ui.FinishDailog;
import got.ui.MarchDialog;
import got.ui.MusteringDialog;
import got.ui.OrderDialog;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

public class ClickManager {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Node node;
    private GameInfo gameInfo;

    public ClickManager(Node node) {
        this.node = node;
        this.gameInfo = node.getGameInfo();
    }

    public void handler(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            FinishHandler(e);
            return;
        }
        switch (node.getGameInfo().getState()) {
        case Do_Nothing:
            break;
        case Start_Order:
        case Start_Raven:
            OrderHandler(e);
            break;
        case Choose_Raid_Order:
            RaidHandler(e);
            break;
        case Choose_Raid_Territory:
            RaidedHandler(e);
            break;
        case Choose_March_Order:
            MarchHandler(e);
            break;
        case Choose_March_Territory:
            MarchTerritoryHandler(e);
            break;
        case Choose_Muster_Territory:
            MusterTerritoryHandler(e);
            break;
        case Choose_Muster_Ship_Territory:
            MusterShipTerritoryHandler(e);
            break;
        case Start_Retreat:
            RetreatTerritoryHandler(e);
            break;
        case Start_Change_Supply:
            SupplyTerritoryHandler(e);
            break;
        case Start_Asha_Special:
            AshaSpecialTerritoryHandler(e);
            break;
        case Start_Cersei_Special:
            CerseiSpecialTerritoryHandler(e);
            break;
        case Start_Disarm:
            DisarmTerritoryHandler(e);
            break;
        }
    }

    private void DisarmTerritoryHandler(MouseEvent e) {
        // TODO Auto-generated method stub
        TerritoryInfo ti = findTerritory(e.getPoint());
        if (ti == null && !node.getMyFamilyInfo().getConquerTerritories().contains(ti.getName())) {
            return;
        }
        new DisarmamentDialog(node, e.getLocationOnScreen(), ti);
    }

    private void CerseiSpecialTerritoryHandler(MouseEvent e) {
        // TODO Auto-generated method stub
        TerritoryInfo ti = findTerritory(e.getPoint());
        if (ti == null) {
            return;
        }
        String battleTerritoryName = gameInfo.getLastSelect();
        TerritoryInfo battleTerritory = gameInfo.getTerrMap().get(battleTerritoryName);
        String loseFamilyName = battleTerritory.getAttackFamilyName().equals("LANNISTER") ? battleTerritory
                .getConquerFamilyName() : battleTerritory.getAttackFamilyName();
        FamilyInfo loseFamilyInfo = gameInfo.getFamiliesMap().get(loseFamilyName);
        if (!loseFamilyInfo.getConquerTerritories().contains(ti.getName()) && !ti.getAction().equals(Action.NONE)) {
            return;
        }
        ti.setAction(Action.NONE);
        try {
            node.sendGameStatusUpdate(MessageType.Cersei_Special);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        node.getGameInfo().shiftTO(GameState.Do_Nothing);
    }

    private void AshaSpecialTerritoryHandler(MouseEvent e) {
        // TODO Auto-generated method stub
        TerritoryInfo ti = findTerritory(e.getPoint());
        if (ti == null) {
            return;
        }
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
        if (!landSupports.contains(ti.getName())) {
            return;
        }
        ti.setAction(Action.NONE);
        try {
            node.sendGameStatusUpdate(MessageType.Asha_Special);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        node.getGameInfo().shiftTO(GameState.Do_Nothing);
    }

    private void SupplyTerritoryHandler(MouseEvent e) {
        // TODO Auto-generated method stub
        TerritoryInfo ti = findTerritory(e.getPoint());
        if (ti == null) {
            return;
        }
        FamilyInfo myFamilyInfo = node.getMyFamilyInfo();
        if (!myFamilyInfo.getConquerTerritories().contains(ti.getName())) {
            return;
        }
        new DisarmamentDialog(node, e.getLocationOnScreen(), ti);
    }

    private void RetreatTerritoryHandler(MouseEvent e) {
        // TODO Auto-generated method stub
        TerritoryInfo ti = findTerritory(e.getPoint());
        if (ti == null) {
            return;
        }
        if (ti.getConquerFamilyName() == node.getMyFamilyInfo().getName()
                || ti.getConquerFamilyName().equals(TerritoryInfo.NEUTRAL_FAMILY)) {
            TerritoryInfo battleTI = gameInfo.getTerrMap().get(gameInfo.getLastSelect());
            ti.setConquerFamilyName(node.getMyFamilyInfo().getName());
            ti.setRetreatArms(battleTI.getConquerArms());
            try {
                node.sendGameStatusUpdate(MessageType.Retreat);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private void MusterShipTerritoryHandler(MouseEvent e) {
        // TODO Auto-generated method stub
        TerritoryInfo ti = findTerritory(e.getPoint());
        if (ti == null) {
            return;
        }
        TerritoryInfo musterTI = gameInfo.getTerrMap().get(gameInfo.getLastSelect());
        List<String> mySeas = node.getGameInfo().getNearbyTerritory(musterTI.getName(), TerritoryType.SEA,
                node.getMyFamilyInfo().getName());
        List<String> neturalSeas = node.getGameInfo().getNearbyTerritory(musterTI.getName(), TerritoryType.SEA,
                TerritoryInfo.NEUTRAL_FAMILY);

        List<String> ports = node.getGameInfo().getNearbyTerritory(musterTI.getName(), TerritoryType.PORT);
        logger.debug(ports.size());
        if (!mySeas.contains(ti.getName()) && !neturalSeas.contains(ti.getName()) && !ports.contains(ti.getName())) {
            return;
        }

        if (!node.getGameInfo().isEnoughSupplyWithMuster(node.getMyFamilyInfo().getName(), ti.getName())) {
            return;
        }

        ti.getConquerArms().put(Arm.SHIP, ti.getConquerArms().get(Arm.SHIP) + 1);
        musterTI.setAvailMustering(musterTI.getAvailMustering() - 1);

        node.getGameInfo().shiftTO(GameState.Choose_Muster_Territory);
        return;
    }

    private void FinishHandler(MouseEvent e) {
        if (node.getGameInfo().getState().equals(GameState.Do_Nothing)) {
            return;
        }
        new FinishDailog(node, e.getLocationOnScreen());
    }

    private void MusterTerritoryHandler(MouseEvent e) {
        TerritoryInfo ti = findTerritory(e.getPoint());
        if (ti == null || ti.getMustering() == 0) {
            return;
        }
        FamilyInfo myFamilyInfo = node.getMyFamilyInfo();
        if (!myFamilyInfo.getConquerTerritories().contains(ti.getName())) {
            return;
        }
        node.getGameInfo().setLastSelect(ti.getName());
        new MusteringDialog(node, e.getLocationOnScreen(), ti);
    }

    private void RaidedHandler(MouseEvent e) {
        if (node.getMyFamilyInfo().getRaidRemain() == 0) {
            return;
        }
        TerritoryInfo ti = findTerritory(e.getPoint());
        if (ti == null) {
            return;
        }
        if (!node.getGameInfo().canRaid(node.getGameInfo().getLastSelect(), ti.getName())) {
            return;
        }
        ti.setAction(Action.NONE);
        node.getMyFamilyInfo().setRaidRemain(node.getMyFamilyInfo().getRaidRemain() - 1);
        logger.debug("Client: Remove the Territory " + ti.getName() + "'s action");
    }

    private void RaidHandler(MouseEvent e) {
        TerritoryInfo ti = findTerritory(e.getPoint());
        if (ti == null || !ti.getAction().getType().equals(ActionType.Raid)) {
            return;
        }
        FamilyInfo myFamilyInfo = node.getMyFamilyInfo();
        if (!myFamilyInfo.getConquerTerritories().contains(ti.getName())) {
            return;
        }
        if (ti.getAction().equals(Action.RAID_III)) {
            node.getMyFamilyInfo().setRaidRemain(2);
        } else {
            node.getMyFamilyInfo().setRaidRemain(1);
        }
        ti.setAction(Action.NONE);
        logger.debug("Client: Use the Territory " + ti.getName() + "'s raid");
        node.getGameInfo().setLastSelect(ti.getName());
        node.getGameInfo().shiftTO(GameState.Choose_Raid_Territory);
    }

    private void OrderHandler(MouseEvent e) {
        TerritoryInfo ti = findTerritory(e.getPoint());
        if (ti == null) {
            return;
        }
        FamilyInfo myFamilyInfo = node.getMyFamilyInfo();
        if (!myFamilyInfo.getConquerTerritories().contains(ti.getName())) {
            return;
        }
        new OrderDialog(node, e.getLocationOnScreen(), ti);
    }

    private void MarchHandler(MouseEvent e) {
        TerritoryInfo ti = findTerritory(e.getPoint());
        if (ti == null || !ti.getAction().getType().equals(ActionType.March)) {
            return;
        }
        // fix me the march+1
        ti.setAction(Action.NONE);
        node.getGameInfo().setLastSelect(ti.getName());
        node.getGameInfo().shiftTO(GameState.Choose_March_Territory);
    }

    private void MarchTerritoryHandler(MouseEvent e) {
        TerritoryInfo ti = findTerritory(e.getPoint());
        if (ti == null) {
            return;
        }
        TerritoryInfo marchTI = gameInfo.getTerrMap().get(gameInfo.getLastSelect());
        if (!node.getGameInfo().canMarch(marchTI.getName(), ti.getName(), node.getMyFamilyInfo().getName())) {
            return;
        }
        new MarchDialog(node, e.getLocationOnScreen(), marchTI);
        ti.getAttackArms().put(Arm.KNIGHT, node.getGameInfo().getMarchArms().get(Arm.KNIGHT));
        ti.getAttackArms().put(Arm.FOOTMAN, node.getGameInfo().getMarchArms().get(Arm.FOOTMAN));
        ti.getAttackArms().put(Arm.SHIP, node.getGameInfo().getMarchArms().get(Arm.SHIP));
        ti.setAttackFamilyName(node.getMyFamilyInfo().getName());
        ti.setAttackRate(GameInfoHelper.getAttackRate(marchTI.getAction()));
        ti.setAttackTerritoryName(marchTI.getName());

        // fix me leave the power token
    }

    private TerritoryInfo findTerritory(Point p) {
        for (TerritoryInfo ti : node.getGameInfo().getTerrMap().values()) {
            if (ti.getPoly().contains(p)) {
                return ti;
            }
        }
        return null;
    }
}
