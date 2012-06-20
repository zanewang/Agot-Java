package got.ui;

import got.core.Node;
import got.logic.LogInitiator;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class MainApplet extends JApplet {

    @Override
    public void init() {
        this.setSize(960,1768);
//        new LogInitiator().init();
        // TODO Auto-generated method stub
        Node node = new Node();
        try {
            node.init("127.0.0.1", null);
            node.sendJoin("hello");
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        node.gameFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
//        JScrollPane jscrollPane = new JScrollPane(new MainPanel(node));
//        jscrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        node.gameApplet = this;
//        node.gameApplet.getContentPane().add(jscrollPane);
//        node.gameFrame = findParentFrame();
    }

    private JFrame findParentFrame() {
        Container c = this;
        while (c != null) {
            if (c instanceof JFrame)
                return (JFrame) c;
            c = c.getParent();
        }
        return null;
    }
}
