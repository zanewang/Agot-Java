package got.ui;

import got.pojo.MusterType;

import javax.swing.JButton;

public class MusterButton extends JButton {

    private MusterType type;

    public MusterButton(MusterType type) {
        this.type = type;
    }

    public MusterType getType() {
        return type;
    }
}
