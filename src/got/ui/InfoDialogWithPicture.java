package got.ui;

import got.core.Node;
import got.core.ShiftStateEvent;
import got.logic.GameState;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.Timer;

public class InfoDialogWithPicture extends JDialog {

    final Image dialogImage;
    final Image familyImage;

    private JPanel panel;
    private JTextPane titlePane;
    private JTextPane infoPane;

    private Node node;
    private String title;
    private String info;
    private final GameState state;
    private boolean onQueue;

    private int dialogWidth = 400;
    private int dialogHeight = 200;
    private boolean isShift = false;

    public InfoDialogWithPicture(Node node, String title, String info, GameState state, boolean onQueue) {
        super(node.gameFrame);
        this.info = info;
        this.title = title;
        this.node = node;
        this.state = state;
        this.onQueue = onQueue;
        familyImage = node.getImageLoader().getInfoFamilyImages().get(node.getMyFamilyInfo().getName());
        dialogImage = node.getImageLoader().getDialogImage();

        Timer timer = new Timer(6000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shift();
                InfoDialogWithPicture.this.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();

        createComponent();
        layoutCoponents();
        setupListeners();
        this.setLocation(caculateLocation());
        this.setSize(dialogWidth, dialogHeight);
        this.setUndecorated(true);
        this.setModalityType(Dialog.ModalityType.MODELESS);
        this.setVisible(true);
    }

    public void shift() {
        if (!isShift) {
            isShift = true;
            node.getGameInfo().shiftTO(state);
            if (onQueue) {
                node.onQueueEvent(new ShiftStateEvent());
            }
        }
    }

    private void setupListeners() {
        this.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                shift();
                InfoDialogWithPicture.this.dispose();
            }
        });
        titlePane.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                shift();
                InfoDialogWithPicture.this.dispose();
            }
        });
        infoPane.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                shift();
                InfoDialogWithPicture.this.dispose();
            }
        });
    }

    private void layoutCoponents() {
        // TODO Auto-generated method stub
        this.setLayout(null);

        titlePane.setText(title);
        titlePane.setFont(new Font("Arial", Font.BOLD, 20));

        titlePane.setLocation(150, 20);
        titlePane.setSize(250, 50);

        titlePane.setOpaque(false);
        titlePane.setForeground(Color.white);

        infoPane.setText(info);
        infoPane.setFont(new Font("Arial Black", Font.PLAIN, 13));

        infoPane.setLocation(150, 80);
        infoPane.setSize(250, 150);

        infoPane.setOpaque(false);
        infoPane.setForeground(Color.white);

        panel.setLocation(0, 0);
        panel.setSize(400, 200);
        panel.add(titlePane);
        panel.add(infoPane);
        panel.setLayout(null);
        this.add(panel);
    }

    private void createComponent() {
        // TODO Auto-generated method stub

        titlePane = new JTextPane();
        infoPane = new JTextPane();

        panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(dialogImage, 0, 0, this);
                g.drawImage(familyImage, 30, 30, this);
            }
        };
        panel.repaint();

    }

    private Point caculateLocation() {
        Point ownerLocation = getOwner().getLocation();
        Dimension ownerSize = getOwner().getSize();
        // Get x and y by geometry relationship
        double x = 0.5 * ownerSize.getWidth() + ownerLocation.getX() - 0.5 * dialogWidth;
        double y = 0.5 * ownerSize.getHeight() + ownerLocation.getY() - 0.5 * dialogHeight;
        int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().height;
        // Make the dialog display in the area of screen
        if (x < 1) {
            x = 1;
        }
        if (y < 1) {
            y = 1;
        }
        if (x > screenWidth - dialogWidth) {
            x = screenWidth - dialogWidth;
        }
        if (y > screenHeight - dialogHeight) {
            y = screenHeight - dialogHeight;
        }
        return new Point((int) x, (int) y);
    }
}
