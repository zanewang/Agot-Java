package got.ui.update;

import java.awt.Graphics;
import java.awt.Image;

public class RoundPanel extends ImagePanel implements Observer {

    private GameData gameData;

    public RoundPanel(Image image, GameData gameData) {
        super(image);
        this.gameData = gameData;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        this.repaint();
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
    }
}
