package core;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LayerManager {
    private List<Layer> layers = new ArrayList<>();
    private int selectedIndex = -1;
    private java.util.Stack<java.util.List<Layer>> history = new java.util.Stack<>();

    private void saveState() {
        java.util.List<Layer> snapshot = new java.util.ArrayList<>();
        for (Layer l : layers) {
            snapshot.add(new Layer(
                    l.getColor(),
                    l.getOpacity(),
                    l.getBlendMode(),
                    new java.awt.Rectangle(l.getShape())
            ));
        }
        history.push(snapshot);
    }
    public void addLayer(Layer layer) {
        saveState();
        layers.add(layer);
    }

    public void removeLayer(int index) {
        if (index >= 0 && index < layers.size()) {
            saveState();
            layers.remove(index);
        }
    }

    public void deleteSelectedLayer() {
        if (selectedIndex >= 0 && selectedIndex < layers.size()) {
            saveState();
            layers.remove(selectedIndex);
            selectedIndex = -1;
        }
    }
    public void undo() {
        if (!history.isEmpty()) {
            layers = history.pop();
            selectedIndex = -1;
        }
    }
    public List<Layer> getLayers() { return layers; }
    public Layer getSelectedLayer() {
        if (selectedIndex >= 0 && selectedIndex < layers.size()) return layers.get(selectedIndex);
        return null;
    }
    public void selectLayer(int index) {
        if (index >= 0 && index < layers.size()) selectedIndex = index;
    }

    // Select the topmost layer that contains the click point
    public void selectLayerByClick(Point p) {
        for (int i = layers.size() - 1; i >= 0; i--) {
            if (layers.get(i).getShape().contains(p)) {
                selectLayer(i);
                return;
            }
        }
        selectedIndex = -1; // clicked empty space
    }

    // Move currently selected layer by dx and dy. Calls the layer's own method
    public void moveSelectedLayer(int dx, int dy) {
        Layer l = getSelectedLayer();
        if (l != null) {
            l.move(dx, dy);
        }
    }
    public Layer.HandlePosition getHandleAt(Point p) {
        Layer selected = getSelectedLayer();
        if (selected != null) {
            return selected.getHandleAt(p);
        }
        return Layer.HandlePosition.NONE;
    }

    public void setLayers(List<Layer> newLayers) {
        this.layers = newLayers;
        selectedIndex = -1;
    }
    public void clear() {
        layers.clear();
        selectedIndex = -1;
    }
}
