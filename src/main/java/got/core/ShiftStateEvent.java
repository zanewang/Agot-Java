package got.core;

import got.io.MessageType;
import got.logic.GameState;
import got.pojo.Action;
import got.pojo.ActionType;
import got.pojo.GameInfo;
import got.pojo.TerritoryType;
import got.pojo.event.FamilyInfo;
import got.pojo.event.TerritoryInfo;
import got.ui.BladeDialog;
import got.ui.CharacterDialog;
import got.ui.CompeteDialog;
import got.ui.InfoDialogWithPicture;
import got.ui.JudgeDialog;
import got.ui.ReadyStartDialog;
import got.ui.SupportDialog;

import java.util.List;

public class ShiftStateEvent implements GameEvent {
    @Override
    public void handler(Node node) {
        // TODO Auto-generated method stub
        GameInfo gameInfo = node.getGameInfo();
        switch (node.getGameInfo().getState()) {
        case Ready_Start:
            if (node.getGameInfo().isHost()) {
                new ReadyStartDialog(node);
            }
            break;
        case Ready_Order:
            String orderHelpStr = "Round for Place the Order. Left click the your territroy to place the Order, Right click when finish Order";
            new InfoDialogWithPicture(node, "Round Planning Pharse", orderHelpStr, GameState.Start_Order, false);
            break;

        case Ready_Raven:
            String ravenHelpStr = "The holder may swap one of his Order tokens on the game board for one of his unused Order tokens.";
            new InfoDialogWithPicture(node, "Round Messenger Raven", ravenHelpStr, GameState.Start_Raven, false);
            break;
        case Ready_Raid:
            String raidHelpStr = "Round for Raid your nearby Territory";
            new InfoDialogWithPicture(node, "Round Raid", raidHelpStr, GameState.Choose_Raid_Order, false);
            break;
        case Ready_March:
            String marchHelpStr = "Round for March your army";
            new InfoDialogWithPicture(node, "Round March", marchHelpStr, GameState.Choose_March_Order, false);
            break;
        case Ready_Consolidate:
            int tokens = node.getGameInfo().getTerritoryConsolidateActions(node.getMyFamilyInfo().getName());
            String consolidateHelpStr = String.format("Round for Consolidating your power, get %d powers", tokens);
            new InfoDialogWithPicture(node, "Round Consolidate", consolidateHelpStr, GameState.Reply_Consolidate, true);
            break;
        case Reply_Consolidate:
            tokens = node.getGameInfo().getTerritoryConsolidateActions(node.getMyFamilyInfo().getName());
            node.getMyFamilyInfo().setCurPower(node.getMyFamilyInfo().getCurPower() + tokens);
            try {
                node.sendGameStatusUpdate(MessageType.Power);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        case Ready_Westeros_Mustering:
            String musterHelpStr = "Round for Mustering";
            new InfoDialogWithPicture(node, "Round Muster", musterHelpStr, GameState.Choose_Muster_Territory, false);
            break;
        case Ready_Westeros_Clash_Kings_Iron:
            String ironHelpStr = "Round for Clashing of The King Iron";
            new InfoDialogWithPicture(node, "Round Compete the Iron", ironHelpStr, GameState.Start_Compete_Iron, true);
            break;
        case Start_Compete_Iron:
            new CompeteDialog(node);
            break;
        case Ready_Westeros_Clash_Kings_Blade:
            String bladeHelpStr = "Round for Clashing of The King Blade";
            new InfoDialogWithPicture(node, "Round Compete the Blade", bladeHelpStr, GameState.Start_Compete_Blade,
                    true);
            break;
        case Start_Compete_Blade:
            new CompeteDialog(node);
            break;
        case Ready_Westeros_Clash_Kings_Raven:
            String clashKingHelpStr = "Round for Clashing of The King Raven";
            new InfoDialogWithPicture(node, "Round Compete the Raven", clashKingHelpStr, GameState.Start_Compete_Raven,
                    true);
            break;
        case Start_Compete_Raven:
            new CompeteDialog(node);
            break;

        case Ready_Westeros_Judge_Iron:
            String judgeIronHelpStr = "Round for Judging Iron";
            new InfoDialogWithPicture(node, "Round Judge Iron", judgeIronHelpStr, GameState.Judge_Iron, true);
            break;
        case Judge_Iron:
            new JudgeDialog(node);
            break;
        case Ready_Westeros_Judge_Blade:
            String judgeBladeHelpStr = "Round for Judging Iron";
            new InfoDialogWithPicture(node, "Round Judge Blade", judgeBladeHelpStr, GameState.Judge_Blade, true);
            break;
        case Judge_Blade:
            new JudgeDialog(node);
            break;
        case Ready_Westeros_Judge_Raven:
            String judgeRavenHelpStr = "Round for Judging Raven";
            new InfoDialogWithPicture(node, "Round Judge Raven", judgeRavenHelpStr, GameState.Judge_Raven, true);
            break;
        case Judge_Raven:
            new JudgeDialog(node);
            break;
        case Ready_Battle_Support:
            String battleSupportHelpStr = "Round for Support";
            new InfoDialogWithPicture(node, "Round Battle Support", battleSupportHelpStr, GameState.Start_Support, true);
            break;
        case Start_Support:
            new SupportDialog(node);
            break;

        case Ready_Battle_General:
            String battleGeneraltHelpStr = node.getGameInfo().powerVS(node.getGameInfo().getLastSelect());
            battleGeneraltHelpStr += "\nPlease Choose Your House Card";
            new InfoDialogWithPicture(node, "Round Choose Battle General", battleGeneraltHelpStr, GameState.Start_General,
                    true);
            break;
        case Start_General:
            new CharacterDialog(node, node.getMyFamilyInfo().getName());
            break;

        case Ready_Battle_Blade:
            String battleBladeHelpStr = "You're holding the Valyrian Steel Blade token, once per game round, use the token in combat to grant himself a +1 Combat Strength bonus.";
            new InfoDialogWithPicture(node, "Round Valyrian Steel Blade", battleBladeHelpStr, GameState.Start_Blade, true);
            break;
        case Start_Blade:
            new BladeDialog(node);
            break;

        case Ready_Retreat:
            String retreatHelpStr = "Round for Retreating";
            new InfoDialogWithPicture(node, "Round Retreat", retreatHelpStr, GameState.Start_Retreat, false);
            break;

        case Ready_Change_Supply:
            FamilyInfo myFamilyInfo = node.getMyFamilyInfo();
            node.getGameInfo().resetSupplyBuckets(myFamilyInfo.getName());
            String changeSupplyHelpStr = String.format("Your buckets change to %d. ", myFamilyInfo.getSupply());
            boolean isEnough = node.getGameInfo().isEnoughSupply(myFamilyInfo.getName());
            if (!isEnough) {
                changeSupplyHelpStr += "Your armies exceed the supply and require disarmament";
            }

            if (isEnough) {
                new InfoDialogWithPicture(node, "Round Change Supply", changeSupplyHelpStr,
                        GameState.Reply_Change_Supply, true);

            } else {
                new InfoDialogWithPicture(node, "Round Change Supply", changeSupplyHelpStr, GameState.Start_Disarm,
                        false);
            }
            break;
        case Finish_Change_Supply:
            String supplyHelpStr = "Supply is ok now";
            new InfoDialogWithPicture(node, "Round Finish Supply", supplyHelpStr, GameState.Reply_Change_Supply, true);
            break;

        case Reply_Change_Supply:
            try {
                node.sendGameStatusUpdate(MessageType.Supply);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            break;
        case Ready_Asha_Special:
            String battleTerritoryName = gameInfo.getLastSelect();
            TerritoryInfo battleTerritory = gameInfo.getTerrMap().get(battleTerritoryName);
            List<String> seaConsolidates = gameInfo.getNearbyTerritoryWtihAction(battleTerritoryName,
                    TerritoryType.SEA, battleTerritory.getConquerFamilyName(), ActionType.CONSOLIDATE);
            List<String> seaSupports = gameInfo.getNearbyTerritoryWtihAction(battleTerritoryName, TerritoryType.SEA,
                    battleTerritory.getConquerFamilyName(), ActionType.Support);
            List<String> landConsolidates = gameInfo.getNearbyTerritoryWtihAction(battleTerritoryName,
                    TerritoryType.LAND, battleTerritory.getConquerFamilyName(), ActionType.CONSOLIDATE);
            List<String> landSupports = gameInfo.getNearbyTerritoryWtihAction(battleTerritoryName, TerritoryType.LAND,
                    battleTerritory.getConquerFamilyName(), ActionType.Support);
            landSupports.addAll(landConsolidates);
            landSupports.addAll(seaSupports);
            landSupports.addAll(seaConsolidates);
            if (landSupports.size() > 0) {
                String AshaHelpStr = "Asha could cast her special ability, you may immediately remove a Support or Consolidate Power order in any Sea or Land area adjacent to the battle.";
                new InfoDialogWithPicture(node, "Round Asha Ability", AshaHelpStr, GameState.Start_Asha_Special, false);
            } else {
                String AshaHelpStr = "Asha could not case her special ability, because no avaiable territory with Support or Conolidate Power order in any Sea or Land area adjacent to the battle";
                new InfoDialogWithPicture(node, "Round Asha Ability", AshaHelpStr, GameState.Reply_Asha_Special, true);

            }
            break;
        case Reply_Asha_Special:
            try {
                node.sendGameStatusUpdate(MessageType.Asha_Special);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        case Ready_LuWin_Special:
            myFamilyInfo = node.getMyFamilyInfo();
            boolean flag = false;
            for (boolean isUsed : myFamilyInfo.getCharacterMap().values()) {
                if (isUsed == false) {
                    flag = true;
                }
            }
            String helpStr;
            if (!flag) {
                helpStr = "No other used House Card, skip LuWin's special ability";
                new InfoDialogWithPicture(node, "Round LuWin Ability", helpStr, GameState.Do_Nothing, false);
            } else {
                helpStr = "you may return one of your discarded house cards to your hand.";
                new InfoDialogWithPicture(node, "Round LuWin Ability", helpStr, GameState.Start_LuWin_Special, false);
            }
            break;
        case Start_LuWin_Special:
            new CharacterDialog(node, node.getMyFamilyInfo().getName());
            break;

        case Ready_Cersei_Special:
            battleTerritoryName = gameInfo.getLastSelect();
            battleTerritory = gameInfo.getTerrMap().get(battleTerritoryName);
            String loseFamilyName = battleTerritory.getAttackFamilyName().equals("LANNISTER") ? battleTerritory
                    .getConquerFamilyName() : battleTerritory.getAttackFamilyName();
            FamilyInfo loseFamilyInfo = gameInfo.getFamiliesMap().get(loseFamilyName);
            flag = false;
            for (String terrName : loseFamilyInfo.getConquerTerritories()) {
                TerritoryInfo terr = gameInfo.getTerrMap().get(terrName);
                if (!terr.getAction().equals(Action.NONE)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                helpStr = "You may remove any one of the losing opponents Order tokens from the board.";
                new InfoDialogWithPicture(node, "Round Cerseri Ability", helpStr, GameState.Start_Cersei_Special, false);
            } else {
                helpStr = "You may remove any one of the losing opponents Order tokens from the board. But no any order in your opponenet's territories, Skip now";
                new InfoDialogWithPicture(node, "Round Cerseri Ability", helpStr, GameState.Reply_Cersei_Special, true);

            }
        case Reply_Cersei_Special:
            try {
                node.sendGameStatusUpdate(MessageType.Cersei_Special);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        case Ready_Melisandre_Special:
            flag = node.getMyFamilyInfo().getCurPower() > 0 ? true : false;
            if (flag) {
                helpStr = "you may expend one power token to choose and discard one House Card from the losing Players hand.";
                new InfoDialogWithPicture(node, "Round Melisandre Ability", helpStr,
                        GameState.Start_Melisandre_Special, true);
            } else {
                helpStr = "you may expend one power token to choose and discard one House Card from the losing Players hand. But you don't have enough money, Skip now";
                new InfoDialogWithPicture(node, "Round Melisandre Ability", helpStr,
                        GameState.Reply_Melisandre_Special, true);

            }
            break;
        case Start_Melisandre_Special:
            battleTerritoryName = gameInfo.getLastSelect();
            battleTerritory = gameInfo.getTerrMap().get(battleTerritoryName);
            loseFamilyName = battleTerritory.getAttackFamilyName().equals("BARATHEON") ? battleTerritory
                    .getConquerFamilyName() : battleTerritory.getAttackFamilyName();
            new CharacterDialog(node, loseFamilyName);
            break;
        case Reply_Melisandre_Special:
            try {
                node.sendGameStatusUpdate(MessageType.Melisandre_Special);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;

        case Ready_Wild_Attack:
            helpStr = "Wilding Attack!!!! We need gather " + String.valueOf(gameInfo.getWildAttakRate())
                    + " armies to denfense the attack";
            new InfoDialogWithPicture(node, "Round Wild Attack", helpStr, GameState.Start_Wild_Attack, true);
            break;
        case Start_Wild_Attack:
            new CompeteDialog(node);
            break;

        case Ready_Judge_Disarm:
            helpStr = "judge disarm";
            new InfoDialogWithPicture(node, "Round Judge Disarm", helpStr, GameState.Start_Judge_Disarm, true);
            break;
        case Start_Judge_Disarm:
            new JudgeDialog(node);
            break;

        case Ready_Judge_General:
            helpStr = "judge general";
            new InfoDialogWithPicture(node, "Round Judge General", helpStr, GameState.Start_Judge_General, true);
            break;
        case Start_Judge_General:
            new JudgeDialog(node);
            break;

        case Ready_Ransom:
            helpStr = "start ransom";
            new InfoDialogWithPicture(node, "Round Judge Ransom", helpStr, GameState.Start_Ransom, true);
            break;
        case Start_Ransom:
            new CharacterDialog(node, node.getMyFamilyInfo().getName());
            break;

        case Ready_Disarm:
            helpStr = "disarm";
            new InfoDialogWithPicture(node, "Round Disarm", helpStr, GameState.Start_Disarm, true);
            break;
        case Finish_Disarm:
            helpStr = "finish disarm";
            new InfoDialogWithPicture(node, "Round Finish Disarm", helpStr, GameState.Ready_Disarm, true);
            break;
        case Reply_Disarm:
            try {
                node.sendGameStatusUpdate(MessageType.Disarm);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        }
    }
}
