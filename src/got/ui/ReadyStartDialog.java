package got.ui;

import got.core.Node;
import got.io.MessageType;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ReadyStartDialog extends JDialog {

    public ReadyStartDialog(Node node) {
        super(node.gameFrame);
        this.setLocationRelativeTo(node.gameFrame);
        this.setUndecorated(true);
        this.setVisible(true);
        String confirmStr = String.format(
                "New player joined the game, total %d players, would you like to start the game?", node.getGameInfo()
                        .getAddrMap().size());
        int options = JOptionPane.showConfirmDialog(this, confirmStr, "Operation Prompt", JOptionPane.YES_NO_OPTION);
        if (options == JOptionPane.YES_OPTION) {
            try {
                node.send(MessageType.Start);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.dispose();
        }
        if (options == JOptionPane.NO_OPTION) {
            this.dispose();
        }
    }
}
