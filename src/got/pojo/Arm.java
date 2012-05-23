package got.pojo;

public enum Arm {

    FOOTMAN(1),
    KNIGHT(2),
    SHIP(1);

    private int capability;

    Arm(int capability) {
        this.capability = capability;
    }

    public int getCapability() {
        return capability;
    }
}
