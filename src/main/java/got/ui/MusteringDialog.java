package got.ui;

import got.core.Node;
import got.logic.GameState;
import got.pojo.Arm;
import got.pojo.MusterType;
import got.pojo.TerritoryType;
import got.pojo.event.TerritoryInfo;
import got.utility.Utility;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import org.apache.log4j.Logger;

public class MusteringDialog extends JDialog {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private TerritoryInfo ti;
    private Node node;

    private List<MusterButton> btns = new ArrayList<MusterButton>();
    private JButton noBtn;

    public MusteringDialog(Node node, Point p, TerritoryInfo ti) {
        super(node.gameFrame);
        this.setSize(300, 60);
        this.setUndecorated(true);
        this.setLocation(p);
        this.setVisible(true);
        this.ti = ti;
        this.node = node;
        createComponent();
        layoutCoponents();
        setupListeners();
    }

    protected void createComponent() {
        // TODO Auto-generated method stub
        MusterType[] types = new MusterType[] { MusterType.Knight, MusterType.FootMan, MusterType.Ship,
                MusterType.Upgrade };
        for (MusterType type : types) {
            MusterButton musterButton = new MusterButton(type);
            ImageIcon icon = new ImageIcon(node.getImageLoader().getMusterImages().get(type));
            musterButton.setIcon(icon);
            btns.add(musterButton);
        }
        noBtn = new JButton();
        ImageIcon icon = new ImageIcon(Utility.loadImage("got/resource/muster/Cancel.png"));
        noBtn.setIcon(icon);

    }

    protected void layoutCoponents() {
        // TODO Auto-generated method stub
        this.setLayout(new GridLayout(1, MusterType.values().length + 1));
        for (MusterButton musterButton : btns) {
            this.add(musterButton);
            if (musterButton.getType().getRequireCapacity() > ti.getAvailMustering()) {
                musterButton.setEnabled(false);
            }
            if (musterButton.getType().equals(MusterType.Knight) || musterButton.getType().equals(MusterType.FootMan)) {
                if (!node.getGameInfo().isEnoughSupplyWithMuster(node.getMyFamilyInfo().getName(), ti.getName())) {
                    musterButton.setEnabled(false);
                }
            }
            if (musterButton.getType().equals(MusterType.Ship)) {
                Boolean flag = false;
                for (String sea : node.getGameInfo().getNearbyTerritory(ti.getName(), TerritoryType.SEA,
                        node.getMyFamilyInfo().getName())) {
                    if (node.getGameInfo().isEnoughSupplyWithMuster(node.getMyFamilyInfo().getName(), sea)) {
                        flag = true;
                        break;
                    }
                }
                for (String sea : node.getGameInfo().getNearbyTerritory(ti.getName(), TerritoryType.SEA,
                        TerritoryInfo.NEUTRAL_FAMILY)) {
                    flag = true;
                    break;
                }
                for(String port : node.getGameInfo().getNearbyTerritory(ti.getName(), TerritoryType.PORT)){
                    flag = true;
                    break;
                }
                if (!flag) {
                    musterButton.setEnabled(false);
                }
            }
        }
        this.add(noBtn);
    }

    protected void setupListeners() {
        // TODO Auto-generated method stub
        for (final MusterButton btn : btns) {
            btn.addMouseListener(new MouseListenerAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // TODO Auto-generated method stub
                    if (!btn.isEnabled()) {
                        return;
                    }
                    MusterType type = btn.getType();
                    node.getMyFamilyInfo().setCurPower(node.getMyFamilyInfo().getCurPower() - type.getRequireCapacity());
                    switch (type) {
                    case Knight:
                        logger.debug("Client: Muster a Knight");
                        ti.getConquerArms().put(Arm.KNIGHT, ti.getConquerArms().get(Arm.KNIGHT) + 1);
                        ti.setAvailMustering(ti.getAvailMustering() - type.getRequireCapacity());
                        break;
                    case FootMan:
                        logger.debug("Client: Muster a FootMan");
                        ti.getConquerArms().put(Arm.FOOTMAN, ti.getConquerArms().get(Arm.FOOTMAN) + 1);
                        ti.setAvailMustering(ti.getAvailMustering() - type.getRequireCapacity());
                        break;
                    case Ship:
                        logger.debug("Client: Muster a Ship");
                        node.getGameInfo().shiftTO(GameState.Choose_Muster_Ship_Territory);
                        break;
                    case Upgrade:
                        logger.debug("Client: Upgrade a FootMan to Knight");
                        ti.getConquerArms().put(Arm.FOOTMAN, ti.getConquerArms().get(Arm.FOOTMAN) - 1);
                        ti.getConquerArms().put(Arm.KNIGHT, ti.getConquerArms().get(Arm.KNIGHT) + 1);
                        ti.setAvailMustering(ti.getAvailMustering() - type.getRequireCapacity());
                        break;
                    }
                    MusteringDialog.this.dispose();
                }
            });
        }
        noBtn.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MusteringDialog.this.dispose();
            }
        });
    }
}
