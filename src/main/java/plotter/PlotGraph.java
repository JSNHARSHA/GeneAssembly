package plotter;

import java.awt.*;
import javax.swing.*;

public class PlotGraph {

    public static void plot(int[][] points)
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(600, 400);

        int offset = 20;
        int startPoint = 100;

        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.drawString("Original", 30, 200);
                g.drawString("Computed", 150,75);
                g.setColor(Color.BLUE);
                for(int j=1;j<points.length;j++)
                    g.drawLine(startPoint + points[j-1][0]*offset, startPoint + points[j-1][1]*offset, startPoint + points[j][0]*offset, startPoint + points[j][1]*offset);
                g.drawLine(startPoint,startPoint,startPoint,startPoint+(points[points.length-1][0]*offset));
                g.drawLine(startPoint,startPoint,startPoint + (points[points.length-1][1]*offset),100);
            }
        };
        frame.add(panel);
        frame.validate(); // because you added panel after setVisible was called
        frame.repaint();
    }
}

