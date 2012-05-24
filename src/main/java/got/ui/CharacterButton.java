package got.ui;

import javax.swing.JButton;

public class CharacterButton extends JButton {

    private String characterName;

    public CharacterButton(String characterName) {
        this.setCharacterName(characterName);
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }
}
