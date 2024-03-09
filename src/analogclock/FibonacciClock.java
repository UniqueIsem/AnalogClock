package analogclock;

import java.awt.*;
import java.util.Calendar;
import javax.swing.*;

public class FibonacciClock extends JFrame implements Runnable {

    private Thread thr;
    private Image offScreenImage;
    private Image bgImage, spiralImageSec, spiralImageMin, spiralImageHour;
    private Graphics offScreenGraphics;

    public FibonacciClock() {
        super("Fibonacci Clock");
        setSize(1500, 838);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        String imagePath = "C:\\Users\\isaac\\OneDrive\\Documentos\\NetBeansProjects\\AnalogClock\\src\\images\\background.jpg";
        bgImage = new ImageIcon(imagePath).getImage();
        String secImgPath = "C:\\Users\\isaac\\OneDrive\\Documentos\\NetBeansProjects\\AnalogClock\\src\\images\\fibonacciSpiralRed.png";
        spiralImageSec = new ImageIcon(secImgPath).getImage();
        String minImgPath = "C:\\Users\\isaac\\OneDrive\\Documentos\\NetBeansProjects\\AnalogClock\\src\\images\\fibonacciSpiralWhite.png";
        spiralImageMin = new ImageIcon(minImgPath).getImage();
        String hourImgPath = "C:\\Users\\isaac\\OneDrive\\Documentos\\NetBeansProjects\\AnalogClock\\src\\images\\fibonacciSpiralWhite.png";
        spiralImageHour = new ImageIcon(hourImgPath).getImage();

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
        int radius = Math.min(getWidth(), getHeight()) / 3;

        // Clock styling
        int squareSize = radius * 2 + 38;
        int x = centerX - radius - 19;
        int y = centerY - radius - 19;
        // Gte hour from system
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

        // Naming hours
        g.setColor(Color.white);

        Graphics2D g2d = (Graphics2D) g.create();
        ((Graphics2D) g2d).setStroke(new BasicStroke(2));
        
        //g.drawArc(centerX - 200, centerY - 100, radius, radius, 40, 80);

        g2d.dispose();

        double angle;

        // Seconds
        ((Graphics2D) g).setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(Color.red);
        angle = Math.toRadians((15 - second - (millisecond / 1000.0)) * 6);
        x = (int) (Math.cos(angle) * (radius - 20));
        y = (int) (Math.sin(angle) * (radius - 20));
        int startXS = centerX - x / 5; // Posición opuesta en el eje X
        int startYS = centerY + y / 5; // Posición opuesta en el eje Y
        // Rotar la imagen de la manecilla de la hora
        g2d = (Graphics2D) g.create();
        g2d.rotate(-angle, centerX, centerY);
        if (spiralImageSec != null) {
            g2d.drawImage(spiralImageSec, centerX -305 , centerY - 185, null);
        }
        g2d.dispose();
        

        // Minutes
        float initialStrokeWidth = 3;
        ((Graphics2D) g).setStroke(new BasicStroke(initialStrokeWidth));
        angle = Math.toRadians((15 - minute - (second / 60.0)) * 6);
        int minuteHandLength = 90;
        x = (int) (Math.cos(angle) * minuteHandLength);
        y = (int) (Math.sin(angle) * minuteHandLength);
        int startXMin = centerX + (int) (Math.cos(angle) * circleRadius);
        int startYMin = centerY - (int) (Math.sin(angle) * circleRadius);
        // Rotar la imagen de la manecilla de la hora
        g2d = (Graphics2D) g.create();
        g2d.rotate(-angle, centerX, centerY);
        if (spiralImageMin != null) {
            g2d.drawImage(spiralImageMin, centerX -300 , centerY - 190, null);
        }
        g2d.dispose();

        // Hours
        initialStrokeWidth = 4;
        ((Graphics2D) g).setStroke(new BasicStroke(initialStrokeWidth));
        g.setColor(Color.white);
        angle = Math.toRadians((15 - (hour % 12) - (minute / 60.0) - (second / 3600.0)) * 30);
        int handLength = 60;
        x = (int) (Math.cos(angle) * handLength);
        y = (int) (Math.sin(angle) * handLength);
        int startX = centerX + (int) (Math.cos(angle) * circleRadius);
        int startY = centerY - (int) (Math.sin(angle) * circleRadius);
        g.drawLine(startX, startY, centerX + x * 3, centerY - y * 3); // Delgado hasta otherX, otherY
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
