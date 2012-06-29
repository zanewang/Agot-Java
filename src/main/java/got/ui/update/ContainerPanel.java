package got.ui.update;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class ContainerPanel extends JPanel {

    public void update(Object arg) {
        // TODO Auto-generated method stub
        this.setLayout(new BorderLayout());
        this.removeAll();
        this.add((JPanel) arg, BorderLayout.CENTER);
        this.validate();
    }

}
