package got.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.Timer;

public class Invoker implements ActionListener{
    private static final int ANIMATION_INTERVAL=10;
    private static final int ANIMATION_FRAMES=10;
    /**
     * 调用此静态方法显示窗口，可以出现动画效果
     * 被显示的窗口是任何继承自Window的窗口，包括显示模态的对话框
     */
    public static void show(JDialog w){
        if(w.isVisible())
            return;
        new Invoker(w).invoke();
    }
    //要被显示的窗口
    private JDialog jdialog;
    //窗口全部展开时大小
    private Dimension full_size;
    //动画定时器
    private Timer timer;
    //当前动画帧
    private int frameIndex;
    //私有构造函数，不允许直接访问
    private Invoker(JDialog w){
        //初始化
        jdialog=w;
        full_size=jdialog.getSize();
        timer=new Timer(150, this);
        frameIndex=0;
        jdialog.setSize(0, 0);
    }
    //激活动画过程
    private void invoke(){
        if(!jdialog.isVisible()){
            timer.start();
            jdialog.setVisible(true);
        }
    }
    //动画的一帧处理方法
    public void actionPerformed(ActionEvent e) {
        //计算和设置当前帧尺寸
        int w=full_size.width*frameIndex/ANIMATION_FRAMES;
        int h=full_size.height*frameIndex/ANIMATION_FRAMES;
        jdialog.setSize(w, h);
        if(frameIndex==ANIMATION_FRAMES){
            //最后一帧，停止时钟，释放资源
            timer.stop();
            timer=null;
            jdialog=null;
            full_size=null;
        }else
            //前进一帧
            frameIndex++;
    }
}