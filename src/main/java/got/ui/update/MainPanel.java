package got.ui.update;

import got.client.Client;
import got.utility.Utility;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class MainPanel extends JPanel implements Observer {
    private static final int defaultWidth = 800;
    private static final int defaultHeight = 600;

    private LargeMapPanel largeMapPanel;
    private JPanel infoPanel;
    private Client client;

    public MainPanel(Client client) {
        this.setSize(defaultWidth, defaultHeight);
        this.setLayout(null);
        this.client = client;
        createComponent();
    }

    private void createComponent() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        ImageScrollModel model = new ImageScrollModel();
        model.setBoxDimensions(500, 600);
        Image image = Utility.loadImage("got/resource/agotsmall.png");
        ImageScrollerSmallView small = new ImageScrollerSmallView(image, model);
        LargeMapPanel large = new LargeMapPanel(new Dimension(1105, 600), model,client);
        large.setPreferredSize(new Dimension(600, 500));
        leftPanel.add(large);
        leftPanel.add(new InfoPanel());
        rightPanel.add(small);

        ImagePanel roundPanel = new ImagePanel(Utility.loadImage("got/resource/round-small.png"));
        ImagePanel influencePanel = new ImagePanel(Utility.loadImage("got/resource/influence-small.png"));
        rightPanel.add(roundPanel);
        rightPanel.add(influencePanel);

        JSplitPane midSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        midSplitPane.setSize(defaultWidth, defaultHeight);
        midSplitPane.setDividerLocation(0.75);
        midSplitPane.setDividerSize(5);
        this.add(midSplitPane);
        System.out.println(large.getBounds());
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        this.repaint();
    }
}
