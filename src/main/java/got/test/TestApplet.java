package got.test;

import got.client.Client;
import got.ui.update.ContainerPanel;
import got.ui.update.MainPanel;

import javax.swing.JApplet;

public class TestApplet extends JApplet {
    private Client client = new Client();

    @Override
    public void init() {

        this.setSize(800, 600);
        
        client.init();
        ContainerPanel gsp = new ContainerPanel();
        gsp.update(new MainPanel(client));
        this.add(gsp);
    }
    
}
