package analogclock;

import java.awt.*;
import java.util.Calendar;
import javax.swing.*;

public class FibonacciClock extends JFrame implements Runnable {

    private Thread thr;
    private Image offScreenImage;
    private Image bgImage, spiralImageRed, spiralImageWhite;
    private Graphics offScreenGraphics;
    private Font myFont = new Font("Monospaced", Font.BOLD, 20);

    public FibonacciClock() {
        super("Fibonacci Clock");
        setSize(1500, 838);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        String imagePath = "C:\\Users\\isaac\\OneDrive\\Documentos\\NetBeansProjects\\AnalogClock\\src\\images\\background.jpg";
        bgImage = new ImageIcon(imagePath).getImage();
        String secImgPath = "C:\\Users\\isaac\\OneDrive\\Documentos\\NetBeansProjects\\AnalogClock\\src\\images\\fibonacciSpiralRed.png";
        spiralImageRed = new ImageIcon(secImgPath).getImage();
        String minImgPath = "C:\\Users\\isaac\\OneDrive\\Documentos\\NetBeansProjects\\AnalogClock\\src\\images\\fibonacciSpiralWhite.png";
        spiralImageWhite = new ImageIcon(minImgPath).getImage();

        thr = new Thread(this);
        thr.start();

        setVisible(true);
    }

