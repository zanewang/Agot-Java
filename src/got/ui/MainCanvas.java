package got.ui;

import got.utility.Utility;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;


public class MainCanvas extends JPanel implements  MouseListener, MouseMotionListener{
    private Image screen;
    private Graphics2D graphics;
    private int width = 1002;
    private int height = 2021;
    private List<BImage> images = new ArrayList<BImage>();
    
    public MainCanvas(){
        this.screen = Utility.createImage(width, height, true);
        this.setPreferredSize(new Dimension(width, height));
        this.graphics = (Graphics2D) screen.getGraphics();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        
        Image test = Utility.loadImage("got/resource/agot.jpg");
        BImage bi = new BImage(new IntRec(0,0,1002,2021));
        bi.setImage(test);
        bi.setShow(true);
        images.add(bi);
        this.repaint();
    }

    private void drawImage(BImage image) {
        if (image.getShow()) {
            graphics.drawImage(image.getImage(), image.getRec().getPosX(), image.getRec().getPosY(), null);
        }
    }
    
    public void draw() {
       for(BImage image : images){
           drawImage(image);
       }
    }

    public void update(Graphics g) {
        draw();
        g.drawImage(screen, 0, 0, null);
        g.dispose();
    }

    public void paint(Graphics g) {
        update(g);
    }
    @Override
    public void mouseDragged(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent event) {
        // TODO Auto-generated method stub
//        BImage bi = images.get(0);
//        bi.getRec().setPosX(event.getX());
//        bi.getRec().setPosY(event.getY());
//        System.out.println(bi.getRec().getPosX());
//        System.out.println(bi.getRec().getPosY());
//        this.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }
   

}
