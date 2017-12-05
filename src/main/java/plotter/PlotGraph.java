package plotter;

import java.awt.*;
import javax.swing.*;

public class PlotGraph {

    public static void plot(int[][] points, double similarity)
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(1000, 1000);

        int offset = 8;
        int startPoint = 100;
        int dotSize = 5;

        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.drawString("Original", 30, 200);
                g.drawString("Computed", 150,75);
                g.drawLine(startPoint,startPoint,startPoint,startPoint + offset + (points[points.length-1][1]*offset));
                g.drawLine(startPoint,startPoint,startPoint + offset + (points[points.length-1][0]*offset),100);
                g.setColor(Color.BLUE);
                g.fillOval(startPoint + points[0][0]*offset - (dotSize/2), startPoint + points[0][1]*offset - (dotSize/2), dotSize, dotSize);
                for(int j=1; j<points.length; j++)
                {
                    g.drawLine(startPoint + points[j-1][0]*offset, startPoint + points[j-1][1]*offset, startPoint + points[j][0]*offset, startPoint + points[j][1]*offset);
                    g.fillOval(startPoint + points[j][0]*offset - (dotSize/2), startPoint + points[j][1]*offset - (dotSize/2), dotSize, dotSize);
                }
                g.setColor(Color.DARK_GRAY);
                g.setFont(new Font("default", Font.BOLD, 15));
                g.drawString("Similarity: "+String.valueOf(similarity*100).substring(0, 5)+"%", 30, 40);
            }
        };
        frame.add(panel);
        frame.validate(); // because you added panel after setVisible was called
        frame.repaint();
    }
}

