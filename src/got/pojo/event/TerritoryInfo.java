package got.pojo.event;

import got.pojo.Action;
import got.pojo.Arm;
import got.pojo.TerritoryType;

import java.awt.Point;
import java.awt.Polygon;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerritoryInfo implements Serializable {

    // dynamic configs

    public final static String NEUTRAL_FAMILY = "NEUTRAL";
    private Action action;
    private Map<Arm, Integer> conquerArms = new HashMap<Arm, Integer>();

    private String conquerFamilyName = NEUTRAL_FAMILY;
    private int conquerRate = 0;
    private int conquerSeaSupport = 0;
    private int conquerLandSupport = 0;

    private Map<Arm, Integer> attackArms = new HashMap<Arm, Integer>();
    private int attackRate = 0; // march rate
    private int attackSeaSupport = 0;
    private int attackLandSupport = 0;
    private String attackFamilyName;
    private String attackTerritoryName;

    private Map<Arm, Integer> retreatArms = new HashMap<Arm, Integer>();
    // static configs

    private String name;
    private int mustering;
    private int availMustering;
    private int supply;
    private int power;

    private TerritoryType type;
    private Polygon poly;
    private Point orderP;
    private List<Point> armPs = new ArrayList<Point>();

    /**
     * @return the conquerSeaSupport
     */
    public int getConquerSeaSupport() {
        return conquerSeaSupport;
    }

    /**
     * @param conquerSeaSupport
     *            the conquerSeaSupport to set
     */
    public void setConquerSeaSupport(int conquerSeaSupport) {
        this.conquerSeaSupport = conquerSeaSupport;
    }

    /**
     * @return the conquerLandSupport
     */
    public int getConquerLandSupport() {
        return conquerLandSupport;
    }

    /**
     * @param conquerLandSupport
     *            the conquerLandSupport to set
     */
    public void setConquerLandSupport(int conquerLandSupport) {
        this.conquerLandSupport = conquerLandSupport;
    }

    /**
     * @return the attackSeaSupport
     */
    public int getAttackSeaSupport() {
        return attackSeaSupport;
    }

    /**
     * @param attackSeaSupport
     *            the attackSeaSupport to set
     */
    public void setAttackSeaSupport(int attackSeaSupport) {
        this.attackSeaSupport = attackSeaSupport;
    }

    /**
     * @return the attackLanSupport
     */
    public int getAttackLandSupport() {
        return attackLandSupport;
    }

    /**
     * @param attackLanSupport
     *            the attackLanSupport to set
     */
    public void setAttackLandSupport(int attackLandSupport) {
        this.attackLandSupport = attackLandSupport;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getMustering() {
        return mustering;
    }

    public void setMustering(int mustering) {
        this.mustering = mustering;
    }

    public int getSupply() {
        return supply;
    }

    public void setSupply(int supply) {
        this.supply = supply;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public TerritoryType getType() {
        return type;
    }

    public void setType(TerritoryType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoly(Polygon poly) {
        this.poly = poly;
    }

    public Polygon getPoly() {
        return poly;
    }

    public Boolean canPlaceOrder() {
        return true;
    }

    public void setOrderP(Point orderP) {
        this.orderP = orderP;
    }

    public Point getOrderP() {
        return orderP;
    }

    public Map<Arm, Integer> getConquerArms() {
        return conquerArms;
    }

    public void setConquerArms(Map<Arm, Integer> conquerArms) {
        this.conquerArms = conquerArms;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((action == null) ? 0 : action.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TerritoryInfo other = (TerritoryInfo) obj;
        if (action != other.action)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public Map<Arm, Integer> getAttackArms() {
        return attackArms;
    }

    public void setAttackArms(Map<Arm, Integer> attackArms) {
        this.attackArms = attackArms;
    }

    public String getAttackFamilyName() {
        return attackFamilyName;
    }

    public void setAttackFamilyName(String attackFamilyName) {
        this.attackFamilyName = attackFamilyName;
    }

    public String getConquerFamilyName() {
        return conquerFamilyName;
    }

    public void setConquerFamilyName(String conquerFamilyName) {
        this.conquerFamilyName = conquerFamilyName;
    }

    public String getAttackTerritoryName() {
        return attackTerritoryName;
    }

    public void setAttackTerritoryName(String attackTerritoryName) {
        this.attackTerritoryName = attackTerritoryName;
    }

    public List<Point> getArmPs() {
        return armPs;
    }

    public void setArmPs(List<Point> armPs) {
        this.armPs = armPs;
    }

    public void setAttackRate(int attackRate) {
        this.attackRate = attackRate;
    }

    public int getAttackRate() {
        return attackRate;
    }

    public int getConquerArmNum() {
        int sum = 0;
        for (int arms : conquerArms.values()) {
            sum += arms;
        }
        return sum;
    }

    public int getAttackArmNum() {
        int sum = 0;
        for (int arms : attackArms.values()) {
            sum += arms;
        }
        return sum;
    }

    public int getConquerCapabilities() {
        int sum = 0;
        for (Arm arm : conquerArms.keySet()) {
            sum += arm.getCapability() * conquerArms.get(arm);
        }
        return sum;
    }

    public int getAttackCapabilities() {
        int sum = 0;
        for (Arm arm : attackArms.keySet()) {
            sum += arm.getCapability() * attackArms.get(arm);
        }
        return sum;
    }

    public void resetAfterAttack(boolean attackWin) {
        if (attackWin) {
            this.setAction(Action.NONE);
            this.setConquerFamilyName(this.getAttackFamilyName());
            for (Arm arm : attackArms.keySet()) {
                conquerArms.put(arm, attackArms.get(arm));
            }
            this.setConquerRate(0);
            this.setConquerSeaSupport(0);
            this.setConquerLandSupport(0);
            this.resetAttackArms();
            this.setAttackFamilyName(null);
            this.setAttackRate(0);
            this.setAttackSeaSupport(0);
            this.setAttackLandSupport(0);
            this.setAttackTerritoryName(null);
        } else {
            this.setAction(Action.NONE);
            this.resetAttackArms();
            this.setAttackFamilyName(null);
            this.setAttackRate(0);
            this.setAttackSeaSupport(0);
            this.setAttackLandSupport(0);
            this.setAttackTerritoryName(null);
        }
    }

    public void resetAttackArms() {
        for (Arm arm : attackArms.keySet()) {
            attackArms.put(arm, 0);
        }
    }

    public void setAvailMustering(int availMustering) {
        this.availMustering = availMustering;
    }

    public int getAvailMustering() {
        return availMustering;
    }

    public void resetAvailMustering() {
        this.availMustering = this.mustering;
    }

    public void setConquerRate(int conquerRate) {
        this.conquerRate = conquerRate;
    }

    public int getConquerRate() {
        return conquerRate;
    }

    public void setRetreatArms(Map<Arm, Integer> retreatArms) {
        for (Arm arm : retreatArms.keySet()) {
            this.retreatArms.put(arm, retreatArms.get(arm));
        }
    }

    public Map<Arm, Integer> getRetreatArms() {
        return retreatArms;
    }
}
