package got.pojo;

public enum Action {

    MARCH_I(ActionType.March, 0),
    SUPPORT_I(ActionType.Support, 0),
    RAID_I(ActionType.Raid, 0),
    CONSOLIDATE_I(ActionType.CONSOLIDATE, 0),
    DEFENSE_I(ActionType.DEFENSE, 0),
    MARCH_II(ActionType.March, 1),
    SUPPORT_II(ActionType.Support, 1),
    RAID_II(ActionType.Raid, 1),
    CONSOLIDATE_II(ActionType.CONSOLIDATE, 1),
    DEFENSE_II(ActionType.DEFENSE, 1),
    MARCH_III(ActionType.March, 2),
    SUPPORT_III(ActionType.Support, 2),
    RAID_III(ActionType.Raid, 2),
    CONSOLIDATE_III(ActionType.CONSOLIDATE, 2),
    DEFENSE_III(ActionType.DEFENSE, 2),
    NONE(ActionType.None, 0);

    private ActionType type;
    private int star;

    Action(ActionType type, int star) {
        this.type = type;
        this.star = star;
    }

    public ActionType getType() {
        return this.type;
    }

    public Boolean isStar() {
        return star == 2 ? true : false;
    }
    
    public Boolean isCommon(){
        return star == 1 ? true : false;
    }
    
    public Boolean isLow(){
        return star == 0 ? true : false;
    }
}
