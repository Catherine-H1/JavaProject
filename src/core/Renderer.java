package core;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Renderer {
    private Graphics2D g2d;

    public Renderer(Graphics2D g2d) {
        this.g2d = g2d;
    }

    // 🧩 Helper: Blend two images pixel-by-pixel
    private BufferedImage blend(BufferedImage base, BufferedImage top, BlendMode mode) {
        int width = base.getWidth();
        int height = base.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int baseRGB = base.getRGB(x, y);
                int topRGB = top.getRGB(x, y);
                int topAlpha = (topRGB >> 24) & 0xff;
                if (topAlpha == 0) {
                    result.setRGB(x, y, baseRGB); // skip blending, keep base pixel
                    continue;
                }
                Color baseC = new Color(baseRGB, true);
                Color topC = new Color(topRGB, true);

                float r1 = baseC.getRed() / 255f;
                float g1 = baseC.getGreen() / 255f;
                float b1 = baseC.getBlue() / 255f;

                float r2 = topC.getRed() / 255f;
                float g2 = topC.getGreen() / 255f;
                float b2 = topC.getBlue() / 255f;

                float r, g, b;

                // 🎨 Apply blending logic
                switch (mode) {
                    case ADD:
                        r = Math.min(1.0f, r1 + r2);
                        g = Math.min(1.0f, g1 + g2);
                        b = Math.min(1.0f, b1 + b2);
                        break;
                    case MULTIPLY:
                        r = r1 * r2;
                        g = g1 * g2;
                        b = b1 * b2;
                        break;
                    case SUBTRACT:
                        r = Math.max(0.0f, r1 - r2);
                        g = Math.max(0.0f, g1 - g2);
                        b = Math.max(0.0f, b1 - b2);
                        break;
                    default:
                        // NORMAL mode = just take the top pixel
                        r = r2;
                        g = g2;
                        b = b2;
                }

                // Combine alpha (optional simplification)
                float a = Math.max(baseC.getAlpha(), topC.getAlpha()) / 255f;

                int outR = (int) (r * 255);
                int outG = (int) (g * 255);
                int outB = (int) (b * 255);
                int outA = (int) (a * 255);

                Color resultColor = new Color(outR, outG, outB, outA);
                result.setRGB(x, y, resultColor.getRGB());
            }
        }

        return result;
    }

    // 🧠 Main draw method
    public void draw(List<Layer> layers) {
        int width = 800;
        int height = 600;

        // Step 1: create blank base image
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gResult = result.createGraphics();
        gResult.setColor(Color.WHITE);
        gResult.fillRect(0, 0, width, height);

        // Step 2: draw and blend each layer
        for (Layer layer : layers) {
            BufferedImage layerImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gLayer = layerImg.createGraphics();
            gLayer.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, layer.getOpacity()));
            gLayer.setColor(layer.getColor());
            gLayer.fill(layer.getShape());
            gLayer.dispose();

            // Blend into the result
            result = blend(result, layerImg, layer.getBlendMode());
        }

        // Step 3: draw final result to the panel's Graphics2D
        gResult.dispose();
        g2d.drawImage(result, 0, 0, null);
    }
}
