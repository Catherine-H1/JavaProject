package core;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends JPanel {
    private List<Layer> layers;

    public Main() {
        layers = new ArrayList<>();
//        layers.add(new Layer(new Color(255, 0, 0, 150), 0.6f, BlendMode.SUBTRACT, new Rectangle(100, 80, 50, 50)));
//        layers.add(new Layer(new Color(0, 255, 255, 150), 0.6f, BlendMode.SUBTRACT, new Rectangle(150, 150, 50, 400)));
        layers.add(new Layer(new Color(255, 255, 0, 128), 0.6f, BlendMode.ADD, new Rectangle(100, 100, 120, 400)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Renderer renderer = new Renderer((Graphics2D) g);
        renderer.draw(layers);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Spectral Layers - Renderer Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 500);
            frame.add(new Main());
            frame.setVisible(true);
        });
    }
}
