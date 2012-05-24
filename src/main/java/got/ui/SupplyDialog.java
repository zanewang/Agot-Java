package got.ui;

import got.core.Node;
import got.pojo.event.FamilyInfo;
import got.utility.Utility;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SupplyDialog extends JDialog {

    private Image roundImage = Utility.loadImage("got/resource/supply.png");
    private Node node;

    private Map<String, Image> familyIcons = new HashMap<String, Image>();

    public SupplyDialog(Node node) {
        super(node.gameFrame);
        this.node = node;
        int width = 480;
        int height = 289;
        this.setSize(width, height);
        this.setLocationRelativeTo(node.gameFrame);
        this.add(createPanel());
        this.setUndecorated(true);
        this.setVisible(true);
        Timer timer = new Timer(6000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SupplyDialog.this.dispose();
            }
        });
        timer.setRepeats(true);
        timer.start();
        for (FamilyInfo fi : node.getGameInfo().getFamiliesMap().values()) {
            Image image = Utility.loadImage("got/resource/house/" + fi.getName() + ".png");
            image = Utility.sacleImage(image, 35.0 / 175, 35.0 / 175);
            familyIcons.put(fi.getName(), image);
        }
    }

    public JPanel createPanel() {
        JPanel panel = new JPanel() {
            public void update(Graphics g) {
                g.drawImage(roundImage, 0, 0, null);
                for (int i = 0; i <= 6; i++) {
                    int j = 0;
                    for (FamilyInfo fi : node.getGameInfo().getFamiliesMap().values()) {
                        System.out.println(fi.getName() + String.valueOf(fi.getSupply()));
                        if (fi.getSupply() == i || (i == 6 && fi.getSupply() > 6)) {
                            System.out.println(i);
                            Point p = node.getGameInfo().getSupplyPoints().get(String.valueOf(i)).get(j++);
                            System.out.println(String.valueOf(p.x) + " : " + String.valueOf(p.y));
                            g.drawImage(familyIcons.get(fi.getName()), p.x, p.y, null);
                        }
                    }
                }
                g.dispose();
            }

            public void paint(Graphics g) {
                update(g);
            }
        };
        panel.repaint();
        return panel;
    }
}
