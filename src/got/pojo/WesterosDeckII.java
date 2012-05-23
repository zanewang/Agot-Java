package got.pojo;

public enum WesterosDeckII {
    END_OF_SUMMER(1,true),
    BEGIN_OF_WINTER(1,false),
    POWER_GAME(4,false),
    CLASH_OF_KING(4, false);

    private int num;
    private boolean flag;

    WesterosDeckII(int num, boolean flag) {
        this.num = num;
        this.flag = flag;
    }

    public boolean hasWildToken() {
        return flag;
    }

    public int getCardNum() {
        return num;
    }

}
