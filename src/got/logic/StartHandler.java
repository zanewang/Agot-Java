package got.logic;

import got.core.Node;
import got.io.MessageType;
import got.io.Packet;
import got.pojo.Action;
import got.pojo.ActionType;
import got.pojo.Arm;
import got.pojo.CharacterInfo;
import got.pojo.WesterosDeckI;
import got.pojo.WesterosDeckII;
import got.pojo.WesterosDeckIII;
import got.pojo.event.FamilyInfo;
import got.pojo.event.GameInfoHelper;
import got.pojo.event.TerritoryInfo;
import got.test.TestCase;
import got.test.TestInject;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.jgroups.Address;

public class StartHandler extends EventManager {

    private int countdownN;
    private int maxRound = 10;

    public StartHandler(Node node) {
        super(node);

        // TODO Auto-generated constructor stub
    }

    @Override
    public void handler(Address address, Packet packet) throws Exception {
        // TODO Auto-generated method stub
        if (!serverGameInfo.isHost()) {
            return;
        }
        for (Address addr : node.getServerGameInfo().getAddrMap().keySet()) {
            node.sendInfo("Random assign the family " + node.getServerGameInfo().getAddrMap().get(addr).getName(), addr);
        }
        countdownN = serverGameInfo.getPlayerNum();
        int round = 1;
        while (round <= maxRound) {
            reset();
            if (round != 1) {
                westerosEvent();
            }
            // orderEvent();
            // ravenEvent();
            // raidEvent();
            if (TestCase.BATTLE_TEST) {
                TestInject.battleInject(node);
                syncGameStatus();
                battleEvent();
            }
            // marchEvent();
            // powerEvent();
            round++;
        }

    }

    private void reset() throws Exception {
        // TODO Auto-generated method stub

        for (FamilyInfo fi : serverGameInfo.getFamiliesMap().values()) {
            // Rest all order actions
            fi.resetAction();
            // Rest the blade Remain
            fi.setBladeRemain(1);
            fi.setJudgeRank(0);
            fi.setRaidRemain(0);
            fi.setDisarmRemain(0);
        }
        for (TerritoryInfo ti : serverGameInfo.getTerrMap().values()) {
            ti.setAction(Action.NONE);
        }
        syncGameStatus();

    }

    private void westerosEvent() throws Exception {
        westerosPharseI();
        westerosPharseII();
        westerosPharseIII();
    }

    private void westerosPharseI() throws Exception {
        List<WesterosDeckI> deckI = serverGameInfo.getShuffledWesterosDeckI();
        WesterosDeckI choosedCard = deckI.remove(0);
        if (choosedCard.hasWildToken()) {
            summerEvent();
        }
        deckI.add(choosedCard);
        logger.debug("Server: Westeros Pharse 1 " + choosedCard.toString());
        switch (choosedCard) {
        case SUPPLY:
            supplyEvent();
            break;
        case MUSTER:
            musterEvent();
            break;
        case END_OF_SUMMER:
            summerEvent();
            break;
        case BEGIN_OF_WINTER:
            serverGameInfo.shuffleWestorsDeckI();
            westerosPharseI();
            break;
        }
    }

    private void westerosPharseII() throws Exception {
        List<WesterosDeckII> deckII = serverGameInfo.getShuffledWesterosDeckII();
        WesterosDeckII choosedCard = deckII.remove(0);
        if (choosedCard.hasWildToken()) {
            summerEvent();
        }
        if (TestCase.CLASH_KING_TEST) {
            choosedCard = WesterosDeckII.CLASH_OF_KING;
        }
        deckII.add(choosedCard);
        logger.debug("Server: Westeros Pharse 2 " + choosedCard.toString());
        switch (choosedCard) {
        case POWER_GAME:
            powerEvent();
            break;
        case CLASH_OF_KING:
            clashKingEvent();
            break;
        case END_OF_SUMMER:
            summerEvent();
            break;
        case BEGIN_OF_WINTER:
            serverGameInfo.shuffleWestorsDeckII();
            westerosPharseII();
            break;
        }
    }

