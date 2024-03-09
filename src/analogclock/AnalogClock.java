package analogclock;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class AnalogClock extends JFrame implements Runnable {

    private Thread thr;
    private Image background, buffer;
    private int hour, min, sec;
    private boolean running = true;

    public AnalogClock() {
        initComponents();
        thr = new Thread(this);
        thr.start();
    }

    public void paint(Graphics g) {
        if (background == null) {
            background = createImage(getWidth(), getHeight());
            //Paint clock circle
            Graphics gBackground = background.getGraphics();
            //gBackground.setClip(0, 0, getWidth(), getHeight());
            gBackground.setColor(Color.black);
            gBackground.fillOval((getWidth() - 400) / 2, (getHeight() - 400) / 2, 400, 400);
            gBackground.setColor(Color.red);
            gBackground.drawOval((getWidth() - 400) / 2, (getHeight() - 400) / 2, 400, 400);
            
        }
        update(g);
    }

    public void update(Graphics g) {
        g.setClip(0, 0, getWidth(), getHeight());
        Calendar cal = Calendar.getInstance();

        if (cal.get(Calendar.MINUTE) != min) {
            hour = cal.get(Calendar.HOUR);
            min = cal.get(Calendar.MINUTE);

            buffer = createImage(getWidth(), getHeight());
            Graphics gBuffer = buffer.getGraphics();
            gBuffer.setClip(0, 0, getWidth(), getHeight());
            gBuffer.drawImage(background, 0, 0, this);
            gBuffer.setColor(Color.white);
            gBuffer.fillArc((getWidth() - 330) / 2 + 5, (getHeight() - 330) / 2 + 5, 320, 320, angle12(hour), 3);
            gBuffer.fillArc((getWidth() - 370) / 2 + 5, (getHeight() - 370) / 2 + 5, 360, 360, angle60(min), 3);
        }
        g.drawImage(buffer, 0, 0, this);
        sec = cal.get(Calendar.SECOND);
        g.setColor(Color.red);
        g.fillArc((getWidth() - 370) / 2 + 5, (getHeight() - 370) / 2 + 5, 360, 360, angle60(sec), 3);
    }

    public int angle12(int hour) {
        int hourAngle = (hour % 12) * 30; // Cada hora representa 30 grados
        return hourAngle - 90; // Ajustar para que 0 sea en la posición de las 12 en punto
    }

    public int angle60(int min) {
        int minAngle = min * 6; // Cada minuto representa 6 grados
        return minAngle - 90; // Ajustar para que 0 sea en la posición de las 12 en punto
    }

    private void initComponents() {
        setTitle("Analog Clock");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(500, 500);
        setLocationRelativeTo(null);
        show();
    }

    public static void main(String[] args) {
        new AnalogClock();
    }

    @Override
    public void run() {
        while (running) {
            try {
                thr.sleep(1000);
            } catch (Exception e) {
                System.out.println(e);
            }
            repaint();
        }
    }

}
