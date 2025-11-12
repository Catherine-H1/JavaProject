package core;
/**
 * External panel that shows the goal/target drawing. Has hint giver.
 * Checks if your drawing is close in terms of size, distance, color, and mode.
 */

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChallengeWindow extends JFrame {

    private JTextArea hintArea;
    private int hintIndex = 0;
    private List<Layer> challengeLayers;

    public ChallengeWindow(List<Layer> challengeLayers, LayerManager playerManager) {
        this.challengeLayers = challengeLayers;

        setTitle("Challenge Viewer");
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Preview Panel, try to match this
        JPanel preview = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Renderer r = new Renderer((Graphics2D) g);
                r.draw(challengeLayers);
            }
        };
        add(preview, BorderLayout.CENTER);

        // Hint Area, Scrollable
        hintArea = new JTextArea(10, 40);
        hintArea.setEditable(false);
        hintArea.setLineWrap(true);
        hintArea.setWrapStyleWord(true);
        hintArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(hintArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.EAST);

        // Controls on the bottom to give next hint or check match
        JPanel bottom = new JPanel(new FlowLayout());
        JButton nextHint = new JButton("Next Hint");
        nextHint.addActionListener(e -> showNextHint());
        JButton clearHints = new JButton("Clear Hints");
        clearHints.addActionListener(e -> hintArea.setText(""));
        JButton checkButton = new JButton("Check My Drawing");
        checkButton.addActionListener(e -> {
            double score = ChallengeScorer.compare(challengeLayers, playerManager.getLayers());
            JOptionPane.showMessageDialog(this,
                    score >= 0.85 ? "Great match! (" + (int)(score * 100) + "%)" :
                            "Keep trying (" + (int)(score * 100) + "%)");
        });
        bottom.add(nextHint);
        bottom.add(clearHints);
        bottom.add(checkButton);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Logic behind the hints. Below 7, gives small hints. Above 7, starts giving cheater hints
    private void showNextHint() {
        hintIndex++;
        int total = challengeLayers.size();
        StringBuilder sb = new StringBuilder();

        if (hintIndex <=  7) { // normal hints
            Layer l = challengeLayers.get(Math.min(hintIndex - 1, total - 1));
            Color c = l.getColor();
            Rectangle r = l.getShape();

            switch (hintIndex) {
                case 1 -> sb.append("Hint 1: There are ").append(total).append(" layers.\n");
                case 2 -> sb.append("Hint 2: First layer color ≈ ").append(colorToString(c)).append("\n");
                case 3 -> sb.append("Hint 3: One blend mode is ").append(l.getBlendMode()).append("\n");
                case 4 -> sb.append("Hint 4: Top-left layer starts near (").append(r.x).append(", ").append(r.y).append(")\n");
                case 5 -> sb.append("Hint 5: Smallest layer ≈ ").append(r.width).append("×").append(r.height).append("\n");
                case 6 -> sb.append("Hint 6: Average opacity ≈ ").append(String.format("%.2f", l.getOpacity())).append("\n");
                case 7 -> sb.append("Hint 7: Try overlapping layers to see blending!\n");
                default -> sb.append("Final normal hint: focus on color & blend accuracy.\n");
            }
        } else {
            // cheater mode
            int cheatIndex = hintIndex-8;

            if (cheatIndex < challengeLayers.size()) {
                Layer l = challengeLayers.get(cheatIndex);
                sb.append("Cheater Hint Because you Suck ").append(cheatIndex + 1).append(": Layer ")
                        .append(cheatIndex + 1)
                        .append(" → Color ").append(colorToString(l.getColor()))
                        .append(", Opacity ").append(String.format("%.2f", l.getOpacity()))
                        .append(", Blend ").append(l.getBlendMode())
                        .append(", Rect (").append(l.getShape().x).append(", ").append(l.getShape().y)
                        .append(", ").append(l.getShape().width).append(", ").append(l.getShape().height).append(")\n");
            } else {
                sb.append("Wow, you really suck! \n");
            }
        }

        hintArea.append(sb.toString());
        hintArea.append("--------------------------\n");
        hintArea.setCaretPosition(hintArea.getDocument().getLength()); // auto-scroll
    }

    private String colorToString(Color c) {
        return "RGB(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")";
    }
}
