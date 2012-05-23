package got.test;

import got.core.Node;
import got.pojo.Arm;
import got.pojo.GameInfo;
import got.pojo.event.FamilyInfo;
import got.pojo.event.TerritoryInfo;

import java.util.HashMap;
import java.util.Map;

public class TestInject {

    public static void battleInject(Node node){
        GameInfo gameInfo = node.getServerGameInfo();
        TerritoryInfo ti = gameInfo.getTerrMap().get("Ironman's Bay");
        ti.setAttackFamilyName("LANNISTER");
        Map<Arm,Integer> attackArms = new HashMap<Arm,Integer>();
        attackArms.put(Arm.KNIGHT, 0);
        attackArms.put(Arm.FOOTMAN, 0);
        attackArms.put(Arm.SHIP, 1);
        ti.setAttackArms(attackArms);
        ti.setAttackTerritoryName("The Golden Sound");
        
        TerritoryInfo ti1 = gameInfo.getTerrMap().get("The Golden Sound");
        ti1.getConquerArms().put(Arm.SHIP, 0);
    }
    
    public static void characterInject(Node node){
        FamilyInfo attack = node.getGameInfo().getFamiliesMap().get("LANNISTER");
        FamilyInfo defense = node.getGameInfo().getFamiliesMap().get("GREYJOY");
        attack.setBattleCharacter("LANNISTER");
        defense.setBattleCharacter("GREYJOY");
    }
}
