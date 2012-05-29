package got.ui;

import got.core.Node;
import got.io.MessageType;
import got.logic.GameState;
import got.pojo.Action;
import got.pojo.ActionType;
import got.pojo.TerritoryType;
import got.pojo.event.FamilyInfo;
import got.pojo.event.GameInfoHelper;
import got.pojo.event.TerritoryInfo;

import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;

import org.apache.log4j.Logger;

public class OrderDialog extends JDialog {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Map<Action, OrderButton> btnMap;

    private TerritoryInfo ti;

    private final Node node;

    public OrderDialog(final Node node, Point p, TerritoryInfo ti) {
        super(node.gameFrame);
        this.node = node;
        this.ti = ti;
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        createComponent();
        layoutCoponents();
        setupListeners();
        this.setSize(175, 140);
        this.setLocation(p);
        this.setUndecorated(true);
        this.setVisible(true);
    }

    protected void createComponent() {
        // TODO Auto-generated method stub
        btnMap = new HashMap<Action, OrderButton>();
        for (Action order : Action.values()) {
            btnMap.put(order, new OrderButton(order, node));
        }
    }

    protected void layoutCoponents() {
        // TODO Auto-generated method stub
        this.setLayout(new GridLayout(4, 5));
        FamilyInfo myFamilyInfo = node.getMyFamilyInfo();
        for (ActionType type : ActionType.values()) {
            for (Action action : btnMap.keySet()) {
                if (action.getType().equals(type) && action.isLow()) {
                    final OrderButton btn = btnMap.get(action);
                    this.add(btn);
                    btn.setEnabled(node.getMyFamilyInfo().getActionMap().get(btn.getOrder()));
                    if (btn.getOrder().isStar()
                            && GameInfoHelper.getRavenStar(myFamilyInfo.getRavenRank())
                                    - node.getGameInfo().getUsedStarOrders(myFamilyInfo.getName()) == 0) {
                        btn.setEnabled(false);
                    }
                }

            }
            for (Action action : btnMap.keySet()) {
                if (action.getType().equals(type) && action.isCommon()) {
                    final OrderButton btn = btnMap.get(action);
                    this.add(btn);
                    btn.setEnabled(node.getMyFamilyInfo().getActionMap().get(btn.getOrder()));
                    if (btn.getOrder().isStar()
                            && GameInfoHelper.getRavenStar(myFamilyInfo.getRavenRank())
                                    - node.getGameInfo().getUsedStarOrders(myFamilyInfo.getName()) == 0) {
                        btn.setEnabled(false);
                    }
                }

            }
            for (Action action : btnMap.keySet()) {
                if (action.getType().equals(type) && action.isStar()) {
                    final OrderButton btn = btnMap.get(action);
                    this.add(btn);
                    btn.setEnabled(node.getMyFamilyInfo().getActionMap().get(btn.getOrder()));
                    if (btn.getOrder().isStar()
                            && GameInfoHelper.getRavenStar(myFamilyInfo.getRavenRank())
                                    - node.getGameInfo().getUsedStarOrders(myFamilyInfo.getName()) == 0) {
                        btn.setEnabled(false);
                    }
                }

            }
            if (ti.getType().equals(TerritoryType.SEA)) {
                for (Action action : btnMap.keySet()) {
                    if (action.getType().equals(ActionType.CONSOLIDATE)) {
                        OrderButton btn = btnMap.get(action);
                        btn.setEnabled(false);
                    }
                }
            }
        }

    }

    protected void setupListeners() {
        // TODO Auto-generated method stub
        for (final OrderButton btn : btnMap.values()) {
            btn.addMouseListener(new MouseListenerAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // TODO Auto-generated method stub
                    if (!btn.isEnabled()) {
                        return;
                    }
                    Action order = btn.getOrder();
                    node.getMyFamilyInfo().getActionMap().put(ti.getAction(), true);
                    ti.setAction(order);
                    if (!order.equals(Action.NONE)) {
                        node.getMyFamilyInfo().getActionMap().put(order, false);
                        logger.debug("Place the command " + order.toString() + " at " + ti.getName());
                    } else {
                        node.getMyFamilyInfo().getActionMap().put(order, true);
                        logger.debug("Remove the command from " + ti.getName());
                    }
                    if (node.getGameInfo().getState().equals(GameState.Start_Raven)) {
                        try {
                            node.sendGameStatusUpdate(MessageType.Raven);
                            node.getGameInfo().shiftTO(GameState.Do_Nothing);
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                    OrderDialog.this.dispose();
                }
            });
        }
    }
}
