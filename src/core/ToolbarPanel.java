package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;


public class ToolbarPanel extends JPanel {
    private final LayerManager manager;
    private final JPanel previewPanel;
    private final JComboBox<BlendMode> modeSelector;
    private final JSpinner widthSpinner;
    private final JSpinner heightSpinner;

    public ToolbarPanel(LayerManager manager, Runnable repaintCallback) {
        this.manager = manager;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(220, 600));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Use built in JColorChooser dialog menu to choose color
        JLabel colorLabel = new JLabel("Layer Color");
        add(colorLabel);

        previewPanel = new JPanel();
        previewPanel.setBackground(Color.CYAN);
        previewPanel.setPreferredSize(new Dimension(100, 50));
        add(previewPanel);

        JButton colorButton = new JButton("Choose Color");
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Select Layer Color", previewPanel.getBackground());
            if (newColor != null) {
                previewPanel.setBackground(newColor);
            }
        });
        add(colorButton);
        add(Box.createVerticalStrut(10));

        // Change Size
        JLabel sizeLabel = new JLabel("Layer Size (px)");
        add(sizeLabel);

        JPanel sizePanel = new JPanel(new GridLayout(2, 2, 5, 5));
        sizePanel.add(new JLabel("Width:"));
        widthSpinner = new JSpinner(new SpinnerNumberModel(150, 10, 800, 10));
        sizePanel.add(widthSpinner);
        sizePanel.add(new JLabel("Height:"));
        heightSpinner = new JSpinner(new SpinnerNumberModel(150, 10, 800, 10));
        sizePanel.add(heightSpinner);
        add(sizePanel);
        add(Box.createVerticalStrut(10));

        // Select Blend Mode
        JLabel modeLabel = new JLabel("Blend Mode");
        add(modeLabel);
        modeSelector = new JComboBox<>(BlendMode.values());
        add(modeSelector);
        add(Box.createVerticalStrut(10));

        // Add button
        JButton addButton = new JButton("Add Layer");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener((ActionEvent e) -> {
            Color color = previewPanel.getBackground();
            BlendMode mode = (BlendMode) modeSelector.getSelectedItem();
            int width = (int) widthSpinner.getValue();
            int height = (int) heightSpinner.getValue();

            // Create layer roughly centered on 800x600 canvas
            Rectangle rect = new Rectangle(400 - width / 2, 300 - height / 2, width, height);
            Layer newLayer = new Layer(color, 0.8f, mode, rect);

            manager.addLayer(newLayer);
            repaintCallback.run();
        });
        add(addButton);

        // FILE operations for save file
        add(Box.createVerticalStrut(10));

        JButton saveButton = new JButton("Save File");
        saveButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    FileHandler.saveLayers(manager.getLayers(), file);
                    JOptionPane.showMessageDialog(this, "File saved successfully!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
                }
            }
        });
        add(saveButton);

        JButton loadButton = new JButton("Load File");
        loadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    FileHandler.loadLayers(manager, file);
                    repaintCallback.run(); // Refresh canvas
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage());
                }
            }
        });
        add(loadButton);


        // Stretch Fill to make the right side not an empty void
        add(Box.createVerticalGlue());
    }
}
