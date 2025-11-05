package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main extends JPanel {
    private LayerManager manager;
    private Point lastMouse; // keeps track of last mouse position

    public Main() {
        manager = new LayerManager();

        // Initial layers for testing
        manager.addLayer(new Layer(new Color(0, 255, 255, 180), 0.8f, BlendMode.MULTIPLY, new Rectangle(150, 150, 250, 250))); // Cyan
        manager.addLayer(new Layer(new Color(255, 0, 0, 180), 0.8f, BlendMode.MULTIPLY, new Rectangle(100, 100, 250, 250))); // Red

        // Add mouse listeners here (inside constructor, after adding layers)
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMouse = e.getPoint();
                manager.selectLayerByClick(e.getPoint());
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (manager.getSelectedLayer() != null) {
                    int dx = e.getX() - lastMouse.x;
                    int dy = e.getY() - lastMouse.y;
                    manager.moveSelectedLayer(dx, dy);
                    lastMouse = e.getPoint();
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Renderer renderer = new Renderer((Graphics2D) g);
        renderer.draw(manager.getLayers());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Spectral Layers - Renderer Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new Main());
            frame.setVisible(true);
        });
    }
}
