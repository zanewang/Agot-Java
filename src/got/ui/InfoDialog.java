package got.ui;

import got.core.Node;
import got.utility.Utility;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class InfoDialog extends JDialog {

    public InfoDialog(Node node, String info) {
        super(node.gameFrame);
        this.setLocationRelativeTo(node.gameFrame);
        Timer timer = new Timer(3000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InfoDialog.this.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
        Image image = Utility.loadImage("got/resource/house/" +
                 node.getMyFamilyInfo().getName() + ".png");
        image = Utility.sacleImage(image, 0.4, 0.4);
        ImageIcon icon = new ImageIcon(image);
        JOptionPane.showMessageDialog(this, info,null,JOptionPane.INFORMATION_MESSAGE,icon);

    }

}