    private void westerosPharseIII() throws Exception {
        List<WesterosDeckIII> deckIII = serverGameInfo.getShuffledWesterosDeckIII();
        WesterosDeckIII choosedCard = deckIII.remove(0);
        if (choosedCard.hasWildToken()) {
            summerEvent();
        }
        deckIII.add(choosedCard);
        logger.debug("Server: Westeros Pharse 3 " + choosedCard.toString());
        switch (choosedCard) {
        case WILDLING_ATTACK:
            wildAttackEvent();
            break;
        case STORM_SEA:
            // can not use the raid command
            diableOrderEvent(ActionType.Raid);
            break;
        case STORM_SWORDS:
            // can not use the defense command
            diableOrderEvent(ActionType.DEFENSE);
            break;
        case RAVEN_FEAST:
            // can not use the consolidate power command
            diableOrderEvent(ActionType.CONSOLIDATE);
            break;
        case RAIN_AUTUMN:
            // the footman support power would be zero
            break;
        case END_OF_SUMMER:
            summerEvent();
            break;
        case BEGIN_OF_WINTER:
            serverGameInfo.shuffleWestorsDeckII();
            westerosPharseII();
            break;
        }
    }

    private void wildAttackEvent() throws Exception {
        event(MessageType.Wild_Attack);
        serverGameInfo.reOrderInfluence(MessageType.Wild_Attack);
        int total = 0;
        for (FamilyInfo fi : serverGameInfo.getFamiliesMap().values()) {
            total += fi.getCompetePower();
        }
        if (total < serverGameInfo.getWildAttakRate()) {
            judgeEvent(MessageType.Judge_Disarm);
            List<FamilyInfo> disarmFamilies = serverGameInfo.getFamiliesJudgeRanking();
            for (FamilyInfo fi : disarmFamilies) {
                fi.setDisarmRemain(2);
            }
            disarmFamilies.get(disarmFamilies.size() - 1).setDisarmRemain(4);
            event(MessageType.Disarm);
        } else {
            judgeEvent(MessageType.Judge_General);
            event(MessageType.Ransom, serverGameInfo.getFamiliesJudgeRanking().get(0).getAddress());
        }
    }

    private void diableOrderEvent(ActionType type) throws Exception {
        for (FamilyInfo fi : node.getGameInfo().getFamiliesMap().values()) {
            for (Action action : fi.getActionMap().keySet()) {
                if (action.getType().equals(type)) {
                    fi.getActionMap().put(action, false);
                }
            }
        }
        syncGameStatus();
    }

    private void summerEvent() throws Exception {
        node.getGameInfo().setWildAttakRate(node.getGameInfo().getWildAttakRate() + 2);
    }

    private void supplyEvent() throws Exception {
        event(MessageType.Supply);
    }

    private void musterEvent() throws Exception {
        logger.debug("Server: Send the Muster message to all");
        List<FamilyInfo> ironFamilies = serverGameInfo.getFamiliesIronRanking();
        for (FamilyInfo fi : ironFamilies) {
            CountDownLatch musterCDL = new CountDownLatch(1);
            serverGameInfo.getCdl().put(MessageType.Mustering, musterCDL);
            node.send(MessageType.Mustering, fi.getAddress());
            musterCDL.await();
            syncGameStatus();
        }
    }

    private void event(MessageType type) throws Exception {
        logger.debug("Server: Send the command " + type.toString());
        CountDownLatch cdl = new CountDownLatch(countdownN);
        serverGameInfo.getCdl().put(type, cdl);
        node.send(type);
        cdl.await();
        logger.debug("Server: Receive all commands of type " + type.toString());
        syncGameStatus();
    }

