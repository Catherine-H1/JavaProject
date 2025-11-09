package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class Main extends JPanel {
    private LayerManager manager;
    private Point lastMouse;
    private Layer.HandlePosition activeHandle = Layer.HandlePosition.NONE;

    // Simple undo stack (stores deleted layers)
    private final Stack<Layer> undoStack = new Stack<>();

    public Main() {
        manager = new LayerManager();

        // Initial layers for testing
//        manager.addLayer(new Layer(new Color(0, 255, 255, 180), 0.8f, BlendMode.MULTIPLY, new Rectangle(150, 150, 250, 250))); // Cyan
//        manager.addLayer(new Layer(new Color(255, 0, 0, 180), 0.8f, BlendMode.MULTIPLY, new Rectangle(100, 100, 250, 250))); // Red

        // Make sure we can capture keyboard input
        setFocusable(true);
        requestFocusInWindow();

        // Mouse logic
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow(); // regain focus after clicking toolbar
                lastMouse = e.getPoint();

                // Step 1: Check for resize handle on selected layer
                activeHandle = manager.getHandleAt(e.getPoint());

                // Step 2: If not resizing, check if clicking on a layer body
                if (activeHandle == Layer.HandlePosition.NONE) {
                    manager.selectLayerByClick(e.getPoint());
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                activeHandle = Layer.HandlePosition.NONE;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (manager.getSelectedLayer() != null) {
                    int dx = e.getX() - lastMouse.x;
                    int dy = e.getY() - lastMouse.y;

                    if (activeHandle != Layer.HandlePosition.NONE) {
                        // resize
                        manager.getSelectedLayer().resize(activeHandle, dx, dy);
                    } else {
                        // move
                        manager.moveSelectedLayer(dx, dy);
                    }

                    lastMouse = e.getPoint();
                    repaint();
                }
            }
        });

        // Keyboard shortcuts
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                // Backspace → delete layer
                if (key == KeyEvent.VK_BACK_SPACE) {
                    Layer selected = manager.getSelectedLayer();
                    if (selected != null) {
                        undoStack.push(selected); // store for undo
                        manager.deleteSelectedLayer();
                        repaint();
                    }
                }

                // Ctrl+Z → undo actions
                if (e.isControlDown() && key == KeyEvent.VK_Z) {
                    if (!undoStack.isEmpty()) {
                        Layer restored = undoStack.pop();
                        manager.addLayer(restored);
                        manager.selectLayer(manager.getLayers().size() - 1); // select it again
                        repaint();
                    }
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if ((e.isControlDown() || e.isMetaDown()) && e.getKeyCode() == java.awt.event.KeyEvent.VK_Z) {
                    manager.undo();
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

        // draw resize handles for selected layer. White corner thingys
        Layer selected = manager.getSelectedLayer();
        if (selected != null) {
            selected.drawHandles((Graphics2D) g);
        }
    }

    public LayerManager getManager() {
        return manager;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Spectral Layers");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);

            // Create canvas and toolbar
            Main canvas = new Main();
            canvas.setPreferredSize(new Dimension(780, 600)); // <-- key line
            ToolbarPanel toolbar = new ToolbarPanel(canvas.getManager(), canvas::repaint);

            // Wrap them in a parent panel using BorderLayout
            JPanel rootPanel = new JPanel(new BorderLayout());
            rootPanel.add(canvas, BorderLayout.CENTER);
            rootPanel.add(toolbar, BorderLayout.EAST);

            frame.setContentPane(rootPanel);
            frame.pack(); // respects preferred sizes
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // Ensure focus returns to canvas so Backspace works
            SwingUtilities.invokeLater(canvas::requestFocusInWindow);
        });
    }
}
