package core;

import java.awt.*;

public class Layer {
    private Color color;
    private float opacity;
    private BlendMode blendMode;
    private Rectangle shape;

    // Handle size in pixels
    private static final int HANDLE_SIZE = 7;

    public enum HandlePosition {
        NONE, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }

    public Layer(Color color, float opacity, BlendMode blendMode, Rectangle shape) {
        this.color = color;
        this.opacity = opacity;
        this.blendMode = blendMode;
        this.shape = shape;
    }

    // Move entire layer
    public void move(int dx, int dy) {
        shape.translate(dx, dy);
    }

    // Resize based on which handle is being dragged
    public void resize(HandlePosition handle, int dx, int dy) {
        switch (handle) {
            case TOP_LEFT -> {
                shape.x += dx;
                shape.y += dy;
                shape.width -= dx;
                shape.height -= dy;
            }
            case TOP_RIGHT -> {
                shape.y += dy;
                shape.width += dx;
                shape.height -= dy;
            }
            case BOTTOM_LEFT -> {
                shape.x += dx;
                shape.width -= dx;
                shape.height += dy;
            }
            case BOTTOM_RIGHT -> {
                shape.width += dx;
                shape.height += dy;
            }
        }

        // Prevent inverted rectangles
        if (shape.width < 10) shape.width = 10;
        if (shape.height < 10) shape.height = 10;
    }

    // Determine if a point is inside one of the resize handles
    public HandlePosition getHandleAt(Point p) {
        Rectangle tl = new Rectangle(shape.x - HANDLE_SIZE / 2, shape.y - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE);
        Rectangle tr = new Rectangle(shape.x + shape.width - HANDLE_SIZE / 2, shape.y - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE);
        Rectangle bl = new Rectangle(shape.x - HANDLE_SIZE / 2, shape.y + shape.height - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE);
        Rectangle br = new Rectangle(shape.x + shape.width - HANDLE_SIZE / 2, shape.y + shape.height - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE);

        if (tl.contains(p)) return HandlePosition.TOP_LEFT;
        if (tr.contains(p)) return HandlePosition.TOP_RIGHT;
        if (bl.contains(p)) return HandlePosition.BOTTOM_LEFT;
        if (br.contains(p)) return HandlePosition.BOTTOM_RIGHT;
        return HandlePosition.NONE;
    }

    // Getters
    public Color getColor() { return color; }
    public float getOpacity() { return opacity; }
    public BlendMode getBlendMode() { return blendMode; }
    public Rectangle getShape() { return shape; }

    // Drawing handles
    public void drawHandles(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        int s = HANDLE_SIZE;
        g2d.fillRect(shape.x - s / 2, shape.y - s / 2, s, s);
        g2d.fillRect(shape.x + shape.width - s / 2, shape.y - s / 2, s, s);
        g2d.fillRect(shape.x - s / 2, shape.y + shape.height - s / 2, s, s);
        g2d.fillRect(shape.x + shape.width - s / 2, shape.y + shape.height - s / 2, s, s);
    }
}