    private void event(MessageType type, Address address) throws Exception {
        logger.debug("Server: Send the command " + type.toString() + " to address " + address);
        CountDownLatch cdl = new CountDownLatch(1);
        serverGameInfo.getCdl().put(type, cdl);
        node.send(type, address);
        cdl.await();
        logger.debug("Server: Receive all commands of type " + type.toString());
        syncGameStatus();
    }

    private void judgeEvent(MessageType sendType) throws Exception {
        if (node.getServerGameInfo().needIronJudgemenet()) {
            logger.debug("Server : we need the iron judge");
            Address ironAddress = serverGameInfo.getFamiliesIronRanking().get(0).getAddress();
            event(sendType, ironAddress);
            switch (sendType) {
            case Judge_Iron:
                for (FamilyInfo fi : serverGameInfo.getFamiliesMap().values()) {
                    fi.setIronRank(fi.getJudgeRank());
                }
                break;
            case Judge_Blade:
                for (FamilyInfo fi : serverGameInfo.getFamiliesMap().values()) {
                    fi.setBladeRank(fi.getJudgeRank());
                }
                break;
            case Judge_Raven:
                for (FamilyInfo fi : serverGameInfo.getFamiliesMap().values()) {
                    fi.setRavenRank(fi.getJudgeRank());
                }
                break;
            }
            syncGameStatus();
        }
    }

    private void clashKingEvent() throws Exception {
        logger.debug("Server: Send the Clash King message to all");

        event(MessageType.Compete_Iron);

        judgeEvent(MessageType.Judge_Iron);

        event(MessageType.Compete_Blade);

        judgeEvent(MessageType.Judge_Blade);

        event(MessageType.Compete_Raven);

        judgeEvent(MessageType.Judge_Raven);
    }

    private void orderEvent() throws Exception {
        event(MessageType.Order);
    }

    private void ravenEvent() throws Exception {
        FamilyInfo ravenTopFamilyInfo = serverGameInfo.getFamiliesRavenRanking().get(0);
        logger.debug("Server: The Raven 1st is " + ravenTopFamilyInfo.getName());
        event(MessageType.Raven, ravenTopFamilyInfo.getAddress());
    }

    private void raidEvent() throws Exception {
        logger.debug("Server: Send the raid message as the iron sequences");
        List<FamilyInfo> ironFamilies = serverGameInfo.getFamiliesIronRanking();
        while (getRemainCommands(ActionType.Raid) != 0) {
            for (FamilyInfo fi : ironFamilies) {
                if (getRemainCommands(ActionType.Raid, fi) == 0) {
                    logger.debug("Server: Has no raid command for family " + fi.getName());
                    continue;
                }
                event(MessageType.Raid, fi.getAddress());
            }
        }
    }

    private void marchEvent() throws Exception {
        logger.debug("Server: Send the march message as the iron sequences");
        List<FamilyInfo> ironFamilies = serverGameInfo.getFamiliesIronRanking();
        while (getRemainCommands(ActionType.March) != 0) {
            for (FamilyInfo fi : ironFamilies) {
                if (getRemainCommands(ActionType.March, fi) == 0) {
                    logger.debug("Server: Has no march command for family " + fi.getName());
                    continue;
                }
                event(MessageType.March, fi.getAddress());
                battleEvent();
            }
        }
    }

    private void battleEvent() throws Exception {
        List<String> battleTerritoryNames = serverGameInfo.getBattleTerritories();
        if (battleTerritoryNames.size() == 0) {
            logger.debug("Server: No battle happen");
            return;
        }
        for (String battleTerritoryName : battleTerritoryNames) {
            serverGameInfo.setLastSelect(battleTerritoryName);
            TerritoryInfo battleTerritory = serverGameInfo.getTerrMap().get(battleTerritoryName);
            if (battleTerritory.getAction().getType().equals(ActionType.DEFENSE)) {
                battleTerritory.setConquerRate(GameInfoHelper.getAttackRate(battleTerritory.getAction()));
            }
            syncGameStatus();
            String defenseFamilyName = battleTerritory.getConquerFamilyName();
            String attackFamilyName = battleTerritory.getAttackFamilyName();

            if (defenseFamilyName.equals(TerritoryInfo.NEUTRAL_FAMILY)) {
                logger.debug("Server: " + attackFamilyName + " attack the NEUTRAL territory");
                battleNeutral(battleTerritory.getName(),  attackFamilyName);
                return;
            } else {
                logger.debug("Sever: " + attackFamilyName + " attack the " + defenseFamilyName);
                battlePlayer(battleTerritory.getName(), defenseFamilyName, attackFamilyName);
            }
        }
    }

