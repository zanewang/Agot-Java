package got.ui;

import got.core.Node;
import got.pojo.Action;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class OrderButton extends JButton {

    private Action order;

    public OrderButton(Action order, Node node) {
        this.order = order;
        Image image = node.getImageLoader().getActionImages().get(order);
        ImageIcon icon = new ImageIcon(image);
        // this.setText(order.toString());
        this.setIcon(icon);
    }

    public Action getOrder() {
        return order;
    }
}
