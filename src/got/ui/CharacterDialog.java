package got.ui;

import got.core.Node;
import got.io.MessageType;
import got.logic.GameState;
import got.pojo.CharacterInfo;
import got.pojo.event.FamilyInfo;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

import org.apache.log4j.Logger;

public class CharacterDialog extends JDialog {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Node node;
    private List<CharacterButton> btns = new ArrayList<CharacterButton>();
    private String familyName;

    public CharacterDialog(Node node, String familyName) {
        super(node.gameFrame);
        this.node = node;
        this.familyName = familyName;
        this.setSize(448, 200);
        createComponent();
        setupListeners();
        this.setUndecorated(true);
        this.setVisible(true);
        this.setLocationRelativeTo(node.gameFrame);
    }

    public void createComponent() {
        FamilyInfo familyInfo = node.getGameInfo().getFamiliesMap().get(familyName);
        this.setLayout(new GridLayout(familyInfo.getCharacterMap().size(), 1));
        for (String characterName : familyInfo.getCharacterMap().keySet()) {
            CharacterInfo ci = node.getGameInfo().getCharacterMap().get(characterName);
            CharacterButton btn = new CharacterButton(characterName);
            btn.setText(String.format("%s Power:%d Sword:%d Shield:%d %s",characterName,ci.getPower(),ci.getSword(),ci.getShield(),ci.getSpecial()));
            btns.add(btn);
            this.add(btn);
            if (node.getGameInfo().getState().equals(GameState.Start_General)) {
                btn.setEnabled(familyInfo.getCharacterMap().get(characterName));
            } else if (node.getGameInfo().getState().equals(GameState.Start_LuWin_Special)) {
                btn.setEnabled(!familyInfo.getCharacterMap().get(characterName));
            } else if (node.getGameInfo().getState().equals(GameState.Start_Melisandre_Special)) {
                btn.setEnabled(familyInfo.getCharacterMap().get(characterName));
            } else if(node.getGameInfo().getState().equals(GameState.Start_Ransom)){
                btn.setEnabled(!familyInfo.getCharacterMap().get(characterName));
            }
        }
    }

    public void setupListeners() {
        for (final CharacterButton btn : btns) {
            btn.addMouseListener(new MouseListenerAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (btn.isEnabled() == false) {
                        return;
                    }
                    if (node.getGameInfo().getState().equals(GameState.Start_General)) {
                        node.getMyFamilyInfo().setBattleCharacter(btn.getCharacterName());
                        node.getMyFamilyInfo().getCharacterMap().put(btn.getCharacterName(), false);
                        logger.debug("Client : Choose the battle character "
                                + node.getMyFamilyInfo().getBattleCharacter());
                        try {
                            node.sendGameStatusUpdate(MessageType.General);
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    } else if (node.getGameInfo().getState().equals(GameState.Start_LuWin_Special)) {
                        node.getMyFamilyInfo().getCharacterMap().put(btn.getCharacterName(), true);
                        try {
                            node.sendGameStatusUpdate(MessageType.LuWin_Special);
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    } else if (node.getGameInfo().getState().equals(GameState.Start_Melisandre_Special)) {
                        node.getGameInfo().getFamiliesMap().get(familyName).getCharacterMap().put(
                                btn.getCharacterName(), false);
                        node.getMyFamilyInfo().setCurPower(node.getMyFamilyInfo().getCurPower() - 1);
                        try {
                            node.sendGameStatusUpdate(MessageType.Melisandre_Special);
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    } else if (node.getGameInfo().getState().equals(GameState.Start_Ransom)) {
                        node.getGameInfo().getFamiliesMap().get(familyName).getCharacterMap().put(
                                btn.getCharacterName(), true);
                        try {
                            node.sendGameStatusUpdate(MessageType.Ransom);
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                    CharacterDialog.this.dispose();
                }
            });
        }
    }
}