    private void battleNeutral(String battleTerrName, String attackFamilyName) {
        // TODO Auto-generated method stub
        // fix me the support
        TerritoryInfo ti = serverGameInfo.getTerrMap().get(battleTerrName);
        int defenseFamilyCapabilities = ti.getConquerCapabilities();
        int attackFamilyCapabilities = ti.getAttackCapabilities() + ti.getAttackRate() + ti.getAttackSeaSupport()
                + ti.getAttackLandSupport();
        if(attackFamilyCapabilities >= defenseFamilyCapabilities){
            ti.resetAfterAttack(true);
            serverGameInfo.getFamiliesMap().get(attackFamilyName).getConquerTerritories().add(ti.getName());
        }
        else{
            TerritoryInfo attackTerritory = serverGameInfo.getTerrMap().get(ti.getAttackTerritoryName());
            attackTerritory.setRetreatArms(ti.getAttackArms());
            ti.resetAfterAttack(false);
        }
    }

    private void battlePlayer(String battleTerrName, String defenseFamilyName, String attackFamilyName)
            throws Exception {
        // TODO Auto-generated method stub

        logger.debug("Server: Battle Territory " + battleTerrName);
        List<String> supportTerrNames = serverGameInfo.searchNearbyTerriotryWithSupport(battleTerrName,
                defenseFamilyName, attackFamilyName);
        for (String supportTerrName : supportTerrNames) {
            logger.debug("Server: " + supportTerrName + " could give support");
            TerritoryInfo supportTerritory = serverGameInfo.getTerrMap().get(supportTerrName);
            serverGameInfo.setLastSelect(supportTerrName);
            FamilyInfo supportFamily = serverGameInfo.getFamiliesMap().get(supportTerritory.getConquerFamilyName());
            event(MessageType.Support, supportFamily.getAddress());
        }
        FamilyInfo defenseFamily = serverGameInfo.getFamiliesMap().get(defenseFamilyName);
        FamilyInfo attackFamily = serverGameInfo.getFamiliesMap().get(attackFamilyName);
        List<FamilyInfo> bladeRanks = serverGameInfo.getFamiliesBladeRanking();
        if (bladeRanks.get(0).getName().equals(defenseFamilyName)) {
            if (defenseFamily.getBladeRemain() == 1) {
                event(MessageType.Blade, defenseFamily.getAddress());
            }
        } else if (bladeRanks.get(0).getName().equals(attackFamilyName)) {
            if (attackFamily.getBladeRemain() == 1) {
                event(MessageType.Blade, attackFamily.getAddress());
            }
        }

        if (TestCase.GENERAL_INGORE_TEST) {
            TestInject.characterInject(node);
        } else {
            CountDownLatch cdl = new CountDownLatch(2);
            serverGameInfo.getCdl().put(MessageType.General, cdl);
            node.send(MessageType.General, node.getGameInfo().getFamiliesMap().get(attackFamilyName).getAddress());
            node.send(MessageType.General, node.getGameInfo().getFamiliesMap().get(defenseFamilyName).getAddress());
            cdl.await();
        }
        defenseFamily = serverGameInfo.getFamiliesMap().get(defenseFamilyName);
        attackFamily = serverGameInfo.getFamiliesMap().get(attackFamilyName);
        logger.debug("Server: Defense Character " + defenseFamily.getBattleCharacter());
        logger.debug("Server: Attack Character " + attackFamily.getBattleCharacter());
        CharacterInfo defenseCharacter = serverGameInfo.getCharacterMap().get(defenseFamily.getBattleCharacter());
        CharacterInfo attackCharacter = serverGameInfo.getCharacterMap().get(attackFamily.getBattleCharacter());
        logger.debug("Server: Defense Character " + defenseCharacter.getName());
        logger.debug("Server: Attack Character " + attackCharacter.getName());
        characterCastBeforeBattle(battleTerrName, defenseFamilyName, attackFamilyName);
        TerritoryInfo ti = serverGameInfo.getTerrMap().get(battleTerrName);
        int defenseFamilyCapabilities = defenseCharacter.getPower() + ti.getConquerCapabilities() + ti.getConquerRate()
                + ti.getConquerSeaSupport() + ti.getConquerLandSupport();
        int attackFamilyCapabilities = attackCharacter.getPower() + ti.getAttackCapabilities() + ti.getAttackRate()
                + ti.getAttackSeaSupport() + ti.getAttackLandSupport();

        if (attackFamilyCapabilities == defenseFamilyCapabilities) {
            if (attackFamily.getBladeRank() < defenseFamily.getBladeRank()) {
                attackFamilyCapabilities += 1;
            } else {
                defenseFamilyCapabilities += 1;
            }
        }
        if (attackFamilyCapabilities > defenseFamilyCapabilities) {
            int killed = attackCharacter.getSword() - defenseCharacter.getShield();
            killed = killed > ti.getConquerArmNum() ? ti.getConquerArmNum() : killed;
            logger.debug("Server: " + attackFamilyName + " win the battle and would kill " + String.valueOf(killed)
                    + " units");
            while (killed > 0) {
                for (Arm arm : ti.getConquerArms().keySet()) {
                    if (ti.getConquerArms().get(arm) > 0) {
                        ti.getConquerArms().put(arm, ti.getConquerArms().get(arm) - 1);
                        killed--;
                        break;
                    }
                }
            }
            if (ti.getConquerArmNum() > 0) {
                event(MessageType.Retreat, defenseFamily.getAddress());
            }
            characterCastAfterBattleWin(ti, attackFamilyName, defenseFamilyName);
            ti.resetAfterAttack(true);
            defenseFamily.getConquerTerritories().remove(ti.getName());
            attackFamily.getConquerTerritories().add(ti.getName());
        } else if (attackFamilyCapabilities < defenseFamilyCapabilities) {
            int killed = defenseCharacter.getSword() - attackCharacter.getShield();
            killed = killed > ti.getAttackArmNum() ? ti.getAttackArmNum() : killed;
            logger.debug("Server: " + defenseFamilyName + " win the battle and would kill " + String.valueOf(killed)
                    + " units");
            while (killed > 0) {
                for (Arm arm : ti.getAttackArms().keySet()) {
                    if (ti.getAttackArms().get(arm) > 0) {
                        ti.getAttackArms().put(arm, ti.getAttackArms().get(arm) - 1);
                        killed--;
                        break;
                    }
                }
            }
            TerritoryInfo attackTerritory = serverGameInfo.getTerrMap().get(ti.getAttackTerritoryName());
            attackTerritory.setRetreatArms(ti.getAttackArms());
            characterCastAfterBattleWin(ti, defenseFamilyName, attackFamilyName);
            ti.resetAfterAttack(false);
        }

        syncGameStatus();
    }

