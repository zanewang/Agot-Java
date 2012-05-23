package got.pojo;

public enum WesterosDeckIII {
    END_OF_SUMMER(2,true),
    BEGIN_OF_WINTER(1,false),
    WILDLING_ATTACK(3,false),
    STORM_SEA(1,false),
    STORM_SWORDS(1,true),
    RAVEN_FEAST(1,false),
    RAIN_AUTUMN(1,true);
    
    private int num;
    private boolean flag;

    WesterosDeckIII(int num, boolean flag) {
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
