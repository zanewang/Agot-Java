package got.ui;

import got.core.Node;
import got.io.MessageType;
import got.logic.GameState;
import got.pojo.Action;
import got.pojo.event.TerritoryInfo;

import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class FinishDailog extends JDialog {

    private final Node node;

    public FinishDailog(Node node, Point p) {
        super(node.gameFrame);
        this.node = node;
        this.setSize(160, 40);
        this.setLocation(p);
        this.setUndecorated(true);
        this.setVisible(true);
        String confirmStr = String.format("Would you like to finish the current action?");
        int options = JOptionPane.showConfirmDialog(this, confirmStr, "Operation Prompt", JOptionPane.YES_NO_OPTION);
        if (options == JOptionPane.YES_OPTION) {
            try {
                switch (node.getGameInfo().getState()) {
                case Start_Order:
                    node.sendGameStatusUpdate(MessageType.Order);
                    break;
                case Start_Raven:
                    node.sendGameStatusUpdate(MessageType.Raven);
                    break;
                case Choose_Raid_Territory:
                    node.sendGameStatusUpdate(MessageType.Raid);
                    break;
                case Choose_March_Order:
                    node.sendGameStatusUpdate(MessageType.March);
                    break;
                case Choose_March_Territory:
                    TerritoryInfo ti = node.getGameInfo().getTerrMap().get( node.getGameInfo().getLastSelect());
                    ti.setAction(Action.NONE);
                    node.sendGameStatusUpdate(MessageType.March);
                    break;
                case Choose_Muster_Territory:
                    node.sendGameStatusUpdate(MessageType.Mustering);
                    break;
                case Choose_Muster_Ship_Territory:
                    node.sendGameStatusUpdate(MessageType.Mustering);
                    break;
                }
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            node.getGameInfo().shiftTO(GameState.Do_Nothing);
            this.dispose();
        }
        if (options == JOptionPane.NO_OPTION) {
            this.dispose();
        }
    }

}
