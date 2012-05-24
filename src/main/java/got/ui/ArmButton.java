package got.ui;

import got.pojo.Arm;

import javax.swing.JButton;

public class ArmButton extends JButton {

    private Arm type;
    private boolean retreat;

    public ArmButton(Arm type,boolean retreat) {
        this.type = type;
        this.retreat = retreat;
    }

    public Arm getType() {
        return type;
    }
    
    public boolean isRetreat(){
        return retreat;
    }
}
