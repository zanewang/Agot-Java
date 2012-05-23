package got.ui;

import got.core.Node;
import got.core.ShiftStateEvent;
import got.logic.GameState;
import got.pojo.Arm;
import got.pojo.event.TerritoryInfo;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;

import org.apache.log4j.Logger;

public class DisarmamentDialog extends JDialog {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private TerritoryInfo ti;
    private Node node;

    private List<ArmButton> btns = new ArrayList<ArmButton>();

    public DisarmamentDialog(Node node, Point p, TerritoryInfo ti) {
        super(node.gameFrame);
        this.setSize(256, 64);
        this.setUndecorated(true);
        this.setLocation(p);
        this.setVisible(true);
        this.ti = ti;
        this.node = node;
        createComponent();
        layoutCoponents();
        setupListeners();
    }

    // private setButtonStatus()

    protected void createComponent() {
        // TODO Auto-generated method stub
        for (Arm type : Arm.values()) {
            ArmButton armButton = new ArmButton(type, false);
            armButton.setText(type.toString());
            btns.add(armButton);

            ArmButton armRetreatButton = new ArmButton(type, true);
            armRetreatButton.setText(type.toString() + "Retreat");
            btns.add(armRetreatButton);
        }
    }

    protected void layoutCoponents() {
        // TODO Auto-generated method stub
        this.setLayout(new GridLayout(Arm.values().length, 2));
        for (ArmButton armButton : btns) {
            this.add(armButton);
            Arm arm = armButton.getType();
            if (!armButton.isRetreat() && ti.getConquerArms().get(arm) == 0) {
                armButton.setEnabled(false);
            }
            if (armButton.isRetreat() && ti.getRetreatArms().get(arm) == 0) {
                armButton.setEnabled(false);
            }
        }
    }

    protected void setupListeners() {
        // TODO Auto-generated method stub
        for (final ArmButton btn : btns) {
            btn.addMouseListener(new MouseListenerAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // TODO Auto-generated method stub
                    if (!btn.isEnabled()) {
                        return;
                    }
                    Arm arm = btn.getType();
                    Map<Arm, Integer> armies = btn.isRetreat() ? ti.getRetreatArms() : ti.getConquerArms();
                    armies.put(arm, armies.get(arm) - 1);
                    node.getMyFamilyInfo().setDisarmRemain(node.getMyFamilyInfo().getDisarmRemain() - 1);
                    if (node.getGameInfo().getState().equals(GameState.Start_Change_Supply)) {
                        if (node.getGameInfo().isEnoughSupply(node.getMyFamilyInfo().getName())) {
                            node.getGameInfo().shiftTO(GameState.Finish_Change_Supply);
                            node.onQueueEvent(new ShiftStateEvent());
                        }
                    } else if (node.getGameInfo().getState().equals(GameState.Start_Disarm)) {
                        if(node.getMyFamilyInfo().getDisarmRemain() == 0){
                            node.getGameInfo().shiftTO(GameState.Finish_Disarm);
                            node.onQueueEvent(new ShiftStateEvent());
                        }
                    }
                    DisarmamentDialog.this.dispose();
                }
            });
        }
    }
}
