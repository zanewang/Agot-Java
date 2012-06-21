package got.ui.update;

import got.pojo.MusterType;
import got.ui.MusterButton;
import got.ui.image.ImageLoader;
import got.utility.Utility;

import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import org.apache.log4j.Logger;

public class MusteringDialog extends JDialog {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private List<MusterButton> btns = new ArrayList<MusterButton>();
    private JButton noBtn;

    public MusteringDialog(Point p) {
        this.setSize(300, 60);
        this.setUndecorated(true);
        this.setLocation(p);
        this.setVisible(true);
        createComponent();
        layoutCoponents();
    }

    protected void createComponent() {
        // TODO Auto-generated method stub
        MusterType[] types = new MusterType[] { MusterType.Knight, MusterType.FootMan, MusterType.Ship,
                MusterType.Upgrade };
        ImageLoader loader = new ImageLoader();
        loader.load();
        for (MusterType type : types) {
            MusterButton musterButton = new MusterButton(type);
            ImageIcon icon = new ImageIcon(loader.getMusterImages().get(type));
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

        }
        this.add(noBtn);
    }

}
