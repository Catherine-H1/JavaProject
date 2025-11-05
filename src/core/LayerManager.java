package core;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LayerManager {
    private List<Layer> layers = new ArrayList<>();
    private int selectedIndex = -1;

    public void addLayer(Layer layer) { layers.add(layer); }
    public void removeLayer(int index) {
        if (index >= 0 && index < layers.size()) layers.remove(index);
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
}
