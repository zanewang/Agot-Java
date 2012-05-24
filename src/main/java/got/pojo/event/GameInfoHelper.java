package got.pojo.event;

import got.pojo.Action;
import got.pojo.GameInfo;

import org.jgroups.Address;

public class GameInfoHelper {

    public static int getRavenStar(int ravenRank) {
        switch (ravenRank) {
        case 1:
            return 3;
        case 2:
            return 3;
        case 3:
            return 2;
        case 4:
            return 1;
        case 5:
            return 0;
        }
        return 0;
    }

    public static int getAttackRate(Action action) {
        switch (action) {
        case MARCH_I:
            return -1;
        case MARCH_II:
            return 0;
        case MARCH_III:
            return 1;
        }
        return 0;
    }
    
    public static int getDefenseRate(Action action) {
        switch (action) {
        case DEFENSE_I:
            return -1;
        case DEFENSE_II:
            return 0;
        case DEFENSE_III:
            return 1;
        }
        return 0;
    }

    public static int[] getSupplies(int buckets) {
        switch (buckets) {
        case 0:
            return new int[] { 2, 2 };
        case 1:
            return new int[] { 3, 2 };
        case 2:
            return new int[] { 3, 2, 2, };
        case 3:
            return new int[] { 3, 2, 2, 2 };
        case 4:
            return new int[] { 3, 3, 2, 2 };
        case 5:
            return new int[] { 4, 3, 2, 2 };
        case 6:
            return new int[] { 4, 3, 2, 2, 2 };
        }
        return new int[] { 0 };
    }

    public static void dumpGameStatus(GameInfo gameInfo) {
        for (Address address : gameInfo.getAddrMap().keySet()) {
//            System.out.println(address.toString() + gameInfo.getAddrMap().get(address).toString());
        }
        for (String name : gameInfo.getFamiliesMap().keySet()) {
          System.out.println(name + gameInfo.getFamiliesMap().get(name).toString());
      }
    }
}
