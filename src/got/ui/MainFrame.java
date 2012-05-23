package got.ui;

import got.core.Node;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainFrame extends JFrame {

    private int state = 0;
    
    public MainFrame(final Node node) {
        int widthWindow = 960;
        int heightWindow = 720;
        this.setPreferredSize(new Dimension(widthWindow, heightWindow));
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int X = (screen.width / 2) - (widthWindow / 2); // Center horizontally.
        int Y = (screen.height / 2) - (heightWindow / 2); // Center vertically.
        this.setLocation(X, Y);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Status");
        menuBar.add(menu);
        
        JMenuItem roundMenuItem = new JMenuItem("Round");
        menu.add(roundMenuItem);
        roundMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                new RoundDialog(node);
            }

        });
        
        JMenuItem supplyMenuItem = new JMenuItem("Supply");
        menu.add(supplyMenuItem);
        supplyMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                new SupplyDialog(node);
            }

        });
        
        JMenuItem influenceMenuItem = new JMenuItem("Influence");
        menu.add(influenceMenuItem);
        influenceMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                new InfluenceDialog(node);
            }

        });
        
        this.addWindowStateListener(new WindowStateListener(){

            @Override
            public void windowStateChanged(WindowEvent arg0) {
                // TODO Auto-generated method stub
                state = arg0.getNewState();
            }
            
        });
        
        this.setJMenuBar(menuBar);
        this.pack();
        this.setVisible(true);
    }
    
    
    public int getState(){
        return this.state;
    }
}
