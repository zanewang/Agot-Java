package got.ui;

import got.core.Node;
import got.pojo.Arm;
import got.pojo.event.TerritoryInfo;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;

public class MarchDialog extends JDialogAdapter {

    private Node node;
    private TerritoryInfo ti;
    private JButton curKnightBtn;
    private JButton curFootManBtn;
    private JButton curShipBtn;

    private JLabel curKnightLabel;
    private JLabel curFootManLabel;
    private JLabel curShipLabel;

    private JButton marchKnightBtn;
    private JButton marchFootManBtn;
    private JButton marchShipBtn;

    private JLabel marchKnightLabel;
    private JLabel marchFootManLabel;
    private JLabel marchShipLabel;

    private JButton yesBtn;
    private JButton noBtn;

    public MarchDialog(Node node, Point p, TerritoryInfo ti) {
        super(node.gameFrame);
        // TODO Auto-generated constructor stub
        this.node = node;
        this.ti = ti;

        curKnightBtn.setText("Remain Knight");
        curFootManBtn.setText("Remain FootMan");
        curShipBtn.setText("Reamin Ship");

        curKnightLabel.setText(String.valueOf(ti.getConquerArms().get(Arm.KNIGHT)));
        curFootManLabel.setText(String.valueOf(ti.getConquerArms().get(Arm.FOOTMAN)));
        curShipLabel.setText(String.valueOf(ti.getConquerArms().get(Arm.SHIP)));

        marchKnightBtn.setText("March Knight");
        marchFootManBtn.setText("March FootMan");
        marchShipBtn.setText("March Ship");

        node.getGameInfo().getMarchArms().put(Arm.KNIGHT, 0);
        node.getGameInfo().getMarchArms().put(Arm.FOOTMAN, 0);
        node.getGameInfo().getMarchArms().put(Arm.SHIP, 0);
        marchKnightLabel.setText("0");
        marchFootManLabel.setText("0");
        marchShipLabel.setText("0");

        this.setSize(450, 270);
        this.setUndecorated(true);
        this.setLocation(p);
        this.setVisible(true);

    }

    @Override
    protected void createComponent() {
        // TODO Auto-generated method stub
        curKnightBtn = new JButton();
        curFootManBtn = new JButton();
        curShipBtn = new JButton();

        curKnightLabel = new JLabel();
        curFootManLabel = new JLabel();
        curShipLabel = new JLabel();

        marchKnightBtn = new JButton();
        marchFootManBtn = new JButton();
        marchShipBtn = new JButton();

        marchKnightLabel = new JLabel();
        marchFootManLabel = new JLabel();
        marchShipLabel = new JLabel();

        yesBtn = new JButton();
        noBtn = new JButton();
    }

    @Override
    protected void layoutCoponents() {
        // TODO Auto-generated method stub
        this.setLayout(new GridLayout(5, 3));

        this.add(curKnightBtn);
        this.add(curFootManBtn);
        this.add(curShipBtn);
        this.add(curKnightLabel);
        this.add(curFootManLabel);
        this.add(curShipLabel);

        this.add(marchKnightBtn);
        this.add(marchFootManBtn);
        this.add(marchShipBtn);
        this.add(marchKnightLabel);
        this.add(marchFootManLabel);
        this.add(marchShipLabel);

        this.add(yesBtn);
        this.add(noBtn);
    }

    @Override
    protected void setupListeners() {
        // TODO Auto-generated method stub
        curKnightBtn.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                int marchKnights = node.getGameInfo().getMarchArms().get(Arm.KNIGHT);
                if (marchKnights > 0) {
                    ti.getConquerArms().put(Arm.KNIGHT, ti.getConquerArms().get(Arm.KNIGHT) + 1);
                    marchKnightLabel.setText(String.valueOf(marchKnights - 1));
                }
            }
        });

        marchKnightBtn.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                int curKnights = ti.getConquerArms().get(Arm.KNIGHT);
                if (curKnights > 0) {
                    ti.getConquerArms().put(Arm.KNIGHT, ti.getConquerArms().get(Arm.KNIGHT) - 1);
                    curKnightLabel.setText(String.valueOf(ti.getConquerArms().get(Arm.KNIGHT)));
                    node.getGameInfo().getMarchArms().put(Arm.KNIGHT,
                            node.getGameInfo().getMarchArms().get(Arm.KNIGHT) + 1);
                    marchKnightLabel.setText(String.valueOf(node.getGameInfo().getMarchArms().get(Arm.KNIGHT)));
                }
            }
        });

        curShipBtn.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                int marchShips = node.getGameInfo().getMarchArms().get(Arm.SHIP);
                if (marchShips > 0) {
                    ti.getConquerArms().put(Arm.SHIP, ti.getConquerArms().get(Arm.SHIP) + 1);
                    marchShipLabel.setText(String.valueOf(marchShips - 1));
                }
            }
        });
        marchShipBtn.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                int curShips = ti.getConquerArms().get(Arm.SHIP);
                if (curShips > 0) {
                    ti.getConquerArms().put(Arm.SHIP, ti.getConquerArms().get(Arm.SHIP) - 1);
                    curShipLabel.setText(String.valueOf(ti.getConquerArms().get(Arm.SHIP)));
                    node.getGameInfo().getMarchArms()
                            .put(Arm.SHIP, node.getGameInfo().getMarchArms().get(Arm.SHIP) + 1);
                    marchShipLabel.setText(String.valueOf(node.getGameInfo().getMarchArms().get(Arm.SHIP)));
                }
            }
        });
        curFootManBtn.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                int marchFootmen = node.getGameInfo().getMarchArms().get(Arm.FOOTMAN);
                if (marchFootmen > 0) {
                    ti.getConquerArms().put(Arm.FOOTMAN, ti.getConquerArms().get(Arm.FOOTMAN) + 1);
                    marchFootManLabel.setText(String.valueOf(marchFootmen - 1));
                }
            }
        });
        marchFootManBtn.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                int curFootMen = ti.getConquerArms().get(Arm.FOOTMAN);
                if (curFootMen > 0) {
                    ti.getConquerArms().put(Arm.FOOTMAN, ti.getConquerArms().get(Arm.FOOTMAN) - 1);
                    curFootManLabel.setText(String.valueOf(ti.getConquerArms().get(Arm.FOOTMAN)));
                    node.getGameInfo().getMarchArms().put(Arm.FOOTMAN,
                            node.getGameInfo().getMarchArms().get(Arm.FOOTMAN) + 1);
                    marchFootManLabel.setText(String.valueOf(node.getGameInfo().getMarchArms().get(Arm.FOOTMAN)));
                }
            }
        });
        yesBtn.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                MarchDialog.this.dispose();
            }
        });
    }

}