    private void characterCastBeforeBattle(String terrName, String defenseFamilyName, String attackFamilyName) {
        FamilyInfo defenseFamily = serverGameInfo.getFamiliesMap().get(defenseFamilyName);
        FamilyInfo attackFamily = serverGameInfo.getFamiliesMap().get(attackFamilyName);
        CharacterInfo defenseCharacter = serverGameInfo.getCharacterMap().get(defenseFamily.getBattleCharacter());
        CharacterInfo attackCharacter = serverGameInfo.getCharacterMap().get(attackFamily.getBattleCharacter());
        TerritoryInfo ti = serverGameInfo.getTerrMap().get(terrName);
        // attack side
        if (defenseFamily.getBattleCharacter().equals("Queen of Thorns")
                || attackFamily.getBattleCharacter().equals("Queen of Thorns")) {
            // all specify ability is disable
            // Queen of Thorns,Cancel the special ability of your opponents
            // House Card.
            return;
        }
        if (attackFamily.getBattleCharacter().equals("Robb Stark")) {
            // Attacking Knights in your army add +3 to your combat strength
            // instead of +2.
            ti.setAttackRate(ti.getAttackRate() + ti.getAttackArms().get(Arm.KNIGHT));
        } else if (attackFamily.getBattleCharacter().equals("Ser Loras Tyrell")) {
            // Immediately kill one of your opponent’s attacking or defending
            // Footman units. If your opponent don't have other units, win the
            // battle immediately
            if (ti.getConquerArms().get(Arm.FOOTMAN) > 0) {
                ti.getConquerArms().put(Arm.FOOTMAN, ti.getConquerArms().get(Arm.FOOTMAN) - 1);
            }
        } else if (attackFamily.getBattleCharacter().equals("Vargo Hoat")) {
            // Attacking Footman in your army count +2 to your combat strength
            // instead of +1.
            ti.setAttackRate(ti.getAttackRate() + ti.getAttackArms().get(Arm.FOOTMAN));
        } else if (attackFamily.getBattleCharacter().equals("Victarion Greyjoy")) {
            // Attacking ships in your army may add +2 to your combat strength
            // instead of +1.
            ti.setAttackRate(ti.getAttackRate() + ti.getAttackArms().get(Arm.SHIP));
        } else if (attackFamily.getBattleCharacter().equals("Brienne of Tarth")) {
            // Immediately discard your opponent’s Defence order in the area of
            // battle.
            if (ti.getAction().getType().equals(ActionType.DEFENSE)) {
                ti.setAction(Action.NONE);
                if (ti.getAction().isStar()) {
                    ti.setConquerRate(ti.getConquerRate() - 2);
                } else {
                    ti.setConquerRate(ti.getConquerRate() - 1);
                }
            }
        } else if (attackFamily.getBattleCharacter().equals("Salladhor Saan")) {
            // The combat strength of any opposed supporting ships is reduced to
            // zero.
            ti.setConquerSeaSupport(0);
        } else if (attackFamily.getBattleCharacter().equals("Balon Greyjoy")) {
            // The combat strength of your opponents Leader is 0.
            ti.setAttackRate(ti.getAttackRate() + defenseCharacter.getPower());
        }

        // defense side
        if (defenseFamily.getBattleCharacter().equals("Ser Loras Tyrell")) {
            // Immediately kill one of your opponent’s attacking or defending
            // Footman units. If your opponent don't have other units, win the
            // battle immediately
            if (ti.getAttackArms().get(Arm.FOOTMAN) > 0) {
                ti.getAttackArms().put(Arm.FOOTMAN, ti.getAttackArms().get(Arm.FOOTMAN) - 1);
            }
        } else if (defenseFamily.getBattleCharacter().equals("Salladhor Saan")) {
            // The combat strength of any opposed supporting ships is reduced to
            // zero.
            ti.setAttackSeaSupport(0);
        } else if (defenseFamily.getBattleCharacter().equals("Balon Greyjoy")) {
            // The combat strength of your opponents Leader is 0.
            ti.setConquerRate(ti.getConquerRate() + attackCharacter.getPower());
        } else if (defenseFamily.getBattleCharacter().equals("Catelyn Stark")) {
            if (ti.getAction().getType().equals(ActionType.DEFENSE)) {
                if (ti.getAction().isStar()) {
                    ti.setConquerRate(ti.getConquerRate() + 2);
                } else {
                    ti.setConquerRate(ti.getConquerRate() + 1);
                }
            }
        }
    }

