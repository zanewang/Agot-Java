package got.pojo;

public enum MusterType {
    FootMan(1),
    Ship(1),
    Knight(2),
    Upgrade(1);

    private int cap;

    MusterType(int cap) {
        this.cap = cap;
    }

    public int getRequireCapacity() {
        return cap;
    }

}
