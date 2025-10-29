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

    public Color getColor() { return color; }
    public float getOpacity() { return opacity; }
    public BlendMode getBlendMode() { return blendMode; }
    public Rectangle getShape() { return shape; }
}
