package got.pojo;

import java.io.Serializable;

public class CharacterInfo implements Serializable {

    private String name;
    private String familyName;
    private int power;
    private int sword;
    private int shield;
    private String special;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the familyName
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * @param familyName
     *            the familyName to set
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * @return the power
     */
    public int getPower() {
        return power;
    }

    /**
     * @param power
     *            the power to set
     */
    public void setPower(int power) {
        this.power = power;
    }

    /**
     * @return the sword
     */
    public int getSword() {
        return sword;
    }

    /**
     * @param sword
     *            the sword to set
     */
    public void setSword(int sword) {
        this.sword = sword;
    }

    /**
     * @return the shield
     */
    public int getShield() {
        return shield;
    }

    /**
     * @param shield
     *            the shield to set
     */
    public void setShield(int shield) {
        this.shield = shield;
    }

    /**
     * @return the special
     */
    public String getSpecial() {
        return special;
    }

    /**
     * @param special
     *            the special to set
     */
    public void setSpecial(String special) {
        this.special = special;
    }

}
