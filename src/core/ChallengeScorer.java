package core;

import java.awt.*;
import java.util.List;

public class ChallengeScorer {
    public static double compare(List<Layer> target, List<Layer> player) {
        if (player.isEmpty() || target.isEmpty()) return 0.0; // It's like turning in no homework
        int matches = 0;

        for (int i = 0; i < Math.min(target.size(), player.size()); i++) {
            Layer t = target.get(i);
            Layer p = player.get(i);

            double colorDiff = colorDistance(t.getColor(), p.getColor());
            double posDiff = posDistance(t.getShape(), p.getShape());
            boolean blendMatch = t.getBlendMode() == p.getBlendMode();

            if (colorDiff < 60 && posDiff < 80 && blendMatch)
                matches++;
        }

        return (double) matches / target.size();
    }

    private static double colorDistance(Color c1, Color c2) {
        int dr = c1.getRed() - c2.getRed();
        int dg = c1.getGreen() - c2.getGreen();
        int db = c1.getBlue() - c2.getBlue();
        return Math.sqrt(dr * dr + dg * dg + db * db);
    }

    private static double posDistance(Rectangle r1, Rectangle r2) {
        int dx = (r1.x + r1.width/2) - (r2.x + r2.width/2);
        int dy = (r1.y + r1.height/2) - (r2.y + r2.height/2);
        return Math.sqrt(dx * dx + dy * dy);
    }
}