    private void characterCastAfterBattleWin(TerritoryInfo ti, String winFamilyName, String loseFamilyName)
            throws Exception {
        FamilyInfo winFamily = serverGameInfo.getFamiliesMap().get(winFamilyName);
        FamilyInfo loseFamily = serverGameInfo.getFamiliesMap().get(loseFamilyName);
        if (winFamily.getBattleCharacter().equals("Queen of Thorns")
                || loseFamily.getBattleCharacter().equals("Queen of Thorns")) {
            // all specify ability is disable
            // Queen of Thorns,Cancel the special ability of your opponents
            // House Card.
            return;
        }
        if (winFamily.getBattleCharacter().equals("Master Luwin")) {
            // If you win this battle, you may return one of your discarded
            // house cards to your hand.
            event(MessageType.LuWin_Special, winFamily.getAddress());
        } else if (winFamily.getBattleCharacter().equals("Sir Jamie Lannister")) {
            // If you win this battle, immediately gain two power tokens.
            winFamily.setCurPower(winFamily.getCurPower() + 2);
        } else if (winFamily.getBattleCharacter().equals("Cersei Lannister")) {
            // If you win this battle, remove any one of the losing opponents
            // Order tokens from the board.
            event(MessageType.Cersei_Special, winFamily.getAddress());
        } else if (winFamily.getBattleCharacter().equals("Renly Baratheon")) {
            // upgrade footman
            event(MessageType.Renly_Special, winFamily.getAddress());
        } else if (winFamily.getBattleCharacter().equals("Melisandre of Asshai")) {
            // If you win the battle, you may expend one power token to choose
            // and discard one House Card from the losing Players hand.
            event(MessageType.Melisandre_Special, winFamily.getAddress());
        } else if (winFamily.getBattleCharacter().equals("Asha Greyjoy")) {
            // If you win this battle, you may immediately remove a Support or
            // Consolidate Power order in any Sea or Land area adjacent to the
            // battle.
            event(MessageType.Asha_Special, winFamily.getAddress());
        }

    }

