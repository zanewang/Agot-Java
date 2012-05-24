package got.test;

import got.ui.InfoStatusDialog;
import got.ui.Invoker;
import got.utility.Utility;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TestMainUI extends JFrame {

    public int state;
    
    public TestMainUI() {
        this.setSize(960, 720);
        this.addWindowStateListener(new WindowStateListener(){

            @Override
            public void windowStateChanged(WindowEvent e) {
                // TODO Auto-generated method stub
                state = e.getNewState();
                System.out.println(e.getOldState());
                System.out.println(e.getNewState());
            }
            
        });
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        final TestMainUI frame = new TestMainUI();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int X = (screen.width / 2) - (960 / 2); // Center horizontally.
        int Y = (screen.height / 2) - (720 / 2); // Center vertically.
        frame.setLocation(X, Y);
        System.out.println(frame.getLocation().getX());
        System.out.println(frame.getLocation().getY());
      
        frame.addWindowListener(new WindowListener() {

            @Override
            public void windowActivated(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowClosed(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowClosing(WindowEvent e) {
                // TODO Auto-generated method stub
                System.exit(0);
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowIconified(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowOpened(WindowEvent e) {
                // TODO Auto-generated method stub

            }

        });

        // JScrollPane jscrollPane = new JScrollPane(new MainPanel());
        // jscrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        frame.add(frame.new MainPanel(frame));
//        frame.pack();
        frame.setVisible(true);

    }
    class MainPanel extends JPanel {
        private Image backgroundImage = Utility.loadImage("got/resource/agot_cut.png");
        private Image screen;
        private Graphics graphics;
        private int width = 960;
        private int height = 1768;
        
        private TestMainUI fram;

        public MainPanel(TestMainUI fram) {
            this.screen = Utility.createImage(width, height, true);
            this.setPreferredSize(new Dimension(width, height));
            this.graphics = (Graphics2D) screen.getGraphics();
            graphics.drawImage(backgroundImage, 0, 0, null);
            this.fram = fram;
            this.repaint();
            this.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    // TODO Auto-generated method stub
                    Timer timer = new Timer(3000, new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // TODO Auto-generated method stub
                            Random random = new Random(System.nanoTime());
//                            System.out.println("2 " + MainPanel.this.fram.getX());
//                            System.out.println("2 " + MainPanel.this.fram.getY());
                            if(MainPanel.this.fram.state == 0){
                            new InfoStatusDialog(TestMainUI.this, String.valueOf(random.nextLong()));
                            }
                        }

                    });
                    timer.start();
                    timer.setRepeats(true);

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // TODO Auto-generated method stub

                }

            });
        }

        public void update(Graphics g) {
//            g.drawImage(backgroundImage, 0, 0, null);
            g.dispose();
        }

        public void paint(Graphics g) {
            update(g);
        }
    }
}

