package core;

import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    public static void saveLayers(List<Layer> layers, File file) throws IOException {
        JSONArray arr = new JSONArray();
        for (Layer layer : layers) {
            JSONObject obj = new JSONObject();
            Color c = layer.getColor();
            Rectangle r = layer.getShape();
            obj.put("r", c.getRed());
            obj.put("g", c.getGreen());
            obj.put("b", c.getBlue());
            obj.put("opacity", layer.getOpacity());
            obj.put("blendMode", layer.getBlendMode().name());
            obj.put("x", r.x);
            obj.put("y", r.y);
            obj.put("width", r.width);
            obj.put("height", r.height);
            arr.put(obj);
        }

        JSONObject root = new JSONObject();
        root.put("layers", arr);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(root.toString(4)); // pretty print
        }
    }

    public static void loadLayers(LayerManager manager, File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }

        JSONObject root = new JSONObject(sb.toString());
        JSONArray arr = root.getJSONArray("layers");

        List<Layer> newLayers = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            Color c = new Color(obj.getInt("r"), obj.getInt("g"), obj.getInt("b"));
            float opacity = (float) obj.getDouble("opacity");
            BlendMode mode = BlendMode.valueOf(obj.getString("blendMode"));
            Rectangle rect = new Rectangle(
                    obj.getInt("x"), obj.getInt("y"),
                    obj.getInt("width"), obj.getInt("height")
            );
            newLayers.add(new Layer(c, opacity, mode, rect));
        }

        manager.setLayers(newLayers);
    }
    public static List<Layer> loadChallenge(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }

        JSONObject root = new JSONObject(sb.toString());
        JSONArray arr = root.getJSONArray("layers");
        List<Layer> layers = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            Color c = new Color(obj.getInt("r"), obj.getInt("g"), obj.getInt("b"));
            float opacity = (float) obj.getDouble("opacity");
            BlendMode mode = BlendMode.valueOf(obj.getString("blendMode"));
            Rectangle rect = new Rectangle(
                    obj.getInt("x"), obj.getInt("y"),
                    obj.getInt("width"), obj.getInt("height")
            );
            layers.add(new Layer(c, opacity, mode, rect));
        }
        return layers;
    }

}