    private void powerEvent() throws Exception {
        event(MessageType.Power);
    }

    private void syncGameStatus() throws Exception {
        logger.debug("Server: Send the update families to all");
        CountDownLatch updateFamiliesCDL = new CountDownLatch(countdownN);
        serverGameInfo.getCdl().put(MessageType.Update_Families, updateFamiliesCDL);
        node.sendGameStatusToClient();
        updateFamiliesCDL.await();
    }

    private int getRemainCommands(ActionType actionType, FamilyInfo fi) {
        int cmds = 0;
        for (FamilyInfo tmp : serverGameInfo.getAddrMap().values()) {
            if (fi.getName().equals(tmp.getName())) {
                for (String terrName : tmp.getConquerTerritories()) {
                    TerritoryInfo ti = serverGameInfo.getTerrMap().get(terrName);
                    if (ti.getAction().getType().equals(actionType)) {
                        logger.debug("Server: " + actionType.toString() + " " + fi.getName());
                        cmds++;
                    }
                }
            }
        }
        logger.debug("Server: " + actionType.toString() + " " + fi.getName() + " " + String.valueOf(cmds));
        return cmds;
    }

    private int getRemainCommands(ActionType actionType) {
        int cmds = 0;
        for (FamilyInfo fi : serverGameInfo.getAddrMap().values()) {
            for (String terrName : fi.getConquerTerritories()) {
                TerritoryInfo ti = serverGameInfo.getTerrMap().get(terrName);
                if (ti.getAction().getType().equals(actionType)) {
                    logger.debug("Server: " + actionType.toString());
                    cmds++;
                }
            }
        }
        logger.debug("Server: " + actionType.toString() + " " + String.valueOf(cmds));
        return cmds;
    }

}
