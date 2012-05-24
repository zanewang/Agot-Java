package got.ui;

import got.core.Node;
import got.io.MessageType;
import got.logic.GameState;
import got.pojo.event.FamilyInfo;
import got.utility.Utility;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class CompeteDialog extends JDialog {

    private Node node;

    private JButton outBtn;
    private JButton backBtn;
    private JLabel outLabel;
    private JLabel currentLabel;
    private JButton yesBtn;

    public CompeteDialog(Node node) {
        super(node.gameFrame);
        this.node = node;
        createComponent();
        layoutCoponents();
        setupListeners();
        this.setSize(200, 40);
        this.setLocationRelativeTo(node.gameFrame);
        this.setUndecorated(true);
        this.setVisible(true);
    }

    private void setupListeners() {
        // TODO Auto-generated method stub
        backBtn.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FamilyInfo myFamilyInfo = node.getMyFamilyInfo();
                if (myFamilyInfo.getCurPower() > 0) {
                    myFamilyInfo.setCurPower(myFamilyInfo.getCurPower() - 1);
                    myFamilyInfo.setCompetePower(myFamilyInfo.getCompetePower() + 1);
                    outLabel.setText(String.valueOf(myFamilyInfo.getCompetePower()));
                    currentLabel.setText(String.valueOf(node.getMyFamilyInfo().getCurPower()));
                }
            }
        });

        outBtn.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FamilyInfo myFamilyInfo = node.getMyFamilyInfo();
                if (myFamilyInfo.getCompetePower() > 0) {
                    myFamilyInfo.setCurPower(myFamilyInfo.getCurPower() + 1);
                    myFamilyInfo.setCompetePower(myFamilyInfo.getCompetePower() - 1);
                    outLabel.setText(String.valueOf(myFamilyInfo.getCompetePower()));
                    currentLabel.setText(String.valueOf(node.getMyFamilyInfo().getCurPower()));
                }
            }
        });
        yesBtn.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    switch (node.getGameInfo().getState()) {
                    case Start_Compete_Iron:
                        node.sendGameStatusUpdate(MessageType.Compete_Iron);
                        break;
                    case Start_Compete_Blade:
                        node.sendGameStatusUpdate(MessageType.Compete_Blade);
                        break;
                    case Start_Compete_Raven:
                        node.sendGameStatusUpdate(MessageType.Compete_Raven);
                        break;
                    case Start_Wild_Attack:
                        node.sendGameStatusUpdate(MessageType.Wild_Attack);
                        break;
                    }
                    node.getGameInfo().shiftTO(GameState.Do_Nothing);
                    CompeteDialog.this.dispose();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
    }

    private void layoutCoponents() {
        // TODO Auto-generated method stub
        this.setLayout(new GridLayout(1, 5));
        this.add(outBtn);
        this.add(currentLabel);
        this.add(outLabel);
        this.add(backBtn);
        this.add(yesBtn);
    }

    private void createComponent() {
        // TODO Auto-generated method stub
        Image imageOut = Utility.loadImage("got/resource/power_in.png");
        Image imageIn = Utility.loadImage("got/resource/power_out.png");
        imageOut = Utility.sacleImage(imageOut, 0.5, 0.5);
        imageIn = Utility.sacleImage(imageIn, 0.5, 0.5);
        
        this.outBtn = new JButton();
        this.outBtn.setIcon(new ImageIcon(imageOut));
        this.outLabel = new JLabel();
        outLabel.setText("0");
        this.currentLabel = new JLabel();
        currentLabel.setText(String.valueOf(node.getMyFamilyInfo().getCurPower()));
        this.backBtn = new JButton();
        this.backBtn.setIcon(new ImageIcon(imageIn));
        yesBtn = new JButton();
        Image imageAccept = Utility.loadImage("got/resource/accept.png");
        yesBtn.setIcon(new ImageIcon(imageAccept));

    }
}
