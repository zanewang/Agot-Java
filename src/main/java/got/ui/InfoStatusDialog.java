package got.ui;

import got.core.Node;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.Timer;

public class InfoStatusDialog extends JDialog {

    private JPanel panel;
    private JTextPane infoPane;

    private String info;

    private int dialogWidth = 860;
    private int dialogHeight = 35;

    public InfoStatusDialog(final Node node, String info) {
        super(node.gameFrame);
        this.info = info;

        Timer timer = new Timer(10000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                node.gameFrame.removeAndUpdate(InfoStatusDialog.this);
                InfoStatusDialog.this.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();

        createComponent();
        layoutCoponents();
        this.repaint();
        this.setSize(dialogWidth, dialogHeight);
//        this.setLocation((int)frame.getLocation().getX()+50, (int)frame.getLocation().getY()+50);
        this.setUndecorated(true);
        this.setFocusableWindowState(false);
//        this.setVisible(true);
      
        
    }

    private void layoutCoponents() {
        // TODO Auto-generated method stub
        this.setLayout(null);

        infoPane.setText(info);
        infoPane.setFont(new Font("Arial Black", Font.ITALIC, 13));

        infoPane.setLocation(130, 3);
        infoPane.setSize(700, 20);

        infoPane.setOpaque(false);
        infoPane.setForeground(Color.white);

        panel.setLocation(0, 0);
        panel.setSize(960, 50);
        panel.add(infoPane);
        panel.setLayout(null);
        this.add(panel);
    }

    private void createComponent() {
        // TODO Auto-generated method stub

        infoPane = new JTextPane();

        panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            }
        };

    }

}
