package got.ui.update;

import got.client.Client;
import got.logic.GameState;
import got.pojo.Arm;
import got.pojo.event.TerritoryInfo;

import java.awt.Graphics;
import java.awt.Point;

public class DrawManager {

    private Graphics graphics;
    private Client client;
    private Point mousePoint;

    public DrawManager() {

    }

    public void draw(Graphics graphics, Client client) {
        this.graphics = graphics;
        this.client = client;
        if (client.getClientData().getState() != null && client.getClientData().getState().equals(GameState.Ready_Start)) {
          
            for (TerritoryInfo ti : client.getClientData().getTerrMap().values()) {
                System.out.println("draw arym");
                drawArmy(ti);
            }
        }
    }

    private void drawArmy(TerritoryInfo ti) {
        int placed = 0;
        for (Arm arm : ti.getConquerArms().keySet()) {
            int num = ti.getConquerArms().get(arm);
            for (int i = 0; i < num; i++) {
                Point p = ti.getArmPs().get(placed++);
                graphics.drawImage(client.getImageLoader().getArmyImages().get(ti.getConquerFamilyName()).get(arm),
                        p.x, p.y, null);
            }
        }
        placed = 0;
        for (Arm arm : ti.getAttackArms().keySet()) {
            int num = ti.getAttackArms().get(arm);
            for (int i = 0; i < num; i++) {
                Point p = ti.getArmPs().get(placed++);
                graphics.drawImage(client.getImageLoader().getArmyImages().get(ti.getAttackFamilyName()).get(arm),
                        p.x + 5, p.y + 5, null);
            }
        }

        placed = ti.getConquerArmNum();
        for (Arm arm : ti.getRetreatArms().keySet()) {
            int num = ti.getRetreatArms().get(arm);
            for (int i = 0; i < num; i++) {
                Point p = ti.getArmPs().get(placed++);
                graphics.drawImage(client.getImageLoader().getRetreatArmyImages().get(ti.getConquerFamilyName()).get(
                        arm), p.x + 5, p.y + 5, null);
            }
        }
    }

    public void setMousePoint(Point mousePoint) {
        this.mousePoint = mousePoint;
    }

    public Point getMousePoint() {
        return mousePoint;
    }
}
