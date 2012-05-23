package got.ui;

import got.core.Node;
import got.io.MessageType;
import got.pojo.TerritoryType;
import got.pojo.event.TerritoryInfo;

import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;

public class SupportDialog extends JDialog {

    private Node node;
    private JButton attackBtn;
    private JButton conquerBtn;
    private TerritoryInfo battleTerritory;

    public SupportDialog(Node node) {
        super(node.gameFrame);
        this.node = node;
    }

    public void createComponent() {
        battleTerritory = node.getGameInfo().getTerrMap().get(node.getGameInfo().getLastSelect());
        attackBtn = new JButton();
        attackBtn.setText(battleTerritory.getAttackFamilyName());
        conquerBtn = new JButton();
        conquerBtn.setText(battleTerritory.getConquerFamilyName());
    }

    public void setupListener() {
        attackBtn.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TerritoryInfo ti = node.getGameInfo().getTerrMap().get(node.getGameInfo().getLastSelect());
                if (ti.getType().equals(TerritoryType.LAND)) {
                    battleTerritory.setAttackLandSupport(battleTerritory.getAttackLandSupport()
                            + ti.getConquerCapabilities());
                    if (ti.getAction().isStar()) {
                        battleTerritory.setAttackLandSupport(battleTerritory.getAttackLandSupport() + 1);
                    }
                }
                else{
                    battleTerritory.setAttackSeaSupport(battleTerritory.getAttackSeaSupport()
                            + ti.getConquerCapabilities());
                    if (ti.getAction().isStar()) {
                        battleTerritory.setAttackSeaSupport(battleTerritory.getAttackSeaSupport() + 1);
                    }
                }
               
                try {
                    node.sendGameStatusUpdate(MessageType.Supply);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                SupportDialog.this.dispose();
            }
        });

        conquerBtn.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TerritoryInfo ti = node.getGameInfo().getTerrMap().get(node.getGameInfo().getLastSelect());
                if (ti.getType().equals(TerritoryType.LAND)) {
                    battleTerritory.setConquerLandSupport(battleTerritory.getConquerLandSupport()
                            + ti.getConquerCapabilities());
                    if (ti.getAction().isStar()) {
                        battleTerritory.setConquerLandSupport(battleTerritory.getConquerLandSupport() + 1);
                    }
                }
                else{
                    battleTerritory.setConquerSeaSupport(battleTerritory.getConquerSeaSupport()
                            + ti.getConquerCapabilities());
                    if (ti.getAction().isStar()) {
                        battleTerritory.setConquerSeaSupport(battleTerritory.getConquerSeaSupport() + 1);
                    }
                }
               
                try {
                    node.sendGameStatusUpdate(MessageType.Supply);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                SupportDialog.this.dispose();
            }
        });
    }
}
