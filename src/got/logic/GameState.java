package got.logic;

public enum GameState {
    
    Game_Init,
    
    Ready_Start,
    Ready_Order,
    Start_Order,
    Ready_Raven,
    Start_Raven,
    Ready_Raid,
    Choose_Raid_Order,
    Choose_Raid_Territory,
    Ready_March,
    Choose_March_Order,
    Choose_March_Territory,

    Ready_Consolidate,
    Reply_Consolidate,

    Ready_Westeros_Consolidate,
    Start_Westeros_Consolidate,

    Ready_Westeros_Mustering,
    Choose_Muster_Territory,
    Choose_Muster_Ship_Territory,

    Ready_Westeros_Clash_Kings_Iron,
    Start_Compete_Iron,
    Ready_Westeros_Clash_Kings_Blade,
    Start_Compete_Blade,
    Ready_Westeros_Clash_Kings_Raven,
    Start_Compete_Raven,

    Ready_Westeros_Judge_Iron,
    Judge_Iron,
    Ready_Westeros_Judge_Blade,
    Judge_Blade,
    Ready_Westeros_Judge_Raven,
    Judge_Raven,

    Ready_Battle_Support,
    Start_Support,

    Ready_Battle_General,
    Start_General,

    Ready_Battle_Blade,
    Start_Blade,

    Ready_Retreat,
    Start_Retreat,

    Ready_Change_Supply,
    Start_Change_Supply,
    Finish_Change_Supply,
    Reply_Change_Supply,

    Ready_Asha_Special,
    Start_Asha_Special,
    Reply_Asha_Special,
    
    Ready_Cersei_Special,
    Start_Cersei_Special,
    Reply_Cersei_Special,
    
    Ready_LuWin_Special,
    Start_LuWin_Special,
    Ready_Renly_Special,
    Start_Renly_Special,
    
    Ready_Melisandre_Special,
    Start_Melisandre_Special,
    Reply_Melisandre_Special,

    Ready_Wild_Attack,
    Start_Wild_Attack,

    Ready_Disarm,
    Start_Disarm,
    Ready_Ransom,
    Start_Ransom,
    Finish_Disarm,
    Reply_Disarm,

    Ready_Judge_Disarm,
    Start_Judge_Disarm,
    Ready_Judge_General,
    Start_Judge_General,

    Show_Info,

    Update_Families,

    Do_Nothing,
}
