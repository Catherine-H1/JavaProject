package core;

import java.awt.*;

public class Layer {
    private Color color;
    private float opacity;
    private BlendMode blendMode;
    private Rectangle shape;

    public Layer(Color color, float opacity, BlendMode blendMode, Rectangle shape) {
        this.color = color;
        this.opacity = opacity;
        this.blendMode = blendMode;
        this.shape = shape;
    }

    // ✅ Move the layer by a delta
    public void move(int dx, int dy) {
        shape.translate(dx, dy);
    }

    // ✅ Optional convenience: set absolute position
    public void setPosition(int x, int y) {
        shape.setLocation(x, y);
    }

    // ✅ Center point helper (optional, for snapping etc.)
    public Point getCenter() {
        return new Point(shape.x + shape.width / 2, shape.y + shape.height / 2);
    }

    public Color getColor() { return color; }
    public float getOpacity() { return opacity; }
    public BlendMode getBlendMode() { return blendMode; }
    public Rectangle getShape() { return shape; }
}
