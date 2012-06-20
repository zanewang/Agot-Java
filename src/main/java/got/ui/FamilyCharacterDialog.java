package got.ui;

import got.core.Node;
import got.pojo.CharacterInfo;
import got.pojo.event.FamilyInfo;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class FamilyCharacterDialog extends JDialog {

    private Node node;
    private String familyName;

    public FamilyCharacterDialog(Node node, String familyName) {
        super(node.gameFrame);
        this.node = node;
        this.familyName = familyName;
        this.setSize(700, 300);
        this.setLocationRelativeTo(node.gameFrame);
        this.setUndecorated(true);
        this.setVisible(true);
        createComponent();
        layoutCoponents();
        setupListeners();
    
    }

    private void setupListeners() {
        // TODO Auto-generated method stub

    }

    private void layoutCoponents() {
        // TODO Auto-generated method stub

    }

    private void createComponent() {
        // TODO Auto-generated method stub
        JTabbedPane tabbedPane = new JTabbedPane();
        FamilyInfo familyInfo = node.getGameInfo().getFamiliesMap().get(familyName);
        for (String charName : familyInfo.getCharacterMap().keySet()) {
            tabbedPane.addTab(charName, familyCharacter(charName));
        }
        this.add(tabbedPane);
    }

    private JComponent familyCharacter(String charName) {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(1, 2));
        JLabel nameLabel = new JLabel(charName);
        panel.add(nameLabel);
        CharacterInfo characterInfo = node.getGameInfo().getCharacterMap().get(charName);
        String des = String
                .format(
                        "<html><B>Power</B> : %d <br><br> <B>Sword</B> : %d <br><br> <B>Sheild</B> : %d <br><br> <B>Speical</B> : %s <br><br></html>",
                        characterInfo.getPower(), characterInfo.getSword(), characterInfo.getShield(), characterInfo
                                .getSpecial());
        JLabel desLabel = new JLabel(des);
        panel.add(desLabel);
        return panel;
    }

}
