package org.gradle.module1;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class graphFuncBuild extends JFrame {
    //константи
    private static final double aValue = 3.0;
    private static final double bValue = 5.0;
    private static final double minTValue = 0.0;
    private static final double maxTValue = 2 * Math.PI;

    private double a;
    private double b;
    private double minT;
    private double maxT;

    public graphFuncBuild() {
        setTitle("Графік функції");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        graph graphPanel = new graph();
        add(graphPanel, BorderLayout.CENTER);

        setting settingPanel = new setting();
        add(settingPanel, BorderLayout.SOUTH);

        setSize(800, 600);
        setVisible(true);

        a = aValue;
        b = bValue;
        minT = minTValue;
        maxT = maxTValue;
    }

    private class graph extends JPanel {
        //побудова частин графіка
        private static final int axes = 40;
        private static final int radius = 3;

        @Override
        protected void paintComponent(Graphics g) {
            int width = getWidth();
            int height = getHeight();

            g.setColor(getBackground());
            g.fillRect(0, 0, width, height);

            //осі x та у
            g.setColor(Color.BLACK);
            g.drawLine(axes, height / 2, width - axes, height / 2); // X-axis
            g.drawLine(width / 2, axes, width / 2, height - axes); // Y-axis

            //стрілки осей
            drawArrowHead(g, width - axes, height / 2, 0); // X-axis arrowhead
            drawArrowHead(g, width / 2, axes, 90); // Y-axis arrowhead

            //позначення осей
            g.drawString("x", width - axes + 10, height / 2 - 10);
            g.drawString("y", width / 2 + 10, axes - 10);

            //відрізки на осі координат х
            int numSegmentsX = 10;
            int segmentLengthX = (width - 2 * axes) / numSegmentsX;
            for (int i = 1; i <= numSegmentsX; i++) {
                int markerX = axes + i * segmentLengthX;
                g.drawLine(markerX, height / 2 - 5, markerX, height / 2 + 5);
            }

            //відрізки на осі координат у
            int numSegmentsY = 10;
            int segmentLengthY = (height - 2 * axes) / numSegmentsY;
            for (int i = 1; i <= numSegmentsY; i++) {
                int markerY = height - axes - i * segmentLengthY;
                g.drawLine(width / 2 - 5, markerY, width / 2 + 5, markerY);
            }

            //графік функції
            g.setColor(Color.BLUE);
            double step = 0.01;
            double t = minT;
            int xValue1 = 0, yValue1 = 0;
            while (t <= maxT) {
                double x = Math.sin(a * t) + Math.cos(a * t);
                double y = Math.sin(b * t) + Math.cos(b * t);

                int xValue2 = (int) (width / 2 + x * (width / 4));
                int yValue2 = (int) (height / 2 - y * (height / 4));

                g.fillOval(xValue2 - radius, yValue2 - radius, 2 * radius, 2 * radius); //точки графіка функцій
                if (xValue1 != 0 && yValue1 != 0) {
                    g.drawLine(xValue1, yValue1, xValue2, yValue2);
                }
                xValue1 = xValue2;
                yValue1 = yValue2;
                t += step;
            }
        }

        private void drawArrowHead(Graphics g, int x, int y, int angle) {
            //координати "стрілок" осей
            int arrowSize = 10;
            double arrowAngle = Math.toRadians(30);

            int x1 = (int) (x - arrowSize * Math.cos(arrowAngle + Math.toRadians(angle)));
            int y1 = (int) (y + arrowSize * Math.sin(arrowAngle + Math.toRadians(angle)));
            int x2 = (int) (x - arrowSize * Math.cos(arrowAngle - Math.toRadians(angle)));
            int y2 = (int) (y - arrowSize * Math.sin(arrowAngle - Math.toRadians(angle)));

            g.drawLine(x, y, x1, y1);
            g.drawLine(x, y, x2, y2);
        }

        public void saveGraphImage(String filename) {
            //збереження графіка у вигляді зображення
            BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            paintComponent(g);
            
            String caption = "Графік функції: x = sin(At) + cos(At), y = sin(Bt) + cos(Bt)";
            String range = String.format("Діапазон: [%.2f; %.2f]", minT, maxT);
            String coefficients = String.format("Коефіцієнти: A = %.2f, B = %.2f", a, b);
            String fullCaption = caption + "  |  " + range + "  |  " + coefficients;

            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.setColor(Color.BLACK);
            g.drawString(fullCaption, 10, getHeight() - 10);

            g.dispose();

            try {
                File file = new File(filename);
                ImageIO.write(image, "png", file);
                System.out.println("Графік збережено: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class setting extends JPanel {
        //поле з заповненням даних
        private JLabel labelA;
        private JLabel labelB;
        private JLabel labelMinT;
        private JLabel labelMaxT;

        private JTextField textFieldA;
        private JTextField textFieldB;
        private JTextField textFieldMinT;
        private JTextField textFieldMaxT;

        private JButton buttonUpdate;
        private JButton buttonSave;

        public setting() {
            labelA = new JLabel("A:");
            labelB = new JLabel("B:");
            labelMinT = new JLabel("мінімальне t:");
            labelMaxT = new JLabel("максимальне t:");

            textFieldA = new JTextField(String.valueOf(aValue), 5);
            textFieldB = new JTextField(String.valueOf(bValue), 5);
            textFieldMinT = new JTextField(String.valueOf(minTValue), 5);
            textFieldMaxT = new JTextField(String.valueOf(maxTValue), 5);

            buttonUpdate = new JButton("Побудувати графік");
            buttonSave = new JButton("Зберегти як зображення");
            buttonUpdate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        a = Double.parseDouble(textFieldA.getText());
                        b = Double.parseDouble(textFieldB.getText());
                        minT = Double.parseDouble(textFieldMinT.getText());
                        maxT = Double.parseDouble(textFieldMaxT.getText());

                        //перероблення графіка при зміні координат
                        graph graphPanel = (graph) getContentPane().getComponent(0);
                        graphPanel.repaint();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(setting.this, "Помилка!");
                    }
                }
            });

            buttonSave.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveGraph();
                }
            });

            setLayout(new FlowLayout());
            add(labelA);
            add(textFieldA);
            add(labelB);
            add(textFieldB);
            add(labelMinT);
            add(textFieldMinT);
            add(labelMaxT);
            add(textFieldMaxT);
            add(buttonUpdate);
            add(buttonSave);
        }

        private void saveGraph() {
            //вибрати папку для збереження файлу
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Зберегти як зображення");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PNG зображення", "png"));

            int result = fileChooser.showSaveDialog(graphFuncBuild.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filename.toLowerCase().endsWith(".png")) {
                    filename += ".png";
                }

                ((graph) getContentPane().getComponent(0)).saveGraphImage(filename);
            }
        }
    }
    }
