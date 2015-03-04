package de.tucottbus.kt.csl.led.monitorSim;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LedAnimation extends JPanel implements ActionListener {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  Timer timer;
  double x1 = 298, y1 = 50, dx = 2;
  double x2 = 320, y2 = 50;
  int interval = 20;
  BufferedImage myPicture2=readImage();
  
  /*
  float[] children = new float[32];
  
  children[ 0] = new float(-85.0,220.0,+205.0,0);
  children[ 1] = new Mm1(-38.3,220.0,+205.0,1);
  children[ 2] = new Mm1(-16.7,220.0,+205.0,2);
  children[ 3] = new Mm1(-6.6,220.0,+205.0,3);
  children[ 4] = new Mm1(-2.0,220.0,+205.0,4);
  children[ 5] = new Mm1(+2.0,220.0,+205.0,5);
  children[ 6] = new Mm1(+6.6,220.0,+205.0,6);
  children[ 7] = new Mm1(+16.7,220.0,+205.0,7);
  children[ 8] = new Mm1(+38.3,220.0,+205.0,8);
  children[ 9] = new Mm1(+85.0,220.0,+205.0,9);
  children[10] = new Mm1(-85.0,220.0,+103.0,10);
  children[11] = new Mm1(-38.3,220.0,+103.0,11);
  children[12] = new Mm1(-16.7,220.0,+103.0,12);
  children[13] = new Mm1(-6.6,220.0,+103.0,13);
  children[14] = new Mm1(-2.0,220.0,+103.0,14);
  children[15] = new Mm1(+2.0,220.0,+103.0,15);
  children[16] = new Mm1(+6.6,220.0,+103.0,16);
  children[17] = new Mm1(+16.7,220.0,+103.0,17);
  children[18] = new Mm1(+38.3,220.0,+103.0,18);
  children[19] = new Mm1(+85.0,220.0,+103.0,19);
  children[20] = new Mm1(-85.0,220.0,+133.6,20);
  children[21] = new Mm1(-85.0,220.0,+146.5,21);
  children[22] = new Mm1(-85.0,220.0,+152.0,22);
  children[23] = new Mm1(-85.0,220.0,+156.0,23);
  children[24] = new Mm1(-85.0,220.0,+161.5,24);
  children[25] = new Mm1(-85.0,220.0,+174.4,25);
  children[26] = new Mm1(+85.0,220.0,+133.6,26);
  children[27] = new Mm1(+85.0,220.0,+146.5,27);
  children[28] = new Mm1(+85.0,220.0,+152.0,28);
  children[29] = new Mm1(+85.0,220.0,+156.0,29);
  children[30] = new Mm1(+85.0,220.0,+161.5,30);
  children[31] = new Mm1(+85.0,220.0,+174.4,31);
*/
  public LedAnimation() {
    setBackground(Color.white);
    timer = new Timer(interval, this);
    timer.setInitialDelay(0);
  }
  
  private static void initFrame() {
    JFrame frame = new JFrame("SimpleAnimation");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    LedAnimation animation = new LedAnimation();
    animation.setPreferredSize(new Dimension(620, 420));
    animation.setBackground(Color.WHITE);
    frame.getContentPane().add(animation, BorderLayout.CENTER);

    AnimationControl control = new AnimationControl(animation);

    JButton start = new JButton("Start");
    JButton stop = new JButton("Stop");
    JButton reset = new JButton("Reset");
    start.addActionListener(control);
    stop.addActionListener(control);
    reset.addActionListener(control);

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(start);
    buttonPanel.add(stop);
    buttonPanel.add(reset);
    frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

    frame.pack();
    frame.setVisible(true);
  }
  
  public static BufferedImage readImage(){
    BufferedImage myPicture = null;
    try {
      myPicture = ImageIO.read(new File("Bildschirm.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return resize(myPicture, 620, 420);
  }

  public void actionPerformed(ActionEvent event) {
    x1 += dx;
    repaint();
  }

  public void start() {
    if (!timer.isRunning())
      timer.start();
  }

  public void stop() {
    if (timer.isRunning())
      timer.stop();
  }

  public void reset() {
    stop();
    x1 = 0;
    repaint();
  }

  public void paintComponent(Graphics gfx) {
    super.paintComponent(gfx);
    Graphics2D g = (Graphics2D) gfx;
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    
    g.drawImage(myPicture2, null, 0, 0);
    
    double r = 5;
    Ellipse2D.Double point = new Ellipse2D.Double(x1 - r, y1 - r, 2 * r, 2 * r);
    g.setPaint(Color.blue);
    g.fill(point);
    g.draw(point);
  }

  public static BufferedImage resize(BufferedImage img, int newW, int newH) {
    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
    BufferedImage dimg = new BufferedImage(newW, newH,
        BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = dimg.createGraphics();
    g2d.drawImage(tmp, 0, 0, null);
    g2d.dispose();

    return dimg;
  }

  public static void main(String[] args) {
    initFrame();
  }

}

// --- 

class AnimationControl implements ActionListener {
  LedAnimation animation;

  AnimationControl(LedAnimation sa) {
    animation = sa;
  }

  public void actionPerformed(ActionEvent event) {
    String actionCommand = event.getActionCommand();
    if (actionCommand.equals("Start"))
      animation.start();
    else if (actionCommand.equals("Stop"))
      animation.stop();
    else if (actionCommand.equals("Reset"))
      animation.reset();
  }
}
