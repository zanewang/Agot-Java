package got.test;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class TestMain {

    /**
     * @param args
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        // TODO Auto-generated method stub

        JButton button = new JButton ("button");
        JLabel label = new JLabel ("label");
       
        final JSplitPane splitPane = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT,button,label);
        splitPane.setPreferredSize (new Dimension (500,600));//设置大小
        splitPane.setDividerSize (5);//设置分隔条的粗细
        splitPane.setDividerLocation(100);//设置分隔条的位置，基于setPreferredSize方法中的值，
                 //此处为200/500，大概在中间靠左
        JFrame frame = new JFrame ("JSplitPanelDemo");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.setVisible (true);
        frame.setContentPane (new JApplet(){
            public void init(){
                this.setSize(500, 600);
                this.add(splitPane);
            }
        });
//        frame.pack ();//此方法必须在加载splitPane之后调用
    }

 }
