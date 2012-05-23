package got.ui;

import got.core.Node;
import got.logic.LogInitiator;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JScrollPane;

public class Main {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        new LogInitiator().init();
        // TODO Auto-generated method stub
        Node node = new Node();
        if (args[0].equals("joiner1")) {
            node.init("127.0.0.2", "127.0.0.1");
            node.sendJoin(args[1]);
        }else if (args[0].equals("joiner2")) {
            node.init("127.0.0.3", "127.0.0.1");
            node.sendJoin(args[1]);
        }
        else {
            node.init("127.0.0.1", null);
            node.sendJoin(args[1]);
        }

        node.gameFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        JScrollPane jscrollPane = new JScrollPane(new MainPanel(node));
        jscrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        node.gameFrame.getContentPane().add(jscrollPane);
        node.gameFrame.pack();
        node.gameFrame.setVisible(true);
    }

}
