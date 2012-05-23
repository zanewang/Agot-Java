package got.pojo;

public enum WesterosDeckI {
    SUPPLY(4,false),
    MUSTER(4,false),
    END_OF_SUMMER(1,true),
    BEGIN_OF_WINTER(1,false);

    private int num;
    private boolean flag;
    
    WesterosDeckI(int num,boolean flag){
        this.num = num;
        this.flag = flag;
    }
    
    public boolean hasWildToken(){
        return flag;
    }
    
    public int getCardNum(){
        return num;
    }
}
