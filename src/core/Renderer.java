package core;

import java.awt.*;
import java.util.List;

public class Renderer {
    private Graphics2D g2d;

    public Renderer(Graphics2D g2d) {
        this.g2d = g2d;
    }
    

    public void draw(List<Layer> layers) {
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 800, 600);

        for (Layer layer : layers) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, layer.getOpacity()));
            g2d.setColor(layer.getColor());
            g2d.fill(layer.getShape());
        }
    }
}