    private void delayAnimation() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = createImage(getWidth(), getHeight());
            offScreenGraphics = offScreenImage.getGraphics();
        }

        drawBackground(offScreenGraphics);
        drawClock(offScreenGraphics);

        g.drawImage(offScreenImage, 0, 0, null);
    }

    private void drawBackground(Graphics g) {
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void drawClock(Graphics g) {
        // Center and clock radius
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(getWidth(), getHeight()) / 10; // smaller > bigger radious

        // Clock styling
        int x = centerX - radius - 19;
        int y = centerY - radius - 19;
        // Get real time from system
        Calendar time = Calendar.getInstance();
        int hour = time.get(Calendar.HOUR_OF_DAY);
        int minute = time.get(Calendar.MINUTE);
        int second = time.get(Calendar.SECOND);
        int millisecond = time.get(Calendar.MILLISECOND);

        if (hour > 12) {
            hour -= 12;
        }
        // Dot in clock's center
        int circleRadius = 5;
        g.setColor(Color.white);
        g.fillOval(centerX - circleRadius, centerY - circleRadius, circleRadius * 2, circleRadius * 2);

        // Dot inside previous dot
        int smallCircleRadius = 3;
        g.setColor(Color.red);
        g.fillOval(centerX - smallCircleRadius, centerY - smallCircleRadius, smallCircleRadius * 2, smallCircleRadius * 2);

        Graphics2D g2d = (Graphics2D) g.create();
        ((Graphics2D) g2d).setStroke(new BasicStroke(2));

        // Lines for small clock seconds
        for (int i = 0; i < 60; i++) {
            g2d.setColor(Color.gray);
            double angle = Math.toRadians(-i * 6 + 90);
            int x1 = (int) (Math.cos(angle) * (radius - 5));
            int y1 = (int) (Math.sin(angle) * (radius - 5));
            int x2 = (int) (Math.cos(angle) * (radius - 10));
            int y2 = (int) (Math.sin(angle) * (radius - 10));
            g2d.drawLine(centerX + x1, centerY - y1, centerX + x2, centerY - y2);
        }

        // Lines for small clock hours
        for (int i = 0; i < 12; i++) {
            g2d.setColor(Color.black);
            ((Graphics2D) g2d).setStroke(new BasicStroke(3));
            double angle = Math.toRadians(-i * 30);
            int x1 = (int) (Math.cos(angle) * (radius - 5));
            int y1 = (int) (Math.sin(angle) * (radius - 5));
            int x2 = (int) (Math.cos(angle) * (radius - 15));
            int y2 = (int) (Math.sin(angle) * (radius - 15));
            g2d.drawLine(centerX + x1, centerY - y1, centerX + x2, centerY - y2);
        }

        g.setColor(Color.red);
        g.setFont(myFont);
        float initialStrokeWidth = 4;
        int radiusFibonacci = Math.min(getWidth(), getHeight()) / 4;
        
        //Top arcs and numbers for fibonacci clock
        ((Graphics2D) g).setStroke(new BasicStroke(initialStrokeWidth));
        int arcSpacing = 20; // Space between arcs
        g.drawArc(centerX - 150, centerY - 155, radiusFibonacci + arcSpacing * 2, radiusFibonacci + arcSpacing * 2, 30, 50);
        //g.drawString("10", centerX, centerY - 165);
        g.drawArc(centerX - 200, centerY - 187 - arcSpacing, radiusFibonacci + 2 * arcSpacing * 3, radiusFibonacci + 2 * arcSpacing * 2, 20, 50);
        //g.drawString("20", centerX + 50 , centerY - 195);
        g.drawArc(centerX - 420, centerY - 290 - arcSpacing, radiusFibonacci + 3 * arcSpacing * 6, radiusFibonacci + 3 * arcSpacing * 2, 15, 50);
        //g.drawString("30", centerX + 115, centerY - 185);

        //Bottom lines for fibonacci clock
        g.drawArc(centerX - 104, centerY - 100, radiusFibonacci + arcSpacing * 2, radiusFibonacci + arcSpacing * 2, 180, 50);
        g.drawArc(centerX - 135, centerY - 70 - arcSpacing, radiusFibonacci + 2 * arcSpacing * 3, radiusFibonacci + 2 * arcSpacing * 2, 185, 50);
        g.drawArc(centerX - 180, centerY - 20 - arcSpacing, radiusFibonacci + 3 * arcSpacing * 6, radiusFibonacci + 3 * arcSpacing * 2, 190, 50);
        //g.drawArc(centerX - 130, centerY - 65, radiusFibonacci + arcSpacing * 2, radiusFibonacci + arcSpacing * 2, -80, -80);

        double angle;
        // Hours
        ((Graphics2D) g).setStroke(new BasicStroke(initialStrokeWidth));
        g.setColor(Color.white);
        angle = Math.toRadians((15 - (hour % 12) - (minute / 60.0) - (second / 3600.0)) * 30);
        int startX = centerX + (int) (Math.cos(angle) * circleRadius);
        int startY = centerY - (int) (Math.sin(angle) * circleRadius);
        int otherX = centerX + (int) (Math.cos(angle) * (circleRadius + 20));
        int otherY = centerY - (int) (Math.sin(angle) * (circleRadius + 20));
        g.drawLine(startX, startY, otherX, otherY);

        // Minutes
        ((Graphics2D) g).setStroke(new BasicStroke(initialStrokeWidth));
        angle = Math.toRadians((15 - minute - (second / 60.0)) * 6);
        g2d = (Graphics2D) g.create();
        g2d.rotate(-angle, centerX, centerY);
        if (spiralImageWhite != null) {
            g2d.drawImage(spiralImageWhite, centerX - 300, centerY - 190, null);
        }
        g2d.dispose();

        // Seconds
        ((Graphics2D) g).setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(Color.red);
        angle = Math.toRadians((15 - second - (millisecond / 1000.0)) * 6);
        x = (int) (Math.cos(angle) * (radius - 20));
        y = (int) (Math.sin(angle) * (radius - 10)); // smaller < taller
        int startXS = centerX - x / 5; // Oposite position on X
        int startYS = centerY + y / 5; // Oposite position on Y
        g.drawLine(centerX, centerY, centerX + x, centerY - y);
        g.drawLine(centerX, centerY, startXS, startYS);
        // Rotar la imagen de la manecilla de la hora
        /*g2d = (Graphics2D) g.create();
        g2d.rotate(-angle, centerX, centerY);
        if (spiralImageRed != null) {
            g2d.drawImage(spiralImage, centerX -305 , centerY - 185, null);
        }
        g2d.dispose();*/
    }

    public static void main(String[] args) {
        new FibonacciClock();
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            delayAnimation();
        }
    }
}
