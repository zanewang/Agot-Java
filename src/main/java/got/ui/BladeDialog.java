package got.ui;

import got.core.Node;
import got.io.MessageType;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class BladeDialog extends JDialog {

    public BladeDialog(Node node) {
        super(node.gameFrame);
        this.setLocationRelativeTo(node.gameFrame);
        this.setUndecorated(true);
        this.setVisible(true);
        String confirmStr = node.getGameInfo().powerVS(node.getGameInfo().getLastSelect()) + "\n\nWould you like to use the blade?";
        int options = JOptionPane.showConfirmDialog(this, confirmStr, "Operation Prompt", JOptionPane.YES_NO_OPTION);
        if (options == JOptionPane.YES_OPTION) {
            node.getMyFamilyInfo().setBladeRemain(0);
            try {
              node.sendGameStatusUpdate(MessageType.Blade);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.dispose();
        }
        if (options == JOptionPane.NO_OPTION) {
            try {
                node.sendGameStatusUpdate(MessageType.Blade);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.dispose();
        }
    }
}
