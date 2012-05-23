package got.ui;

import got.core.Node;
import got.io.MessageType;
import got.pojo.event.FamilyInfo;
import got.utility.Utility;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class JudgeDialog extends JDialog {

    private Node node;
    private List<JudgeButton> btns = new ArrayList<JudgeButton>();
    private List<FamilyInfo> families;
    private List<JButton> familyBtns = new ArrayList<JButton>();
    private JButton yesBtn;

    public JudgeDialog(Node node) {
        super(node.gameFrame);
        this.node = node;
        createComponent();
        setupListener();
        this.setSize(315, 35);
        this.setLocationRelativeTo(node.gameFrame);
        this.setUndecorated(true);
        this.setVisible(true);
    }

    public void createComponent() {
        this.setLayout(new GridLayout(1, 9));
        families = node.getGameInfo().orderByCompetePower();
        FamilyInfo last = null;
        int i = 1;
        for (final FamilyInfo fi : families) {
            if (last != null && last.getCompetePower() == fi.getCompetePower()) {
                JudgeButton judgeButton = new JudgeButton(last.getName(), fi.getName(), i - 1, i);
                btns.add(judgeButton);
                this.add(judgeButton);
            }
            JButton familyBtn = new JButton();
            Image image = Utility.loadImage("got/resource/house/" + fi.getName() + ".png");
            ImageIcon icon = new ImageIcon(image);
            // this.setText(order.toString());
            familyBtn.setIcon(icon);
            familyBtns.add(familyBtn);
            this.add(familyBtn);
            last = fi;
            i++;
        }
        yesBtn = new JButton();
        this.add(yesBtn);
    }

    public void setupListener() {
        for (final JudgeButton btn : btns) {
            btn.addMouseListener(new MouseListenerAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    FamilyInfo prefi = node.getGameInfo().getFamiliesMap().get(btn.getPreFamily());
                    FamilyInfo nextfi = node.getGameInfo().getFamiliesMap().get(btn.getNextFamily());
                    prefi.setJudgeRank(btn.getNextRank());
                    nextfi.setJudgeRank(btn.getPreRank());
                    Image preimage = Utility.loadImage("got/resource/house/" + prefi.getName() + ".png");
                    ImageIcon preicon = new ImageIcon(preimage);
                    familyBtns.get(btn.getNextRank() - 1).setIcon(preicon);
                    Image nextimage = Utility.loadImage("got/resource/house/" + nextfi.getName() + ".png");
                    ImageIcon nexticon = new ImageIcon(nextimage);
                    familyBtns.get(btn.getPreRank() - 1).setIcon(nexticon);
                    JudgeDialog.this.repaint();
                    btn.change();
                }
            });
        }

        yesBtn.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    switch (node.getGameInfo().getState()) {
                    case Judge_Iron:
                        node.sendGameStatusUpdate(MessageType.Judge_Iron);
                        break;
                    case Judge_Blade:
                        node.sendGameStatusUpdate(MessageType.Judge_Blade);
                        break;
                    case Judge_Raven:
                        node.sendGameStatusUpdate(MessageType.Judge_Raven);
                        break;
                    case Start_Judge_Disarm:
                        node.sendGameStatusUpdate(MessageType.Judge_Disarm);
                        break;
                    case Start_Judge_General:
                        node.sendGameStatusUpdate(MessageType.Judge_General);
                        break;
                    }
                    JudgeDialog.this.dispose();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }
        });
    }
}
